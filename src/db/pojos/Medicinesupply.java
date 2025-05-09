package db.pojos;

import java.io.Serializable;


import java.util.Objects;

public class MedicineSupply implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 4119697357748661386L;
		/*
		 * Represents a medicine and its stock level.
		 */
		private Integer id;
	    private String name; //name of the medicine
	    private Integer quantity; //amount of medicine in stock
	
	    /*
	     * Constructor
	     */
	    public MedicineSupply(String name, int quantity) {
	        this.name = name;
	        this.quantity = quantity;
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
			MedicineSupply other = (MedicineSupply) obj;
			return id == other.id;
		}




	public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }




	@Override
	public String toString() {
		return "Medicinesupply [id=" + id + ", name=" + name + ", quantity=" + quantity + "]";
	}
    
    
}
