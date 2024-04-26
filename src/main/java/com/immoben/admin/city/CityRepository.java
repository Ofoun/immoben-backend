package com.immoben.admin.city;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.immoben.admin.paging.SearchRepository;
import com.immoben.common.entity.City;


public interface CityRepository extends SearchRepository<City, Integer> {

	public Long countById (Integer id);


	public City findByName (String name);


	public List<City> findAllByOrderByNameAsc ();


	@Query("SELECT c FROM City c WHERE c.name LIKE %?1%")
	public Page<City> findAll (String keyword, Pageable pageable);


	@Query("SELECT NEW City(c.id, c.name) FROM City c ORDER BY c.name ASC")
	public List<City> findAll ();
}
