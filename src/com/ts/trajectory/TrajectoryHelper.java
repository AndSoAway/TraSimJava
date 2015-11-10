package com.ts.trajectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @author tanayun
 * 
 *         Helper class to facilitate on: 1£¬read raw trajectories from files; 2,
 *         clean up trajectories, e.g., break those with long intervals.
 *
 */

public class TrajectoryHelper {
 
	public static final int ENLARGE = 1000000;
	
	public TrajectoryHelper() {
		super();
		// PropertyConfigurator.configure("./conf/log4j.properties");
	}

	// Logger logger = Logger.getLogger(TrajectoryHelper.class.getName());

	/**
	 * 
	 * @param filePath
	 *            path of raw trajectory files
	 * @return ArrayList of TrajectorySamplePoint, may contain trajectories of
	 *         multiple vehicles, need further processing
	 */
	public ArrayList<TrajectorySamplePoint> readTrajectoryFromFile(
			String filePath) {
		ArrayList<TrajectorySamplePoint> traces = new ArrayList<TrajectorySamplePoint>();
		try {
			File file = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			int i = 1;
			String line = "";
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");

				while (st.hasMoreTokens()) {
					// sample:
					// 1,2009-05-04 00:03:31,39.944580,116.357964,69,1800,1
					int id = Integer.parseInt(st.nextToken());
					String timeStr = st.nextToken();
					Date time;
					try {
						time = sdf.parse(timeStr);
					} catch (ParseException e) {
						System.out.println("error in line " + i);
						System.out.println(line);
						System.out.println(timeStr);
						e.printStackTrace();
						break;
					}
					float latitude = Float.parseFloat(st.nextToken()) * ENLARGE;
					float longitude = Float.parseFloat(st.nextToken()) * ENLARGE;
					float heading = Float.parseFloat(st.nextToken());
					float speed = Float.parseFloat(st.nextToken());
					boolean loaded = st.nextToken().equals("1") ? true : false;

					TrajectorySamplePoint point = new TrajectorySamplePoint(id,
							time, latitude, longitude, heading, speed, loaded);

					//System.out.println(point);
					traces.add(point);
					i++;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			// logger.error(e);
			e.printStackTrace();
		}
		System.out.println("trajectory points loaded: " + traces.size());
		// logger.info("trajectory points loaded: " + traces.size());
		return traces;
	}

	/**
	 * Clean up raw trajectories: 1, break if the difference of time stamps of
	 * two continuous sample points are bigger than the given interval; 2,
	 * calculate the length of each new trajectory for later use.
	 * 
	 * @param rawTrajectoryData
	 *            original traces of raw sample points
	 * @param cutoffInterval
	 *            interval in seconds to break a trajectory
	 * @param minPointNumber
	 *            minimal number of points a valid trajectory should contain
	 * @return a list that contains valid trajectories
	 */
	public ArrayList<Trajectory> trajectoryCleanUp(
			ArrayList<TrajectorySamplePoint> rawTrajectoryData,
			int cutoffInterval, int minPointNumber) {

		if (rawTrajectoryData == null || rawTrajectoryData.size() == 0
				|| rawTrajectoryData.get(0) == null) {
			System.out.println("error!");
			return null;
		}

		ArrayList<Trajectory> trajectories = new ArrayList<Trajectory>();

		int prevVID = rawTrajectoryData.get(0).getVehicalID();
		Trajectory traj = new Trajectory(prevVID);

		for (int i = 0; i < rawTrajectoryData.size(); i++) {
			TrajectorySamplePoint sp = rawTrajectoryData.get(i);
			int currentVID = sp.getVehicalID();

			if (currentVID != prevVID) {
				if (traj.getPointList().size() >= minPointNumber) {
					traj.setEndTime(rawTrajectoryData.get(i - 1).getTimestamp());
					trajectories.add(traj);
				}
				prevVID = currentVID;
				traj = new Trajectory(prevVID);
			} else if (i > 0
					&& exceedInterval(rawTrajectoryData.get(i - 1)
							.getTimestamp(), sp.getTimestamp(), cutoffInterval)) {
				if (traj.getPointList().size() >= minPointNumber) {
					traj.setEndTime(rawTrajectoryData.get(i - 1).getTimestamp());
					trajectories.add(traj);
				}
				traj = new Trajectory(prevVID);
			}

			if (traj.getPointList().size() == 0) {
				// this is the first sample point to of a valid trajectory,
				// initiate
				traj.setStartTime(sp.getTimestamp());
			}
			traj.getPointList().add(sp);

			if (i == rawTrajectoryData.size() - 1) {
				// last element
				if (traj.getPointList().size() >= minPointNumber) {
					traj.setEndTime(rawTrajectoryData.get(i).getTimestamp());
					trajectories.add(traj);
				}
			}
		}

		System.out.println("trajectory #£º" + trajectories.size());
		return trajectories;
	}

	private boolean exceedInterval(Date d1, Date d2, int cutoffInterval) {
		if ((d2.getTime() - d1.getTime()) / 1000 > cutoffInterval)
			return true;
		return false;
	}
}
