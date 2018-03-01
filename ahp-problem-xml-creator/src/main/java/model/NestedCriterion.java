  package model;

import java.util.ArrayList;
import java.util.Arrays;

import org.ejml.data.DenseMatrix64F;

public class NestedCriterion extends Criterion {
	
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public DenseMatrix64F getMatrix() {
		return comparsionMatrix;
	}
	public void setMatrix(DenseMatrix64F matrix) {
		this.comparsionMatrix = matrix;
	}
	
	public NestedCriterion(String name, Integer id, String matrix) {
		this.name = name;
		this.id = id;
		this.comparsionMatrix = new DenseMatrix64F();
		this.comparsionMatrix.setNumCols(3);
		this.comparsionMatrix.setNumRows(3);
		double[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		this.comparsionMatrix.setData(data);
	}
	
	public NestedCriterion(String name, Integer id, String matrix, int maxcr) {
		this(name, id, matrix);
		this.maxConsistencyRatio = maxcr;
	}
	
	public NestedCriterion(String name, Integer id) {
		this.name = name;
		this.id = id;
	}
	
	public NestedCriterion(String name, Integer id, Double maxCR) {
		this(name, id);
		this.maxConsistencyRatio = maxCR;
	}
	
	public void addSubcriterion(Criterion c) {
		subcriteria.add(c);
	}
	
	@Override
	public ArrayList<Criterion> getSubcriteria() {
		return subcriteria;
	}
	public void setSubcriteria(ArrayList<Criterion> subcriteria) {
		this.subcriteria = subcriteria;
	}
	
	@Override
	public void init(int altCount) {
		this.comparsionMatrix = new DenseMatrix64F(this.subcriteria.size(), this.subcriteria.size());
		double[] data = new double[this.subcriteria.size()*this.subcriteria.size()];
		Arrays.fill(data, 1.0);
		comparsionMatrix.setData(data);
		
		for (Criterion c : subcriteria) {
			c.init(altCount);
		}
	}
	
	public String oldtoString() {
		String subcriteriaString = new String();
		String tabs = new String();
		for (int i = 0; i < den; i++)
			tabs += "\t";
		for (Criterion c : subcriteria) {
			subcriteriaString = subcriteriaString + tabs +" ->"+c.toString();
		}
		return name+" ["+id+"]"+"\n"+subcriteriaString;
	}
	
	public String toString() {
		return name+" ["+id+"]";
	}
	
	public String toStringList() {
		String list = new String();
		
		for (Criterion c : subcriteria) {
			list += c.getNameT() + ", ";
		}
		return list;
	}
	
	@Override
	public String getNameT() {
		return name;
	}
	
	public Integer getComparsionsCount() {
		Integer subcriteriaCount = subcriteria.size();
		
		return (subcriteriaCount*subcriteriaCount - subcriteriaCount)/2;
	}
	
	private String name;
	private Integer id;
	private ArrayList<Criterion> subcriteria = new ArrayList<Criterion>();
}
