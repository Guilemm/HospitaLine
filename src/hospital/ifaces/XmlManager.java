package hospital.ifaces;

import java.io.File;
import javax.xml.bind.UnmarshalException;
import java.util.ArrayList;

import db.pojos.Appointment;
import db.pojos.MedicalRecord;
import db.pojos.MedicineSupply;
import db.pojos.Patient;

public interface XmlManager {
	
	public void Java2XmlAppointments(ArrayList<Appointment> apos);
	
	public ArrayList<Appointment> Xml2JavaAppointments();
	
	public void Java2XmlMedicines(ArrayList<MedicineSupply> meds);
	
	public ArrayList<MedicineSupply> Xml2JavaMedicines() throws UnmarshalException;
	
	public void Java2HTMLMedicines(ArrayList<MedicineSupply> meds);
	
}
