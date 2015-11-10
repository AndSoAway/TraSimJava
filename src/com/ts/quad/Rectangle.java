package com.ts.quad;

import java.util.ArrayList;

public class Rectangle {
	private Point leftBottom;
	private Point rightUpper;
	
	
	public Rectangle(double aLbX, double aLbY, double aRuX, double aRuY) {
		this.leftBottom = new Point(aLbX, aLbY);
		this.rightUpper = new Point(aRuX, aRuY);
	}
	
	/**
	 * The leftBottom(x1, y1), the rightUpper(x2, y2), the arguments consists of(x1, y1, x2, y2)
	 * @param coordinates
	 */
	public Rectangle(ArrayList<Double> coordinates) {
		this.leftBottom = new Point(coordinates.get(0), coordinates.get(1));
		this.rightUpper = new Point(coordinates.get(2), coordinates.get(3));
	}
	
	/*
	public Rectangle(Point leftBottom, Point rightUpper) {
		this.leftBottom = new Point(leftBottom);
		this.rightUpper = new Point(rightUpper);
	}
	*/
	
	public Rectangle(Point one, Point two, boolean sorted) {
		Point leftBottom = null;
		Point rightUpper = null;
		if(sorted) {
			leftBottom = one;
			rightUpper = two;
		} else {
			double minX = 0.0;
			double maxX = 0.0;
			double minY = 0.0;
			double maxY = 0.0;
			if (one.getX() > two.getX()) {
				minX = two.getX();
				maxX = one.getX();
			} else {
				minX = one.getX();
				maxX = two.getX();
			}
			if (one.getY() > two.getY()) {
				minY = two.getY();
				maxY = one.getY();
			} else {
				minY = one.getY();
				maxY = two.getY();
			}
			leftBottom = new Point(minX, minY);
			rightUpper = new Point(maxX, maxY);
		}
		this.leftBottom = leftBottom;
		this.rightUpper = rightUpper;
	} 
	
	/**
	 * Get the center point of this rectangle
	 * @return
	 */
	public Point getCenterPoint() {
		double cntrX = (this.leftBottom.getX() + this.rightUpper.getX()) / 2;
		double cntrY = (this.leftBottom.getY() + this.rightUpper.getY()) / 2;
		Point centerPoint = new Point(cntrX, cntrY);
		return centerPoint;
	}
	
	public Point getLeftBottom() {
		return this.leftBottom;
	}
	
	public Point getRightUpper() {
		return this.rightUpper;
	}
	
	public boolean containPoint(Point p) {
		return leftBottom.getX() <= p.getX() && p.getX() <= rightUpper.getX() && leftBottom.getY() <= p.getY() && p.getY() <= rightUpper.getY();
	}
	
	/**
	 * Firstly, if rectangle contains one or two end point of segment, they must intersect. 
	 * Otherwise, if diagonals of rectangle intersect segment, they must intersect.
	 * If not, the segment doesn't intersect rectangle.
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean isIntersectSegment(Point begin, Point end) {
		if(containPoint(begin) || containPoint(end))
			return true;
		
		Point leftUpper = new Point(this.leftBottom.getX(), this.rightUpper.getY());
		Point rightBottom = new Point(this.rightUpper.getX(), this.leftBottom.getY());
		if(GeoCalculate.segIntersectSeg(leftUpper, rightBottom, begin, end)) {
			return true;
		} else if(GeoCalculate.segIntersectSeg(this.leftBottom, this.rightUpper, begin, end)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Split the current rectangle containing current node into four small rectangles using the center point
	 * @return
	 */
	public ArrayList<Rectangle> getSplitRectangles() {
		Point centerPoint = this.getCenterPoint();
		Rectangle leftUpper = new Rectangle(this.getLeftBottom().getX(), centerPoint.getY(), centerPoint.getX(), this.getRightUpper().getY());
		Rectangle leftBottom = new Rectangle(this.getLeftBottom(), centerPoint, true);
		Rectangle rightUpper = new Rectangle(centerPoint, this.getRightUpper(), true);
		Rectangle rightBottom = new Rectangle(centerPoint.getX(), this.getLeftBottom().getY(), this.getRightUpper().getX(), centerPoint.getY());
		ArrayList<Rectangle> childRectangles = new ArrayList<Rectangle>();
		childRectangles.add(leftUpper);
		childRectangles.add(leftBottom);
		childRectangles.add(rightUpper);
		childRectangles.add(rightBottom);
		return childRectangles;
	}
	
	/**
	 * Judge whether rectangle intersects another rectangle
	 * @param rect
	 * @return
	 */
	public boolean intersectRectangle(Rectangle rect) {
		return !(leftBottom.getX() > rect.getRightUpper().getX() || rect.getLeftBottom().getX() > rightUpper.getX() || leftBottom.getY() > rect.getRightUpper().getY() || rect.getLeftBottom().getY() > rightUpper.getY());
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("End Points:" + this.leftBottom.toString()).append(" ").append(this.rightUpper.toString());
		return sb.toString();
	}
}
