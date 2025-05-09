package hospital.ui;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;

import db.pojos.Doctor;
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
			System.out.println("\nEnter your name: ");
			String username=br.readLine();
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
			
			User user=new User(username, password, email);
			Doctor doc=new Doctor(email, name, Date.valueOf(dobstr), experience, address, department, sexstr);
			userman.register(user);
			Role role=userman.getRole("doctor");
			userman.assignRole(user, role);
			//Aqui iría hospiman.adddoctor(), solo con la parte de la database
			
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
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
			System.out.println("\nEnter your username: ");
			String username=br.readLine();
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
			User user=new User(username, password, email);
			Patient pat=new Patient(name, email, address, sexstr, Date.valueOf(dobstr));
			userman.register(user);
			Role role=userman.getRole("patient");
			userman.assignRole(user, role);
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		/*catch( e)//Esto para cuando haya un username igual
		{
			System.out.println("\nUsername already chose, choose another");
		}*/
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
			User user1= userman.login(email, password);//Si el user es null va directo al catch NullPointerException
			if(user1.getRole().getName().equals("patient"))
			{
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
							//hospiman.ViewDoctorInfo();//Hacer que este sea para ver a todos los doctors
							System.out.println("\nFrom this doctors, choose one for you appointment (indicate its id)");
							int docid=Integer.parseInt(br.readLine());
							//Pedir aqui datos del appointment
							//hospiman.BookAppointment();//Pasarle como argumento el doctor que he creado
							break;
						case 2:
							//hospiman.getAppointments();//Para obtener los appointments de este patient
							System.out.println("\nWhich of your appointments do you want to cancel?");
							int apcancel=Integer.parseInt(br.readLine());
							//hospiman.EliminateAppointment();//Hacer que como argumento coja un appointment, y el id del paciente que este usando este metodo
							
							break;
						case 3:
							//hospiman.getAppointments();//Para obtener los appointments de este patient
							System.out.println("\nWhich of your appointments do you want to update?");
							int apup=Integer.parseInt(br.readLine());
							//Pedir aqui los datos de la modificacion
							//hospiman.ModifyAppointment();//Hacer que se le pase el appointment id y el id de este patient
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
							//hospiman.ViewDoctorInfo();//Este metodo separarlo en ver todo y ver solo uno entre estos dos casos
							break;
						case 2:
							
							break;
						default:
							System.out.println("\nInvalid option");
						}
						break;
					case 3:
						System.out.println("\nDo you want to see: \n1) All your medical records \n2) Just one \n 3) Claim medicine from a medical record");
						int opc=Integer.parseInt(br.readLine());
						switch(opc)
						{
						case 1:
							//Pedir aqui nada y enseñar todos sus medrecords
							break;
						case 2:
							//Pedir aqui el id del medical record que quiere ver, y pasarselo al metodo
							break;
						case 3:
							//Aqui iria el metodo claim medicine
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
