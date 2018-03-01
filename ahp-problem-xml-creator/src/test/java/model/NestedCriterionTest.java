package model;

import org.junit.Test;

public class NestedCriterionTest {

	@Test
	public void initTest() {
		NestedCriterion nested = new NestedCriterion("pierwszy_n", 0, "");
		NestedCriterion subnested = new NestedCriterion("podpierwszy_n", 0, "");
		
		SimpleCriterion criterion1 = new SimpleCriterion("A", 1);
		SimpleCriterion criterion2 = new SimpleCriterion("B", 1);
		SimpleCriterion criterion3 = new SimpleCriterion("C", 1);
		
		SimpleCriterion subcriterion1 = new SimpleCriterion("a", 1);
		SimpleCriterion subcriterion2 = new SimpleCriterion("b", 1);
		SimpleCriterion subcriterion3 = new SimpleCriterion("c", 1);
		SimpleCriterion subcriterion4 = new SimpleCriterion("d", 1);
		
		subnested.addSubcriterion(subcriterion1);
		subnested.addSubcriterion(subcriterion2);
		subnested.addSubcriterion(subcriterion3);
		subnested.addSubcriterion(subcriterion4);
		
		nested.addSubcriterion(subnested);
		nested.addSubcriterion(criterion1);
		nested.addSubcriterion(criterion2);
		nested.addSubcriterion(criterion3);
		
		nested.init(2);
		
		System.out.println(nested);
	}

}
