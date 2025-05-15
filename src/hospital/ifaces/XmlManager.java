package hospital.ifaces;

import java.io.File;
import java.util.ArrayList;

import db.pojos.Appointment;
import db.pojos.MedicalRecord;
import db.pojos.Patient;

public interface XmlManager {
	
	public void Java2XmlAppointments(ArrayList<Appointment> apos);
	
	public ArrayList<Appointment> Xml2JavaAppointments();
	
	public void Java2XmlMedicalRecords(ArrayList<MedicalRecord> medrecs);
	
	public ArrayList<MedicalRecord> Xml2JavaMedicalRecords();
	
}
