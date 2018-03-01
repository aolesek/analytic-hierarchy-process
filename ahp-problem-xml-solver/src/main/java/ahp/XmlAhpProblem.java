package ahp;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ahp.exceptions.InconsistentDataException;

public class XmlAhpProblem implements AhpInput {
	
	AHPProblem problem = new AHPProblem();
	String filepath;
	
	public XmlAhpProblem(String filepath) {
		this.filepath = filepath;
	}
	
	public static void check(Node node){
		  if (node == null || node.getNodeName() == null)
		    return;

		  // Do PostOrder on all children
		  check(node.getFirstChild());  

		  // Now that all children were traversed, process the current node:
		  System.out.println(node); 

		  // Do PostOrder on following siblings
		  check(node.getNextSibling());  
		}
	
	public AHPProblem getFinalProblem() {

		AHPProblem ahp = null;
		
		try {
			File fXmlFile = new File(filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			String goal = ((Element) doc.getElementsByTagName("goal").item(0)).getElementsByTagName("name").item(0).getTextContent();

			Node rootNode = doc.getElementsByTagName("criteria").item(0);
			int altQuantity = Integer.parseInt(
					((Element) rootNode).getElementsByTagName("alt").item(0).getTextContent()
			);


			NodeList criteria = rootNode.getChildNodes();
			int critQuantityCount = 0;
			for (int i = 0; i < criteria.getLength(); i++) {
				if (criteria.item(i).getNodeName().equals("criterion")) {
					critQuantityCount++;
				}
			}

			ahp = parse(rootNode,altQuantity,critQuantityCount);
			ahp.setPriorities(new SimpleCriterion(
					"goal",
					-1,
					"1.0",
					1
			));

			ahp.setGoal(goal);

			NodeList alternatives = doc.getElementsByTagName("alternative");

			for (int i = 0; i < alternatives.getLength(); i++) {
				ahp.addAlternative(
						new Alternative(
								((Element) alternatives.item(i)).getElementsByTagName("name").item(0).getTextContent(),
								i
						)
				);
			}

		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ahp;
	}
	
	public AHPProblem parse(Node rootNode, int altQuantity, int criteriaQuantity) {
		AHPProblem problem = new AHPProblem();
		problem.setAlternativesQuantity(altQuantity);
		problem.setCriteriaQuantity(criteriaQuantity);
		try {		
			Node child = rootNode.getFirstChild();


			while( child.getNextSibling()!=null ){          
				child = child.getNextSibling();         
				if((child.getNodeType() == Node.ELEMENT_NODE) && (child.getNodeName().equals("criterion"))){
					Element childElement = (Element) child;

					NodeList childNodes = childElement.getChildNodes();
					int subnodesQuantity = 0;
					for (int i = 0; i < childNodes.getLength(); i++) {
						if (childNodes.item(i).getNodeName().equals("criterion")) {
							subnodesQuantity++;
						}
					}


					if (subnodesQuantity == 0) {
						problem.addCriterion(
								new SimpleCriterion(
										childElement.getElementsByTagName("name").item(0).getTextContent(),
										Integer.parseInt(childElement.getElementsByTagName("id").item(0).getTextContent()),
										childElement.getElementsByTagName("matrix").item(0).getTextContent(),
										problem.getAlternativesQuantity()

										)
								);

					}
					else {
						NodeList criteria = child.getChildNodes();
						int critQuantityCount = 0;
						for (int i = 0; i < criteria.getLength(); i++) {
							if (criteria.item(i).getNodeName().equals("criterion")) {
								critQuantityCount++;
							}
						}

						AHPProblem subahp;
						subahp = parse(childElement,altQuantity,critQuantityCount);
						subahp.setPriorities(
								new SimpleCriterion(
										childElement.getElementsByTagName("name").item(0).getTextContent()+"priorities",
										0,
										childElement.getElementsByTagName("criteriamatrix").item(0).getTextContent(),
										critQuantityCount
										)
						);

						problem.addCriterion(
								new NestedCriterion(
										childElement.getElementsByTagName("name").item(0).getTextContent(),
										Integer.parseInt(childElement.getElementsByTagName("id").item(0).getTextContent()),
										subahp
										));

					}
				}
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return problem;
	}

}
