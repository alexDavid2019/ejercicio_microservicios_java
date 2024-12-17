package ar.example.registration.dto;

import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthCreateUserRequest {
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRolesRequest() {
		return rolesRequest;
	}

	public void setRolesRequest(List<String> rolesRequest) {
		this.rolesRequest = rolesRequest;
	}

	public List<PhoneDTO> getPhonesRequest() {
		return phonesRequest;
	}

	public void setPhonesRequest(List<PhoneDTO> phonesRequest) {
		this.phonesRequest = phonesRequest;
	}

	private String name;
    private String password;
    private String email;
    
	@Size(max = 3, message = "The user cannot have more than 3 roles") 
    @JsonProperty("roles")
    List<String> rolesRequest;
    
    @JsonProperty("phones")
    List<PhoneDTO> phonesRequest;

}
