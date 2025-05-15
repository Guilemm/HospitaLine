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
	private String sex;
	private Date dob;
	private List<MedicalRecord> medrecords;
	private List<Appointment> appointments;
	
	public Patient()
	{
		super();
		appointments = new ArrayList<Appointment>();
		medrecords = new ArrayList<MedicalRecord>();
	}
	
	
	public Patient(Integer id, String name, String email, String address, String sex, Date dob) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.sex = sex;
		this.dob = dob;
	}
	
	

	public Patient(String name, String email, String address, String sex, Date dob) {
		super();
		this.name = name;
		this.email = email;
		this.address = address;
		this.sex = sex;
		this.dob = dob;
	}
	
	

	public Patient(Integer id, String name, String email, String address, String sex, Date dob,
			List<MedicalRecord> medrecords, List<Appointment> appointments) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.sex = sex;
		this.dob = dob;
		this.medrecords = medrecords;
		this.appointments = appointments;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String adrdress) {
		this.address = adrdress;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public List<MedicalRecord> getMedrecords() {
		return medrecords;
	}

	public void setMedrecords(List<MedicalRecord> medrecords) {
		this.medrecords = medrecords;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
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
