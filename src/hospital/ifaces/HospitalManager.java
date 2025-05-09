package hospital.ifaces;

import db.pojos.Appointment;
import db.pojos.Doctor;
import db.pojos.Patient;

public interface HospitalManager {
	
	public void BookAppointment();//Solo para el paciente
	
	public void EliminateAppointment();//Solo para el paciente
	
	public void ModifyAppointment();//Solo para paciente, hacer antes de bookear appointments que se vean todos los doctors Ver si hacer view appointments
	
	public void ViewDoctorInfo();//Este deberian ser dos, uno para ver a todos y otro para ver solo uno, usado por pacientes
	
	public void ViewMedicalRecord();//Patient y doctors, patient si es suyo, doctor si el patient tiene un appointment con el
	
	//Hasta aqui son los de patient, creo que los tres primeros los puede compartir con doctor
	
	public void ViewPatientInfo();//Doctors solo si quieren ver un patient, hacer otro para verlos a todos con los que tienen appointments
	
	public void UpdateMedicalRecord();//Solo doctors, igual que el de add, con patients con los que tengan appointments
	
	public void AddMedicalRecord();
	
	public void ClaimMedicine();//Patients, se entra dentro de la opcion medical records. Con este metodo ya sí que se va la medicine del medicine supply, y tambien se va la medprescid de esa medicina del medical record concreto
	
	public void AddMedicine();//Para añadir medicina al medicinesupply, solo doctors
	
	public void AddPatient();
	
	public void AddDoctor();
}
