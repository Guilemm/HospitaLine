package db.pojos;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patient implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7074079287243196378L;
	
	private Integer id;
	private String name;
	private String email;
	private String address;
	private Sex sex;
	private Date dob;
	private List<MedicalRecords> medrecords;
	private List<Appointments> appointments;
	
	public Patient()
	{
		super();
		appointments = new ArrayList<Appointments>();
		medrecords = new ArrayList<MedicalRecords>();
	}
	
	
	public Patient(Integer id, String name, String email, String address, Sex sex, Date dob) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.sex = sex;
		this.dob = dob;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdrdress() {
		return address;
	}
	public void setAdrdress(String adrdress) {
		this.address = adrdress;
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public List<MedicalRecords> getMedrecords() {
		return medrecords;
	}

	public void setMedrecords(List<MedicalRecords> medrecords) {
		this.medrecords = medrecords;
	}

	public List<Appointments> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointments> appointments) {
		this.appointments = appointments;
	}
	
	public void BookAppointment()
	{
		
	}
	
	public void EliminateAppointment()
	{
		
	}
	public void ModifyAppointment()
	{
		
	}
	
	
	
	

	@Override
	public String toString() {
		return "id:" + this.id + ", name:" + this.name + ", email:" + this.email + ", address:" + this.address + ", sex:" + this.sex + ", dob:" + this.dob + "Medical records:" + this.medrecords + 
				"Appointments:" + this.appointments;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		return id == other.id;
	}
	
	
	
}
