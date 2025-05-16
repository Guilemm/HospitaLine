package hospital.ifaces;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import db.pojos.Appointment;
import db.pojos.Doctor;
import db.pojos.MedicalRecord;
import db.pojos.MedicineSupply;
import db.pojos.Patient;

public interface HospitalManager {
	

	public boolean EliminateAppointment(int id, int patid);//Solo para el paciente
	
	public boolean ModifyAppointment(int apoid, Appointment apo) throws SQLException;
	
	public void BookAppointment(Appointment apo) throws SQLException;
	
	public boolean CheckAvailability(int hour, int docid, Date date);
	
	public void AddAppointmentsfromXml(ArrayList<Appointment> apos, int patid) throws SQLException;
	
	public void AddDoctor(Doctor doc);

	public Doctor ViewDoctorInfo(int docidsee);//Este deberian ser dos, uno para ver a todos y otro para ver solo uno, usado por pacientes

    public ArrayList<Doctor> ViewAllDoctors();
    
    public Patient getPatientByEmail(String email);
    
    public ArrayList<Appointment> getAppointmentsPatient(int id);

    public ArrayList<MedicalRecord> ViewAllMedicalRecords(int patid);//Patient y doctors, patient si es suyo, doctor si el patient tiene un appointment con el

    public MedicalRecord ViewOneMedicalRecord(int medrecid, int patid);
    
    public boolean ClaimMedicine(int medrecidclaim, int patid);
    
    public void AddPatient(Patient pati);
    
    public Doctor getDoctorByEmail(String email);
    
    public ArrayList<Appointment> getAppointmentsDoc(int docid);
    
    public ArrayList<Patient> getPatientsByDoctor(int docid);
    
    public ArrayList<MedicineSupply> getAllMedicines();
    
    public void addMedicine(MedicineSupply med);
    
    public boolean validatePatientOfTheDoctor(int docid, int patid);
    
    public ArrayList<MedicalRecord> getMedicalRecordsByPatient(int patid);
    
    public boolean addMedicalRecord(MedicalRecord record);
    
    public boolean isMedicineAvailable(int medId);
    
    public boolean VerifyMedicalRecord(int medrecid, int patid);
    
    public boolean updateMedicalRecord(MedicalRecord medrec);
    
    //public void AddMedicalRecordsfromXml(ArrayList<MedicalRecord> medrecs);
    
    public void AddMedicinesfromXml(ArrayList<MedicineSupply> meds);
    
    public void DeletePatient(int id);
    
    public void DeleteDoctor(int id);

}
