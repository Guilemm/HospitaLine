package hospital.xml;

import java.util.ArrayList;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import db.pojos.MedicalRecord;

@XmlRootElement(name="MedicalRecords")
public class MedicalRecordsList {

	
    private ArrayList<MedicalRecord> medicalrecords;
	
	@XmlElement(name="MedicalRecord")//Cada medical record de la lista  
    public ArrayList<MedicalRecord> getMedicalRecord() {
        return medicalrecords;
    }

    public void setMedicalRecord(ArrayList<MedicalRecord> medicalRecords) {
        this.medicalrecords = medicalRecords;
    }
}
