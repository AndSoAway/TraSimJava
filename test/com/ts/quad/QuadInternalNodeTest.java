package com.ts.quad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import com.ts.trajectory.Trajectory;
import com.ts.trajectory.TrajectoryHelper;
import com.ts.trajectory.TrajectorySamplePoint;

/**
 * Test the QuadInternalNode.java
 * @author Qinger
 *
 */
public class QuadInternalNodeTest {
	
	private static final String bjMapInfoFilePath = "conf/mapinfo.bj.txt";
	private static int trajCount = 0;
	
	private static final double MAXX = 41.083332;
	private static final double MAXY = 117.5;
	private static final double MINX = 39.416668;
	private static final double MINY = 115.375;
	
	/**
	 * 
	 * @return
	 */
	public static ArrayList<Point> readBjMapInfo() {
		ArrayList<Double> coordinates = new ArrayList<Double>();
		coordinates.add(MAXX * TrajectoryHelper.ENLARGE);
		coordinates.add(MAXY * TrajectoryHelper.ENLARGE);
		coordinates.add(MINX * TrajectoryHelper.ENLARGE);
		coordinates.add(MINY * TrajectoryHelper.ENLARGE);
		
		/**
		File bjMapInfoFile = new File(bjMapInfoFilePath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(bjMapInfoFile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] lineParts = line.split(" ");
				coordinates.add(Double.valueOf(lineParts[2]) * TrajectoryHelper.ENLARGE);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(new Point(coordinates.get(2), coordinates.get(3)));
		points.add(new Point(coordinates.get(0), coordinates.get(1)));
		return points;
	}
	
	public static int readDataInsert(String filePath, QuadInternalNode quadInternalNode) {
		int cutoffInterval = 300;
		int minPointNumber = 3;
		TrajectoryHelper trajectoryHelper =  new TrajectoryHelper();
		
		Date readPointTime = new Date();
		ArrayList<TrajectorySamplePoint> samplePointsList = trajectoryHelper.readTrajectoryFromFile(filePath);
		Date endPointTime = new Date();
		System.out.println("Reading date needs: " + (endPointTime.getTime() - readPointTime.getTime()));
		
		System.out.println("FilePath: " + filePath + "\n" + "Point Counts: " + samplePointsList.size());
		ArrayList<Trajectory> trajectorys = trajectoryHelper.trajectoryCleanUp(samplePointsList, cutoffInterval, minPointNumber);
		System.out.println("Trajectory¡¡Count: " + trajectorys.size());
		Date begin = new Date();
		for(Trajectory trajectory : trajectorys) {
			//if(QuadInternalNodeTest.trajCount == 14378)
				//System.out.println("1000 trajectories");;
			quadInternalNode.insertTrajectory(trajectory);
			QuadInternalNodeTest.trajCount++;
			System.out.println("Insert traj: " + QuadInternalNodeTest.trajCount + "Remains :" + (trajectorys.size() - QuadInternalNodeTest.trajCount));
		}
		
		Date end = new Date();
		System.out.println("Insert Cost Time :" + Long.valueOf(end.getTime() - begin.getTime()).toString());

		/*
		ArrayList<Trajectory> trajectories = quadInternalNode.getCandidate(trajectorys.get(0));
		Date queryEnd = new Date();
		System.out.println("Query Cost Time :" + Long.valueOf(queryEnd.getTime() - end.getTime()));
		printTrajectories(trajectories);
		*/
		
		return trajectorys.size();
	}
	
	
	public static int readAllData(String dirPath, QuadInternalNode quadInternalNode) {
		int sumInsertTrajectory = 0;
		File dir = new File(dirPath);
		if(dir.isFile()) {
			sumInsertTrajectory += readDataInsert(dir.getAbsolutePath(), quadInternalNode);
		}
		File[] files = dir.listFiles();
		for(File file : files) {
			sumInsertTrajectory += readDataInsert(file.getAbsolutePath(), quadInternalNode);
		}
		return sumInsertTrajectory;
	}
	
	public static void printTrajectories(ArrayList<Trajectory> trajs) {
		System.out.println("Candidate trajectory:********");
		for (Trajectory traj : trajs) {
			System.out.print(traj.toString());
		}
		System.out.println("**************");
	}

	public static void main(String[] args) {
		if(args[0].equalsIgnoreCase("--help")) {
			System.out.println("args: the trajectory filepath¡£");
			System.out.println(System.getProperty("user.dir"));
			return;
		}
		
		ArrayList<Point> rectEndPoints = QuadInternalNodeTest.readBjMapInfo();
		QuadInternalNode quadInternalNode = new QuadInternalNode(new Rectangle(rectEndPoints.get(0), rectEndPoints.get(1), true));
		quadInternalNode.setTreeDepth(ConfigParams.ROOTNODEDEPTH);
		String filePath = args[0];
		Date begin = new Date();
		int insertTrajectory = readAllData(filePath, quadInternalNode);
		Date end = new Date();
		System.out.println("-------------");
		System.out.println("Insert:" + insertTrajectory);
		System.out.println("Time: " + (end.getTime() - begin.getTime()));

	}
}