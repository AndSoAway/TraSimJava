package com.ts.quad;

import java.util.ArrayList;

/**
 * 
 * @author Qinger
 *
 */
public class DisRectangle {
	private static double distance = 100;
	
	/**
	 * The length of segment, whose end points are beginPoint and endPoint 
	 */
	private double segLen;
	
	private Point beginPoint;
	private Point endPoint;
	
	/**
	 * Four endpoints of a distance-rectangle
	 */
	private Point beginOne;
	private Point beginTwo;
	private Point endOne;
	private Point endTwo;
	
	public DisRectangle(Point begin, Point end) {
		this.segLen = GeoCalculate.segmentLength(begin, end);
		calculateFourPoints(begin, end);
	}
	
	/**
	 * Check whether the distance-rectangle intersects the segment.
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean intersectSegment(Point begin, Point end) {
		if (this.containPoint(begin) && this.containPoint(end))
			return true;
		
		return (GeoCalculate.segIntersectSeg(beginOne, endTwo, begin, end) || GeoCalculate.segIntersectSeg(beginTwo, endOne, begin, end));
	}
	
	/**
	 * Check whether the the random-direction rectangle contains the point.
	 * Sort the end-point of rectangle clockwise or counterclockwise, and connect the 
	 * end-point and the checking point. We will get four vector. 
	 * If cross-product of any two adjacent vector is negative or positive, then the point lies in the rectangle.
	 * Otherwise, it lies in the outer area.  
	 * @param point
	 * @return
	 */
	public boolean containPoint(Point point) {
		ArrayList<Point> endPoints = getOneWayEndPoints();
		ArrayList<Vector> endVectors = new ArrayList<Vector>();
		for (Point endPoint : endPoints) {
			endVectors.add(new Vector(point, endPoint));
		}
		
		int endPointCount = endPoints.size();
		double crossProduct = endVectors.get(0).getCrossProduct(endVectors.get(1));
		if (crossProduct == 0 && checkMidPoint(endVectors.get(0).getEndPoint(), endVectors.get(1).getEndPoint(), point)) {
			return true;
		}
		
		boolean isPositive = crossProduct > 0;
		for (int i = 1; i < endPointCount; i++) {
			Vector v1 = endVectors.get(i);
			Vector v2 = endVectors.get((i + 1) % endPointCount);
			crossProduct = v1.getCrossProduct(v2); 
			if (crossProduct == 0 && checkMidPoint(v1.getEndPoint(), v2.getEndPoint(), point)) {
				return true;
			}
			if(isPositive != (crossProduct > 0))
				return false;
		}
		return true;
	} 

	/**
	 * Check whether the point is between the beginPoint and the endPoint
	 * @param begin
	 * @param end
	 * @param point
	 * @return
	 */
	private boolean checkMidPoint(Point begin, Point end, Point point) {
		double xDiff1 = point.getX() - begin.getX();
		double xDiff2 = point.getX() - end.getX();
		double yDiff1 = point.getY() - begin.getY();
		double yDiff2 = point.getY() - end.getY();
		return xDiff1 * xDiff2 <= 0 && yDiff1 * yDiff2 <= 0;
	}
	
	/**
	 * Sort the end-points of a rectangle counterclockwise or clockwise. 
	 * However, the specific clockwise or counterclockwise direction is uncertain.
	 * @return
	 */
	public ArrayList<Point> getOneWayEndPoints() {
		ArrayList<Point> endPoints = new ArrayList<Point>();
		endPoints.add(this.beginOne);
		endPoints.add(this.endOne);
		endPoints.add(this.endTwo);
		endPoints.add(this.beginTwo);
		
		return endPoints;
	}
		
	/**
	 * Calculate the four end points of a rectangle.
	 */
	private void calculateFourPoints(Point begin, Point end) {
		this.setBeginPoint(begin);
		this.setEndPoint(end);
		ArrayList<Point> endExtPoints = getEndPoints(begin, end);
		this.setEndOne(endExtPoints.get(0));
		this.setEndTwo(endExtPoints.get(1));
		
		ArrayList<Point> beginExtPoints = getEndPoints(end, begin);
		this.setBeginOne(beginExtPoints.get(0));
		this.setBeginTwo(beginExtPoints.get(1));
	}
	
	/**
	 * Get two points that close to the end point.
	 * The two points is sorted from left to right, from top to bottom.
	 * @param begin
	 * @param end
	 * @return
	 */
	private ArrayList<Point> getEndPoints(Point begin, Point end) {
		ArrayList<Point> endPoints = new ArrayList<Point>();
		
		double extEndX = end.getX() + distance / segLen * (end.getX() - begin.getX());
		double extEndY = end.getY() + distance / segLen * (end.getY() - begin.getY());
	//	Point extEndPoint = new Point(extEndX, extEndY);
		
		double xSol = extEndY - end.getY();
		double ySol = extEndX - end.getX();
		
		if(xSol == 0 && ySol == 0)
			throw new IllegalArgumentException("The two end points overlaps");			
		
		Point leftEndPoint = new Point(extEndX + xSol, extEndY - ySol);
		Point rightEndPoint = new Point(extEndX - xSol, extEndY + ySol);
		Point pointOne = null;
		Point pointTwo = null;
		if(xSol > 0 || (xSol == 0 && ySol > 0)) {
			pointOne = rightEndPoint;
			pointTwo = leftEndPoint;
		} else if(xSol < 0 || (xSol == 0 && ySol < 0)) {
			pointOne = leftEndPoint;
			pointTwo = rightEndPoint;
		} 
		
		endPoints.add(pointOne);
		endPoints.add(pointTwo);
		return endPoints;
	} 
	
	public static double getDistance() {
		return distance;
	}
	
	public static void setDistance(double dis) {
		distance = dis;
	}

	public Point getBeginPoint() {
		return beginPoint;
	}

	public void setBeginPoint(Point beginPoint) {
		this.beginPoint = beginPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Point getBeginOne() {
		return beginOne;
	}

	public void setBeginOne(Point beginOne) {
		this.beginOne = beginOne;
	}

	public Point getBeginTwo() {
		return beginTwo;
	}

	public void setBeginTwo(Point beginTwo) {
		this.beginTwo = beginTwo;
	}

	public Point getEndOne() {
		return endOne;
	}

	public void setEndOne(Point endOne) {
		this.endOne = endOne;
	}

	public Point getEndTwo() {
		return endTwo;
	}

	public void setEndTwo(Point endTwo) {
		this.endTwo = endTwo;
	}
 }
