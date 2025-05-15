package hospital.ifaces;

import java.util.ArrayList;

import java.util.List;

import db.pojos.Appointment;
import db.pojos.Doctor;
import db.pojos.MedicalRecord;
import db.pojos.Patient;

public interface HospitalManager {
	
	public void BookAppointment(Appointment apo);//Solo para el paciente
	
	public boolean EliminateAppointment(int id, int patid);//Solo para el paciente
	
	public boolean ModifyAppointment(int apoid, Appointment apo);//Solo para paciente, hacer antes de bookear appointments que se vean todos los doctors Ver si hacer view appointments
	
	public Doctor ViewDoctorInfo(int docidsee);//Este deberian ser dos, uno para ver a todos y otro para ver solo uno, usado por pacientes
	
	public ArrayList<Doctor> ViewAllDoctors();
	
	public Patient getPatientByEmail(String email);
	
	public ArrayList<Appointment> getAppointmentsPatient(int id);
	
	public ArrayList<MedicalRecord> ViewAllMedicalRecords(int patid);//Patient y doctors, patient si es suyo, doctor si el patient tiene un appointment con el
	
	public MedicalRecord ViewOneMedicalRecord(int medrecid, int patid);
	
	public void AddAppointmentsfromXml(ArrayList<Appointment> apos, int patid);
	
	public boolean CheckAvailability(int hour, int docid);
	
	//Hasta aqui son los de patient, creo que los tres primeros los puede compartir con doctor
	
	
	
	public void UpdateMedicalRecord();//Solo doctors, igual que el de add, con patients con los que tengan appointments
	
	public void AddMedicalRecord();
	
	public boolean ClaimMedicine(int medrecidclaim, int patid);//Patients, se entra dentro de la opcion medical records. Con este metodo ya sí que se va la medicine del medicine supply, y tambien se va la medprescid de esa medicina del medical record concreto
	
	public void AddMedicine();//Para añadir medicina al medicinesupply, solo doctors
	
	public void AddPatient(Patient pati);
	
	public void AddDoctor(Doctor doc);
}
