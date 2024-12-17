package ar.example.registration.persistence.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ManyToMany;

@Entity
@Table(name = "ROLES")
public class RoleEntity implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleEntity() {}
	
	public RoleEntity(RoleEnum role) {
		this.id = UUID.randomUUID();
		this.roleEnum = role;
	}
	
	public RoleEntity(UUID id, RoleEnum role) {
		this.id = id;
		this.roleEnum = role;
	}
	
    public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public RoleEnum getRoleEnum() {
		return roleEnum;
	}

	public void setRoleEnum(RoleEnum roleEnum) {
		this.roleEnum = roleEnum;
	}

	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
        parameters = {
            @Parameter(
                name = "uuid_gen_strategy_class",
                value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
            )
        }
    )
	@Column(name = "id", unique = true, updatable = false, nullable = false)
	@ColumnDefault("random_uuid()")
	@Type(type = "uuid-char")
	private UUID id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;
    
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<UserEntity> users = new HashSet<>();

}
