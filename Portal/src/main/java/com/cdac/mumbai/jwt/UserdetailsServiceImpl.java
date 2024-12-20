package com.cdac.mumbai.jwt;

import org.springframework.security.authentication.LockedException;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cdac.mumbai.dao.UserRepository;
import com.cdac.mumbai.model.User;
import com.cdac.mumbai.service.impl.IpServiceImpl;

import static com.cdac.mumbai.constants.Message.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service("userDetailsServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class UserdetailsServiceImpl implements UserDetailsService {


	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user  = userRepository.findByUsername(username)
           .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
		if(user.getEmail_verification_timestamp() == null) {
			throw new LockedException("Email not activated");
		}
		return UserPrincipal.create(user);
	}

}
