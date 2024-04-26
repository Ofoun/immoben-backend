package com.immoben.admin.customer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.immoben.admin.paging.SearchRepository;
import com.immoben.common.entity.Customer;


public interface CustomerRepository extends SearchRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', c.phoneNumber, ' ', " + "c.addressLine1, ' ', c.addressLine2, ' ', c.city.name, ' ', c.state.name, " + "' ', c.postalCode, ' ', c.country.name) LIKE %?1%")
	public Page<Customer> findAll (String keyword, Pageable pageable);


	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus (Integer id, boolean enabled);


	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail (String email);


	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByPhoneNumber (String phoneNumber);


	public Long countById (Integer id);


	@Query("SELECT c FROM Customer c WHERE c.email = :email")
	public Customer getCustomerByEmail (@Param("email") String email);


	@Query("SELECT c FROM Customer c WHERE c.phoneNumber = :phoneNumber")
	public Customer getCustomerByPhoneNumber (@Param("phoneNumber") String phoneNumber);


	public List<Customer> findAll ();

}
