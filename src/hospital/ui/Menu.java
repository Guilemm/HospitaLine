package hospital.ui;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import javax.xml.bind.UnmarshalException;
import java.sql.Date;
import java.sql.SQLException;
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
import hospital.xml.XmlManagerImplements;
import hospital.ifaces.XmlManager;



public class Menu {
	
	private static HospitalManagerImplements hospiman;
	private static UserManagerImplements userman;
	private static XmlManager xmlman=new XmlManagerImplements();
	
	public static void main(String args[])
	{
		hospiman=new HospitalManagerImplements();
		userman=new UserManagerImplements();
		
		boolean keep=true;
		while(keep)
		{
			try
			{
				BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Welcome, what do you want to do?\n1) Register as a Doctor\n2) Register as a Patient\n3) Log in\n4) Exit");
				int option=Integer.parseInt(br.readLine());
				
				switch(option)
				{
				case 1:
					registerDoctor();
					break;
					
				case 2:
					registerPatient();
					break;
				case 3:
					login();
					break;
				case 4:
					hospiman.CloseConnection();
					userman.close();
					keep=false;
					break;
				default:
					System.out.println("\nInvalid option");
				}
				
				
			}
			catch (NumberFormatException e) {
				
				System.out.println("\nInvalid option");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
			
		}
	
	}
	
	public static void registerDoctor()
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nEnter your email: ");
			String email=br.readLine();
			if(email.equals(""))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nEnter your name: ");
			String name=br.readLine();
			if(name.equals(""))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nEnter your password: ");
			String password=br.readLine();
			if(password.equals(""))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nEnter your address: ");
			String address=br.readLine();
			System.out.println("\nSex: M or F");
			String sexstr=br.readLine();
			if((!sexstr.equals("M"))&&(!sexstr.equals("F")))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nDate of birth (yyyy-MM-dd): ");
			String dobstr=br.readLine();
			if(LocalDate.parse(dobstr).isAfter(LocalDate.now()))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nDepartment: ");
			String department=br.readLine();
			if(department.equals(""))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nExperience (years): ");
			int experience=Integer.parseInt(br.readLine());
			if(experience<0)
			{
				System.out.println("\nInvalid option");
				return;
			}
			
			User user=new User(name, password, email);
			Doctor doc=new Doctor(name, email, Date.valueOf(dobstr), experience, address, department, sexstr);
			userman.register(user);
			Role role=userman.getRole("doctor");
			userman.assignRole(user, role);
			hospiman.AddDoctor(doc);
			
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		catch(DateTimeParseException e)
		{
			System.out.println("\nInvalid date format, use (yyyy-MM-dd)");
		}
		catch(Exception e)
		{
			if(e.getMessage().contains("(UNIQUE constraint failed: users.EMAIL)"))
			{
				System.out.println("\nThis email already exists in the database");
				return;
			}
			System.out.println("\nError");
			e.printStackTrace();
		}
	}
	
	public static void registerPatient()
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nEnter your email: ");
			String email=br.readLine();
			if(email.equals(""))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nEnter your name: ");
			String name=br.readLine();
			if(name.equals(""))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nEnter your password: ");
			String password=br.readLine();
			if(password.equals(""))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nEnter your address: ");
			String address=br.readLine();
			System.out.println("\nSex: M or F");
			String sexstr=br.readLine();
			if((!sexstr.equals("M"))&&(!sexstr.equals("F")))
			{
				System.out.println("\nInvalid option");
				return;
			}
			System.out.println("\nDate of birth (yyyy-MM-dd): ");
			String dobstr=br.readLine();
			if(LocalDate.parse(dobstr).isAfter(LocalDate.now()))
			{
				System.out.println("\nInvalid option");
				return;
			}
			User user=new User(name, password, email);
			Patient pati=new Patient(name, email, address, sexstr, Date.valueOf(dobstr));
			userman.register(user);
			Role role=userman.getRole("patient");
			userman.assignRole(user, role);
			hospiman.AddPatient(pati);
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		catch(DateTimeParseException e)
		{
			System.out.println("\nInvalid date format, use (yyyy-MM-dd)");
		}
		catch(Exception e)
		{
			if(e.getMessage().contains("(UNIQUE constraint failed: users.EMAIL)"))
			{
				System.out.println("\nThis email already exists in the database");
				return;
			}
			System.out.println("\nError");
			e.printStackTrace();
		}
	}
	
	public static void login()
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nEnter your email: ");
			String email=br.readLine();
			System.out.println("\nEnter your password: ");
			String password=br.readLine();
			User user1= userman.login(email, password);
			if(user1!=null)
			{
				if(user1.getRole().getName().equals("patient"))
				{
					patientMenu(user1);
				}
				if(user1.getRole().getName().equals("doctor"))
				{
					doctorMenu(user1);
				}
			}	
			else
			{
				System.out.println("\nWrong combination of email/password");
			}
			
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
    public static void patientMenu(User user1)
    {
    	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    	boolean keepsesionpat=true;
    	while(keepsesionpat)
    	{
    		try
    		{
    			Patient pat=hospiman.getPatientByEmail(user1.getEmail());
    				
    			System.out.println("Hello "+user1.getUsername()+ ", chose one option:\n1) Your appointments\n2) Doctors\n3) View your medical records\n4) Export information of your appointments to an Xml file \n5) Retrieve the information of your appointments from an Xml file (HospitaLine/xml/Appointments.xml) \n6) Change your password \n7) Delete your account \n8) Exit");
    			int option=Integer.parseInt(br.readLine());
    			switch(option)
    			{
    			case 1:
    				System.out.println("\nDo you want to: \n1) View your appointments\n2) Book an appointment\n3) Cancel an appointment\n4) Modify an appointment");
    				int opcion=Integer.parseInt(br.readLine());
    				switch(opcion)
    				{
    				case 1:
    					ArrayList<Appointment> apossee=hospiman.getAppointmentsPatient(pat.getId());
    					if(apossee.isEmpty())
    					{
    						System.out.println("\nYou have no appointments");
    					}
    					else
    					{
    						System.out.println("\nThis are your appointments: \n");
    						int i=0;
    						while(i<apossee.size())
    						{
    							System.out.println(apossee.get(i).toString());
    							i++;
    						}
    					}
    					
    					break;
    				case 2:
    					ArrayList<Doctor> docs=hospiman.ViewAllDoctors();
    					if(docs.isEmpty())
    					{
    						System.out.println("\nThere are no doctors to book an appointment with right now");
    					}
    					else
    					{
    						int i=0;
    						while(i<docs.size())
    						{
    							System.out.println(docs.get(i).toString());
    							i++;
    						}
    						System.out.println("\nFrom this doctors, choose one for your appointment (indicate its id)");
    						int docid=Integer.parseInt(br.readLine());
    						System.out.println("\nIndicate the date of the appointment (yyyy-MM-dd): ");
    						String datestr=br.readLine();
    						if(LocalDate.parse(datestr).isBefore(LocalDate.now()))
    						{
    							System.out.println("\nInvalid option");
    							return;
    						}
    						System.out.println("\nIndicate the hour of the appointment (from 0-23)");
    						int hour=Integer.parseInt(br.readLine());
    						if((hour<0)||(hour>23))
    						{
    							System.out.println("\nInvalid option");
    						}
    						else
    						{
    							boolean aver=false;
    							aver=hospiman.CheckAvailability(hour, docid, Date.valueOf(datestr));
    							
    							if(aver)
    							{
    								Appointment apo=new Appointment(pat.getId(), docid, Date.valueOf(datestr), hour);
    								try
    								{
    									hospiman.BookAppointment(apo);
    								}
    								catch(SQLException e)
    								{
    									System.out.println("\nInvalid option");
    								}
    							}
    							else
    							{
    								System.out.println("\nThe doctor is not available");
    							}
    							
    						}
    					}
    					
    				
    					break;
    				case 3:
    					ArrayList<Appointment> apos=hospiman.getAppointmentsPatient(pat.getId());
    					if(apos.isEmpty())
    					{
    						System.out.println("\nYou have no appointments");
    					}
    					else
    					{
    						int r=0;
    						while(r<apos.size())
    						{
    							System.out.println(apos.get(r).toString());
    							r++;
    						}
    						System.out.println("\nWhich one of your appointments do you want to cancel? (Indicate the id of the appointment)");
    						int apcancel=Integer.parseInt(br.readLine());
    						boolean cancelled=hospiman.EliminateAppointment(apcancel, pat.getId());
    						if(cancelled)
    						{
    							System.out.println("\nThe appointment was succesfully cancelled");
    						}
    						else
    						{
    							System.out.println("\nThe appointment was not cancelled");
    						}
    					}
    					
    					break;
    				case 4:
    					ArrayList<Appointment> aposup=hospiman.getAppointmentsPatient(pat.getId());
    					if(aposup.isEmpty())
    					{
    						System.out.println("\nYou have no appointments");
    					}
    					else
    					{
    						int k=0;
    						while(k<aposup.size())
    						{
    							System.out.println(aposup.get(k).toString());
    							k++;
    						}
    						System.out.println("\nWhich one of your appointments do you want to update? (Indicate the id of the appointment)");
    						int apupid=Integer.parseInt(br.readLine());
    						System.out.println("\nIntroduce the new doctor id");
    						int docid=Integer.parseInt(br.readLine());
    						System.out.println("\nIntroduce the new date (yyyy-MM-dd)");
    						String datestr=br.readLine();
    						if(LocalDate.parse(datestr).isBefore(LocalDate.now()))
    						{
    							System.out.println("\nInvalid option");
    						}
    						else
    						{
    							System.out.println("\nIntroduce the new hour");
    							int hour=Integer.parseInt(br.readLine());
    							if((hour<0)&&(hour>23))
    							{
    								System.out.println("\nInvalid option");
    							}
    							else
    							{
    								boolean disp=hospiman.CheckAvailability(hour, docid, Date.valueOf(datestr));
    								if(disp)
    								{
    									Appointment apo=new Appointment(pat.getId(), docid, Date.valueOf(datestr), hour);
    									boolean modified=false;
    									try
    									{
    										modified=hospiman.ModifyAppointment(apupid, apo);
    									}
    									catch(SQLException e)
    									{
    										System.out.println("\nInvalid option");
    									}
    									
    									if(modified)
    									{
    										System.out.println("\nThe appointment was updated");
    									}
    									else
    									{
    										System.out.println("\nThe appointment was not updated");
    									}
    								}
    								else
    								{
    									System.out.println("\nThe doctor is not available at that moment");
    								}
    							}
    							
    						}
    						
    					}
    	
    					break;
    				default:
    					System.out.println("\nInvalid option");
    				}
    				
    				break;
    				
    			case 2:
    				System.out.println("What do you want to do: \n1) View all doctors \n2) View an specific doctor");
    				int op=Integer.parseInt(br.readLine());
    				
    				switch(op)
    				{
    				case 1:
    					ArrayList<Doctor> docs=hospiman.ViewAllDoctors();
    					if(docs.isEmpty())
    					{
    						System.out.println("\nThere are no doctors right now");
    					}
    					else
    					{
    						int f=0;
    						while(f<docs.size())
    						{
    							System.out.println(docs.get(f).toString());
    							f++;
    						}
    						
    					}
    					
    					break;
    				case 2:
    					System.out.println("\nIntroduce the id of the doctor you want to see: ");
    					int docidsee=Integer.parseInt(br.readLine());
    					Doctor docsee=hospiman.ViewDoctorInfo(docidsee);
    					if(docsee.getEmail()==null)
    					{
    						System.out.println("\nThere is no such doctor");
    					}
    					else
    					{
    						System.out.println(docsee.toString());
    					}
    					
    					break;
    				default:
    					System.out.println("\nInvalid option");
    				}
    				
    				break;
    			case 3:
    				System.out.println("\nDo you want to see: \n1) All your medical records \n2) Just one \n3) Claim medicine from a medical record");
    				int opc=Integer.parseInt(br.readLine());
    				
    				switch(opc)
    				{
    				case 1:
    					ArrayList<MedicalRecord> medrecs=hospiman.ViewAllMedicalRecords(pat.getId());
    					if(medrecs.isEmpty())
    					{
    						System.out.println("\nYou have no medical records");
    					}
    					else
    					{
    						int y=0;
    						while(y<medrecs.size())
    						{
    							System.out.println(medrecs.get(y).toString());
    							y++;
    						}
    					}
    					
    					
    					break;
    				case 2:
    					System.out.println("\nWhich one of your Mediacl Records do you want to see? (Specify the id): ");
    					int medrecid=Integer.parseInt(br.readLine());
    					MedicalRecord medrec=hospiman.ViewOneMedicalRecord(medrecid, pat.getId());
    					if(medrec.getDiagnosis()==null)
    					{
    						System.out.println("\nThis medical record does not exist");
    					}
    					else
    					{
    						System.out.println(medrec.toString());
    					}
    					
    					
    					break;
    				case 3:
    					System.out.println("\nWhich one of your Medical Records do you want to claim medicine from? (Specify the id of the Medical Record): ");
    					int medrecidclaim=Integer.parseInt(br.readLine());
    					boolean claimed=hospiman.ClaimMedicine(medrecidclaim, pat.getId());
    					if(claimed)
    					{
    						System.out.println("\nMedicine was claimed succesfully");
    					}
    					else
    					{
    						System.out.println("\nIt is not possible to claim the prescribed medicine, due to it having been claimed before, because there was no medicine prescribed in the first place, or because this medical record does not exist");
    					}
    					
    					break;
    				default:
    					System.out.println("\nInvalid option");
    				}
    				
    				
    				break;
    			case 4:
    				ArrayList<Appointment> apos=hospiman.getAppointmentsPatient(pat.getId());
    				if(apos.isEmpty())
    				{
    					System.out.println("\nYou have no appointments");
    				}
    				else
    				{
    					xmlman.Java2XmlAppointments(apos);
    					System.out.println("\nYour appointments are in: HospitaLine/xml/Appointments.xml");
    				}
    				
    				
    				break;
    			case 5:
    				try
    				{
    					ArrayList<Appointment> aposin=xmlman.Xml2JavaAppointments();
        				hospiman.AddAppointmentsfromXml(aposin, pat.getId());
    				}
    				catch(UnmarshalException e)
    				{
    					System.out.println("\nThe Xml file is empty or corrupted");
    				}
    				catch(SQLException e)
    				{
    					System.out.println("\nThere are doctors or patients in the Xml appointments that does not exist");
    				}
    					
    				
    				break;
    			case 6:
    				System.out.println("\nWhat will your new password be?");
    				String newpass=br.readLine();
    				if(newpass.equals(""))
    				{
    					System.out.println("\nInvalid password");
    				}
    				else
    				{
    					boolean cambiada=userman.changePassword(user1, newpass);
    					if(cambiada)
    					{
    						System.out.println("\nPassword changed");
    					}
    					else
    					{
    						System.out.println("\nPassword was not changed");
    					}
    				}
    				
    				break;
    			case 7:
    				System.out.println("\nIf you delete your account, you will be redirected to the principal menu, are you sure (press 1)?");
    				int deci=Integer.parseInt(br.readLine());
    				if(deci==1)
    				{
    					userman.deleteAccount(user1);
    					hospiman.DeletePatient(pat.getId());
    					keepsesionpat=false;
    				}
    				
    				break;
    			case 8:
    				keepsesionpat=false;
    				break;
    			default:
    				System.out.println("\nInvalid option");
    				
    			}
    		}
    		catch (NumberFormatException e) {
    			
    			System.out.println("\nInvalid option");
    		} catch (IOException e) {
    			
    			e.printStackTrace();
    		}
    		catch(NullPointerException e)
    		{
    			e.printStackTrace();
    		}
    		catch(Exception e)
    		{
    			if(e.getMessage().contains("could not be parsed"))
    			{
    				System.out.println("\nInvalid date, use the format (yyyy-MM-dd)");
    			}
    			else
    			{
    				e.printStackTrace();
    			}
    		}
    	}	
    }

	
    public static void doctorMenu(User user1)
    {
    	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    	boolean keepsesiondoc=true;
    	while(keepsesiondoc)
    	{
    		try
    		{
    			Doctor doc=hospiman.getDoctorByEmail(user1.getEmail());
    			
    			System.out.println("Hello Dr. "+user1.getUsername()+", what do you want to do? \n1) Appointments \n2) Medical records \n3) Medicine supply \n4) Export medicines to an Xml file \n5) Retrieve medicines from an Xml file (HospitaLine/xml/MedicinesImp.xml) to add them into the supply \n6) Export medicines to an HTML file \n7) Change your password \n8) Delete your account \n9) Exit");
    			int option=Integer.parseInt(br.readLine());
    			switch(option)
    			{
    			case 1:
    				System.out.println("\nDo you want to:\n1) View all your appointments \n2) View patients you have an appointment with");
    				int ele=Integer.parseInt(br.readLine());
    				
    				switch(ele)
    				{
    				case 1:
    					ArrayList<Appointment> aposdoc=hospiman.getAppointmentsDoc(doc.getId());
    					if(aposdoc.isEmpty())
    					{
    						System.out.println("\nYou have no appointments");
    					}
    					else
    					{
    						int i=0;
    						while(i<aposdoc.size())
    						{
    							System.out.println(aposdoc.get(i).toString());
    							i++;
    						}
    					}
    					
    					break;
    				case 2:
    					ArrayList<Patient> patis=hospiman.getPatientsByDoctor(doc.getId());
    					if(patis.isEmpty())
    					{
    						System.out.println("\nNo patients have appointments with you");
    					}
    					else
    					{
    						int i=0;
    						while(i<patis.size())
    						{
    							System.out.println(patis.get(i).toString());
    							i++;
    						}
    					}
    					
    					break;
    				default:
    					System.out.println("\nInvalid option");
    				}
    				break;
    			case 2:
    				ArrayList<Patient> patis=hospiman.getPatientsByDoctor(doc.getId());
    				if(patis.isEmpty())
    				{
    					System.out.println("\nNo patients have appointments with you");
    				}
    				else
    				{
    					int i=0;
    					while(i<patis.size())
    					{
    						System.out.println(patis.get(i).toString());
    						i++;
    					}
    					System.out.println("\nFrom this patients, do you want to: \n1)View their medical records \n2)Add a medical record \n3)Modify a medical record");
    					int opc=Integer.parseInt(br.readLine());
    					switch(opc)
    					{
    					case 1:
    						System.out.println("\nSpecify the id of the patient whose medical record you want to see");
    						int patiid=Integer.parseInt(br.readLine());
    						boolean is=hospiman.validatePatientOfTheDoctor(doc.getId(), patiid);
    						
    						if(is)
    						{
    							ArrayList<MedicalRecord> medrecs=hospiman.getMedicalRecordsByPatient(patiid);
    							if(medrecs.isEmpty())
    							{
    								System.out.println("\nThis patient does not have any medical record");
    							}
    							else
    							{
    								int j=0;
        							while(j<medrecs.size())
        							{
        								System.out.println(medrecs.get(j).toString());
        								j++;
        							}
    							}
    						}
    						else
    						{
    							System.out.println("\nNo such patient between your associated patients");
    						}
    						
    						break;
    					case 2:
    						System.out.println("\nSpecify the id of the patient whose medical record you want to add");
    						int patid=Integer.parseInt(br.readLine());
    						boolean yes=hospiman.validatePatientOfTheDoctor(doc.getId(), patid);
    						
    						if(yes)
    						{
    							System.out.println("\nIntroduce the diagnosis");
    							String diagnose=br.readLine();
    							if(diagnose.equals(""))
    							{
    								System.out.println("\nA diagnose is required");
    							}
    							else
    							{
    								System.out.println("\nWhat will the treatment be?");
    								String treatment=br.readLine();
    								
    								ArrayList<MedicineSupply> meds=hospiman.getAllMedicines();
    								if(meds.isEmpty())
    								{
    									System.out.println("\nThere are no medicines in the supply to add, so the medicine prescribed will for now be not specified, until this record is modified");
    									MedicalRecord medrec=new MedicalRecord(patid, diagnose, treatment, Date.valueOf(LocalDate.now()), null);
    									
    									boolean added=hospiman.addMedicalRecord(medrec);
    									if(added)
    									{
    										System.out.println("\nThe medical record was succesfully added");
    									}
    									else
    									{
    										System.out.println("\nThe medical record was not added");
    									}
    								}
    								else
    								{
    									int k=0;
    									boolean disponibilidad=false;
    									while(k<meds.size())
    									{
    										boolean disponible=hospiman.isMedicineAvailable(meds.get(k).getId());
    										if(disponible)
    										{
    											System.out.println(meds.get(k).toString());
    											disponibilidad=true;
    										}
    										
    										k++;
    									}
    									if(disponibilidad)
    									{
    										System.out.println("\nFrom this medicines, choose one to be prescribed (if you dont want to prescribe anything, press enter)");
    										String medidstr=br.readLine();
    										if(medidstr.equals(""))
    										{
    											MedicalRecord medrec=new MedicalRecord(patid, diagnose, treatment, Date.valueOf(LocalDate.now()), null);
        										boolean added=hospiman.addMedicalRecord(medrec);
        										if(added)
        										{
        											System.out.println("\nThe medical record was succesfully added");
        										}
        										else
        										{
        											System.out.println("\nThe medical record was not added");
        										}
    										}
    										else
    										{
    											int medid=Integer.parseInt(medidstr);
            									boolean disponible=hospiman.isMedicineAvailable(medid);
            									if(disponible)
            									{
            										MedicalRecord medrec=new MedicalRecord(patid, diagnose, treatment, Date.valueOf(LocalDate.now()), medid);
            										boolean added=hospiman.addMedicalRecord(medrec);
            										if(added)
            										{
            											System.out.println("\nThe medical record was succesfully added");
            										}
            										else
            										{
            											System.out.println("\nThe medical record was not added");
            										}
            									}
            									else
            									{
            										System.out.println("\nInvalid option");
            									}
    										}
    									}
    									else
    									{
    										System.out.println("\nThere are no medicines available, so no medicine will be prescribed for now");
    										MedicalRecord medrec=new MedicalRecord(patid, diagnose, treatment, Date.valueOf(LocalDate.now()), null);
    										boolean added=hospiman.addMedicalRecord(medrec);
    										if(added)
    										{
    											System.out.println("\nThe medical record was succesfully added");
    										}
    										else
    										{
    											System.out.println("\nThe medical record was not added");
    										}
    									}
    								}
    							}
    						}
    						else
    						{
    							System.out.println("\nNo such patient between your associated patients");
    						}
    						
    						break;
    					case 3:
    						System.out.println("\nSpecify the id of the patient whose medical record you want to modify");
    						int idpat=Integer.parseInt(br.readLine());
    						boolean ok=hospiman.validatePatientOfTheDoctor(doc.getId(), idpat);
    						
    						if(ok)
    						{
    							ArrayList<MedicalRecord> medrecs=hospiman.getMedicalRecordsByPatient(idpat);
    							int j=0;
    							while(j<medrecs.size())
    							{
    								System.out.println(medrecs.get(j).toString());
    								j++;
    							}
    							System.out.println("\nFrom this medical records, choose one to modify");
    							int medupid=Integer.parseInt(br.readLine());
    							boolean correct=hospiman.VerifyMedicalRecord(medupid, idpat);
    							
    							if(correct)
    							{
    								System.out.println("\nIntroduce the diagnosis");
    								String diagnose=br.readLine();
    								if(diagnose.equals(""))
    								{
    									System.out.println("\nA diagnose is required");
    								}
    								else
    								{
    									System.out.println("\nWhat will the treatment be?");
    									String treatment=br.readLine();
    									
    									ArrayList<MedicineSupply> meds=hospiman.getAllMedicines();
    									if(meds.isEmpty())
    									{
    										System.out.println("\nThere are no medicines in the supply to add, so the medicine prescribed will for now be not specified, until this record is modified");
    										MedicalRecord medrec=new MedicalRecord(medupid, idpat, diagnose, treatment, Date.valueOf(LocalDate.now()), null);
    										
    										boolean updated=hospiman.updateMedicalRecord(medrec);
    										if(updated)
    										{
    											System.out.println("\nThe medical record was succesfully updated");
    										}
    										else
    										{
    											System.out.println("\nThe medical record was not updated");
    										}
    									}
    									else
    									{
    										int k=0;
    										boolean disponiblemod=false;
    										while(k<meds.size())
    										{
    											boolean disponible=hospiman.isMedicineAvailable(meds.get(k).getId());
    											if(disponible)
    											{
    												System.out.println(meds.get(k).toString());
    												disponiblemod=true;
    											}
    											
    											k++;
    										}
    										if(disponiblemod)
    										{
    											System.out.println("\nFrom this medicines, choose one to be prescribed, if you dont want to prescribe anything, press enter");
    											String medidstr=br.readLine();
    											if(medidstr.equals(""))
    											{
    												MedicalRecord medrec=new MedicalRecord(medupid, idpat, diagnose, treatment, Date.valueOf(LocalDate.now()), null);
    												boolean updated=hospiman.updateMedicalRecord(medrec);
    	    										if(updated)
    	    										{
    	    											System.out.println("\nThe medical record was succesfully updated");
    	    										}
    	    										else
    	    										{
    	    											System.out.println("\nThe medical record was not updated");
    	    										}
    											}
    											else
    											{
    												int medid=Integer.parseInt(medidstr);
            										boolean disponible=hospiman.isMedicineAvailable(medid);
            										if(disponible)
            										{
            											MedicalRecord medrec=new MedicalRecord(medupid, idpat, diagnose, treatment, Date.valueOf(LocalDate.now()), medid);
            											boolean updated=hospiman.updateMedicalRecord(medrec);
            											if(updated)
            											{
            												System.out.println("\nThe medical record was succesfully updated");
            											}
            											else
            											{
            												System.out.println("\nThe medical record was not updated");
            											}
            										}
            										else
            										{
            											System.out.println("\nInvalid option");
            										}
    											}
    										}
    										else
    										{
    											System.out.println("\nThere are no medicines available, so no medicine will be prescribed for now");
    											MedicalRecord medrec=new MedicalRecord(medupid, idpat, diagnose, treatment, Date.valueOf(LocalDate.now()), null);
    											boolean updated=hospiman.updateMedicalRecord(medrec);
    											if(updated)
    											{
    												System.out.println("\nThe medical record was succesfully updated");
    											}
    											else
    											{
    												System.out.println("\nThe medical record was not updated");
    											}
    										}
    									}
    								}
    							}
    							else
    							{
    								System.out.println("\nInvalid option");
    							}
    							
    						}
    						else
    						{
    							System.out.println("\nNo such patient between your associated patients");
    						}
    						
    						break;
    					default:
    						System.out.println("\nInvalid option");
    					}
    				}
    				
    				break;
    			case 3:
    				System.out.println("\nDo you want to: \n1)View medicines of the supply \n2)Add medicine to the supply");
    				int elecio=Integer.parseInt(br.readLine());
    				
    				switch(elecio)
    				{
    				case 1:
    					ArrayList<MedicineSupply> meds=hospiman.getAllMedicines();
    					if(meds.isEmpty())
    					{
    						System.out.println("\nThere are no medicines in the supply");
    					}
    					else
    					{
    						int y=0;
    						while(y<meds.size())
    						{
    							System.out.println(meds.get(y).toString());
    							y++;
    						}
    					}
    					
    					break;
    				case 2:
    					System.out.println("\nSpecify the name of the medicine you want to add");
    					String name=br.readLine();
    					if(name.equals(""))
    					{
    						System.out.println("\nInvalid option");
    					}
    					else
    					{
    						System.out.println("\nSpecify the quantity (in grams)");
    						float quantity=Float.parseFloat(br.readLine());
    						if(quantity<0)
    						{
    							System.out.println("\nInvalid option");
    						}
    						else
    						{
    							MedicineSupply med=new MedicineSupply(name, quantity);
    							hospiman.addMedicine(med);
    						}
    					}
    					
    					break;
    				default:
    					System.out.println("\nInvalid option");
    				}
    				break;
    			case 4:
    				ArrayList<MedicineSupply> meds=hospiman.getAllMedicines();
    				if(meds.isEmpty())
    				{
    					System.out.println("\nThere are no medicines in the supply");
    				}
    				else
    				{
    					xmlman.Java2XmlMedicines(meds);
    					System.out.println("\nThis information has been stored in: HospitaLine/xml/MedicinesExp.xml");
    				}
    				
    				break;
    			case 5:
    				try
    				{
    					ArrayList<MedicineSupply> medsin=xmlman.Xml2JavaMedicines();
        				hospiman.AddMedicinesfromXml(medsin);
    				}
    				catch(UnmarshalException e)
    				{
    					System.out.println("\nThe Xml file is empty or corrupted");
    				}
    				
    				break;
    			case 6:
    				ArrayList<MedicineSupply> medsache=hospiman.getAllMedicines();
    				if(medsache.isEmpty())
    				{
    					System.out.println("\nThere are no medicines in the supply");
    				}
    				else
    				{
    					xmlman.Java2HTMLMedicines(medsache);
    					System.out.println("\nThe HTML is in HospitaLine/xml/Medicines.html");
    				}
    				
    				break;
    			case 7:
    				System.out.println("\nWhat will your new password be?");
    				String newpass=br.readLine();
    				if(newpass.equals(""))
    				{
    					System.out.println("\nInvalid password");
    				}
    				else
    				{
    					boolean cambiada=userman.changePassword(user1, newpass);
    					if(cambiada)
    					{
    						System.out.println("\nPassword changed");
    					}
    					else
    					{
    						System.out.println("\nPassword was not changed");
    					}
    				}
    				
    				break;
    			case 8:
    				System.out.println("\nIf you delete your account, you will be redirected to the principal menu, are you sure (press 1)?");
    				int deci=Integer.parseInt(br.readLine());
    				if(deci==1)
    				{
    					userman.deleteAccount(user1);
    					hospiman.DeleteDoctor(doc.getId());
    					keepsesiondoc=false;
    				}
    				
    				break;
    			case 9:
    				keepsesiondoc=false;
    				break;
    			default:
    				System.out.println("\nInvalid option");
    			}
    		}
    		catch (NumberFormatException e) {
    			
    			System.out.println("\nInvalid option");
    		} catch (IOException e) {
    			
    			e.printStackTrace();
    		}
    		catch(NullPointerException e)
    		{
    			e.printStackTrace();
    		}
    		catch(Exception e)
    		{
    			if(e.getMessage().contains("could not be parsed"))
    			{
    				System.out.println("\nInvalid date, use the format (yyyy-MM-dd)");
    			}
    			else
    			{
    				e.printStackTrace();
    			}
    		}
    	}
    }
	
}



	