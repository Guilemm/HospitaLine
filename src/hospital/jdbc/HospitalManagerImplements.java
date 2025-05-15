package hospital.jdbc;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import db.pojos.Appointment;
import db.pojos.Doctor;
import db.pojos.MedicalRecord;
import db.pojos.MedicineSupply;
import db.pojos.Patient;
import hospital.ifaces.HospitalManager;

public class HospitalManagerImplements implements HospitalManager {

	

//-------------------------------DOCTOR MENU----------------------------------
	 //Retrieves the doctor assigned to taht email.
	@Override
	public Doctor getDoctorByEmail(String email) {
		String sql = "SELECT * from doctors "
				+ "WHERE email = ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, email);
			
			ResultSet rs = prep.executeQuery();
			//int id, String email, String name, Date dob, int experience, String address, String department, String sex
			prep.close();
			rs.close();
			
			if(rs.next()) {
				Doctor doctor = new Doctor (
						rs.getInt("id"),
						rs.getString("email"),
						rs.getString("name"),
						rs.getDate("dob"),
						rs.getInt("experience"),
						rs.getString("address"),
						rs.getString("department"),
						rs.getString("sex")
						);
				return doctor;
			}else {
				return null; // no encontramso doctor
			}
			
		}catch(SQLException e) {
			System.out.print("Error reaching the doctor by email:"+ email);
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Appointment> viewAppointmentsFromDoctor(int doctorID){
		List<Appointment> appointments = new ArrayList<>();
		
		if (doctorID <= 0) {
	        return appointments; // devuelvo la lista vacia porque no es valido el id
	    }
		
		String sql = "SELECT id, patientid, doctorid, date FROM appointments "
				+ "WHERE doctorid = ? AND date >= CURRENT_DATE "
				+ "ORDER BY date ASC"; //solo mostramos las futuras appointments ordenadas de la mas proxima a la menos
	    
		try {
			
			PreparedStatement prep=c.prepareStatement(sql);
			prep.setInt(1,doctorID);
			ResultSet rs = prep.executeQuery();

	        while (rs.next()) {
	            Appointment appointment = new Appointment();
	            appointment.setId(rs.getInt("id"));
	            appointment.setPatientId(rs.getInt("patientid"));
	            appointment.setDoctorId(rs.getInt("doctorid"));
	            appointment.setDate(rs.getDate("date"));
	            appointments.add(appointment);
	        }

	        rs.close();
	        prep.close();

	     
		}catch(SQLException e) {
			System.out.println("Error retrieving appointments for doctor ID: " + doctorID);
			e.printStackTrace();
	        
		}
		return appointments;
		
	}

	//Validates if a patient is assigned to a doctor seeing if there has been an appointment of that doctor and that patient
	@Override
	public boolean validatePatientOfTheDoctor(Doctor doctor, Patient p1) {
		String sql = "SELECT id FROM appointments WHERE patientid = " +p1.getId()+ " AND doctorid = "+ doctor.getId();
		try {
			Statement stmt= c.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			stmt.close();
			
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false; //si no se encuentra o captura una excepcion se devuelve false
		}
	}

	//Retrieves a list of patients associated with a specific doctor based on the appointment records.
	public List<Patient> viewPatientsOfADoctor(Doctor doctor) {
		List<Patient> listOfPatients  = new ArrayList<>();
		
		// Null check for doctor parameter
	    if (doctor == null) {
	        return listOfPatients;
	    }
	    
		try {
			String sql = "SELECT p.id, p.name,p.email, p.dob, p.address, p.sex, p.dob "
					+ " FROM patients AS p JOIN appointments AS a ON p.id = a.patientid "
					+ " WHERE a.doctorid = ?";
			PreparedStatement prep=c.prepareStatement(sql);
			prep.setInt(1, doctor.getId());
			ResultSet rs = prep.executeQuery();
			
			while (rs.next()) {
                 Patient patient = new Patient();
                 patient.setId(rs.getInt("id"));
                 patient.setName(rs.getString("name"));
                 patient.setEmail(rs.getString("email"));
                 patient.setAddress(rs.getString("address"));
                 patient.setSex(rs.getString("sex"));
                 //es neecsario pasarlo???
	                 long dobMiliseconds = rs.getLong("dob");
	                 Date utilDate = new Date (dobMiliseconds);
	                 patient.setDob(utilDate); 
                 
                 listOfPatients.add(patient);
             }		
			prep.close();
			rs.close();
		}catch(SQLException e) {
       	 e.printStackTrace();
        }
		return listOfPatients;
	}
	
	//Retrieves the Id of a medRecord filtered by date and patient
	@Override
	public int getMedRecordIDFilteredByADate(Date date, int patientID) {
		int medRecID=-1;
		
		try {
			String sql = " SELECT id FROM medrecords "
					+ "WHERE patientid= ?  AND date = ?";
					
			PreparedStatement prep=c.prepareStatement(sql);
			prep.setInt(1, patientID);
			Date dateSQL = new java.sql.Date(date.getTime());
			prep.setDate(2, dateSQL);
			ResultSet rs = prep.executeQuery();
			if(rs.next()) {
				medRecID = rs.getInt("id");
			}
			prep.close();
			rs.close();
		}catch(SQLException e) {
	       	 e.printStackTrace();
	       	 
	    }
		return medRecID;
	}

	
//-------------------------------------MEDICINE SUPPLY-----------------	
	//This methods obtains all the medicine supplies from the database.
	@Override
	public List<MedicineSupply> getAllMedicineSupplies() {
		List<MedicineSupply> medicines = new ArrayList<>();
		//VER SI ESTA BIEN EL NOMRBE EN LA BBDD
		String sql = " SELECT id, name, quantity FROM medsupply "; 
		try {
			Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            		
            while(rs.next()) {
            	Integer id = rs.getInt("id");
            	String name = rs.getString("name");
            	Integer quantity = rs.getInt("quantity");
            	
            	medicines.add(new MedicineSupply(id,name,quantity));
            }
            //cerramos
            stmt.close();
            rs.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return medicines;
		
		
	}
	
	//It adds a new supply of medicines to the database
	@Override
	public boolean addMedicineSupply(MedicineSupply medicine) {
		String sql = "INSERT INTO medsupply (name,quantity) VALUES (?,?)";
		
		try {
			PreparedStatement prep = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, medicine.getName());
			prep.setInt(2, medicine.getQuantity());
			prep.executeUpdate();
			prep.close();
			
			return true;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	
//-----------------------------------NUEVO DIANA MANAGEMEDICALRECORDS--------------------------
		
		//Retrieves all patients (in a list) assigned to a specific doctor
		@Override
		public List<Patient> getPatientsByDoctor(Integer doctorID) {
			List<Patient> patients = new ArrayList<>();
			String sql = "SELECT p.id, p.name " //no necesitamos todos los atributos
					+ "FROM patients AS p "
					+ "JOIN appointments AS a ON p.id = a.patientid "
					+ "WHERE a.doctorid = ? ";
			
			try {
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setInt(1, doctorID);
				ResultSet rs = prep.executeQuery();
				
				while(rs.next()) {
					Patient newPatient = new Patient();
					newPatient.setId(rs.getInt("id"));
					newPatient.setName(rs.getString("name"));
					patients.add(newPatient);
				}
				
				prep.close();
				rs.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
						
			return patients;
		}
		
		//This method retrives a list of ids of the appointments shared by a patiet and a doctor
		@Override
		public List<Integer> getSharedAppointmentsIDs(int patientID, int doctorID) {
			String sql = "SELECT id "
					+ "FROM appointments "
					+ "WHERE patientid = ?  AND doctorid = ? ";
			List<Integer> sharedAppointmentIds = new ArrayList<>();
			try {
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setInt(1, patientID);
				prep.setInt(2, doctorID);
				ResultSet rs = prep.executeQuery();
				
				while(rs.next()) {
					sharedAppointmentIds.add(rs.getInt("id"));
				}
				
				prep.close();
				rs.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			return sharedAppointmentIds;
		}
	
		//Retrieves a list of medical records for a specific patient and specific date
		@Override
		public List<MedicalRecord> getMedicalRecordsByPatientAndDate(int patientId, Date date){
			List<MedicalRecord> records = new ArrayList<>();
			String sql = "SELECT id, diagnose, treatment, date , medprescid " //cojo date tambien??
					+ " FROM medrecords "
					+ " WHERE patientid= ? AND date = ? "
					+  "ORDER BY date DESC"; //ordenamos para que salgan de mas recientes a mas antiguos
			try {
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setInt(1, patientId);
				//convierto a millis
				Date dateSQL = new java.sql.Date(date.getTime()); 
				prep.setDate(2, dateSQL); //preguntar si se ahce la conevrsion a millis asi????
				
				ResultSet rs = prep.executeQuery();
				
				
				while(rs.next()) {
					MedicalRecord newRecord =new MedicalRecord();
					newRecord.setId(rs.getInt("id"));
					newRecord.setPatientID(patientId);
					newRecord.setDiagnosis(rs.getString("diagnose"));
					newRecord.setTreatment(rs.getString("treatment"));
					long dobMillis = rs.getLong("date");
					Date utilDate = new Date(dobMillis);
					newRecord.setDate(utilDate); //esta esto bien???
					newRecord.setMedicineID(rs.getInt("medprescid"));
					
					records.add(newRecord);	
				}
			}catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
			return records;
		}

		//updates a medical record (its diagnose or treatment and the date -> the date every time they update sth)
		@Override
		public boolean updateMedicalRecord(MedicalRecord record) {
			String sql = "UPDATE medrecords SET diagnose = ?, treatment = ?, date = ? "
					+ "WHERE id = ? AND patientid = ?";
			try {
				PreparedStatement prep=c.prepareStatement(sql);
				
				prep.setString(1, record.getDiagnosis());
				prep.setString(2, record.getTreatment());
				prep.setDate(3, record.getDate());
				prep.setInt(4, record.getId());
				prep.setInt(5, record.getPatientID());
				
				prep.executeUpdate();
				prep.close();
				return true;
				
			}catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
			
		}

		//Adds a medicalRecord to the dtaabase
		@Override
		public boolean addMedicalRecord(MedicalRecord record)
		{
			String sql = "INSERT INTO medrecords (patientid, diagnose, treatment, date, medprescid) "
					+ "VALUES (?,?,?,?,?);";
			try
			{
				
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setInt(1, record.getPatientID());
				prep.setString(2, record.getDiagnosis());        
				prep.setString(3, record.getTreatment());
				prep.setDate(4,  Date.valueOf(LocalDate.now()));//Haciendo esto se almacena como milisegundos en la base de datos
				prep.setInt(5, record.getMedicineID());
				
				prep.executeUpdate();
				prep.close();
				return true;
			
			}
			catch(SQLException e){
				e.printStackTrace();
				return false;
			}catch (NumberFormatException e) {
				e.printStackTrace();
				return false;
			} 
		}

		//Method that sees if the medicine has been already used (is inside a medical record)
		@Override
		public boolean isMedicineAvailable(int medId) {
			String sql = "SELECT id FROM medrecords "
					+ "WHERE medprescid = ?";
			List<Integer> medRecordsIdWithThatMedicine = new ArrayList<>();
			try { 
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setInt(1,medId);
				
				ResultSet rs = prep.executeQuery();
				
				while(rs.next()) {
					medRecordsIdWithThatMedicine.add(rs.getInt("id"));
				}
				prep.close();
				rs.close();
				
				if(medRecordsIdWithThatMedicine.isEmpty()) {
					return true; //no hay medRecords usando esa medicineSupply
				}else {
					return false;
				}
				
				
			}catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

		//elimina una medicineSupply introducida con su ID
		@Override
		public void deleteMedicineSupply(int medicineId) {
			String sql = "DELETE FROM medsupply WHERE id = ? ";
			try {
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setInt(1, medicineId);
				prep.executeUpdate();
				prep.close();
				
			}catch(SQLException e) {
				System.out.println("ERROR deleting the medicineSupply.");
				e.printStackTrace();
			}
		}
		
}
