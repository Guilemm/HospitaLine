package db.pojos;

import java.io.Serializable;

import java.sql.Date;
import java.util.Objects;



public class MedicalRecord implements Serializable {

    private Integer id; 
    private Integer patientID; 
    private String diagnosis; 
    private String treatment;  
    private Date date;  
    private Integer medicineID;  


    public MedicalRecord(int id, int patientID, String diagnosis, String treatment, Date date, Integer medicineID) {
        this.id = id;
        this.patientID = patientID;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.date = date;
        this.medicineID = medicineID;
    }
    
    

	public MedicalRecord(int patientID, String diagnosis, String treatment, Date date, Integer medicineID) {
		super();
		this.patientID = patientID;
		this.diagnosis = diagnosis;
		this.treatment = treatment;
		this.date = date;
		this.medicineID = medicineID;
	}




	public MedicalRecord() {
        super();
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
        MedicalRecord other = (MedicalRecord) obj;
        return id == other.id;
    }


    
    public int getId() {
        return id;
    }

    
    public int getPatientID() {
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

    
    public Integer getMedicineID() {
        return medicineID;
    }


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

    public void setMedicineID(Integer medicineID) {
        this.medicineID = medicineID;
    }



    @Override
    public String toString() {
        return "MedicalRecord [id=" + id + ", patientID=" + patientID + ", diagnosis=" + diagnosis + ", treatment="
                + treatment + ", date=" + date + ", medicineID=" + medicineID + "]";
    }




}
