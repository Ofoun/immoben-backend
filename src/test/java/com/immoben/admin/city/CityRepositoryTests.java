package com.immoben.admin.city;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.immoben.common.entity.City;
import com.immoben.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CityRepositoryTests {
	
	@Autowired
	private CityRepository repo;
	
	@Test
	public void testCreateCity1() {
		Category laptops = new Category(6);
		City acer = new City(null, "Acer");
		acer.getCategories().add(laptops);
		
		City savedCity = repo.save(acer);
		
		assertThat(savedCity).isNotNull();
		assertThat(savedCity.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCity2() {
		Category cellphones = new Category(4);
		Category tablets = new Category(7);
		
		City apple = new City(null, "Apple");
		apple.getCategories().add(cellphones);
		apple.getCategories().add(tablets);
		
		City savedCity = repo.save(apple);
		
		assertThat(savedCity).isNotNull();
		assertThat(savedCity.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCity3() {
		City samsung = new City(null, "Samsung");
		
		samsung.getCategories().add(new Category(29));	// category memory
		samsung.getCategories().add(new Category(24));	// category internal hard drive

		City savedCity = repo.save(samsung);
		
		assertThat(savedCity).isNotNull();
		assertThat(savedCity.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<City> cities = repo.findAll();
		cities.forEach(System.out::println);
		
		assertThat(cities).isNotEmpty();
	}
	
	@Test
	public void testGetById() {
		City city = repo.findById(1).get();
		
		assertThat(city.getName()).isEqualTo("Acer");
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		City samsung = repo.findById(3).get();
		samsung.setName(newName);
		
		City savedCity = repo.save(samsung);
		assertThat(savedCity.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDelete() {
		Integer id = 2;
		repo.deleteById(id);
		
		Optional<City> result = repo.findById(id);
		
		assertThat(result.isEmpty());
	}
}
