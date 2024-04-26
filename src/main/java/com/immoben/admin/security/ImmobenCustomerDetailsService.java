package com.immoben.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.immoben.admin.customer.CustomerRepository;
import com.immoben.common.entity.Customer;


public class ImmobenCustomerDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepo;

	@Override
	public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
		Customer customer = customerRepo.getCustomerByEmail(email);

		if (customer != null) {
			return new ImmobenCustomerDetails(customer);
		}

		throw new UsernameNotFoundException("Aucun utilisateur avec l'e-mail: " + email + " n'est trouvable.");
	}

}
