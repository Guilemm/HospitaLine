package hospital.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import db.pojos.Appointment;
import db.pojos.MedicalRecord;
import db.pojos.MedicineSupply;
import db.pojos.Patient;
import hospital.ifaces.XmlManager;



public class XmlManagerImplements implements XmlManager{

	

	@Override
	public void Java2XmlAppointments(ArrayList<Appointment> apos) {
		
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(AppointmentsList.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
			
			File file=new File("./xml/Appointments.xml");
			
			AppointmentsList aposlist = new AppointmentsList();
	        aposlist.setAppointment(apos);
			marshaller.marshal(aposlist, file);
			
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void Java2XmlMedicines(ArrayList<MedicineSupply> meds) {
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(MedicinesList.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
			
			File file=new File("./xml/MedicinesExp.xml");
			
			MedicinesList medslist = new MedicinesList();
	        medslist.setMedicine(meds);
			marshaller.marshal(medslist, file);
				
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public ArrayList<MedicineSupply> Xml2JavaMedicines() throws UnmarshalException{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(MedicinesList.class);
			Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
			
			File file = new File("./xml/MedicinesImp.xml");
			
			MedicinesList meds = (MedicinesList) unmarshaller.unmarshal(file);
			ArrayList<MedicineSupply> medsreturn = new ArrayList<>(meds.getMedicine());
			
			return medsreturn;
		}
		catch(UnmarshalException e)
		{
			throw e;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		return null;
	}

	@Override
	public ArrayList<Appointment> Xml2JavaAppointments() throws UnmarshalException{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(AppointmentsList.class);
			Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
			File file = new File("./xml/Appointments.xml");
			
			AppointmentsList apos = (AppointmentsList) unmarshaller.unmarshal(file);
			ArrayList<Appointment> aposreturn = new ArrayList<>(apos.getAppointment());
			
			return aposreturn;
		}
		catch(UnmarshalException e)
		{
			throw e;
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public void Java2HTMLMedicines(ArrayList<MedicineSupply> meds)
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(MedicinesList.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
			
			File file=new File("./xml/MedicinesExpHTML.xml");
			
			MedicinesList medslist = new MedicinesList();
	        medslist.setMedicine(meds);
			marshaller.marshal(medslist, file);
			
			
			InputStream xmlInput = new FileInputStream("./xml/MedicinesExpHTML.xml");
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
