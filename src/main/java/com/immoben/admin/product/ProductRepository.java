package com.immoben.admin.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.product.Product;


public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	public List<Product> findByName (String name);


	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateEnabledStatus (Integer id, boolean enabled);


	public Long countById (Integer id);


	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " + "OR p.shortDescription LIKE %?1% " + "OR p.fullDescription LIKE %?1% " + "OR p.city.name LIKE %?1% " + "OR p.category.name LIKE %?1%" + "OR p.customer.firstName LIKE %?1%")
	public Page<Product> findAll (String keyword, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 " + "OR p.category.allParentIDs LIKE %?2%")
	public Page<Product> findAllInCategory (String categoryIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 " + "OR p.category.allParentIDs LIKE %?2%) AND " + "(p.name LIKE %?3% " + "OR p.shortDescription LIKE %?3% " + "OR p.fullDescription LIKE %?3% " + "OR p.city.name LIKE %?3% " + "OR p.category.name LIKE %?3%)")
	public Page<Product> searchInCategory (Integer categoryId, String categoryIdMatch, String keyword, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> searchProductsByName (String keyword, Pageable pageable);

	/*----------------------------------------------------------*/

//	@Query("SELECT p FROM Product p WHERE ((p.category.id = ?1 " 
//			+ "OR p.category.allParentIDs LIKE %?2%) AND "
//			+ "(p.name LIKE %?3% " 
//			+ "OR p.shortDescription LIKE %?3% " 
//			+ "OR p.fullDescription LIKE %?3% "
//			+ "OR p.city.name LIKE %?3% " 
//			+ "OR p.category.name LIKE %?3%)) AND "
//			+ "(p.city.id = ?4 AND "
//			+ "(p.name LIKE %?5% " 
//			+ "OR p.shortDescription LIKE %?5% " 
//			+ "OR p.fullDescription LIKE %?5% "
//			+ "OR p.city.name LIKE %?5% " 
//			+ "OR p.category.name LIKE %?5%))")	
//	public Page<Product> searchInCategoryAndCity(String keyword, Integer categoryId, 
//			String categoryIdMatch, Integer cityId, String cityIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE  " + "(p.name LIKE %?1% " + "OR p.shortDescription LIKE %?1% " + "OR p.fullDescription LIKE %?1% " + "OR p.city.name LIKE %?1% " + "OR p.category.name LIKE %?1%)")
	public Page<Product> searchInCategoryAndCity (String keyword, String categoryIdMatch, String cityIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 " + "OR p.category.allParentIDs LIKE %?2%) AND " + "(p.name LIKE %?3% " + "OR p.shortDescription LIKE %?3% " + "OR p.fullDescription LIKE %?3% " + "OR p.city.name LIKE %?3% " + "OR p.category.name LIKE %?3%)")
	public Page<Product> searchInCategory (String keyword, String categoryIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE p.city.id = ?1 AND " + "(p.name LIKE %?2% " + "OR p.shortDescription LIKE %?2% " + "OR p.fullDescription LIKE %?2% " + "OR p.city.name LIKE %?2% " + "OR p.category.name LIKE %?2%)")
	public Page<Product> searchInCity (String keyword, String cityIdMatch, Pageable pageable);

//	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " 
//			+ "OR p.district LIKE %?1% "
//			+ "OR p.shortDescription LIKE %?1% "
//			+ "OR p.fullDescription LIKE %?1% "
//			+ "OR p.city.name LIKE %?1% "
//			+ "OR p.category.name LIKE %?1%")
//	public Page<Product> findAll(String keyword, Pageable pageable);
//	


	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 " + "OR p.category.allParentIDs LIKE %?2%) AND " + "p.city.id = ?3 ")
	public Page<Product> findAllInCategoryAndCity (String categoryIdMatch, String cityIdMatch, Pageable pageable);

//	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
//			+ "OR p.category.allParentIDs LIKE %?2%")	
//	public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, 
//			Pageable pageable);
//	


	@Query("SELECT p FROM Product p WHERE p.city.id = ?1 ")
	public Page<Product> findAllInCity (String cityIdMatch, Pageable pageable);


	public Page<Product> findAll (Pageable pageable);

	@Query("SELECT c FROM Customer AS c WHERE c.email = ?1")
	public Customer findCustomerByEmail (String email);
	
	@Query("SELECT p FROM Product AS p WHERE p.customer.id = ?1")
	public Product findProductByCustomerID (Integer id);

	/*-----------------------------------------------------------------------------*/

}
