package db.pojos;

import java.sql.Date;

/*
 * Represents a medical appointment between a patient and a doctor on a specific date.
 */
public class Appointments {
	private int id;
	private int patientId;
	private int doctorId;
	private Date date;
	private boolean isCancelled;
	
	/*
	 * Constructor
	 */
	public Appointments(int id, int patientId, int doctorId, Date doctroId, Date date) {
		this.id = id;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.date = date;
		this.isCancelled = false;
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

    public boolean isCancelled() {
        return isCancelled;
    }
    

}
