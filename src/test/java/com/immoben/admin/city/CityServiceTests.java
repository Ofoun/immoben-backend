package com.immoben.admin.city;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.immoben.common.entity.City;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CityServiceTests {
	
	@MockBean private CityRepository repo;
	
	@InjectMocks private CityService service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicate() {
		Integer id = null;
		String name = "Acer";
		City city = new City(null, name);
		
		Mockito.when(repo.findByName(name)).thenReturn(city);
		
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "AMD";
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicate() {
		Integer id = 1;
		String name = "Canon";
		City city = new City(id, name);
		
		Mockito.when(repo.findByName(name)).thenReturn(city);
		
		String result = service.checkUnique(2, "Canon");
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "Acer";
		City city = new City(id, name);
		
		Mockito.when(repo.findByName(name)).thenReturn(city);
		
		String result = service.checkUnique(id, "Acer Ltd");
		assertThat(result).isEqualTo("OK");
	}	
}
