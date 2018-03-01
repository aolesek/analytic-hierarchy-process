package model;

public class Alternative {
	private String name;
	private Integer id;
	
	public Alternative(String name, Integer id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public Integer getId() {
		return id;
	}
	
	public String toString() {
		return "\n"+name + "[" + id.toString() + "]";
	}
	
	@Override
	public boolean equals(Object alt) {
		Alternative a = (Alternative) alt;
		if (alt == null) return false;
		if (a.name.equals(name)) return true;
		return false;
	}
}
