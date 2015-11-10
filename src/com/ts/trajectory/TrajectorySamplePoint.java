package com.ts.trajectory;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ts.quad.Point;

/**
 * @author tanayun
 * 
 *         To model sample points in a "Trajectory"
 * 
 *         TAXI_ID, TIME, LONGITUDE, LATITUDE, HEADING-VS-NORTH,
 *         SPEED-IN-Meters/MIN, LOADED
 */
public class TrajectorySamplePoint {
	// E.g., 1,2009-05-04 00:03:31,39.944580,116.357964,69,1800,1

	public TrajectorySamplePoint(int vehicalID, Date timestamp, float latitude,
			float longitude, float headingAngleToNorth, float speed,
			boolean loaded) {
		super();
		this.vehicalID = vehicalID;
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.headingAngleToNorth = headingAngleToNorth;
		this.speed = speed;
		this.loaded = loaded;
	}

	int vehicalID;
	Date timestamp;
	float latitude;
	float longitude;
	float headingAngleToNorth;
	float speed;
	boolean loaded;

	public int getVehicalID() {
		return vehicalID;
	}

	public void setVehicalID(int vehicalID) {
		this.vehicalID = vehicalID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getHeadingAngleToNorth() {
		return headingAngleToNorth;
	}

	public void setHeadingAngleToNorth(float headingAngleToNorth) {
		this.headingAngleToNorth = headingAngleToNorth;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "TrajectorySamplePoint [vehicalID=" + vehicalID + ", timestamp="
				+ sdf.format(timestamp) + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", headingAngleToNorth="
				+ headingAngleToNorth + ", speed=" + speed + ", loaded="
				+ loaded + "]";
	}

	public void printAbstract() {
		System.out.println(latitude + ", " + longitude);
	}

	public Point getPoint() {
		Point point = new Point(this.latitude, this.longitude);
		return point;
	}
}