package db.pojos;

import java.util.Objects;

public class Medicinesupply {
		/*
		 * Represents a medicine and its stock level.
		 */
		private int id; //unique identifier of the medicine
	    private String name; //name of the medicine
	    private int quantity; //amount of medicine in stock
	
	    /*
	     * Constructor
	     */
	    public Medicinesupply(int id, String name, int quantity) {
	        this.id = id;
	        this.name = name;
	        this.quantity = quantity;
	    }
    
 
        /*
         * Compares this medicine supply to another object.
         * Two medicine supplies are considered equal if they have the same ID,
         * as IDs are meant to be unique identifiers.
         * 
         * @param obj the object to compare with
         * @return true if the objects are equal, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true; // Same object reference
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false; // Different classes or null
            }
            Medicinesupply other = (Medicinesupply) obj;
            return id == other.id; // Equality based on ID
        }

        /*
         * Generates a hash code for this medicine supply.
         * The hash code is based only on the ID field to maintain consistency
         * with the equals() method..
         * 
         * @return a hash code value for this object
         */
        @Override
        public int hashCode() {
            return Objects.hash(id); // Using only ID for hashCode
        }
    
    
    // Getters

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
}
