package model;

import java.util.Arrays;

import org.ejml.data.DenseMatrix64F;

public class SimpleCriterion extends Criterion {
	public double getMaxConsistencyRatio() {
		return maxConsistencyRatio;
	}

	public void setMaxConsistencyRatio(double maxConsistencyRatio) {
		this.maxConsistencyRatio = maxConsistencyRatio;
	}

	
	public SimpleCriterion(String name, Integer id) {
		this.name = name;
		this.id = id;
	}
	
	public SimpleCriterion(String name, Integer id, Double maxCR) {
		this(name, id);
		this.maxConsistencyRatio = maxCR;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toStringList() {
		return "["+name+"]";
	}

	@Override
	public Integer getId() {
		return id;
	}
	
	public void setMatrix(DenseMatrix64F matrix) {
		this.comparsionMatrix = matrix;
	}
	
	public DenseMatrix64F getMatrix() {
		return this.comparsionMatrix;
	}
	
	@Override
	public void init(int altCount) {
		this.comparsionMatrix = new DenseMatrix64F(altCount, altCount);
		double[] data = new double[altCount*altCount];
		Arrays.fill(data, 1.0);
		comparsionMatrix.setData(data);
	}
	
	public Integer getComparsionsCount() {
		Integer altCount = comparsionMatrix.getNumCols();
		
		return (altCount*altCount - altCount)/2;
	}
	
	@Override
	public String getNameT() {
		return name+"^";
	}
	
	public String toString() {
		return name+" ["+id+"]";
	}
	
	private String name;
	private Integer id;
}
