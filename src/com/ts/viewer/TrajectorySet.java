package com.ts.viewer;

import java.util.ArrayList;

import com.ts.trajectory.Trajectory;
import com.ts.trajectory.TrajectoryHelper;
import com.ts.trajectory.TrajectorySamplePoint;

public class TrajectorySet {

	public static ArrayList<Trajectory> getTrajectories() {
		int cutoffInterval = 600;
		int minPointNumber = 3;
		
		String filePath = "train/output_0501.dat.plain";
		
		TrajectoryHelper trajectoryHelper =  new TrajectoryHelper();
		ArrayList<TrajectorySamplePoint> samplePointsList = trajectoryHelper.readTrajectoryFromFile(filePath);
		System.out.println("FilePath: " + filePath + "\n" + "Point Counts: " + samplePointsList.size());
		ArrayList<Trajectory> trajectorys = trajectoryHelper.trajectoryCleanUp(samplePointsList, cutoffInterval, minPointNumber);
		System.out.println("Trajectory¡¡Count: " + trajectorys.size());
		
		return trajectorys;
	}
}
