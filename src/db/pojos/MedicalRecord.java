package db.pojos;

import java.io.Serializable;


import java.sql.Date;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import hospital.xml.SQLDateAdapter;

/*
 * Represents a patient's medical history or visit record
 */

@XmlRootElement
public class MedicalRecord implements Serializable {

    private Integer id; //unique id for the medical record, coment
    private Integer patientID; //ID of the patient (foreign key)
    private String diagnosis; //Diagnosis made during the visit
    private String treatment;  //treatment prescribed
    private Date date;  //date of the medical visit
    private Integer medicineID;  //id of the medicine used (foreign key)


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


    // Getters

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


    @XmlElement
    public int getId() {
        return id;
    }

    @XmlElement
    public int getPatientID() {
        return patientID;
    }

    @XmlElement
    public String getDiagnosis() {
        return diagnosis;
    }

    @XmlElement
    public String getTreatment() {
        return treatment;
    }

    @XmlJavaTypeAdapter(SQLDateAdapter.class)
    public Date getDate() {
        return date;
    }

    @XmlElement
    public Integer getMedicineID() {
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

    public void setMedicineID(Integer medicineID) {
        this.medicineID = medicineID;
    }



    @Override
    public String toString() {
        return "MedicalRecord [id=" + id + ", patientID=" + patientID + ", diagnosis=" + diagnosis + ", treatment="
                + treatment + ", date=" + date + ", medicineID=" + medicineID + "]";
    }




}
