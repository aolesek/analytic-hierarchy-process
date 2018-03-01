package ahp;

import java.util.ArrayList;
import java.util.Collections;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;
import org.ejml.simple.SimpleMatrix;

public class SimpleCriterion implements Criterion{

	public SimpleCriterion(String name,int id,  String comparsionMatrixString, int dim) {
		super();
		this.name = name;
		this.id = id;
		this.comparsionMatrixString = comparsionMatrixString;
		this.dim = dim;
		parseString();
		calculatePriorityVector();
	}
	
	public SimpleCriterion() {
		super();
		this.name = new String();
		this.comparsionMatrix = new DenseMatrix64F();
		this.priorityVector = new DenseMatrix64F();
		this.comparsionMatrixString = new String();
	}

	private void parseString() {
		String[] parts = comparsionMatrixString.split(" ");
		double[] numbers = new double[parts.length];
		for (Integer i = 0; i < parts.length; i++) {
			numbers[i] = Double.parseDouble(parts[i]);
			
		}
		comparsionMatrix = new DenseMatrix64F(dim, dim, true, numbers);
	}
	
	public void calculatePriorityVector() throws IllegalArgumentException {
		EigenDecomposition<DenseMatrix64F> decomposition = DecompositionFactory.eig(dim, true);
		decomposition.decompose(comparsionMatrix);
		
		Double theGreatest = 0.0;
		Integer theGreatestIndex = -1;
		
		for (int j = 0; j < decomposition.getNumberOfEigenvalues(); j++) {
			if (decomposition.getEigenvalue(j).isReal() && decomposition.getEigenvalue(j).getReal() > theGreatest) {
				theGreatest = decomposition.getEigenvalue(j).getReal();
				theGreatestIndex = j;
			}
		}
		
		if (theGreatestIndex < 0) throw new IllegalArgumentException();
		
		priorityVector = decomposition.getEigenVector(theGreatestIndex);
		
		Double sum = 0.0;	priorityVector = decomposition.getEigenVector(theGreatestIndex);
		for (int j = 0; j < priorityVector.getNumRows(); j++) {
			if (priorityVector.get(j, 0) < 0) priorityVector.set(j, 0, -1.0 * priorityVector.get(j,0));
			sum += priorityVector.get(j,0);
		}
		for (int j = 0; j < priorityVector.getNumRows(); j++) {
			 priorityVector.set(j, 0, 1/sum * priorityVector.get(j,0));
		}

	}
	
	@Override
	public String toString() {
		return name + "(" + id + "): " + comparsionMatrix.toString() + "\n" + priorityVector.toString();
	}
	
	public String getName() {
		return name;
	}
		
	public DenseMatrix64F getComparsionMatrix() {
		return comparsionMatrix;
	}
		
	public DenseMatrix64F getPriorityVector() {
		return priorityVector;
	}
	
    @Override
    public int compareTo(Criterion o) {
        return Integer.compare(this.id, o.getId());
    }
		
	private String name;
	private DenseMatrix64F comparsionMatrix;
	private String comparsionMatrixString;
	private DenseMatrix64F priorityVector;
	private int dim;
	private int id;
	@Override
	public int getId() {
		return this.id;
		
	}

		
}
