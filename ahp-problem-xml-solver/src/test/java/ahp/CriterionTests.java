package ahp;

import static org.junit.Assert.*;

import org.junit.Test;

public class CriterionTests {

	@Test
	public void test() {
		SimpleCriterion criterion = new SimpleCriterion("Klimat", 1, "1 2 3 0.5 1 4 0.33 0.25 1", 3);
		System.out.println(criterion.getPriorityVector().toString());
		fail("Not yet implemented");
	}

}

//"1 2 3 0.5 1 4 0.33 0.25 1"