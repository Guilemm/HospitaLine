package hospital.ifaces;

import java.util.List;

import db.pojos.Role;
import db.pojos.User;

public interface UserManager {
	
	public User login(String name, String password);//Going to check if the user with that password exists in the database, aqui checkeamos el role, y dependiendo del role le enseñamos un menu u otro
	
	public List<Role> getRoles();

	public void createRole(Role role);
	
	public Role getRole(String name);
	
	public void register(User user);
	
	public void assignRole(User user, Role role);
	
	public void deleteAccount(User user);
	
	public boolean changePassword(User user, String newpassword);
	
}
