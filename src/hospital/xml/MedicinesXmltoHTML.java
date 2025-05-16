package hospital.xml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class MedicinesXmltoHTML {
	
	public static void main(String[] args)
	{
		try
		{
			InputStream xmlInput = new FileInputStream("./xml/Medicines.xml");
            InputStream xsltInput = new FileInputStream("./xml/MedicinesStyle.xslt");
            OutputStream htmlOutput = new FileOutputStream("./xml/Medicines.html");
            
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltInput));
            
            transformer.transform(new StreamSource(xmlInput), new StreamResult(htmlOutput));
            
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
