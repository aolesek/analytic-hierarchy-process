package model;

public class ComparsionCoords {
	public Integer x = -1, y = -1;
	public ComparsionCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "("+x.toString()+", "+y.toString()+")";
	}
	
	
}
