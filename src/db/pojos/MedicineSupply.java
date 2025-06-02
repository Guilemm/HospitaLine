package db.pojos;

import java.io.Serializable;




import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class MedicineSupply implements Serializable{
   
	
    private static final long serialVersionUID = 4119697357748661386L;
    
    
    private Integer id;
    private String name; 
    private Float quantity; 

    

    public MedicineSupply() {
		super();
	}



	public MedicineSupply(String name, float quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    
    

    public MedicineSupply(int id, String name, float quantity) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
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
        MedicineSupply other = (MedicineSupply) obj;
        return id == other.id;
    }



    @XmlElement
    public int getId() {
        return id;
    }
    
    @XmlElement
    public String getName() {
        return name;
    }
    
    @XmlElement
    public float getQuantity() {
        return quantity;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }




    @Override
    public String toString() {
        return "Medicinesupply [id=" + id + ", name=" + name + ", quantity=" + quantity + "]";
    }


}
