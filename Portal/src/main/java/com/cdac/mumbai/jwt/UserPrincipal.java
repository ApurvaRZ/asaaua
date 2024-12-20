package com.cdac.mumbai.jwt;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cdac.mumbai.model.User;
import com.cdac.mumbai.service.impl.IpServiceImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;
	private final Integer regId;
    private final String username;
    private final String password;
  //  private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public static UserPrincipal create(User user) {
   
        return new UserPrincipal(user.getRegId(), user.getUsername(), user.getPassword());
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	 return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
