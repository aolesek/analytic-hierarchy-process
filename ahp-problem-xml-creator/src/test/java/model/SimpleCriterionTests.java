package model;

import static org.junit.Assert.*;

import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

public class SimpleCriterionTests {

	@Test
	public void consistencyIndexTest() {
		SimpleCriterion criterion = new SimpleCriterion("Kryterium", 0);
		double[][] data = new double[][] {
			{1, 		2, 		4},
			{0.5,		1,		8},
			{0.25,		0.125,	1}
		};
		
		DenseMatrix64F comparsionMatrix = new DenseMatrix64F(data);
		
		criterion.setMatrix(comparsionMatrix);
		//System.out.println(criterion.getConsistencyRatio());
		
		assertEquals(0.2119, criterion.getConsistencyRatio(), 0.01);
	}
	
	@Test
	public void zeroConsistencyRatioTest() {
		SimpleCriterion criterion = new SimpleCriterion("Kryterium", 0);
		double[][] data = new double[][] {
			{1, 		2, 		4},
			{0.5,		1,		2},
			{0.25,		0.5,	1}
		}; //perfectly consistent
		
		DenseMatrix64F comparsionMatrix = new DenseMatrix64F(data);
		
		criterion.setMatrix(comparsionMatrix);
		
		assertEquals(0.0, criterion.getConsistencyRatio(), 0.01);
		
		criterion = new SimpleCriterion("Kryterium", 0);
		data = new double[][] {
			{1, 		1, 		1},
			{1,			1,		1},
			{1,			1,		1}
		}; //perfectly consistent
		
		comparsionMatrix = new DenseMatrix64F(data);
		
		criterion.setMatrix(comparsionMatrix);
		
		assertEquals(0.0, criterion.getConsistencyRatio(), 0.01);
	}
	
	@Test
	public void stepConsistencyRatioTest() {
		SimpleCriterion criterion = new SimpleCriterion("Kryterium", 0);
		double[][] data = new double[][] {
			{1, 		2, 		4},
			{0.5,		1,		1},
			{0.25,		1,		1}
		}; //perfectly consistent		
		DenseMatrix64F comparsionMatrix = new DenseMatrix64F(data);
		
		criterion.setMatrix(comparsionMatrix);
		assertTrue(criterion.isConsistent());
		assertTrue(criterion.getConsistencyRatio() < 0.1);
	}
	
	@Test
	public void set_SinleValueTest() {
		SimpleCriterion criterion = new SimpleCriterion("arek", 0);
		double[][] data = new double[][] {
			{1, 		1, 		1,		1,		1},
			{1,			1,		1,		1,		1},
			{1,			1,		1,		1,		1},
			{1,			1,		1,		1,		1},
			{1,			1,		1,		1,		1}
		};	
		DenseMatrix64F comparsionMatrix = new DenseMatrix64F(data);
		criterion.setMatrix(comparsionMatrix);

		criterion.setSingleValue(1, 2);
		criterion.setSingleValue(6, 8);
		
		assertEquals(criterion.getMatrix().get(1, 0), 0.5, 0.001);
		assertEquals(criterion.getMatrix().get(0, 1), 2, 0.001);
		assertEquals(criterion.getMatrix().get(3, 1), 0.125, 0.001);
		assertEquals(criterion.getMatrix().get(1, 3), 8, 0.001);

	}
	
	@Test
	public void initTest() {
		SimpleCriterion criterion = new SimpleCriterion("arek", 0);
		criterion.init(2);
		assertEquals(criterion.getMatrix().getNumCols(), 2);

		double sum = 0;
		for (int i = 0; i < criterion.getMatrix().getNumElements(); i++) {
			sum += criterion.getMatrix().get(i);
		}
		
		assertEquals(sum, 4.0, 0.001);
		
		criterion = new SimpleCriterion("arek", 0);
		criterion.init(7);
		assertEquals(criterion.getMatrix().getNumCols(), 7);

		sum = 0;
		for (int i = 0; i < criterion.getMatrix().getNumElements(); i++) {
			sum += criterion.getMatrix().get(i);
		}
		
		assertEquals(sum, 49.0, 0.001);
	}


}
