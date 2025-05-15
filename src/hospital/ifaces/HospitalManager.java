package hospital.ifaces;

import java.sql.Date;
import java.util.ArrayList;

import java.util.List;

import db.pojos.Appointment;
import db.pojos.Doctor;
import db.pojos.MedicalRecord;
import db.pojos.MedicineSupply;
import db.pojos.Patient;

public interface HospitalManager {
	
	

//----------------------------DOCTOR MENU-------------------------
	public Doctor getDoctorByEmail(String email); //Retrieves the doctor assigned to taht email.
	
	public List<Appointment> viewAppointmentsFromDoctor(int doctorID); //retrieves the list of Appointments of a specific doctor.
	
	public boolean validatePatientOfTheDoctor(Doctor doctor, Patient p1); //Validates if a patient is assigned to a doctor seeing if there has been an appointment of that doctor and that patient
	
	public List<Patient> viewPatientsOfADoctor(Doctor doctor); //Retrieves a list of patients associated with a specific doctor based on the appointment records.
	
	public int getMedRecordIDFilteredByADate(Date date, int patientID);//Retrieves the Id of a medRecord filtered by date and patient
	
	
//-------------------------------------MEDICINE SUPPLY-----------------	
	
	public List<MedicineSupply> getAllMedicineSupplies(); //This methods obtains all the medicine supplies from the database.
	
	public boolean addMedicineSupply(MedicineSupply medicine); //It adds a new supply of medicines to the database
	
//-----------------------------------NUEVO DIANA MANAGEMEDICALRECORDS--------------------------
	
	public List<Patient> getPatientsByDoctor(Integer doctorID);//Retrieves all patients (in a list) assigned to a specific doctor
	
	public List<Integer> getSharedAppointmentsIDs(int patientID, int doctorID); //This method retrives a list of ids of the appointments shared by a patiet and a doctor
	
	public List<MedicalRecord> getMedicalRecordsByPatientAndDate(int patientId, Date date); //Retrieves a list of medical records for a specific patient and specific date

	public boolean updateMedicalRecord(MedicalRecord record); //updates a medical record (its diagnose or treatment and the date -> the date every time they update sth)
	
	public boolean addMedicalRecord(MedicalRecord record); //Adds a medicalRecord to the dtaabase

	public boolean isMedicineAvailable(int medId); //Method that sees if the medicine has been already used (is inside a medical record)

	public void deleteMedicineSupply(int medicineId); //elimina una medicineSupply introducida con su ID

	
	
}
