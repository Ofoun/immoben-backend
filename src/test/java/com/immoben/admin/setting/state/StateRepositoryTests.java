package com.immoben.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.immoben.common.entity.Country;
import com.immoben.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {

	@Autowired private StateRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateStatesInIndia() {
		Integer countryId = 1;
		Country country = entityManager.find(Country.class, countryId);
		
//		State state = repo.save(new State(null, "Karnataka", country));
//		State state = repo.save(new State(null, "Punjab", country));
//		State state = repo.save(new State(null, "Uttar Pradesh", country));
		State state = repo.save(new State(null, "West Bengal", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateStatesInUS() {
		Integer countryId = 2;
		Country country = entityManager.find(Country.class, countryId);
		
//		State state = repo.save(new State(null, "California", country));
//		State state = repo.save(new State(null, "Texas", country));
//		State state = repo.save(new State(null, "New York", country));
		State state = repo.save(new State(null, "Washington", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListStatesByCountry() {
		Integer countryId = 2;
		Country country = entityManager.find(Country.class, countryId);
		List<State> listStates = repo.findByCountryOrderByNameAsc(country);
		
		listStates.forEach(System.out::println);
		
		assertThat(listStates.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateState() {
		Integer stateId = 3;
		String stateName = "Tamil Nadu";
		State state = repo.findById(stateId).get();
		
		state.setName(stateName);
		State updatedState = repo.save(state);
		
		assertThat(updatedState.getName()).isEqualTo(stateName);
	}
	
	@Test
	public void testGetState() {
		Integer stateId = 1;
		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isPresent());
	}
	
	@Test
	public void testDeleteState() {
		Integer stateId = 8;
		repo.deleteById(stateId);

		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isEmpty());		
	}
}
