package ahp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

public class AHPProblem {
	
	private String goal;
	private SimpleCriterion priorities;
	private ArrayList<Criterion> criteria = new ArrayList<Criterion>();
	private ArrayList<Alternative> alternatives = new ArrayList<Alternative>();
	private int criteriaQuantity, alternativesQuantity;
	private DenseMatrix64F result;
	
	public DenseMatrix64F getResult() {
		result = new DenseMatrix64F(alternativesQuantity, 1);
		updateResult();
		return result;
	}
	
	public void updateResult() {
		Collections.sort(criteria);
		Collections.sort(alternatives);
		
		this.result.zero();
		
		for (int i = 0; i < criteriaQuantity; i++) {
			DenseMatrix64F scale = new DenseMatrix64F(alternativesQuantity, 1);
			scale.zero();
			DenseMatrix64F tmpres = new DenseMatrix64F(alternativesQuantity, 1);
			tmpres.zero();
		    org.ejml.ops.CommonOps.scale(priorities.getPriorityVector().get(i % alternativesQuantity, 0), criteria.get(i).getPriorityVector(), scale);
		    CommonOps.add(result, scale, tmpres);
		    result = tmpres;
		}
	}
	
	public void setGoal(String goal) {
		this.goal = goal;
	}
	
	public void addCriterion(Criterion c) {
		this.criteria.add(c);
	}
	
	public void setPriorities(SimpleCriterion priorities) {
		this.priorities = priorities; 
	}
	
	public void addAlternative(Alternative a) {
		this.alternatives.add(a);
	}
	
	public String toString() {
		return goal + ": \n"+ priorities + "\n" + criteria + "\n" + alternatives;
	}

	public String getGoal() {
		return goal;
	}

	public SimpleCriterion getPriorities() {
		return priorities;
	}

	public ArrayList<Criterion> getCriteria() {
		return criteria;
	}

	public ArrayList<Alternative> getAlternatives() {
		return alternatives;
	}

	public int getCriteriaQuantity() {
		return criteriaQuantity;
	}

	public void setCriteriaQuantity(int criteriaQuantity) {
		this.criteriaQuantity = criteriaQuantity;
	}

	public int getAlternativesQuantity() {
		return alternativesQuantity;
	}

	public void setAlternativesQuantity(int alternativesQuantity) {
		this.alternativesQuantity = alternativesQuantity;
	}

	

}
