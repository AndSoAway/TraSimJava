package com.ts.viewer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import com.ts.trajectory.Trajectory;
import com.ts.trajectory.TrajectorySamplePoint;

public class DrawTrajectory {
	public static void main(String[] args) {
		createAndShowGUI();
	}
	
	public static void createAndShowGUI() {
		Frame frame = new Frame("Trajectory Line demo");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.add(new TrajectoryCanvas(), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}

class TrajectoryCanvas extends Canvas {
	public static final int WIDTH = 5000;
	public static final int HEIGHT = 4000;
	
	public TrajectoryCanvas() {
		this.setBackground(Color.WHITE);
	}
	
	public void paint(Graphics g) {
		drawTrajectorys(g);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	private void drawTrajectorys(Graphics g) {
		ArrayList<Trajectory> trajectories = TrajectorySet.getTrajectories();
		int count = 0;
		for (Trajectory traj : trajectories) {
			drawTrajectory(g, traj);
			count++;
			if(count > 1)
				break;
		}
	}
	
	private void drawTrajectory(Graphics g, Trajectory traj) {
		ArrayList<TrajectorySamplePoint> points = traj.getPointList();
		int nPoints = points.size();
		
		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		
		int index = 0;
		double bjMinX = BeijingInfo.getBjRect().getLeftBottom().getX();
		double bjMinY = BeijingInfo.getBjRect().getLeftBottom().getY();
		
		for(TrajectorySamplePoint point : points) {
			xPoints[index] = (int)((point.getLatitude() - bjMinX) * WIDTH / BeijingInfo.getBjWidth());
			yPoints[index] = (int)((point.getLongitude() - bjMinY) * HEIGHT / BeijingInfo.getBjHeight());
			index++;
		}
		
		g.drawPolyline(xPoints, yPoints, nPoints);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}