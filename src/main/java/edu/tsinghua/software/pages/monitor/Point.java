package edu.tsinghua.software.pages.monitor;

public class Point {

	
	private String time;
	private String value;
	public Point(String time, String value)
	{
		this.time = time;
		this.setValue(value);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	

}
