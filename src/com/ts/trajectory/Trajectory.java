package com.ts.trajectory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ts.quad.DisRectangle;
import com.ts.quad.Point;

/**
 * @author tanayun
 * 
 *         To model a real "Trajectory",
 * 
 *         TAXI_ID, START_TIME, END_TIME, LENTH, ARRAY OF TrajectorySamplePoint
 */
public class Trajectory {
	
	private static int trajectoryId = 1;
			
	public Trajectory(int vehicalID, Date startTime, Date endTime,
			float trajLenth, ArrayList<TrajectorySamplePoint> pointList) {
		super();
		this.vehicalID = vehicalID;
		this.startTime = startTime;
		this.endTime = endTime;
		this.trajLenth = trajLenth;
		this.pointList = pointList;
	
		this.trajectoryID = trajectoryId++;
	}

	/**
	 *Create the list of distance-Rectangle using the point-list
	 *@return
	 */
	private void calculateDisRectangleList() {
		this.disRectangles = new ArrayList<DisRectangle>();		
		if(this.pointList == null || this.pointList.size() <= 1)
			return;
	
		int pointCount = this.pointList.size();
		for(int index = 1; index < pointCount; index++) {
			TrajectorySamplePoint beginSample = this.pointList.get(index);
			TrajectorySamplePoint endSample = this.pointList.get(index - 1);
			Point begin = new Point(beginSample.getLatitude(), beginSample.getLongitude());
			Point end = new Point(endSample.getLatitude(), endSample.getLongitude());
			DisRectangle aDisRectangle = new DisRectangle(begin, end);
			this.disRectangles.add(aDisRectangle);
		}
			
	}

	/**
	 * 
	 * @param vehicalID
	 */
	public Trajectory(int vehicalID) {
		this.vehicalID = vehicalID;
		this.pointList = new ArrayList<TrajectorySamplePoint>();
		this.trajectoryID = trajectoryId++;
	}

	//The ID of the taxi
	int vehicalID;
	Date startTime;
	Date endTime;
	float trajLenth = -1f;
	ArrayList<TrajectorySamplePoint> pointList;
	//The ID of the trajectory
	int trajectoryID;
	
	private ArrayList<DisRectangle> disRectangles = null;
	
	/**
	 * When two trajectories have the same point list, they are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || (obj  instanceof Trajectory) == false) 
			return false;
		
		Trajectory traj = (Trajectory)obj;
		if(this.vehicalID != traj.getVehicalID() || this.pointList.size() != traj.getPointList().size())
			return false;
		
		return (this.pointList.equals(traj.getPointList()));
	}
	
	/**
	 * Judge whether two Trajectory intersects.
	 * When any segment  intersects the distance-rectangle of the queryTrajectory,
	 * they intersects. 
	 * @param traj
	 * @return
	 */
	public boolean intersectTraj(Trajectory traj) {
		for(DisRectangle queryDisRectangle : traj.getDisRectangles()) {
			for (DisRectangle disRectangle : this.getDisRectangles()) {
				if (queryDisRectangle.intersectSegment(disRectangle.getBeginPoint(), disRectangle.getEndPoint())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DisRectangle> getDisRectangles() {
		if(disRectangles == null)
			calculateDisRectangleList();
		return disRectangles;
	}
	
	public ArrayList<TrajectorySamplePoint> getSamplePoints() {
		return this.pointList;
	}
	
	public int getTrajectoryID() {
		return this.trajectoryID;
	}
	
	public int getVehicalID() {
		return vehicalID;
	}

	public void setVehicalID(int vehicalID) {
		this.vehicalID = vehicalID;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public float getTrajLenth() {
		return trajLenth;
	}

	public void setTrajLenth(float trajLenth) {
		this.trajLenth = trajLenth;
	}

	public ArrayList<TrajectorySamplePoint> getPointList() {
		return pointList;
	}

	public void setPointList(ArrayList<TrajectorySamplePoint> pointList) {
		this.pointList = pointList;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "Trajectory [trajectoryID = " + trajectoryId + ",vehicalID=" + vehicalID + ", startTime="
				+ sdf.format(startTime) + ", endTime=" + sdf.format(endTime)
				+ ", trajLenth=" + trajLenth + ", pointList size="
				+ pointList.size() + "]";
	}
}
