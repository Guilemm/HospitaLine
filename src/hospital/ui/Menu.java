package hospital.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

import hospital.jdbc.HospitalManagerImplements;

public class Menu {
	
	public static void main(String args[])//Asi no se va a quedar el menu, es para probar lo metodos
	{
		HospitalManagerImplements hospiman=new HospitalManagerImplements();
		
		//hospiman.AddMedicine();
		//hospiman.AddMedicalRecord();
		//hospiman.UpdateMedicalRecord();
		//hospiman.ClaimMedicine();
		//hospiman.ViewDoctorInfo();
		//hospiman.ViewPatientInfo();
		int i=0;
		while(i==0)
		{
			try
			{
				hospiman.ModifyAppointment();
				BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
				System.out.println("ya? 1) no ya 0");
				i=Integer.parseInt(br.readLine());
			}
			catch (NumberFormatException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
			
		}
		hospiman.CloseConnection();//Si se pone esto se cierra la conexion por eso al final esta puesto
	}
}
