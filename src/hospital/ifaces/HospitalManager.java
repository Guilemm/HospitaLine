package hospital.ifaces;

import db.pojos.Appointments;
import db.pojos.Doctor;
import db.pojos.Patient;

public interface HospitalManager {
	
	public void BookAppointment();
	
	public void EliminateAppointment();
	
	public void ModifyAppointment();//Ver si hacer view appointments
	
	public void ViewDoctorInfo();
	
	public void ViewMedicalRecord();
	
	//Hasta aqui son los de patient, creo que los tres primeros los puede compartir con doctor
	
	public void ViewPatientInfo();
	
	public void UpdateMedicalRecord();
	
	public void AddMedicalRecord();
	
	public void ClaimMedicine();//Con este metodo ya sí que se va la medicine del medicine supply, y tambien se va la medprescid de esa medicina del medical record concreto
	
	public void AddMedicine();//Para añadir medicina al medicinesupply
	
	public void AddPatient();
	
	public void AddDoctor();
}
