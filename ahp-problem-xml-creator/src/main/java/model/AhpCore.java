package model;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AhpCore {
	private ArrayList<Alternative> alternatives = new ArrayList<Alternative>();
	private NestedCriterion rootCriterion = new NestedCriterion("root", 0, "");
	private Boolean pretty = true;
	private String goal;
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	Document doc;
	private static AhpCore instance;
	private Double consistencyRatio = 0.1;
	
	
	private AhpCore() {
	}
	
	public static AhpCore getInstance() {
		if (instance == null) {
			instance = new AhpCore();
			instance.setGoal("Cel");
			return instance;
		}
		return instance;
	}
	
	public Double getCR() {
		return consistencyRatio;
	}
	
	public void setCR(Double cr) {
		this.consistencyRatio = cr;
	}
	
	public void setGoal(String goal) {
		this.goal = goal;
	}
	
	public String getGoal() {
		return this.goal;
	}
	
	public void setRoot(NestedCriterion root) {
		this.rootCriterion = root;
	}
	
	public void prettyFormat() {
		this.pretty = true;
	}
	
	public void noPrettyFormat() {
		this.pretty = false;
	}
	
	public NestedCriterion getRootCriterion() {
		return this.rootCriterion;
	}
	
	public void addAlternative(Alternative a) {
		alternatives.add(a);
	}
	
	public void removeAlternative(Alternative a) {
		alternatives.remove(a);
	}
	
	public ArrayList<Alternative> getAlternatives() {
		return alternatives;
	}
	
	public void addCriterion(Criterion c) {
		rootCriterion.getSubcriteria().add(c);
	}
	
	public void addCriterion(Criterion c, Integer parentId) {
		if (parentId == 0) {
			rootCriterion.getSubcriteria().add(c);
		} else {
			Criterion parent = searchCriterion(parentId, rootCriterion);
			if (parent == null) {
				System.out.println("Nie moge znalezc rodzica, na pewno wybrano odpowiedni typ kryterium?");
			} else
				c.den = parent.den + 1;
				parent.getSubcriteria().add(c);
		}
	}
	
	public Criterion searchCriterion(Integer id, Criterion parentNode) {
		if (parentNode.getId() == id) return parentNode;
		if (parentNode.getSubcriteria() != null) {
			for (int i = 0; i < parentNode.getSubcriteria().size(); i++) {
				Criterion c = searchCriterion(id, parentNode.getSubcriteria().get(i));
				if (c != null) return c;
			}
		}
		

		return null;
	}
	
	public void processXML() {
		try {

			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement("ahp");
			doc.appendChild(rootElement);
			
			Element goalElement = doc.createElement("goal");
			Element goalName = doc.createElement("name");
			goalName.setTextContent(this.goal);
			goalElement.appendChild(goalName);
			rootElement.appendChild(goalElement);
			
			Element altRoot = doc.createElement("alternatives");
			rootElement.appendChild(altRoot);
			
			for (Alternative currentAlt : alternatives) {
				Element altElement = doc.createElement("alternative");
				altRoot.appendChild(altElement);
				
				Element altName = doc.createElement("name");
				altName.setTextContent(currentAlt.getName());
				altElement.appendChild(altName);
				
				Element altId = doc.createElement("id");
				altId.setTextContent(currentAlt.getId().toString());
				altElement.appendChild(altId);
			}
			//Element criteriaMatrix = doc.createElement("criteriamatrix");
			//criteriaMatrix.setTextContent("1");
			Element altCount = doc.createElement("alt");
			altCount.setTextContent( ((Integer)alternatives.size()).toString());
			Element criCount = doc.createElement("cri");
			criCount.setTextContent("1");
			Element criRoot = doc.createElement("criteria");
			criRoot.appendChild(altCount);
			criRoot.appendChild(criCount);
			rootElement.appendChild(criRoot);
			//rootElement.appendChild(criteriaMatrix);
			
						
			processCriterion(criRoot, rootCriterion);
			
			//Creating XML file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", new Integer(2));
			Transformer transformer = transformerFactory.newTransformer();
			if (this.pretty) {
	 			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			}
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("file.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("Plik zapisano !");

		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}
	
	private void processCriterion(Element parent, Criterion criterion) {
		if (criterion.getClass().toString().equals("class model.SimpleCriterion")) {
			SimpleCriterion simple = (SimpleCriterion) criterion;
			Element simpleCriElement = doc.createElement("criterion");
			Element simpleCriName = doc.createElement("name");
			Element simpleCriId = doc.createElement("id");
			Element simpleCriMatrix = doc.createElement("matrix");
			
			simpleCriName.setTextContent(simple.getName());
			simpleCriId.setTextContent(simple.getId().toString());
			simpleCriMatrix.setTextContent(simple.stringMatrix());

			simpleCriElement.appendChild(simpleCriName);
			simpleCriElement.appendChild(simpleCriId);
			simpleCriElement.appendChild(simpleCriMatrix);
			parent.appendChild(simpleCriElement);
		} else {
			NestedCriterion nested = (NestedCriterion) criterion;
			Element nestedCriElement = doc.createElement("criterion");
			Element nestedCriName = doc.createElement("name");
			Element nestedCriId = doc.createElement("id");
			Element nestedCriMatrix = doc.createElement("criteriamatrix");
			Element nestedSubcriteriaCount = doc.createElement("cri");
			
			nestedCriName.setTextContent(nested.getName());
			nestedCriId.setTextContent(nested.getId().toString());
			nestedCriMatrix.setTextContent(nested.stringMatrix());
			nestedSubcriteriaCount.setTextContent( ((Integer) nested.getSubcriteria().size()).toString());
			
			nestedCriElement.appendChild(nestedCriName);
			nestedCriElement.appendChild(nestedCriId);
			nestedCriElement.appendChild(nestedCriMatrix);
			nestedCriElement.appendChild(nestedSubcriteriaCount);
			
			parent.appendChild(nestedCriElement);
			for (Criterion c : nested.getSubcriteria()) {
				processCriterion(nestedCriElement, c);
			}
		}
	}
	
	public String toString() {
		String alts = new String();
		for(Alternative a: alternatives) {
			alts += a.toString();
		}
		
		return alts + "\n" + rootCriterion.oldtoString();
	}

}
