package ahp;

import org.ejml.data.DenseMatrix64F;

public class Application {

	public static void main(String[] args) {
		XmlAhpProblem xml = new XmlAhpProblem("file.xml");
		AHPProblem problem = xml.getFinalProblem();
		DenseMatrix64F result = problem.getResult();

		for (int i = 0; i < problem.getAlternativesQuantity(); i++) {
			System.out.println(problem.getAlternatives().get(i).toString() + "\t\t" + result.get(i,0));
		}
	}

}
