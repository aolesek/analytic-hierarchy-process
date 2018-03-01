package ahp;

public class Alternative implements Comparable<Alternative> {
	private String name;
	private int id;
	
	public Alternative(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return name + "(" + id + ")";
	}
	
	@Override
	public int compareTo(Alternative a) {
        return Integer.compare(this.id,a.id);
    }
}
