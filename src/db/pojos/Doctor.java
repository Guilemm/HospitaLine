package db.pojos;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Doctor implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7129856380505688972L;
	
	private Integer id;
	private String name;
	private String email;
	private Date dob;
	private int experience;
	private String address;
	private String department;
	private Sex sex; //Puede ser que esto sea mejor ponerlo como String
	private List<Appointments> appointments;
	
	public Doctor()
	{
		super();
		appointments = new ArrayList<Appointments>();
	}
	
	public Doctor(int id, String email, String name, Date dob, int experience, String address, String department, Sex sex)
	{
		this.id=id;
		this.email=email;
		this.name=name;
		this.dob=dob;
		this.experience=experience;
		this.address=address;
		this.department=department;
		this.sex=sex;
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
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	public List<Appointments> getAppointments() {
		return appointments;
	}
	public void setAppointments(List<Appointments> appointments) {
		this.appointments = appointments;
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
		Doctor other = (Doctor) obj;
		return id == other.id;
	}
	
	@Override
	public String toString() {
		return "id:" + this.id + ", email: "+ this.email+ ", name:" + this.name + ", dob:" + this.dob + ", experience:" + this.experience + ", address:"
				+ this.address + ", department:" + this.department + ", sex:" + this.sex + ", appointments:" + this.appointments;//El appointments necesita metodo toString
	}
	
	
	
	
}
