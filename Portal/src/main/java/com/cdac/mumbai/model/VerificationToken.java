package com.cdac.mumbai.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import jakarta.persistence.*;



@Entity
@Table(name="pr_verification_token")
public class VerificationToken {

    private static final int EXPIRATION = 60 * 24;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", columnDefinition = "serial")
    private Integer tokenId;
    

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_VERIFY_USER"))
    private User user;

    private Date expiry_timestamp;

    public VerificationToken() {
        super();
    }

    public VerificationToken( String token) {
        super();

        this.token = token;
        this.expiry_timestamp = calculateExpiryDate(EXPIRATION);
    }

    public VerificationToken(String token, User user) {
        super();

        this.token = token;
        this.user = user;
        this.expiry_timestamp = calculateExpiryDate(EXPIRATION);
    }

    public VerificationToken(String token2, Optional<User> usr) {
		
	}

	public Integer getTokenId() {
        return tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken( String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser( User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiry_timestamp;
    }

    public void setExpiryDate( Date expiryDate) {
        this.expiry_timestamp = expiryDate;
    }

    private Date calculateExpiryDate( int expiryTimeInMinutes) {
         Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken( String token) {
        this.token = token;
        this.expiry_timestamp = calculateExpiryDate(EXPIRATION);
    }

    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expiry_timestamp == null) ? 0 : expiry_timestamp.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
         VerificationToken other = (VerificationToken) obj;
        if (expiry_timestamp == null) {
            if (other.expiry_timestamp != null) {
                return false;
            }
        } else if (!expiry_timestamp.equals(other.expiry_timestamp)) {
            return false;
        }
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
         StringBuilder builder = new StringBuilder();
        builder.append("Token [String=").append(token).append("]").append("[Expires").append(expiry_timestamp).append("]");
        return builder.toString();
    }

}
