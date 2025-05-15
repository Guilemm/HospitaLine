package hospital.ui;

import java.io.BufferedReader;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import db.pojos.Appointment;
import db.pojos.Doctor;
import db.pojos.MedicalRecord;
import db.pojos.MedicineSupply;
import db.pojos.Patient;
import db.pojos.Role;
import db.pojos.User;
import hospital.jdbc.HospitalManagerImplements;
import hospital.jpa.UserManagerImplements;





public class Menu {
	
	
	
			if(user1.getRole().getName().equals("doctor"))
//--------------------------------------------------DOCTOR-------------------------------------------			
			{
				Doctor doctor = hospiman.getDoctorByEmail(user1.getEmail());

				boolean keepsesiondoc=true;
				while(keepsesiondoc)//v
				{
					System.out.println("Hello Dr/a."+user1.getUsername()+", what do you want to do: "
							+ "\n1) See your next appointments. "
							+ "\n2) Manage the medical records "
							+ "\n3) Manage the medicine supply "
							+ "\n4) Exit");
					int option=Integer.parseInt(br.readLine());
					switch(option)
					{
					case 1:
						seeAppointmentsInMenu(doctor);
						break;
					case 2://Medical Record
						manageDoctorMedicalRecords2(doctor);
						break;
					case 3:
						//See all medicines, add a new medicine
						manageMedicineSupply();
						break;
					
					case 4:
						keepsesiondoc=false;
						break;
					default:
						System.out.println("\nInvalid option");
					}
					
				}
			}
			
			
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			System.out.println("\nInvalid combination of username and password");
		}
}

//CASE 1 -> APPOINTMENTS //[TERMINADO]
	/**
	 * Sees the future appointments of a doctor by introducing its id
	 * @param doctor The doctor of whom we want to see the appointments
	 */
	public static void seeAppointmentsInMenu(Doctor doctor) {
		
		
		List<Appointment> listOfAppointments = hospiman.viewAppointmentsFromDoctor(doctor.getId());
	
		
		if(listOfAppointments.isEmpty()) {//esta vacio, no tiene appointments
			System.out.println("You don't have any appointments.");
		}else {
			System.out.println("Your next appointments are:");
			System.out.println("-----------------------------------");
			for (Appointment appointment : listOfAppointments) {
	            System.out.println(appointment.toString());
	            System.out.println("-----------------------------------");
			}
		}
	}
	
	
	
//CASE 2 -> MEDICAL RECORDS
	/**
	 * This method manages the menu for the mdical records
	 * 1) View a medical record
	 * 2) Update a medical record 
	 * 3) Add a medical record
	 * 4) Return to main menu");
	 * @param doctor
	 * @throws IOException
	 */
		//[TERMINADO]
	public static void manageDoctorMedicalRecords2(Doctor doctor) throws IOException {
	    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    try {
			boolean continueManaging = true;
			    while (continueManaging) {
			        System.out.println("What do you want to do: \n"
			                + "1) View a medical record \n"
			                + "2) Update a medical record \n"
			                + "3) Add a medical record \n"
			                + "4) Return to main menu");
			        
			        int option = Integer.parseInt(br.readLine());
			        
			        switch (option) {
			            case 1:
			            	viewPatientMedicalRecords(doctor);
			                break;
			            case 2:
			                updateMedicalRecord(doctor);
			                break;
			            case 3:
			                addMedicalRecord(doctor);
			                break;
			            case 4:
			                continueManaging = false;
			                break;
			            default:
			                System.out.println("\nInvalid option");
			        }
			     }
			  }catch(NumberFormatException e) {
			    	System.out.println("Introduce un numero.");
			    	e.printStackTrace();
			    	return;
			   } 
	   
	    
		   
	}
		
		/**
		 * Shows the patient medical records in 2 ways:
		 * 1. All the medical records 
		 * 2. Filtered by date
		 * @param doctor
		 * @throws IOException
		 */
			//[TERMINADO]
		public static void viewPatientMedicalRecords(Doctor doctor) throws IOException {
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    
		    // Show doctor's patients
		    System.out.println("\nYour patients:");
		    List<Patient> patients = hospiman.getPatientsByDoctor(doctor.getId());
		   
		    if (patients.isEmpty()) {
		        System.out.println("You currently have no assigned patients.");
		        return;//vuelve
		    }
		    
		    for (Patient p : patients) {
		            System.out.println(p.getId() + ": " + p.getName());
		            
		        }
		    
		    // pido el paciente id
		    System.out.print("\nEnter patient ID: ");
		    int patientId = Integer.parseInt(br.readLine());
		    
		 // Verifico que el que haya introducido sea paciente del doctor (miro las appointments en comun que tiene)
		    List<Integer> sharedAppointmentsIds = hospiman.getSharedAppointmentsIDs(patientId, doctor.getId());
		    if(sharedAppointmentsIds.isEmpty()){
		    	 System.out.println("This patient is not assigned to you.");
			        return;
		    }
		    
		    
		    // View options
		    System.out.println("\nView options:");
		    System.out.println("1. View all medical records");
		    System.out.println("2. Filter by date");
		    System.out.print("Select an option: ");
		    int choice = Integer.parseInt(br.readLine());
		    
		    switch(choice) {
		    	case 1:  //View all medical records of the patient
		    		List<MedicalRecord> records = hospiman.ViewAllMedicalRecords(patientId);
		    		if(records==null) {
		    			System.out.println("We couldnt retrive the medical records for patientID: "+patientId);
		    			return;
		    		}
		    		if (records.isEmpty()) {
		                System.out.println("No medical records found for this patient.");
		            } else {
		                System.out.println("\nMedical Records for Patient ID " + patientId + ":");
		                for (MedicalRecord mr : records) {
		                    System.out.println("----------------------");
		                    System.out.println("Record ID: " + mr.getId());
		                    System.out.println("Date: " + mr.getDate());
		                    System.out.println("Diagnosis: " + mr.getDiagnosis());
		                    System.out.println("Treatment: " + mr.getTreatment());
		                }
		                System.out.println("----------------------");
		            }
		            break;
		    	case 2: //Filter medical records of the patient by date
		    		System.out.print("Enter date (yyyy-MM-dd): ");
		            Date date = Date.valueOf(br.readLine());
		            List<MedicalRecord> filteredRecords = hospiman.getMedicalRecordsByPatientAndDate(patientId, date);
		            if(filteredRecords==null) {
		    			System.out.println("We couldnt retrive the medical records for patientID: "+patientId+
		    					"in date: "+date);
		    			return;
		    		}
		            if (filteredRecords.isEmpty()) {
		                System.out.println("No records found for this date.");
		            } else {
		                System.out.println("\nRecords for " + date + ":");
		                for (MedicalRecord mr : filteredRecords) {
		                    System.out.println("----------------------");
		                    System.out.println("Record ID: " + mr.getId());
		                    System.out.println("Diagnosis: " + mr.getDiagnosis());
		                    System.out.println("Treatment: " + mr.getTreatment());
		                }
		                System.out.println("----------------------");
		            }
		            break;
	            default: 
	            	System.out.println("Invalid option.");
		    }
		   
		}
		
	
		
		//TODO: hospiman.getMedicalRecordById(recordId);
		//TODO: hospiman.updateMedicalRecord(record);
		private static void updateMedicalRecord(Doctor doctor) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			// Show doctor's patients
		    System.out.println("\nYour patients:");
		    List<Patient> patients = hospiman.getPatientsByDoctor(doctor.getId());
		   
		    if (patients.isEmpty()) {
		        System.out.println("You currently have no assigned patients.");
		        return;//vuelve
		    }
		    
		    for (Patient p : patients) {
		            System.out.println(p.getId() + ": " + p.getName());
		        }
		    
		    // pido el paciente id
		    System.out.print("\nEnter patient ID: ");
		    int patientId = Integer.parseInt(br.readLine());
		    
		 // Verifico que el que haya introducido sea paciente del doctor (miro las appointments en comun que tiene)
		    List<Integer> sharedAppointmentsIds = hospiman.getSharedAppointmentsIDs(patientId, doctor.getId());
		    if(sharedAppointmentsIds.isEmpty()){
		    	 System.out.println("This patient is not assigned to you.");
			        return;
		    }
		    
		    // Show existing records 
		    List<MedicalRecord> records = hospiman.ViewAllMedicalRecords(patientId);
		    if (records.isEmpty()) {
		        System.out.println("This patient has no medical records to update.");
		        return;
		    }
		    
		    System.out.println("\nExisting Medical Records:");
		    for (MedicalRecord mr : records) {
		        System.out.println(mr.getId() + ": " + mr.getDate() + " - " + mr.getDiagnosis());
		    }
		    
		    System.out.print("\nEnter Record ID to update: ");
		    int recordId = Integer.parseInt(br.readLine());
		    
		    //TODO
		    MedicalRecord record = hospiman.ViewOneMedicalRecord(recordId, patientId);
		   
		    
		    if (record == null || record.getPatientID() != patientId) {
		        System.out.println("Invalid record ID for this patient.");
		        return;
		    }
		    
		    System.out.println("\nCurrent values:");
		    System.out.println("1. Diagnosis: " + record.getDiagnosis());
		    System.out.println("2. Treatment: " + record.getTreatment());
		    
		    System.out.println("\nEnter new values (leave blank to keep current):");
		    System.out.print("New Diagnosis: ");
		    String diagnosis = br.readLine();
		    if (!diagnosis.isEmpty()) record.setDiagnosis(diagnosis);
		    
		    System.out.print("New Treatment: ");
		    String treatment = br.readLine();
		    if (!treatment.isEmpty()) record.setTreatment(treatment);
		        
		    // Update date to current date
		    record.setDate(new Date(System.currentTimeMillis()));
		        
		    
		    if (hospiman.updateMedicalRecord(record)) {
		        System.out.println("Medical record updated successfully.");
		    } else {
		        System.out.println("Failed to update medical record.");
		    }
		}
		
		    
		
		//TODO: hospiman.getMedicineSupplyID(medicine);
		//TODO:  hospiman.addMedicalRecord(newRecord);
		//TODO: hospiman.addMedicineSupply(medicine);
		//TODO: hospiman.deleteMedicineSupply(medicineId);
	private static void addMedicalRecord(Doctor doctor) throws IOException{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				// Show doctor's patients
			    System.out.println("\nYour patients:");
			    List<Patient> patients = hospiman.getPatientsByDoctor(doctor.getId());
			   
			    if (patients.isEmpty()) {
			        System.out.println("You currently have no assigned patients.");
			        return;//vuelve
			    }
			    
			    for (Patient p : patients) {
			            System.out.println(p.getId() + ": " + p.getName());
			        }
			    
			    // pido el paciente id
			    System.out.print("\nEnter patient ID: ");
			    int patientId = Integer.parseInt(br.readLine());
			    
			    // Verifico que el que haya introducido sea paciente del doctor (miro las appointments en comun que tiene)
			    List<Integer> sharedAppointmentsIds = hospiman.getSharedAppointmentsIDs(patientId, doctor.getId());
			    if(sharedAppointmentsIds.isEmpty()){
			    	 System.out.println("This patient is not assigned to you.");
				        return;
			    }
			    
			    
			    System.out.println("Enter diagnosis:");
			    String diagnosis = br.readLine();
			    
			    System.out.println("Enter treatment:");
			    String treatment = br.readLine();
			    
			    
			    System.out.println("This are the medicine supply: ");
			    List<MedicineSupply> medicineSupplies = hospiman.getAllMedicineSupplies();
			    for(MedicineSupply ms: medicineSupplies) {
			    	System.out.println("Name: "+ms.getName()+" (ID "+ms.getId()+" )");
			    }
			    
			    System.out.print("Enter medicine ID: ");
			    int medicineId = Integer.parseInt(br.readLine());
			    
			    //comprobar que la medciina no este prescrita para otro paciente
			    if(!hospiman.isMedicineAvailable(medicineId)) {
			    	System.out.println("This medicine has been already prescribed and is not available.");
			    	return;
			    }
			    
			    // Creamos medicalRecord sin el id ya que lo añade la db
			    MedicalRecord newRecord = new MedicalRecord();
			    newRecord.setPatientID(patientId);
			    newRecord.setDiagnosis(diagnosis);
			    newRecord.setTreatment(treatment);
			    newRecord.setDate(new Date(System.currentTimeMillis()));
			    newRecord.setMedicineID(medicineId);
			    
			    // Guardamos el medicalrecord
			    if (hospiman.addMedicalRecord(newRecord)) {
		            System.out.println("\nMedical record created successfully!");
		            System.out.println("Record ID: " + newRecord.getId());
		        } else {
		            System.out.println("Failed to create medical record.");
		            //Eliminamos la medSupply que habiamos añadido
		            hospiman.deleteMedicineSupply(medicineId);
		        }
	
		}
		
		
		
//CASE 3 -> MEDCINE SUPPLY [TERMINADO]
	/**
	 * This method manages the medicine supply (option3)
	 * 1)show all medicines supplies
	 * 2)add a new medicine supply
	 * @throws IOException
	 */
	public static void manageMedicineSupply() throws IOException {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    
	    System.out.println("What do you want to do:\n" +
	                     "1) View all medicine supplies\n" +
	                     "2) Add new medicine");
	    int option = Integer.parseInt(br.readLine());
	    
	    if(option == 1) {
	        List<MedicineSupply> medicines = hospiman.getAllMedicineSupplies();
	        if(medicines.isEmpty()) {
                System.out.println("No medicines found");
	        }else {
                for(MedicineSupply med : medicines) {
	            System.out.println(med.toString());
                }
             }
	    } else if(option == 2) {
	        System.out.println("Enter medicine name:");
	        String name = br.readLine();
	        //compruebo lo que ha introducido
		        if(name.isEmpty()) {
		                System.out.println("Medicine name cannot be empty");
		                return;
		        }
	        System.out.println("Enter quantity:");
	        int quantity = Integer.parseInt(br.readLine());
	        //compruebo lo que ha introducido
		        if(quantity <= 0) {
		                System.out.println("Quantity must be positive");
		                return;
		            }
	       
		    //creo y añado la medicna
	        MedicineSupply newMedicine = new MedicineSupply(name, quantity);
	        Boolean added = hospiman.addMedicineSupply(newMedicine);
		    
		    if (!added) {
	            System.out.println("Failed to save medicine supply information.");
	        }else {
	        	 System.out.println("Medicine added successfully");
	        }
	       
	    } else {
	        System.out.println("Invalid option");
	    }
	   
	}
		
		
		
		
		
		
		
}
		
		
