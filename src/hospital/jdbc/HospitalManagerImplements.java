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
import java.time.chrono.ChronoLocalDate;
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

    Connection c;

    public HospitalManagerImplements()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            c=DriverManager.getConnection("jdbc:sqlite:Database1.db");
            c.createStatement().execute("PRAGMA foreign_keys=ON");

            //Aqui dentro del propio constructor deberia crear todas las tablas con createTables(), no se si es realmente necesario si ya estan hechas las tablas en la base de datos como tal
            createTables();//Esto hace que me de error, de que ya existen esas tablas, obviamente, pero por el momento lo dejo, al no afectar al programa como tal
            
            //Metodo para que si hay appointments en el pasado de la fecha actual, estos se eliminen automaticamente
            eliminatePastAppointments();
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
                    + "date   DATE   NOT NULL,"
                    + "hour INTEGER NOT NULL)";
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
    
    private void eliminatePastAppointments()
    {
    	try
    	{
    		Statement stmt=c.createStatement();
    		String sql="SELECT * FROM appointments";
    		ResultSet rs=stmt.executeQuery(sql);
    		
    		while(rs.next())
    		{
    			long dobMillis = rs.getLong("date");
                Date utilDate = new Date(dobMillis);
                LocalDate fechalocal=utilDate.toLocalDate();
                if(fechalocal.isBefore(LocalDate.now()))
                {
                	int apoid=rs.getInt("id");
                	String sqldelete="DELETE FROM appointments WHERE id = ?";
                	PreparedStatement prep=c.prepareStatement(sqldelete);
                	prep.setInt(1,  apoid);
                	prep.executeUpdate();
                	prep.close();
                }
    		}
    	}
    	catch(SQLException e)
        {
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
    public void DeleteDoctor(int id)
    {
    	try
    	{
    		String sql="DELETE FROM doctors WHERE id= ?";
    		PreparedStatement prep=c.prepareStatement(sql);
    		prep.setInt(1,  id);
    		prep.executeUpdate();
    		prep.close();
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void DeletePatient(int id)
    {
    	try
    	{
    		String sql="DELETE FROM patients WHERE id= ?";
    		PreparedStatement prep=c.prepareStatement(sql);
    		prep.setInt(1,  id);
    		prep.executeUpdate();
    		prep.close();
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
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
            prep.setString(4,  pati.getAddress());
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
    public void BookAppointment(Appointment apo) throws SQLException
    {
    	String sql = "INSERT INTO appointments (patientid, doctorid, date, hour)"
                + "VALUES (?,?,?,?);";
        try(PreparedStatement prep=c.prepareStatement(sql))
        {
            prep.setInt(1, apo.getPatientId());
            prep.setInt(2, apo.getDoctorId());
            prep.setDate(3, apo.getDate());
            prep.setInt(4,  apo.getHour());
            prep.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
        catch (NumberFormatException e) {

            e.printStackTrace();
        }

    }

    @Override
    public boolean CheckAvailability(int hour, int docid, Date date) 
    {
    	try
    	{
    		Statement stmt=c.createStatement();
            String sql="SELECT * FROM appointments";
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {
            	int hourbase=rs.getInt("hour");
                int docidbase=rs.getInt("doctorid");
                long dobMillis = rs.getLong("date");
                Date utilDate = new Date(dobMillis);
                if((docidbase==docid)&&(hourbase==hour)&&(utilDate.equals(date)))
                {
                	return false;
                }

            }
            
            return true;
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {

            e.printStackTrace();
        }
    	
    	return false;
       
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
                int hour=rs.getInt("hour");
                Appointment apo=new Appointment(apoid, patid, docid, utilDate, hour);
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
    public boolean EliminateAppointment(int id, int patid)//Esto es un delete, cuando la id que le doy no existe no dice nada, no se si habria que arreglar eso
    {
        try
        {
            Statement stmt=c.createStatement();
            String sqlprov="SELECT * FROM appointments WHERE id="+id+" AND patientid="+patid;
            ResultSet rs=stmt.executeQuery(sqlprov);
            if(rs.getDate("date")==null)
            {
                stmt.close();
                rs.close();
                return false;
            }
            else
            {
                String sql = "DELETE FROM appointments WHERE id=?";
                PreparedStatement prep = c.prepareStatement(sql);
                prep.setInt(1, id);
                prep.executeUpdate();
                prep.close();
                return true;
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {

            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean ModifyAppointment(int apoid, Appointment apo) throws SQLException
    {
        try
        {
        	Statement stmt=c.createStatement();
            String sqlprov="SELECT * FROM appointments WHERE id="+apoid+" AND patientid="+apo.getPatientId();
            ResultSet rs=stmt.executeQuery(sqlprov);
            if(rs.getDate("date")==null)
            {
                stmt.close();
                rs.close();
                return false;
            }
            else
            {
            
                String sql="UPDATE appointments SET doctorid = ? , date = ? , hour = ? WHERE id= "+apoid+" AND patientid= "+apo.getPatientId();
                PreparedStatement prep=c.prepareStatement(sql);
                prep.setInt(1,  apo.getDoctorId());
                prep.setDate(2,  apo.getDate());
                prep.setInt(3,  apo.getHour());
                prep.executeUpdate();
                prep.close();

                return true;
            }

        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
        catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return false;

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
                Integer medprescid;
                int rawmedid=rs.getInt("medprescid");
                if(rs.wasNull())
                {
                	medprescid=null;
                }
                else
                {
                	medprescid=rawmedid;
                }
                MedicalRecord medreco = new MedicalRecord(id, patid, diagnose, treatment, utilDate, medprescid);//Para que esto funcione se necesita contructor de MedicalRecords
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
            Integer medprescid;
            int rawmedid=rs.getInt("medprescid");
            if(rs.wasNull())
            {
            	medprescid=null;
            }
            else
            {
            	medprescid=rawmedid;
            }
            MedicalRecord medreco = new MedicalRecord(id, patid, diagnose, treatment, utilDate, medprescid);//Para que esto funcione se necesita contructor de MedicalRecords

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
    public void AddAppointmentsfromXml(ArrayList<Appointment> apos, int patid) throws SQLException
    {
        try
        {
            Statement stmt=c.createStatement();
            String sql="SELECT * FROM appointments WHERE patientid="+patid;
            ResultSet rs=stmt.executeQuery(sql);
            ArrayList<Appointment> aposbase=new ArrayList<Appointment>();
            while(rs.next())
            {
                int apoid=rs.getInt("id");
                int docid=rs.getInt("doctorid");
                long dobMillis = rs.getLong("date");
                Date utilDate = new Date(dobMillis);
                int hour=rs.getInt("hour");
                Appointment apo=new Appointment(apoid, patid, docid, utilDate, hour);
                aposbase.add(apo);
            }


            int i=0;
            while(i<apos.size())
            {
                if(!aposbase.contains(apos.get(i)))
                {
                    boolean can=CheckAvailability(apos.get(i).getHour(), apos.get(i).getDoctorId(), apos.get(i).getDate());//Esto impide que un patient se agencia los appointments de otro patient
                    if(can)
                    {
                        BookAppointment(apos.get(i));
                    }

                }
                else
                {
                    boolean can=CheckAvailability(apos.get(i).getHour(), apos.get(i).getDoctorId(), apos.get(i).getDate());
                    if(can)
                    {
                        ModifyAppointment(apos.get(i).getId(), apos.get(i));
                    }
                }

                i++;
            }


        }
        catch(SQLException e)
        {
            throw new SQLException();
        }
        catch (NumberFormatException e) {

            e.printStackTrace();
        }
    }
    
    @Override
    public void AddMedicinesfromXml(ArrayList<MedicineSupply> meds)
    {
    	try
    	{
    		int i=0;
    		while(i<meds.size())
    		{
    			String sql="INSERT INTO medsupply(name, quantity) VALUES(?,?)";
        		PreparedStatement prep=c.prepareStatement(sql);
        		prep.setString(1,  meds.get(i).getName());
        		prep.setFloat(2,  meds.get(i).getQuantity());
        		prep.executeUpdate();
        		prep.close();
    			i++;
    		}
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    
    /*@Override
    public void AddMedicalRecordsfromXml(ArrayList<MedicalRecord> medrecs)
    {
    	try
    	{
    		Statement stmt=c.createStatement();
    		String sql="SELECT * FROM medrecords WHERE patientid="+medrecs.get(0).getPatientID();
    		ResultSet rs=stmt.executeQuery(sql);
    		ArrayList<MedicalRecord> medrecsbase=new ArrayList<MedicalRecord>();
    		while(rs.next())
    		{
    			int id = rs.getInt("id");
                String diagnose = rs.getString("diagnose");
                String treatment = rs.getString("treatment");
                long dobMillis = rs.getLong("date");
                Date utilDate = new Date(dobMillis);
                Integer medprescid;
                int rawmedid=rs.getInt("medprescid");
                if(rs.wasNull())
                {
                	medprescid=null;
                }
                else
                {
                	medprescid=rawmedid;
                }
                MedicalRecord medreco = new MedicalRecord(id, rs.getInt("patientid"), diagnose, treatment, utilDate, medprescid);//Para que esto funcione se necesita contructor de MedicalRecords
                medrecsbase.add(medreco);
    		}
    		
    		int i=0;
    		while(i<medrecs.size())
    		{
    			if((!medrecsbase.contains(medrecs.get(i)))&&((medrecs.get(i).getPatientID()!=medrecsbase.get(i).getPatientID())||(!medrecs.get(i).getDiagnosis().equals(medrecsbase.get(i).getDiagnosis()))))
    			{
    				if(isMedicineAvailable(medrecs.get(i).getMedicineID()))
    				{
    					addMedicalRecord(medrecs.get(i));
    				}
    				
    			}
    			i++;
    		}
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {

            e.printStackTrace();
        }
    }*/
    
    

    @Override
    public boolean ClaimMedicine(int medrecidclaim, int patid)//Exclusivo de pacientes, habria que hacer que una vez reclamada la medicina no la pueda volver a reclamar, esto se comprobara viendo si el medprescid es NULL
    {
        try
        {

            Statement stmt=c.createStatement();
            String sql="SELECT medprescid FROM medrecords WHERE id="+ medrecidclaim+" AND patientid="+patid;
            ResultSet rs=stmt.executeQuery(sql);
            rs.getInt("medprescid");//Primero obtengo el valor que quiero comprobar si es null, y luego ya una vez tengo el valor que quiero comprobar, en este caso medprescid, hago el .wasNull()
            if(rs.wasNull())//Esto es para ver si medprescid es null, no se puede hacer medprescid ==null, porque en ese caso devuelve 0 la base de datos, hay que hacer esto para comprobar si era null
            {
                stmt.close();
                rs.close();
                return false;
            }
            else//En caso de si poder reclamar la medicina, primero quitar el medicine supply, y u8na vez hecho eso automticamnente se vuelve null el medprescid
            {
                String sql2="DELETE FROM medsupply WHERE id = (SELECT medprescid FROM medrecords WHERE id= ? )";//Aqui no hace falta comprobar id del paciente, ya que ya la he comprobado arriba
                PreparedStatement prep=c.prepareStatement(sql2);
                prep.setInt(1, medrecidclaim);
                prep.executeUpdate();
                prep.close();

                return true;
            }


        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public Doctor getDoctorByEmail(String email)
    {
    	try
    	{
    		Statement stmt=c.createStatement();
    		String sql="SELECT * FROM doctors WHERE email LIKE '"+email+"'";
    		ResultSet rs=stmt.executeQuery(sql);
    		int id=rs.getInt("id");
            String name=rs.getString("name");
            long dobMillis = rs.getLong("dob");
            Date utilDate = new Date(dobMillis);
            String address=rs.getString("address");
            String sex=rs.getString("sex");
            String department=rs.getString("department");
            int experience=rs.getInt("experience");
            
            Doctor doc=new Doctor(id, email, name, utilDate, experience, address, department, sex);
            stmt.close();
            rs.close();
            
            return doc;
    		
        }
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
    	
    	return null;
    }
    
    @Override
    public ArrayList<Appointment> getAppointmentsDoc(int docid)
    {
    	try
    	{
    		Statement stmt=c.createStatement();
            String sql="SELECT * FROM appointments WHERE doctorid = "+docid;
            ResultSet rs=stmt.executeQuery(sql);
            ArrayList<Appointment> apos=new ArrayList<Appointment>();
            while(rs.next())
            {
                int apoid=rs.getInt("id");
                int patid=rs.getInt("patientid");
                long dobMillis = rs.getLong("date");
                Date utilDate = new Date(dobMillis);
                int hour=rs.getInt("hour");
                Appointment apo=new Appointment(apoid, patid, docid, utilDate, hour);
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
    	
    	return null;
    }
    
    @Override
    public ArrayList<Patient> getPatientsByDoctor(int docid)
    {
    	try
    	{
    		Statement stmt=c.createStatement();
    		String sql="SELECT * FROM patients WHERE id = (SELECT patientid FROM appointments WHERE doctorid = "+docid+")";
    		ResultSet rs=stmt.executeQuery(sql);
    		ArrayList<Patient> patsgo=new ArrayList<Patient>();
    		
    		while(rs.next())
    		{
    			int id=rs.getInt("id");
                String email=rs.getString("email");
                String name=rs.getString("name");
                long dobMillis = rs.getLong("dob");
                Date utilDate = new Date(dobMillis);
                String address=rs.getString("address");
                String sex=rs.getString("sex");
                Patient pat=new Patient(id, name, email, address, sex, utilDate);
                
                patsgo.add(pat);
    		}
    		
    		stmt.close();
    		rs.close();
    		
    		return patsgo;
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
    	return null;
    }
    
    
    @Override
    public ArrayList<MedicineSupply> getAllMedicines()
    {
    	try
    	{
    		Statement stmt=c.createStatement();
    		String sql="SELECT * FROM medsupply";
    		ResultSet rs=stmt.executeQuery(sql);
    		
    		ArrayList<MedicineSupply> medsgo=new ArrayList<MedicineSupply>();
    		while(rs.next())
    		{
    			int id=rs.getInt("id");
    			String name=rs.getString("name");
    			float quantity=rs.getFloat("quantity");
    			
    			MedicineSupply med=new MedicineSupply(id, name, quantity);
    			medsgo.add(med);
    		}
    		
    		stmt.close();
    		rs.close();
    		
    		return medsgo;
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
    	
    	return null;
    }
    
    @Override
    public void addMedicine(MedicineSupply med)
    {
    	try
    	{
    		String sql="INSERT INTO medsupply(name, quantity) VALUES(?,?)";
    		PreparedStatement prep=c.prepareStatement(sql);
    		prep.setString(1,  med.getName());
    		prep.setFloat(2,  med.getQuantity());
    		prep.executeUpdate();
    		prep.close();
    		
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public boolean validatePatientOfTheDoctor(int docid, int patid)
    {
    	try
    	{
    		Statement stmt=c.createStatement();
    		String sql="SELECT * FROM appointments WHERE doctorid="+docid+" AND patientid="+patid;
    		ResultSet rs=stmt.executeQuery(sql);
    		if(rs.next())
    		{
    			stmt.close();
    			rs.close();
    			return true;
    		}
    		else
    		{
    			stmt.close();
    			rs.close();
    			return false;
    		}
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
    	
    	return false;
    }
    
    @Override
    public ArrayList<MedicalRecord> getMedicalRecordsByPatient(int patid)
    {
    	try
    	{
    		Statement stmt=c.createStatement();
        	String sql="SELECT * FROM medrecords WHERE patientid="+patid;
        	ResultSet rs=stmt.executeQuery(sql);
        	
        	ArrayList<MedicalRecord> medrecs=new ArrayList<MedicalRecord>();
        	while(rs.next())
        	{
        		 int id = rs.getInt("id");
                 String diagnose = rs.getString("diagnose");
                 String treatment = rs.getString("treatment");
                 long dobMillis = rs.getLong("date");
                 Date utilDate = new Date(dobMillis);
                 Integer medprescid;
                 int rawmedid=rs.getInt("medprescid");
                 if(rs.wasNull())
                 {
                	 medprescid=null;
                 }
                 else
                 {
                	 medprescid=rawmedid;
                 }
                 MedicalRecord medreco = new MedicalRecord(id, patid, diagnose, treatment, utilDate, medprescid);
                 medrecs.add(medreco);
        	}
        	
        	stmt.close();
        	rs.close();
        	
        	return medrecs;
    	}
    	catch(SQLException e)
        {
            e.printStackTrace();
        }
    	
    	return null;
    }
    
    @Override
	public boolean addMedicalRecord(MedicalRecord record)
	{
		try
		{
			String sql = "INSERT INTO medrecords (diagnose, treatment, date, medprescid, patientid) "
					+ "VALUES (?,?,?,?,?);";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, record.getDiagnosis());
			prep.setString(2, record.getTreatment());        
			prep.setDate(3, record.getDate());
			if(record.getMedicineID()!=null)
			{
				prep.setInt(4,  record.getMedicineID());
			}
			else
			{
				prep.setNull(4, java.sql.Types.INTEGER);
			}
			prep.setInt(5, record.getPatientID());
			
			prep.executeUpdate();
			prep.close();
			
			return true;
		
		}
		catch(SQLException e){
			e.printStackTrace();
			
		}catch (NumberFormatException e) {
			e.printStackTrace();
			
		} 
		
		return false;
	}
    
    @Override
    public boolean isMedicineAvailable(int medid)
    {
    	try
    	{
    		Statement stmt=c.createStatement();
    		String sql="SELECT * FROM medrecords WHERE medprescid="+medid;
    		ResultSet rs=stmt.executeQuery(sql);
    		if(rs.next())
    		{
    			stmt.close();
    			rs.close();
    			return false;
    		}
    		else
    		{
    			Statement stmt2=c.createStatement();
        		String sql2="SELECT * FROM medsupply WHERE id="+medid;
        		ResultSet rs2=stmt2.executeQuery(sql2);
        		if(rs2.next())
        		{
        			stmt2.close();
        			rs2.close();
        			return true;
        		}
        		else
        		{
        			stmt2.close();
        			rs2.close();
        			return false;
        		}
    		}
    		
    	}
    	catch(SQLException e){
			e.printStackTrace();
			
		}catch (NumberFormatException e) {
			e.printStackTrace();
			
		} 
    	
    	return false;
    }
    
    
    @Override
    public boolean VerifyMedicalRecord(int medrecid, int patid)
    {
    	try
    	{
    		Statement stmt=c.createStatement();
    		String sql="SELECT * FROM medrecords WHERE id="+medrecid+" AND patientid="+patid;
    		ResultSet rs=stmt.executeQuery(sql);
    		if(rs.next())
    		{
    			stmt.close();
    			rs.close();
    			return true;
    		}
    		else
    		{
    			stmt.close();
    			rs.close();
    			return false;
    		}
    	}
    	catch(SQLException e){
			e.printStackTrace();
			
		}catch (NumberFormatException e) {
			e.printStackTrace();
			
		} 
    	
    	return false;
    }
    
    @Override
    public boolean updateMedicalRecord(MedicalRecord medrec)
    {
    	try
    	{
    		String sql = "UPDATE medrecords SET diagnose = ?, treatment = ?, date = ?, medprescid = ? "
					+ "WHERE id = ? AND patientid = ?";
    		PreparedStatement prep=c.prepareStatement(sql);
    		prep.setString(1,  medrec.getDiagnosis());
    		prep.setString(2,  medrec.getTreatment());
    		prep.setDate(3,  medrec.getDate());
    		if(medrec.getMedicineID()!=null)
    		{
    			prep.setInt(4,  medrec.getMedicineID());
    		}
    		else
    		{
    			prep.setNull(4, java.sql.Types.INTEGER);
    		}
    		prep.setInt(5, medrec.getId());
    		prep.setInt(6,  medrec.getPatientID());
    		prep.executeUpdate();
    		prep.close();
    		
    		return true;
    	}
    	catch(SQLException e) {
			e.printStackTrace();
			
		}
    	return false;
    }
   
    
}   	


