package com.cdac.mumbai.model;

import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.GrantedAuthority;

import com.cdac.mumbai.validation.CdName;
import com.cdac.mumbai.validation.City;
import com.cdac.mumbai.validation.EnsureNumber;
import com.cdac.mumbai.validation.OrgNameAdd;
import com.cdac.mumbai.validation.SpDeptMinProjDesg;
import com.cdac.mumbai.validation.ValidEmail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="aua_subaua_registration")
public class User {
	 @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	  ///  @SequenceGenerator(initialValue = 1, allocationSize = 1)
	@Column(name="reg_id")
	private Integer regId;
    
    
	@Column(name="client_type", unique=true)
	private String client_type="E"; //store information about client type ('External','Internal','Testing')
	
	@Size(min=2, max=250)
	@OrgNameAdd
	@Column(name="organization_name", unique=true, nullable=false)
	private String organizationName; // name of organization
	
	//@RegistrationNo
	@Column(name="registration_no")
	private String registartion_no; // registartion number of private Organization
	
	
	@Column(name="dept")
	private String dept; //dept name of government Organization
	

	@Column(name="ministry")
	private String ministry; //ministry name of government Organization
	
	@Size(min=2, max=120)
	@OrgNameAdd
	@Column(name="address_line1", unique=true, nullable=false)
	private String address ;// address of Organization
	  
	
	@Size(min=2, max=50)
	@City
	@Column(name="city", unique=true, nullable=false)
	private String  city ;// city of Organization
	
	//@NotEmpty
	@Column(name="state_code", unique=true)
	private String state_code; //state code of Organization
	
	@NotEmpty
	
	@Size(min=6,max=6)
	@EnsureNumber(message = "Enter 6 digit pin code")
	@Column(name="pincode", unique=true, nullable=false)
	private String pincode;   //pincode of Organization
	
	@NotEmpty
	@Size(min=10,max=11)
	@EnsureNumber(message = "Please enter valid mobile/landline number")
	@Column(name="phone", unique=true, nullable=false)
	private String  phone;        //phone of Organization
	 
	@NotEmpty
	@Column(name="organization_type", unique=true, nullable=false)
	private String  organizationType;  //type of Organization ('Cental','state','psu','private')
	
	@NotEmpty
	@Column(name="service_type", unique=true, nullable=false)
	private String service_type ;// name of service
	
	
	@Column(name="ac_code_pre")
	private String aua_kua_code ;//stores aua_kua_code
	
	
	@Column(name="ac_code_pro")
	//@ColumnDefault("'null'")
	private String aua_kua_code_prod ;//stores aua_kua_code for production
		
	@Column(name="sa_code")
	private String sa_code ;//stores sa_code 
	
	@Column(name="password")
	private String password;
	
	 @JdbcTypeCode(Types.BOOLEAN)
		@Column(name = "is_ekyc")
		private Boolean is_ekyc;
	
		@Size(min=2, max=500)
		@SpDeptMinProjDesg 
		@Column(name="project_description", unique=true, nullable=false)
		private String project_description;//store description of project
		
		@Size(min=2, max=1000)
		@SpDeptMinProjDesg 
		@Column(name="project_purpose", unique=true, nullable=false)
		private String project_purpose; // purpose of project
	  
		@NotNull		   
		 @Column(name="quarterly_transaction_count", unique=true, nullable=false)
			private java.lang.Integer  quarterly_transaction_count;// no of quarterly transaction count

 
	
   @Transient
	private String  quarterly_transaction_in_words;// no of quarterly transaction count in words

   @NotEmpty
	@Column(name = "is_alternate_sp")
	private String is_alternate_sp ;// alernate service provider status
	 
	@Column(name="update_by")
	private String update_by ;

	@Column(name="alternate_sp_name")
	private String alternate_sp_name ;// alernate service provider name
 
	@Size(min=2, max=100)
	@CdName
	@Column(name="cd_name", unique=true, nullable=false)
	private String  cd_name ;
	
	@Column(name="cd_designation")
	private String cd_designation ;//designation of contact details
	
	@Column(name="status", nullable=false)
	private String status=State.INACTIVE.getState();

	
	@Column(name="username")
	private String username; 
	
	@NotEmpty
	@ValidEmail
	@Column(name="cd_email")
	private String  cdemail;// email of contact details
	
	
	
	@NotEmpty
	@Size(min=10, max =10)
	@EnsureNumber(message = "Please enter 10 digit valid mobile number")
	@Column(name="cd_mobile", unique=true, nullable=false)
	private String  cd_mobile; //mobile no of contact details secty
	
	
	@Column(name="cd_phone", unique=true)
	private String  cd_phone;//phone no  of contact details secty
	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="registration_timestamp")
	private Date registration_timestamp; // store date of registartion
    
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="reset_password_timestamp")
	private Date reset_password_timestamp; 
    

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_timestamp")
	private Date update_timestamp;
    
    @Temporal(TemporalType.TIMESTAMP)
  	@Column(name="email_verification_timestamp")
  	private Date email_verification_timestamp;
    
    @Column(name="is_set_password_flag")
	private boolean set_password_flag;
	

    @ManyToMany(fetch = FetchType.EAGER)
 		@JoinTable(name = "PR_CLIENT_ROLES_MAPPING", 
 	             joinColumns = { @JoinColumn(name = "REG_ID") }, 
 	             inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
 		private Set<UserProfile> userProfiles = new HashSet<UserProfile>();
	
	  @ManyToMany
	  @JoinTable(name = "PR_CLIENT_ROLES_MAPPING", joinColumns = @JoinColumn(name = "REG_ID", referencedColumnName = "reg_id"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "role_id"))
	    private List<UserProfile> roles;
//
//	public User orElseThrow(Object object) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	  public Collection<GrantedAuthority> getAuthorities() {
//			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
//			authorities.add(getRoles());
//		
//			return authorities;
//		}
}
