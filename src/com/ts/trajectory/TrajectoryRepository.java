/**
 * 
 */
package com.ts.trajectory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tanayun
 *
 */
public class TrajectoryRepository {
	Map<Integer, List<Trajectory>> trajRepo;

	public Map<Integer, List<Trajectory>> getTrajRepo() {
		return trajRepo;
	}

	public void genRepo(String trajFile) {

		TrajectoryHelper th = new TrajectoryHelper();
		ArrayList<TrajectorySamplePoint> allPointsList = th
				.readTrajectoryFromFile(trajFile);
		ArrayList<Trajectory> allTrajList = th.trajectoryCleanUp(allPointsList,
				180, 5);

		trajRepo = new HashMap<Integer, List<Trajectory>>();
		for (Trajectory traj : allTrajList) {
			Integer carID = traj.getVehicalID();
			List<Trajectory> trajList;
			if (trajRepo.containsKey(carID)) {
				trajList = trajRepo.get(carID);
			} else {
				trajList = new ArrayList<Trajectory>();
				trajRepo.put(carID, trajList); // TODO sorting
			}
			trajList.add(traj);
		}
	}

}
