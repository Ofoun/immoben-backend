package com.immoben.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.immoben.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {

	@Autowired
	private CurrencyRepository repo;
	
	@Test
	public void testCreateCurrencies() {
		List<Currency> listCurrencies = Arrays.asList(
			new Currency(null, "United States Dollar", "$", "USD"),
			new Currency(null, "British Pound", "£", "GPB"),
			new Currency(null, "Japanese Yen", "¥", "JPY"),
			new Currency(null, "Euro", "€", "EUR"),
			new Currency(null, "Russian Ruble", "₽", "RUB"),
			new Currency(null, "South Korean Won", "₩", "KRW"),
			new Currency(null, "Chinese Yuan", "¥", "CNY"),
			new Currency(null, "Brazilian Real", "R$", "BRL"),
			new Currency(null, "Australian Dollar", "$", "AUD"),
			new Currency(null, "Canadian Dollar", "$", "CAD"),
			new Currency(null, "Vietnamese đồng ", "₫", "VND"),
			new Currency(null, "Indian Rupee", "₹", "INR")
		);
		
		repo.saveAll(listCurrencies);
		
		Iterable<Currency> iterable = repo.findAll();
		
		assertThat(iterable).size().isEqualTo(12);
	}
	
	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencies = repo.findAllByOrderByNameAsc();
		
		currencies.forEach(System.out::println);
		
		assertThat(currencies.size()).isGreaterThan(0);
	}
}
