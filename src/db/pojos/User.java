package db.pojos;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2130683098321287710L;
	
	@Id
	@GeneratedValue(generator = "users")
	@TableGenerator(name = "users", table = "sqlite_sequence",
		pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "users")
	private Integer id;
	private String username;//Preguntar si realmente esto como algo unico es buena idea
	private String password;
	@Column(unique =true)
	private String email;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roleId")
	private Role role;
	
	public User()
	{
		super();//v
	
	}

	public User(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public void setRole(Role role)
	{
		this.role=role;
	}

	public Role getRole() {
		return role;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	

	
	
}
