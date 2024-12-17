package ar.example.registration.persistence.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "PHONES")
public class PhoneEntity implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhoneEntity() {}
	
	public PhoneEntity(UUID id, String number, String cityCode, String countryCode) {
		this.id = id;
		this.number = number;
		this.cityCode = cityCode;
		this.countryCode = countryCode;
	}
	
	public PhoneEntity(String number, String cityCode, String countryCode) {
		this.number = number;
		this.cityCode = cityCode;
		this.countryCode = countryCode;
	}
	
    public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

    public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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

    @Column(name = "number")
    private String number;

    @Column(name = "cityCode")
    private String cityCode;

    @Column(name = "countryCode")
    private String countryCode;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName="id")
	private UserEntity user;
}
