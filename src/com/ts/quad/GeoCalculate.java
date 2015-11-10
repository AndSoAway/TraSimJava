package com.ts.quad;

/**
 * 
 * @author Qinger
 *
 */
public class GeoCalculate {
	private static double PRECISE = 1e-6;
	
	/**
	 * Judge whether two segments intersects.
	 * Each segment has two end points. If end point lies in the other segment, they intersects.
	 * Firstly, judge whether the rectangle whose diagonals are segments intersects. If they don't intersect, segments don't intersect.
	 * And then, use the cross product to judge whether end points of one segment lies in the same side of the other segment. 
	 * If not so, segments intersect.
	 * @param begin1
	 * @param end1
	 * @param begin2
	 * @param end2
	 * @return
	 */
	public static boolean segIntersectSeg(Point begin1, Point end1, Point begin2, Point end2) {
		Rectangle rect1 = new Rectangle(begin1, end1, false);
		Rectangle rect2 = new Rectangle(begin2, end2, false);
		if(rect1.intersectRectangle(rect2) == false)
			return false;
		
		double crossProduct1 = GeoCalculate.crossProduct(begin2, begin1, end2);
		if(crossProduct1 <= PRECISE && crossProduct1 >= -PRECISE)
			return true;
		double crossProduct2 = GeoCalculate.crossProduct(begin2, end1, end2);
		if(crossProduct2 <= PRECISE && crossProduct2 >= -PRECISE)
			return true;
		double crossProduct3 = GeoCalculate.crossProduct(begin1, begin2, end1);
		if(crossProduct3 <= PRECISE && crossProduct3 >= -PRECISE)
			return true;
		double crossProduct4 = GeoCalculate.crossProduct(begin1, end1, end2);
		if(crossProduct4 <= PRECISE && crossProduct4 >= -PRECISE)
				return true;
		if(crossProduct1 * crossProduct2 <= PRECISE)
			return true;
		else 
			return false;
	} 

	
	/**
	 * Cross Product of vector v1 = (a, b) and v2 = (c, d) is P = v1 X v2 = (a * d - c * b)
	 * If P > 0, then v1 is at the clockwise direction of v2
	 * P < 0, then v1 is at the anticlockwise direction of v2
	 * P = 0, then v1 is collinear with v2
 	 *  
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static double crossProduct(double a, double b, double c, double d) {
		return (a * d - c * b);
	}
	
	/**
	 * Cross Product of Vector v1 = P2 - P1, and Vector v2 = P3 - P1
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static double crossProduct(Point p1, Point p2, Point p3){
		return crossProduct(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p3.getX() - p1.getX(), p3.getY() - p1.getY());
	}
	
	/**
	 * Get the length of segment
	 * @param begin
	 * @param end
	 * @return
	 */
	public static double segmentLength(Point begin, Point end) {
		double x = begin.getX() - end.getX();
		double y = begin.getY() - end.getY();
		return Math.sqrt(x * x + y * y);
	}
}
