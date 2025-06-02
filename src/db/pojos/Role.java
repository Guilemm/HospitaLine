package db.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name="roles")
public class Role implements Serializable{

	
	private static final long serialVersionUID = 1521935434746480342L;
	
	@Id
	@GeneratedValue(generator = "roles")
	@TableGenerator(name = "roles", table = "sqlite_sequence",
		pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "roles")
	private Integer id;
	private String name;
	
	@OneToMany(mappedBy="role", fetch= FetchType.LAZY)
	private List<User> users;
	
	public Role()
	{
		super();
		this.users=new ArrayList<User>();
	}

	public Role(String name) {
		super();
		this.name = name;
	}
	
	public void addUser(User user)
	{
		if(!this.users.contains(user))
		{
			this.users.add(user);
		}
	}

	public String getName() {
		return name;
	}


}
