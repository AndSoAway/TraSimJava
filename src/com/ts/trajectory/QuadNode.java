package com.ts.trajectory;

import java.util.ArrayList;

import com.ts.trajectory.Trajectory;

public abstract class QuadNode {
	protected ArrayList<Double> coordinates;
	
	protected ArrayList<QuadNode> children;
	
	private static int trjInNodeUpBd = 1000;
	
	abstract public  Boolean isIntersect(Trajectory tj);
	
	public void setTrjInNodeUpBd(int aTrjCount) {
		QuadNode.trjInNodeUpBd = aTrjCount;
	}
	
	public static int getTrjInNodeUpBd() {
		return QuadNode.trjInNodeUpBd;
	}
}
