package ahp;

import org.ejml.data.D1Matrix64F;

public interface Criterion extends Comparable<Criterion>{

	public int getId();

	public void calculatePriorityVector();
	
	public D1Matrix64F getPriorityVector();
	
}
