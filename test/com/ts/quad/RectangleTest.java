package com.ts.quad;

import java.util.ArrayList;

public class RectangleTest {

	public static void getSplitRectanglesTest(Rectangle rect) {
		System.out.println("Original Rectangle:" + rect.toString());
		ArrayList<Rectangle> rectChildren = rect.getSplitRectangles();
		for(Rectangle child : rectChildren) {
			System.out.println("Child Rect: " + child.toString());
		}
	}
	
	public static void isIntersectSegmentTest(Rectangle rect, Point begin, Point end) {
		Boolean res = rect.isIntersectSegment(begin, end);
		System.out.println(begin.toString() + ", " + end.toString() + ": " + res.toString());
	}
	
	public static void getCenterPointTest() {
		
	}
	
	public static void main(String[] args) {
		Rectangle rect = new Rectangle(0, 0, 2, 2);
		Point begin = new Point(-1, 1);
		//Point end1 = new Point(1, -1);
		Point end2= new Point(1, 4);
		isIntersectSegmentTest(rect, begin, end2);
	}
}
