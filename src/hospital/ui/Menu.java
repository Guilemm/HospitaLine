package hospital.ui;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import db.pojos.Appointment;
import db.pojos.Doctor;
import db.pojos.MedicalRecord;
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
	
	public static void main(String args[])//Asi no se va a quedar el menu, es para probar lo metodos
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
					hospiman.CloseConnection();//Si se pone esto se cierra la conexion por eso al final esta puesto
					userman.close();
					keep=false;
					break;
				default:
					System.out.println("\nInvalid option");
				}
				
				
			}
			catch (NumberFormatException e) {
				
				e.printStackTrace();
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
			System.out.println("\nEnter your name: ");
			String name=br.readLine();
			System.out.println("\nEnter your password: ");
			String password=br.readLine();
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
			System.out.println("\nExperience (years): ");
			int experience=Integer.parseInt(br.readLine());
			
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
		catch(Exception e)//Esto para cuando haya un username igual
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
			System.out.println("\nEnter your name: ");
			String name=br.readLine();
			System.out.println("\nEnter your password: ");
			String password=br.readLine();
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
		catch(Exception e)//Esto para cuando haya un username igual
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
				
				System.out.println("Hello "+user1.getUsername()+ ", chose one option:\n1) Your appointments\n2) Doctors\n3) View your medical records\n4) Export information to an Xml file \n5) Retrieve information from an Xml file \n6) Exit");
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
							System.out.println("\nFrom this doctors, choose one for you appointment (indicate its id)");
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
								boolean aver=hospiman.CheckAvailability(hour, docid);
								if(aver)
								{
									Appointment apo=new Appointment(pat.getId(), docid, Date.valueOf(datestr), hour);
									hospiman.BookAppointment(apo);
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
									boolean disp=hospiman.CheckAvailability(hour, docid);
									if(disp)
									{
										Appointment apo=new Appointment(pat.getId(), docid, Date.valueOf(datestr), hour);
										boolean modified=hospiman.ModifyAppointment(apupid, apo);
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
					System.out.println("\nDo you want to export:\n1) Your appointments to Xml\n2) Your medical records to Xml");
					int elec=Integer.parseInt(br.readLine());
					switch(elec)
					{
					case 1:
						ArrayList<Appointment> apos=hospiman.getAppointmentsPatient(pat.getId());
						if(apos.isEmpty())
						{
							System.out.println("\nYou have no appointments");
						}
						else
						{
							xmlman.Java2XmlAppointments(apos);
						}
						break;
						
					case 2:
						ArrayList<MedicalRecord> medrecs=hospiman.ViewAllMedicalRecords(pat.getId());
						if(medrecs.isEmpty())
						{
							System.out.println("\nYou have no medical records");
						}
						else
						{
							xmlman.Java2XmlMedicalRecords(medrecs);
						}
						break;
						
					default:
						System.out.println("\nInvalid option");	
					}
					
					break;
					
				case 5:
					System.out.println("\nDo you want to import:\n1) Your appointments from Xml \n2) Your medical records from Xml");
					int ele=Integer.parseInt(br.readLine());
					switch(ele)
					{
					case 1:
						ArrayList<Appointment> apos=xmlman.Xml2JavaAppointments();
						hospiman.AddAppointmentsfromXml(apos, pat.getId());
						
						break;
					case 2:
						ArrayList<MedicalRecord> medrecs=xmlman.Xml2JavaMedicalRecords();
						//Hacer metodo para añadir esos medical records
						
						break;
					default:
						System.out.println("\nInvalid option");
					}
					
					break;
					
				case 6:
					keepsesionpat=false;
					break;
					
				default:
					System.out.println("\nInvalid option");
				}
			}
			catch (NumberFormatException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			catch(Exception e)//Lo suyo será separar bloque doctors y patients en dos public voids
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
				
				System.out.println("Hello Dr/a. "+user1.getUsername()+", what do you want to do? \n1) Appointments \n2) Medical records \n3) Medicine supply \n4) Exit");
				int option=Integer.parseInt(br.readLine());
				switch(option)
				{
				case 1:
					//Aqui se puede solo ver appointments, todos o uno en concreto, de los que tengas, si no tienes te lo pone 
					break;
				case 2:
					//Aqui se puede ver, update y add medical records, de pacientes con appointments con el doctor, si no hay appointments te lo pone
					break;
				case 3:
					//Aqui hay una opcion para ver todas las medicines del supply , y para añadir medicines nuevas
					break;
				
				case 4:
					keepsesiondoc=false;
					break;
				default:
					System.out.println("\nInvalid option");
				}
			}
			catch (NumberFormatException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			catch(Exception e)//Lo suyo será separar bloque doctors y patients en dos public voids
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
