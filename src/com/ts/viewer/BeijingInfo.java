package com.ts.viewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.ts.quad.Point;
import com.ts.quad.Rectangle;
import com.ts.trajectory.TrajectoryHelper;

public class BeijingInfo {

	private static final String bjMapInfoFilePath = "conf/mapinfo.bj.txt";
	private static Rectangle rect = null;
	private static double bjWidth = 0;
	private static double bjHeight = 0;
	
	public static Rectangle getBjRect() {
		if (rect == null) {
			ArrayList<Point> points = readBjMapInfo();
			rect = new Rectangle(points.get(0), points.get(1), true);
		}
		return rect;
	}
	
	private static ArrayList<Point> readBjMapInfo() {
		ArrayList<Double> coordinates = new ArrayList<Double>();
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
		
		bjWidth = (coordinates.get(0) - coordinates.get(2));
		bjHeight = (coordinates.get(1) - coordinates.get(3));
		
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(new Point(coordinates.get(2), coordinates.get(3)));
		points.add(new Point(coordinates.get(0), coordinates.get(1)));
		return points;
	}
	
	public static double getBjWidth() {
		getBjRect();
		return bjWidth;
	}
	
	public static double getBjHeight() {
		getBjRect();
		return bjHeight;
	}
}
