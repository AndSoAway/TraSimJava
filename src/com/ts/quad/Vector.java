package com.ts.quad;

public class Vector {
	private Point beginPoint;
	private Point endPoint;
	private Point vectorPoint;
	
	public Vector(Point begin, Point end) {
		this.beginPoint = new Point(begin.getX(), begin.getY());
		this.endPoint = new Point(end.getX(), end.getY());
		this.vectorPoint = new Point(end.getX() - begin.getX(), end.getY() - begin.getY());
	}
	
	/**
	 * v1 =(x1, y1), v2 = (x2, y2), the crossProduct is x1 * y2 - x2 * y1
	 * @param vector
	 * @return
	 */
	public double getCrossProduct(Vector vector) {
		return (this.vectorPoint.getX() * vector.getVectorPoint().getY() - this.vectorPoint.getY() * this.vectorPoint.getX());
	}
	
	public Point getVectorPoint() {
		return this.vectorPoint;
	}
	
	public Point getBeginPoint() {
		return this.beginPoint;
	}
	
	public Point getEndPoint() {
		return this.endPoint;
	}
}
