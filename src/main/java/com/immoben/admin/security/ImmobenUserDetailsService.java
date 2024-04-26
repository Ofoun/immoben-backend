package com.immoben.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.immoben.admin.customer.CustomerRepository;
import com.immoben.admin.user.UserRepository;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.User;


public class ImmobenUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CustomerRepository customerRepo;

	@Override
	public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {

//		if (user != null) {
//			return new immobenUserDetails(user);
//		}
//		

		// first try loading from User table
		User user = userRepo.getUserByEmail(email);

		if (user != null) {
			return new ImmobenUserDetails(user);
		} else {
			// Not found in user table, so check admin
//            Admin admin = adminRepository.findByUsername(username);
			Customer customer = customerRepo.getCustomerByEmail(email);

			if (customer != null) {
				return new ImmobenCustomerDetails(customer);
			}
		}

		throw new UsernameNotFoundException("Aucun utilisateur avec l'e-mail: " + email + " n'est trouvable.");
	}

}
