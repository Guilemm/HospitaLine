package hospital.jpa;

import javax.persistence.EntityManager;

import db.pojos.Role;
import db.pojos.User;
import hospital.ifaces.UserManager;

import java.util.List;

import javax.persistence.*;

public class UserManagerImplements implements UserManager
{
	
	EntityManager em;
	
	public UserManagerImplements()
	{
		em=Persistence.createEntityManagerFactory("hospital-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();
		
		//Para crear los roles, si la lista esta vacia significa que la databse no tiene esos roles, y entonces los meto
		
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
	public User login(String username, String password) {
		try
		{
			Query q=em.createNativeQuery("SELECT * FROM users WHERE name = ? AND password = ?", User.class);//Esto para ver si esta el user ya registrado 
			q.setParameter(1, username);
			q.setParameter(2, password);
			User user= (User) q.getSingleResult();//Por que se supone que solo ha de haber un user con estas caracteristicas, al ser el username unique
			return user;
		}
		catch(NoResultException e)//Retornara null si no hay ningun user con ese nombre y password
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
}
