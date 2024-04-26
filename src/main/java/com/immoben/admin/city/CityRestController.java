package com.immoben.admin.city;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.immoben.common.entity.Category;
import com.immoben.common.entity.City;


@RestController
@CrossOrigin(origins = "*")
public class CityRestController {
	@Autowired
	private CityService service;

	@PostMapping("/cities/check_unique")
	public String checkUnique (Integer id, String name) {
		return service.checkUnique(id, name);
	}


	@GetMapping("/cities/{id}/categories")
	public List<CategoryDTO> listCategoriesByCity (@PathVariable(name = "id") Integer cityId) throws CityNotFoundRestException {
		List<CategoryDTO> listCategories = new ArrayList<>();

		try {
			City city = service.get(cityId);
			Set<Category> categories = city.getCategories();

			for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(dto);
			}

			return listCategories;
		} catch (CityNotFoundException e) {
			throw new CityNotFoundRestException();
		}
	}
}
