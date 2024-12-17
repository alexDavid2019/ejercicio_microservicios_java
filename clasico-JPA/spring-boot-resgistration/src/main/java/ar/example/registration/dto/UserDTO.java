package ar.example.registration.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDTO {

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDTO> roles) {
		this.roles = roles;
	}

	public Set<PhoneDTO> getPhones() {
		return phones;
	}

	public void setPhones(Set<PhoneDTO> phones) {
		this.phones = phones;
	}


	private String id;

    private String name;
    
    private String email;
    
    private String password;

    private String token;

    private Date created;

    private Date modified;

    private Date lastAccess;

    private Set<RoleDTO> roles  = new HashSet<>();
    
    private Set<PhoneDTO> phones  = new HashSet<>();
}
