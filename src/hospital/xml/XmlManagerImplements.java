package hospital.xml;

import java.io.File;

import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import db.pojos.Appointment;
import db.pojos.MedicalRecord;
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
	public void Java2XmlMedicalRecords(ArrayList<MedicalRecord> medrecs) {
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(MedicalRecordsList.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
			
			File file=new File("./xml/MedicalRecords.xml");
			
			MedicalRecordsList medrecslist = new MedicalRecordsList();
	        medrecslist.setMedicalRecord(medrecs);
			marshaller.marshal(medrecslist, file);
				
		} 
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public ArrayList<MedicalRecord> Xml2JavaMedicalRecords() {
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(MedicalRecordsList.class);
			Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
			
			File file = new File("./xml/MedicalRecords.xml");
			
			MedicalRecordsList medrecs = (MedicalRecordsList) unmarshaller.unmarshal(file);
			ArrayList<MedicalRecord> medrecsreturn = new ArrayList<>(medrecs.getMedicalRecord());
			
			return medrecsreturn;
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Appointment> Xml2JavaAppointments() {
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(AppointmentsList.class);
			Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
			File file = new File("./xml/Appointments.xml");
			
			AppointmentsList apos = (AppointmentsList) unmarshaller.unmarshal(file);
			ArrayList<Appointment> aposreturn = new ArrayList<>(apos.getAppointment());
			
			return aposreturn;
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	
	

}
