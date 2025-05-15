package hospital.xml;

import java.util.ArrayList;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import db.pojos.Appointment;


@XmlRootElement(name="Appointments")
public class AppointmentsList {
	
	private ArrayList<Appointment> appointments;
	
	@XmlElement(name="Appointment")//Cada appointment de la lista  
    public ArrayList<Appointment> getAppointment() {
        return appointments;
    }

    public void setAppointment(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }
}
