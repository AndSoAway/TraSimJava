package com.ts.quad;

public class Point {
	private double x;
	private double y;
	
	public Point(double aX, double aY) {
		this.setX(aX);
		this.setY(aY);
	}

	public Point(Point aPoint) {
		this.x = aPoint.getX();
		this.y = aPoint.getY();
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(").append(this.x).append(", ").append(this.y).append(")");
		return sb.toString();
	}
}
