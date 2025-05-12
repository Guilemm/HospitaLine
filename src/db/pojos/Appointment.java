package db.pojos;

import java.io.Serializable;

import java.sql.Date;
import java.util.Objects;

/*
 * Represents a medical appointment between a patient and a doctor on a specific date.
 */
public class Appointment implements Serializable{
	private Integer id;
	private Integer patientId;
	private Integer doctorId;
	private Date date;

	
	/*
	 * Constructor
	 */
	public Appointment(int id, int patientId, int doctorId, Date date) {
		this.id = id;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.date = date;
	}
	
	
	

	public Appointment(int patientId, int doctorId, Date date) {
		super();
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.date = date;
	}




	// Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public Date getDate() {
        return date;
    }
   


	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}


	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}


	public void setDate(Date date) {
		this.date = date;
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
		Appointment other = (Appointment) obj;
		return id == other.id;
	}


	@Override
	public String toString() {
		return "Appointment [id=" + id + ", patientId=" + patientId + ", doctorId=" + doctorId + ", date=" + date + "]";
	}

    
    

}
