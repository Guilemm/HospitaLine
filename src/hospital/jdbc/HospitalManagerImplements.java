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
import db.pojos.Patient;
import hospital.ifaces.HospitalManager;

public class HospitalManagerImplements implements HospitalManager {

	Connection c;
	
	public HospitalManagerImplements()
	{
		try {
			Class.forName("org.sqlite.JDBC");
			c=DriverManager.getConnection("jdbc:sqlite:Database1.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			
			//Aqui dentro del propio constructor deberia crear todas las tablas con createTables(), no se si es realmente necesario si ya estan hechas las tablas en la base de datos como tal
            createTables();//Esto hace que me de error, de que ya existen esas tablas, obviamente, pero por el momento lo dejo, al no afectar al programa como tal
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
	}
	
	private void createTables()//Hay que hacerlo(No muy seguro en realidad), primero doctros, luego patients, luego 
    {
		try
		{
			Statement stmt1=c.createStatement();
			String sql1= "CREATE TABLE doctors "
					   + "(id       INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
					   + " email     TEXT     NOT NULL   UNIQUE, "
					   + " name  TEXT	 NOT NULL,"
					   + "dob   DATE   NOT NULL,"
					   + "address TEXT,"
					   + "sex TEXT NOT NULL,"
					   + "department TEXT NOT NULL,"
					   + "experience INTEGER)";
			stmt1.executeUpdate(sql1);
			stmt1.close();
			
			Statement stmt2=c.createStatement();
			String sql2= "CREATE TABLE patients "
					   + "(id       INTEGER  UNIQUE NOT NULL PRIMARY KEY AUTOINCREMENT,"
					   + " email     TEXT    UNIQUE NOT NULL,"
					   + " name  TEXT	 NOT NULL,"
					   + "dob   DATE   NOT NULL,"
					   + "address TEXT,"
					   + "sex TEXT NOT NULL)";
			stmt2.executeUpdate(sql2);
			stmt2.close();
			
			Statement stmt3=c.createStatement();
			String sql3= "CREATE TABLE appointments "
					   + "(id       INTEGER  UNIQUE  NOT NULL PRIMARY KEY AUTOINCREMENT,"
					   + " patientid     INTEGER    REFERENCES  patients(id)  ON DELETE CASCADE, "
					   + " doctorid  INTEGER    REFERENCES  doctors(id)  ON DELETE CASCADE,"
					   + "date   DATE   NOT NULL)";
			stmt3.executeUpdate(sql3);
			stmt3.close();
			
			Statement stmt4=c.createStatement();
			String sql4= "CREATE TABLE medsupply "
					   + "(id       INTEGER UNIQUE NOT NULL PRIMARY KEY AUTOINCREMENT,"
					   + " name  TEXT	 NOT NULL,"
					   + "quantity   REAL   NOT NULL)";
			stmt4.executeUpdate(sql4);
			stmt4.close();
			
			Statement stmt5=c.createStatement();
			String sql5= "CREATE TABLE medrecords "
					   + "(id       INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,"
					   + "diagnose     TEXT     NOT NULL, "
					   + "treatment  TEXT,"
					   + "date   DATE   NOT NULL,"
					   + "medprescid INTEGER REFERENCES medsupply(id) ON DELETE SET NULL,"
					   + "patientid INTEGER NOT NULL REFERENCES patients(id) ON DELETE CASCADE)";
			stmt5.executeUpdate(sql5);
			stmt5.close();
			
			
			Statement stmtSeq = c.createStatement();//Por ahora para appointments y medrecords no voy a hacer esto, aunque habra que hacerlo muy probablemente
			String sqlSeq = "INSERT INTO sqlite_sequence (name, seq) VALUES ('doctors', 1)";
			stmtSeq.executeUpdate(sqlSeq);
			sqlSeq = "INSERT INTO sqlite_sequence (name, seq) VALUES ('patients', 1)";
			stmtSeq.executeUpdate(sqlSeq);
			sqlSeq = "INSERT INTO sqlite_sequence (name, seq) VALUES ('medsupply', 1)";
			stmtSeq.executeUpdate(sqlSeq);
			stmtSeq.close();
		}
		catch(Exception e)
		{
			if(e.getMessage().contains("already exist"))
			{
				return;
			}
			System.out.println("\nDatabase Error");
			e.printStackTrace();
		}
		
    }
	
	public void CloseConnection()
	{
		if(c!=null)
		{
			try
			{
				c.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public ArrayList<Doctor> ViewAllDoctors()
	{
		try
		{
			Statement stmt1=c.createStatement();
			String sql1="SELECT * FROM doctors";
			ResultSet rs1=stmt1.executeQuery(sql1);
			ArrayList<Doctor> docs=new ArrayList<Doctor>();
			while(rs1.next())
			{
				int id=rs1.getInt("id");
				String email=rs1.getString("email");
				String name=rs1.getString("name");
				long dobMillis = rs1.getLong("dob");
				Date utilDate = new Date(dobMillis);
				int experience=rs1.getInt("experience");
				String address=rs1.getString("address");
				String department=rs1.getString("department");
				String sex=rs1.getString("sex");
			
				Doctor doqui=new Doctor(id, email, name, utilDate, experience, address, department, sex);
				docs.add(doqui);
				
			}
			
			rs1.close();
			stmt1.close();
			return docs;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
			
		}
		catch(NullPointerException e)//Esta excepcion puede darla si la date es null, o si no hay doctor con esa id
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	@Override
	public void AddPatient(Patient pati)
	{
		try
		{
			
			String sql="INSERT INTO patients(email, name, dob, address, sex) VALUES (?,?,?,?,?)";
			PreparedStatement prep=c.prepareStatement(sql);
			prep.setString(1,  pati.getEmail());
			prep.setString(2,  pati.getName());
			prep.setDate(3,  pati.getDob());
			prep.setString(4,  pati.getAdrdress());
			prep.setString(5,  pati.getSex());
			prep.executeUpdate();
			prep.close();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} 
		
	}
	
	@Override
	public void AddDoctor(Doctor doc)
	{
		try
		{
			String sql="INSERT INTO doctors(email, name, dob, address, sex, department, experience) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement prep=c.prepareStatement(sql);
			prep.setString(1,  doc.getEmail());
			prep.setString(2,  doc.getName());
			prep.setDate(3,  doc.getDob());
			prep.setString(4,  doc.getAddress());
			prep.setString(5,  doc.getSex());
			prep.setString(6, doc.getDepartment());
			prep.setInt(7, doc.getExperience());
			prep.executeUpdate();
			prep.close();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} 
		
	}
	
	@Override
    public void BookAppointment(Appointment apo)//Esto es un insert, si ya viene el appointment, no es necesario pedir los datos de este, voy a permitir por el momento que hayan appointments iguales (solo diferenciados por el id)
    {
		try
		{
			String sql = "INSERT INTO appointments (patientid, doctorid, date)"
					+ "VALUES (?,?,?);";
			PreparedStatement prep=c.prepareStatement(sql);
			prep.setInt(1, apo.getPatientId());
			prep.setInt(2, apo.getDoctorId());
			prep.setDate(3, apo.getDate());
			prep.executeUpdate();
			prep.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		}
		
    }
	
	@Override
	public Patient getPatientByEmail(String email)
	{
		try
		{
			Statement stmt=c.createStatement();
			String sql="SELECT * FROM patients WHERE email LIKE '"+email+"'";
			ResultSet rs=stmt.executeQuery(sql);
			int id=rs.getInt("id");
			String name=rs.getString("name");
			long dobMillis = rs.getLong("dob");
			Date utilDate = new Date(dobMillis);
			String address=rs.getString("address");
			String sex=rs.getString("sex");
			stmt.close();
			rs.close();
			Patient pat=new Patient(id, name, email, address, sex, utilDate);
			return pat;
			
			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
	public ArrayList<Appointment> getAppointmentsPatient(int id)
	{
		try
		{
			Statement stmt=c.createStatement();
			String sql="SELECT * FROM appointments WHERE patientid = "+id;
			ResultSet rs=stmt.executeQuery(sql);
			ArrayList<Appointment> apos=new ArrayList<Appointment>();
			while(rs.next())
			{
				int apoid=rs.getInt("id");
				int patid=rs.getInt("patientid");
				int docid=rs.getInt("doctorid");
				long dobMillis = rs.getLong("date");
				Date utilDate = new Date(dobMillis);
				Appointment apo=new Appointment(apoid, patid, docid, utilDate);
				apos.add(apo);
			}
			
			stmt.close();
			rs.close();
			return apos;
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
	public void EliminateAppointment(int id, int patid)//Esto es un delete, cuando la id que le doy no existe no dice nada, no se si habria que arreglar eso
	{
		try
		{
			Statement stmt=c.createStatement();
			String sqlprov="SELECT * FROM appointments WHERE id="+id+" AND patientid="+patid;
			ResultSet rs=stmt.executeQuery(sqlprov);
			if(rs.getDate("date")==null)
			{
				System.out.println("\nThis appointment does not exist in your appointments");
				stmt.close();
				rs.close();
			}
			else
			{
				String sql = "DELETE FROM appointments WHERE id=?";
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setInt(1, id);
				prep.executeUpdate();
				prep.close();
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} 
	}
	
	@Override
	public void ModifyAppointment(int apo, int patid) //Por el momento este modify appointment va a ser el del patient(Solo se podra modificar o el doctorid o el date). Esto un Update, en este metodo habria que ver segun seas el doctor o el patient, ademas de poder cambiar la fecha, cambiar el paciente o el doctor respectivamente, alomejor hay que hacer dos metodos de modify uno para el patient y otro para el doctor
	{
		try
		{
			Statement stmtprov=c.createStatement();
			String sqlprov="SELECT * FROM appointments WHERE id="+apo+" AND patientid="+patid;
			ResultSet rsprov=stmtprov.executeQuery(sqlprov);
			if(rsprov.getDate("date")==null)
			{
				System.out.println("\nThis appointment does not exist in your appointments");
				stmtprov.close();
				rsprov.close();
			}
			else
			{
				BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
				Statement stmt=c.createStatement();
				String sql="SELECT * FROM appointments WHERE id="+apo;
				ResultSet rs=stmt.executeQuery(sql);
				System.out.println("\nDoctor id is "+rs.getInt("doctorid")+", if you want to keep it the same press enter, if not type the new id: ");
				String docidstr=br.readLine();
				if(!docidstr.isEmpty())
				{
					String sql1="UPDATE appointments SET doctorid = ? WHERE id = ?";
					PreparedStatement prep=c.prepareStatement(sql1);
					prep.setInt(1,  Integer.parseInt(docidstr));
					prep.setInt(2,  apo);
					prep.executeUpdate();
					prep.close();
				}
				long dobMillis = rs.getLong("date");
				Date utilDate = new Date(dobMillis);
				System.out.println("\nDate is "+utilDate+", if you want to keep it the same press enter, if not type the new date (yyyy-MM-dd): ");
				String datestr=br.readLine();
				if(!datestr.isEmpty())
				{
					if(LocalDate.parse(datestr).isBefore(LocalDate.now()))
					{
						System.out.println("\nInvalid option");
						return;
					}
					String sql2="UPDATE appointments SET date = ? WHERE id = ?";
					PreparedStatement prep=c.prepareStatement(sql2);
					prep.setDate(1,  Date.valueOf(datestr));
					prep.setInt(2,  apo);
					prep.executeUpdate();
					prep.close();
				}
				stmt.close();
				rs.close();
				
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		catch (DateTimeParseException e) {
            System.out.println("\nInvalid date. Use the format (yyyy-MM-dd)");
		}
	}
	
	
	@Override
	public Doctor ViewDoctorInfo(int docidsee) //Este y el de view patient son SELECT, tambien el de view medrec, es mejor quitar lo de Doctor doc, y que solo reciba la id, o que se le pida la id en el propio metodo, igual seria en patient
	{
		try {
			
			Statement stmt = c.createStatement();
			String sql = "SELECT * FROM doctors WHERE id = "+docidsee;
			ResultSet rs = stmt.executeQuery(sql);
			int id=rs.getInt("id");
			String email=rs.getString("email");
			String name=rs.getString("name");
			long dobMillis = rs.getLong("dob");
			Date utilDate = new Date(dobMillis);
			int experience=rs.getInt("experience");
			String address=rs.getString("address");
			String department=rs.getString("department");
			String sex=rs.getString("sex");
		
			rs.close();
			stmt.close();
			
			Doctor doqui=new Doctor(id, email, name, utilDate, experience, address, department, sex);
			return doqui;
				
		}
		catch (SQLException e) {
			
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} 
		catch(NullPointerException e)//Esta excepcion puede darla si la date es null, o si no hay doctor con esa id
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ArrayList<MedicalRecord> ViewAllMedicalRecords(int patid)
	{
		try
		{
			Statement stmt=c.createStatement();
			String sql="SELECT * FROM medrecords WHERE patientid = "+patid;
			ResultSet rs=stmt.executeQuery(sql);
			ArrayList<MedicalRecord> medrecs=new ArrayList<MedicalRecord>();
			while(rs.next())
			{
				int id = rs.getInt("id");
				String diagnose = rs.getString("diagnose");
				String treatment = rs.getString("treatment");
				long dobMillis = rs.getLong("date");
				Date utilDate = new Date(dobMillis);
				int medprescid=rs.getInt("medprescid");//El patient id no es necesario que lo coja, ya que se obvia al estar cogiendo los medrecords de un patient concreto
				MedicalRecord medreco = new MedicalRecord(id, diagnose, treatment, utilDate, medprescid, patid);//Para que esto funcione se necesita contructor de MedicalRecords
				medrecs.add(medreco);
			}
			stmt.close();
			rs.close();
			return medrecs;
		}
        catch (SQLException e) {
			
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
	public MedicalRecord ViewOneMedicalRecord(int medrecid, int patid)
	{
		try
		{
			Statement stmt=c.createStatement();
			String sql="SELECT * FROM medrecords WHERE id = "+medrecid+" AND patientid = "+patid;
			ResultSet rs=stmt.executeQuery(sql);
			int id = rs.getInt("id");
			String diagnose = rs.getString("diagnose");
			String treatment = rs.getString("treatment");
			long dobMillis = rs.getLong("date");
			Date utilDate = new Date(dobMillis);
			int medprescid=rs.getInt("medprescid");//El patient id no es necesario que lo coja, ya que se obvia al estar cogiendo los medrecords de un patient concreto
			MedicalRecord medreco = new MedicalRecord(id, diagnose, treatment, utilDate, medprescid, patid);//Para que esto funcione se necesita contructor de MedicalRecords
			
			stmt.close();
			rs.close();
			return medreco;
			
		}
        catch (SQLException e) {
			
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
	public void ViewPatientInfo() 
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nDo you want to see information of: \nAll the patients 1)\nAn specific patient 2)");
			int choose=Integer.parseInt(br.readLine());
			switch(choose)
			{
			case 1:
				Statement stmt1 = c.createStatement();
				String sql1="SELECT * FROM patients";
				ResultSet rs1=stmt1.executeQuery(sql1);
				while(rs1.next())
				{
					int id=rs1.getInt("id");
					String email=rs1.getString("email");
					String name=rs1.getString("name");
					long dobMillis = rs1.getLong("dob");
					Date utilDate = new Date(dobMillis);
					String address=rs1.getString("address");
					String sex=rs1.getString("sex");
				
					Patient pati=new Patient(id, name, email, address, sex, utilDate);
					System.out.println(pati.toString());
					
				}
				rs1.close();
				stmt1.close();
				break;
			case 2:
				System.out.println("\nSpecify the id of the patient you want to see information from: ");
				int patid=Integer.parseInt(br.readLine());
				Statement stmt = c.createStatement();
				String sql = "SELECT * FROM patients WHERE id = "+patid;
				ResultSet rs = stmt.executeQuery(sql);
				int id=rs.getInt("id");
				String email=rs.getString("email");
				String name=rs.getString("name");
				long dobMillis = rs.getLong("dob");
				Date utilDate = new Date(dobMillis);
				String address=rs.getString("address");
				String sex=rs.getString("sex");
			
				Patient pati=new Patient(id, name, email, address, sex, utilDate);
				System.out.println(pati.toString());
				rs.close();
				stmt.close();
				break;
			default:
				System.out.println("\nInvalid option");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();//Preguntar si con esto sirve, creo que lo suyo seria que pusiera esto pero que no se terminara el programa
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		catch(NullPointerException e)//Esta excepcion puede darla si la date es null, o si no hay paciente con esa id
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void ViewMedicalRecord() 
	{
		try 
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Indicate the id of the patient whose medical record you want to see");
			int patid = Integer.parseInt(br.readLine());
			System.out.println("Do you want to see all the medical records of this patient, or an specific report.\n 1) To view all the reports\n 2) To view only one report\n");
			int choose=Integer.parseInt(br.readLine());
			switch(choose)
			{
			case 1:
				Statement stmt = c.createStatement();
				String sql="SELECT * FROM medrecords WHERE patientid = "+patid;
				ResultSet rs= stmt.executeQuery(sql);
				while(rs.next())
				{
					int id = rs.getInt("id");
					String diagnose = rs.getString("diagnose");
					String treatment = rs.getString("treatment");
					long dobMillis = rs.getLong("date");
					Date utilDate = new Date(dobMillis);
					int medprescid=rs.getInt("medprescid");//El patient id no es necesario que lo coja, ya que se obvia al estar cogiendo los medrecords de un patient concreto
					MedicalRecord medreco = new MedicalRecord(id, diagnose, treatment, utilDate, medprescid, patid);//Para que esto funcione se necesita contructor de MedicalRecords
					System.out.println(medreco);
					
				}
				
				rs.close();
				stmt.close();
				
			break;	
			case 2:
				System.out.println("Select the medical record you want to see, by indicating its id");
				int idmed=Integer.parseInt(br.readLine());
				Statement stmt2 = c.createStatement();
				String sql2="SELECT * FROM medrecords WHERE patientid = "+patid+" AND id="+idmed;
				ResultSet rs2= stmt2.executeQuery(sql2);
				int id = rs2.getInt("id");
				String diagnose = rs2.getString("diagnose");
				String treatment = rs2.getString("treatment");
				long dobMillis = rs2.getLong("date");
				Date utilDate = new Date(dobMillis);
				int medprescid=rs2.getInt("medprescid");//El patient id no es necesario que lo coja, ya que se obvia al estar cogiendo los medrecords de un patient concreto
				
				MedicalRecord medreco2 = new MedicalRecord(id, diagnose, treatment, utilDate, medprescid, patid);//Para que esto funcione se necesita contructor de MedicalRecords
				System.out.println(medreco2);
				
				rs2.close();
				stmt2.close();

			
			break;
			
			default:
				System.out.println("Invalid option");
			}

		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void UpdateMedicalRecord()//Un Update, parece ser que al hacer update e id que no existen, simplement no hace ningun cambio (no suelta ningun error tampoco por consola), al menos eso es lo que parece
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Indicate the id of the patient whose medical record you want to update");//Esto seria redundante al tener el med record un id, pero por el momento no molesta
			int id=Integer.parseInt(br.readLine());
			System.out.println("What medical record do you want to make the changes in");
			int medid=Integer.parseInt(br.readLine());
			int choose=4;
			while(choose!=0)
			{
				System.out.println("What do you want to do: \n1) Change the diagnose \n2) Change the treatment \n3) Change the medical prescription \n0)Finish with the changes\n");//No tendria mucho sentido cambiar la fecha o si?
				choose=Integer.parseInt(br.readLine());
				switch(choose)
				{
					case 1: 
						System.out.println("What will the new diagnose be?\n");
						String diagnose=br.readLine();
						String sql = "UPDATE medrecords SET diagnose=? WHERE id=?";
						PreparedStatement prep = c.prepareStatement(sql);
						prep.setString(1, diagnose);
						prep.setInt(2, medid);
						prep.executeUpdate();
						prep.close();
						
						break;
					case 2:
						System.out.println("What will the new treatment be?\n");
						String treatment=br.readLine();
						String sql2 = "UPDATE medrecords SET treatment=? WHERE id=?";
						PreparedStatement prep2 = c.prepareStatement(sql2);
						prep2.setString(1, treatment);
						prep2.setInt(2, medid);
						prep2.executeUpdate();
						prep2.close();

						break;
					case 3:
						// meter algo del medsupply, sin modificar el medsupply, ya que eso sucede cuando se ClaimMedicine()
						System.out.println("Specifiy the id of the medicine from the new medicine supply that you want to prescribe ");
						int supid=Integer.parseInt(br.readLine());//Probablemente hayan excepciones y esas cosas
						
						String sql3 = "UPDATE medrecords SET medprescid=? WHERE id=?";
						PreparedStatement prep3 = c.prepareStatement(sql3);
						prep3.setInt(1, supid);
						prep3.setInt(2, medid);
						prep3.executeUpdate();
						prep3.close();
						
						break;
					case 0:
						
						break;
					default: 
						System.out.println("\nInvalid option\n");
				}
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void AddMedicalRecord()
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Indicate the id of the patient with this medical record : ");
			int id=Integer.parseInt(br.readLine());
			System.out.println("\nWhat is the diagnose? : ");
			String diagnose=br.readLine();
			System.out.println("\nWhat is the treatmet? : ");
			String treatment=br.readLine();
			//System.out.println();//Seria lo suyo que la date se pusiera de manera automatica
			//Date date=s;
			System.out.println("\nSpecifiy the id of the medicine you want to prescribe: ");
			int medprescid=Integer.parseInt(br.readLine());
			
			String sql = "INSERT INTO medrecords (diagnose, treatment, date, medprescid, patientid) "
					+ "VALUES (?,?,?,?,?);";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, diagnose);
			prep.setString(2, treatment);
			prep.setDate(3,  Date.valueOf(LocalDate.now()));//Haciendo esto se almacena como milisegundos en la base de datos
			prep.setInt(4, medprescid);
			prep.setInt(5, id);
			
			prep.executeUpdate();
			prep.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void AddMedicine()//Se supone que esto lo hacen los doctors
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nSpecifiy the name of the medicine: ");
			String name=br.readLine();
			System.out.println("\nSpecify the quantity (In grams): ");
			Float quantity=Float.parseFloat(br.readLine());
			String sql="INSERT INTO medsupply(name, quantity)" 
			            + "VALUES (?,?);";
			PreparedStatement prep=c.prepareStatement(sql);
			prep.setString(1, name);
			prep.setFloat(2, quantity);
			prep.executeUpdate();
			prep.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void ClaimMedicine(int medrecidclaim, int patid)//Exclusivo de pacientes, habria que hacer que una vez reclamada la medicina no la pueda volver a reclamar, esto se comprobara viendo si el medprescid es NULL
	{
		try
		{

			Statement stmt=c.createStatement();
			String sql="SELECT medprescid FROM medrecords WHERE id="+ medrecidclaim+" AND patientid="+patid;
			ResultSet rs=stmt.executeQuery(sql);
			rs.getInt("medprescid");//Primero obtengo el valor que quiero comprobar si es null, y luego ya una vez tengo el valor que quiero comprobar, en este caso medprescid, hago el .wasNull()
			if(rs.wasNull())//Esto es para ver si medprescid es null, no se puede hacer medprescid ==null, porque en ese caso devuelve 0 la base de datos, hay que hacer esto para comprobar si era null
			{
				System.out.println("\nIt is not possible to claim the prescribed medicine, due to it having been claimed before, because there was no medicine prescribed in the first place, or because this medical record does not exist");
			}
			else//En caso de si poder reclamar la medicina, primero quitar el medicine supply, y u8na vez hecho eso automticamnente se vuelve null el medprescid
			{
				String sql2="DELETE FROM medsupply WHERE id = (SELECT medprescid FROM medrecords WHERE id= ? )";//Aqui no hace falta comprobar id del paciente, ya que ya la he comprobado arriba 
				PreparedStatement prep=c.prepareStatement(sql2);
				prep.setInt(1, medrecidclaim);
				prep.executeUpdate();
				prep.close();
				System.out.println("\nMedicine claimed");
			}
			
			stmt.close();
			rs.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
        catch (NumberFormatException e) {
			
			e.printStackTrace();
		}
		
	}

	

}
