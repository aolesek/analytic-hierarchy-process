package ahp;

import org.ejml.data.D1Matrix64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;

public class NestedCriterion implements Criterion {

	private String name;
	AHPProblem subproblem;
	private DenseMatrix64F priorityVector;
	private int id;
	
	public NestedCriterion(String name, int id, AHPProblem subproblem) {
		this.name = name;
		this. id = id;
		this.subproblem = subproblem;
	}
	
	@Override
	public int getId() {
		return this.id;
		
	}

	
	@Override
	public void calculatePriorityVector() throws IllegalArgumentException {
		this.priorityVector = subproblem.getResult();
	}
	
    public int compareTo(NestedCriterion o) {
        return Integer.compare(this.id, o.id);
    }

	public AHPProblem getSubproblem() {
		return subproblem;
	}

	public void setSubproblem(AHPProblem subproblem) {
		this.subproblem = subproblem;
	}

	@Override
	public int compareTo(Criterion o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public D1Matrix64F getPriorityVector() {
		calculatePriorityVector();
		return this.priorityVector;
	}

}
