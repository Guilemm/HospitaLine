package db.pojos;

import java.sql.Date;

/*
 * Represents a patient's medical history or visit record
 */
public class MedicalRecords {

	private int id; //unique id for the medical record
	private int patientID; //ID of the patient (foreign key)
	private String diagnosis; //Diagnosis made during the visit
	private String treatment;  //treatment prescribed
	private Date date;  //date of the medical visit
	private int medicineID;  //id of the medicine used (foreign key)
	
	/*
	 * Constructor
	 */
	public MedicalRecords(int id, int patientID, String diagnosis, String treatment, Date date, int medicineID) {
        this.id = id;
        this.patientID = patientID;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.date = date;
        this.medicineID = medicineID;
	}
	
	/*
	 * Compares this MedicalRecord object with another.
	 * The comparison is based on the 'id' field since it represents
	 * a unique identifier for medical records.
	 *
	 * @param obj The object to compare with this instance
	 * @return true if the objects are equal (same ID), false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;

	    MedicalRecords other = (MedicalRecords) obj;
	    return id == other.id;
	}

	/*
	 * Generates a hashCode value for this object (medicalRecord).
	 * The hash is calculated only based on the 'id' field to maintain
	 * consistency with the equals() method.
	 *
	 * @return An integer value representing the hash code
	 */
	@Override
	public int hashCode() {
	    return Integer.hashCode(id);
	}
	
	
	// Getters

    public int getId() {
        return id;
    }

    public int getPatientIVD() {
        return patientID;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public Date getDate() {
        return date;
    }

    public int getMedicineID() {
        return medicineID;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

	
	
}
