package model;

import java.util.ArrayList;
import java.util.Collections;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;

public abstract class Criterion {
	
	public Double getConsistencyRatio() {
		if (comparsionMatrix.getNumCols() < 3) return 0.0;
		ArrayList<Double> realEigenvalues = new ArrayList<Double>();
		EigenDecomposition<DenseMatrix64F> eig = DecompositionFactory.eig(comparsionMatrix.getNumCols(), true);
		eig.decompose(comparsionMatrix);
		
		
		for(int i = 0; i < eig.getNumberOfEigenvalues(); i++) {
			if ( Math.abs(eig.getEigenvalue(i).getImaginary()) < 0.0001 )
				realEigenvalues.add(eig.getEigenvalue(i).getReal());
		}
		
		Collections.sort(realEigenvalues);
		
		double maxEigenvalue = realEigenvalues.get(realEigenvalues.size() - 1); //the last element of sorted arraylist of real eigenvalues
		double consistencyIndex = (maxEigenvalue - comparsionMatrix.getNumCols() ) / (comparsionMatrix.getNumCols() - 1);
		double consistencyRatio = consistencyIndex / randomIndex[comparsionMatrix.getNumCols()];
		return Math.abs(consistencyRatio);
	}
	
	public Boolean isConsistent() {
		return this.getConsistencyRatio() < maxConsistencyRatio;
	}
	
	public void setSingleValue(int n, double comparsionValue) {
		Integer sum = 0, y = -1, x = -1, oldsum = 0;
		for (int i = 1; i <= this.comparsionMatrix.getNumCols(); i++) {
			y = i - 1;
			oldsum = sum;
			//diff = n - sum;
			sum += comparsionMatrix.getNumCols() - i;
			if (n <= sum) break;
		}
		
		x = n + y - oldsum;
		
		comparsionMatrix.set(y, x, comparsionValue);
		
		double invertedValue = 1/comparsionValue;
		
		comparsionMatrix.set(x, y, invertedValue);
	}
	
	public ComparsionCoords getSingleComparsionCoords(int n) {
		Integer sum = 0, y = -1, x = -1, oldsum = 0;
		for (int i = 1; i <= this.comparsionMatrix.getNumCols(); i++) {
			y = i - 1;
			oldsum = sum;
			//diff = n - sum;
			sum += comparsionMatrix.getNumCols() - i;
			if (n <= sum) break;
		}
		
		x = n + y - oldsum;
		return new ComparsionCoords(x,y);
	}

	public void init(int altCount) {}
	
	public String stringMatrix() {
		String plainMatrix = new String();
		if (comparsionMatrix == null) return "";
		for (int i = 0; i < comparsionMatrix.getNumElements(); i++) {
			plainMatrix += comparsionMatrix.get(i) + " ";
		}
		
		return plainMatrix;
	}
	
	public ArrayList<Criterion> getSubcriteria() {
		return null;
	}
	
	public Integer getId() {
		return 0;
	}
	
	public abstract String getName();
	
	public abstract String getNameT();
	
	public abstract Integer getComparsionsCount();
	
	public abstract String toStringList();
	
	public void setCR(Double cr) {
		this.maxConsistencyRatio = cr;
	}
	
	protected DenseMatrix64F comparsionMatrix;
	public Integer den = 0;
	protected double maxConsistencyRatio = 0.1;
	protected double[] randomIndex = new double[]{0,0,0,0.5247,0.8816,1.1086,1.2479,1.3417,1.4057,1.4499,1.4854,1.51,1.48,1.56,1.57,1.59};

}
