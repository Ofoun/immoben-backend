package com.immoben.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.immoben.common.entity.Role;
import com.immoben.common.entity.User;


public class ImmobenUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private User user;

	public ImmobenUserDetails (User user) {
		this.user = user;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		Set<Role> roles = user.getRoles();

		List<SimpleGrantedAuthority> authories = new ArrayList<>();

		for (Role role : roles) {
			authories.add(new SimpleGrantedAuthority(role.getDescription()));
		}

		return authories;
	}


	public Integer getId () {
		return user.getId();
	}


	public void setUser (User user) {
		this.user = user;
	}


	@Override
	public String getPassword () {
		return user.getPassword();
	}


	public String getEmail () {
		return user.getEmail();
	}


	public String getUserName () {
		return user.getEmail();
	}


	public String getFirstName () {
		return user.getFirstName();
	}


	public String getLastName () {
		return user.getLastName();
	}


	public String getPhotos () {
		return user.getPhotos();
	}


	@Override
	public boolean isAccountNonExpired () {
		return true;
	}


	@Override
	public boolean isAccountNonLocked () {
		return true;
	}


	@Override
	public boolean isCredentialsNonExpired () {
		return true;
	}


	@Override
	public boolean isEnabled () {
		return user.isEnabled();
	}


	public String getFullName () {
		return this.user.getFirstName() + " " + this.user.getLastName();
	}


	public void setFirstName (String firstName) {
		this.user.setFirstName(firstName);
	}


	public void setLastName (String lastName) {
		this.user.setLastName(lastName);
	}


	public boolean hasRole (String roleName) {
		return user.hasRole(roleName);
	}


	@Override
	public String getUsername () {
		return user.getEmail();
	}

}