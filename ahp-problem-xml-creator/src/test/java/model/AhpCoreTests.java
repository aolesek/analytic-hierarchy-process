package model;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class AhpCoreTests {

	@Test
	public void altAddTest() throws IOException {
		AhpCore core = AhpCore.getInstance();
		Alternative a1 = new Alternative("Pierwsza", 1);
		Alternative a2 = new Alternative("Druga", 2);
		String correctContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ahp><alternatives><alternative><name>Pierwsza</name><id>1</id></alternative><alternative><name>Druga</name><id>2</id></alternative></alternatives></ahp>";
		
		System.out.println(a1.getClass());
		
		core.prettyFormat();
		core.addAlternative(a1);
		core.addAlternative(a2);
		core.processXML();
		
        BufferedReader file = new BufferedReader(new FileReader("file.xml"));

		assertEquals(file.readLine(), correctContent);
		file.close();
	}

}
