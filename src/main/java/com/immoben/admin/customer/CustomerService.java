
package com.immoben.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.immoben.admin.city.CityRepository;
import com.immoben.admin.paging.PagingAndSortingHelper;
import com.immoben.admin.setting.country.CountryRepository;
import com.immoben.admin.setting.state.StateRepository;
import com.immoben.admin.user.RoleRepository;
import com.immoben.common.entity.City;
import com.immoben.common.entity.Country;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.Role;
import com.immoben.common.entity.State;
import com.immoben.common.exception.CustomerNotFoundException;


@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 50;

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private CityRepository cityRepo;

	public void listByPage (int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepo);
	}


	public void updateCustomerEnabledStatus (Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}


	public Customer get (Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Aucun client avec ID: " + id + " n'est trouvable.");
		}
	}


	public Customer getByEmail (String email) {
		return customerRepo.getCustomerByEmail(email);
	}


	public Customer getByPhoneNumber (String phoneNumber) {
		return customerRepo.getCustomerByPhoneNumber(phoneNumber);
	}


	public List<Role> listRoles () {
		return (List<Role>) roleRepo.findAll();
	}


	public List<Country> listAllCountries () {
		return countryRepo.findAllByOrderByNameAsc();
	}


	public List<State> listAllStates () {
		return stateRepo.findAllByOrderByNameAsc();
	}


	public List<City> listAllCities () {
		return cityRepo.findAllByOrderByNameAsc();
	}


	public List<Customer> listAll () {
		return (List<Customer>) customerRepo.findAll();
	}


	public boolean isEmailUnique (Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);

		if (existCustomer != null && existCustomer.getId() != id) {
			// found another customer having the same email
			return false;
		}

		return true;
	}


	public void save (Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();

		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}

		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

		customerRepo.save(customerInForm);
	}


	public void delete (Integer id) throws CustomerNotFoundException {
		Long count = customerRepo.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundException("Aucun client avec ID: " + id + " n'est trouvable.");
		}

		customerRepo.deleteById(id);
	}

}
