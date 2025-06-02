package hospital.xml;
import java.util.ArrayList;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import db.pojos.MedicineSupply;

@XmlRootElement(name="Medicines")
public class MedicinesList {
	
	private ArrayList<MedicineSupply> medicines;
	
	@XmlElement(name="Medicine")
    public ArrayList<MedicineSupply> getMedicine() {
        return medicines;
    }
	
	public void setMedicine(ArrayList<MedicineSupply> medicines) {
        this.medicines = medicines;
    }
}

