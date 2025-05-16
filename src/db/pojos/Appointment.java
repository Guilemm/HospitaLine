package db.pojos;

import java.io.Serializable;


import java.sql.Date;
import java.time.LocalTime;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import hospital.xml.SQLDateAdapter;

/*
 * Represents a medical appointment between a patient and a doctor on a specific date.
 */

@XmlRootElement
public class Appointment implements Serializable{
    private Integer id;
    private Integer patientId;//coment
    private Integer doctorId;
    private Date date;
    private Integer hour;


    /*
     * Constructor
     */
    public Appointment(int id, int patientId, int doctorId, Date date, int hour) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.hour=hour;
    }




    public Appointment(int patientId, int doctorId, Date date, int hour) {
        super();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.hour=hour;
    }


    public Appointment() {
        super();
    }



    @XmlElement
    public int getHour() {
        return hour;
    }




    public void setHour(int hour) {
        this.hour = hour;
    }




    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public int getPatientId() {
        return patientId;
    }

    @XmlElement
    public int getDoctorId() {
        return doctorId;
    }

    @XmlJavaTypeAdapter(SQLDateAdapter.class)
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
        return "Appointment [id=" + id + ", patientId=" + patientId + ", doctorId=" + doctorId + ", date=" + date
                + ", hour=" + hour + "]";
    }







}