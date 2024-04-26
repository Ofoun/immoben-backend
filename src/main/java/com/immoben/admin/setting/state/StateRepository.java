package com.immoben.admin.setting.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.immoben.common.entity.Country;
import com.immoben.common.entity.State;


public interface StateRepository extends CrudRepository<State, Integer> {

	public List<State> findByCountryOrderByNameAsc (Country country);


	public List<State> findAllByOrderByNameAsc ();
}
