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





public class Menu {
	
	
	private static HospitalManagerImplements hospiman;
	private static UserManagerImplements userman;
	
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
	
	public static void login()//Comentario para nuevo push
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nEnter your email: ");
			String email=br.readLine();
			System.out.println("\nEnter your password: ");
			String password=br.readLine();
			User user1= userman.login(email, password);//Si el user es null va directo al catch NullPointerException
			if(user1.getRole().getName().equals("patient"))
			{
				Patient pat=hospiman.getPatientByEmail(user1.getEmail());
				boolean keepsesionpat=true;
				while(keepsesionpat)
				{
					System.out.println("Hello "+user1.getUsername()+ ", chose one option:\n1) Your appointments\n2) Doctors\n3) View your medical records\n4) Exit");
					int option=Integer.parseInt(br.readLine());
					switch(option)
					{
					case 1:
						System.out.println("\nDo you want to: \n1) Book an appointment\n2) Cancel an appointment\n3) Modify an appointment");
						int opcion=Integer.parseInt(br.readLine());
						switch(opcion)
						{
						case 1:
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
								
								Appointment apo=new Appointment(pat.getId(), docid, Date.valueOf(datestr));
								hospiman.BookAppointment(apo);
							}
							
						
							break;
						case 2:
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
								hospiman.EliminateAppointment(apcancel, pat.getId());
							}
							
							break;
						case 3:
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
								hospiman.ModifyAppointment(apupid, pat.getId());
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
								System.out.println("\nThis medical record, does not exist");
							}
							else
							{
								System.out.println(medrec.toString());
							}
							
							
							break;
						case 3:
							System.out.println("\nWhich one of your Medical Records do you want to claim medicine from? (Specify the id of the Medical Record): ");
							int medrecidclaim=Integer.parseInt(br.readLine());
							hospiman.ClaimMedicine(medrecidclaim, pat.getId());
							
							break;
						default:
							System.out.println("\nInvalid option");
						}
						
						
						break;
					case 4:
						keepsesionpat=false;
						break;
					default:
						System.out.println("\nInvalid option");
					}
					
				}
				
			}
			if(user1.getRole().getName().equals("doctor"))
			{
				boolean keepsesiondoc=true;
				while(keepsesiondoc)
				{
					System.out.println("Hello Dr/a."+user1.getUsername()+", what do you want to do: \n1) Appointments \n2) Medical records \n3) Medicine supply \n4) Exit");
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
}
