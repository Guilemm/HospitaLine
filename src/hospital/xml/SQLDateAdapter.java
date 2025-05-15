package hospital.xml;

import java.sql.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SQLDateAdapter extends XmlAdapter<String, Date>{
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public String marshal(Date date) throws Exception {
		return date.toLocalDate().format(formatter);
	}

	@Override
	public Date unmarshal(String datestr) throws Exception {
		LocalDate localDate = LocalDate.parse(datestr, formatter);
		return Date.valueOf(localDate);
	}
	
	

}
