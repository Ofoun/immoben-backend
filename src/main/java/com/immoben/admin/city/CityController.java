package com.immoben.admin.city;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.immoben.admin.category.CategoryService;
import com.immoben.admin.paging.PagingAndSortingHelper;
import com.immoben.admin.paging.PagingAndSortingParam;
import com.immoben.common.entity.Category;
import com.immoben.common.entity.City;


@Controller
public class CityController {
	private String defaultRedirectURL = "redirect:/cities/page/1?sortField=name&sortDir=asc";
	@Autowired
	private CityService cityService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/cities")
	public String listFirstPage () {
		return defaultRedirectURL;
	}


	@GetMapping("/cities/page/{pageNum}")
	public String listByPage (@PagingAndSortingParam(listName = "listCities", moduleURL = "/cities") PagingAndSortingHelper helper, @PathVariable(name = "pageNum") int pageNum) {
		cityService.listByPage(pageNum, helper);
		return "cities/cities";
	}


	@GetMapping("/cities/new")
	public String newCity (Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("city", new City());
		model.addAttribute("pageTitle", "Insérer une nouvelle ville");

		return "cities/city_form";

	}


	@PostMapping("/cities/save")
	public String saveCity (City city, RedirectAttributes ra) throws IOException {

		cityService.save(city);

		ra.addFlashAttribute("message", "La ville " + city.getName() + " a été enregistrée avec succès !");
		return defaultRedirectURL;
	}


	@GetMapping("/cities/edit/{id}")
	public String editCity (@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		try {
			City city = cityService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();

			model.addAttribute("city", city);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Modifier la ville '" + city.getName() + "' (ID: " + id + ")");

			return "cities/city_form";
		} catch (CityNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}


	@GetMapping("/cities/delete/{id}")
	public String deleteCity (@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			cityService.delete(id);

			redirectAttributes.addFlashAttribute("message", "La ville avec ID " + id + " a été supprimée avec succès !");
		} catch (CityNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return defaultRedirectURL;
	}
}
