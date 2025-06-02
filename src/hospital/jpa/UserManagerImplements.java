package hospital.jpa;

import javax.persistence.EntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;//Para poder utilizar esto, emn el pom.xml, hay que añadir la maven dependency 5.8.1, la que añadi yo

import db.pojos.Role;
import db.pojos.User;
import hospital.ifaces.UserManager;

import java.util.List;

import javax.persistence.*;

public class UserManagerImplements implements UserManager
{
	
	EntityManager em;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public UserManagerImplements()
	{
		em=Persistence.createEntityManagerFactory("hospital-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();
		
		
		if(this.getRoles().isEmpty())
		{
			Role patient= new Role("patient");
			Role doctor=new Role("doctor");
			this.createRole(patient);
			this.createRole(doctor);
		}
	}
	

	public void close()
	{
		em.close();
	}
	
	@Override
	public List<Role> getRoles()
	{
		Query q=em.createNativeQuery("SELECT * FROM roles", Role.class);
		List<Role> roles=(List<Role>) q.getResultList();
		return roles;
		
	}


	@Override
	public User login(String email, String password) {
		try
		{
			Query q=em.createNativeQuery("SELECT * FROM users WHERE email = ?", User.class);
			q.setParameter(1, email);
			User user= (User) q.getSingleResult();
			
			if(encoder.matches(password, user.getPassword()))
			{
				return user;
			}
			else
			{
				return null;
			}
			
		}
		catch(NoResultException e)
		{
			return null;
		}
		
	}


	@Override
	public void createRole(Role role) {
		em.getTransaction().begin();
		em.persist(role);
		em.getTransaction().commit();
		
	}


	@Override
	public Role getRole(String name) {
		Query q=em.createNativeQuery("SELECT * FROM roles WHERE name LIKE ?", Role.class);
		q.setParameter(1, name);
		Role r= (Role) q.getSingleResult();
		return r;
	}


	@Override
	public void register(User user) {
		String hashedpassword=encoder.encode(user.getPassword());
		user.setPassword(hashedpassword);
		
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
		
	}


	@Override
	public void assignRole(User user, Role role) {
		em.getTransaction().begin();
		user.setRole(role);
		role.addUser(user);
		em.getTransaction().commit();
	}


	@Override
	public void deleteAccount(User user) {
		em.getTransaction().begin();
		Query q=em.createNativeQuery("DELETE FROM users WHERE id= ?");
		q.setParameter(1,  user.getId());
		q.executeUpdate();
		em.getTransaction().commit();
		
	}


	@Override
	public boolean changePassword(User user, String newpassword) {
		User manageuser=em.find(User.class, user.getId());
		if(manageuser!=null)
		{
			String hashedpassword=encoder.encode(newpassword);
			
			em.getTransaction().begin();
			manageuser.setPassword(hashedpassword);
			em.getTransaction().commit();
			return true;
		}
		
		return false;
		
	}
}
