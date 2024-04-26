package com.immoben.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.immoben.common.entity.City;
import com.immoben.common.entity.Country;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.Role;
import com.immoben.common.entity.State;


public class ImmobenCustomerDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private Customer customer;

	public ImmobenCustomerDetails (Customer customer) {
		this.customer = customer;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		Set<Role> roles = customer.getRoles();

		List<SimpleGrantedAuthority> authories = new ArrayList<>();

		for (Role role : roles) {
			authories.add(new SimpleGrantedAuthority(role.getDescription()));
		}

		return authories;
	}


	public Integer getId () {
		return customer.getId();
	}


	public void setCustomer (Customer customer) {
		this.customer = customer;
	}


	@Override
	public String getPassword () {
		return customer.getPassword();
	}


	public String getEmail () {
		return customer.getEmail();
	}


	public String getUserName () {
		return customer.getEmail();
	}


	public String getFirstName () {
		return customer.getFirstName();
	}


	public String getLastName () {
		return customer.getLastName();
	}


	public String getPhoneNumber () {
		return customer.getPhoneNumber();
	}


	public String getAddressLine1 () {
		return customer.getAddressLine1();
	}


	public String getAddressLine2 () {
		return customer.getAddressLine2();
	}


	public City getCity () {
		return customer.getCity();
	}


	public Country getCountry () {
		return customer.getCountry();
	}


	public State getState () {
		return customer.getState();
	}


	@Override
	public boolean isAccountNonExpired () {
		return true;
	}


	public String getPostalCode () {
		return customer.getPostalCode();
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
		return customer.isEnabled();
	}


	public String getFullName () {
		return this.customer.getFirstName() + " " + this.customer.getLastName();
	}


	public void setFirstName (String firstName) {
		this.customer.setFirstName(firstName);
	}


	public void setLastName (String lastName) {
		this.customer.setLastName(lastName);
	}


	public boolean hasRole (String roleName) {
		return customer.hasRole(roleName);
	}


	@Override
	public String getUsername () {
//		return this.getEmail();
		return customer.getEmail();
	}

}
