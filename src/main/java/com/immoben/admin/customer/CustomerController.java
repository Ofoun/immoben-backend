package com.immoben.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.immoben.admin.paging.PagingAndSortingHelper;
import com.immoben.admin.paging.PagingAndSortingParam;
import com.immoben.common.entity.City;
import com.immoben.common.entity.Country;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.State;
import com.immoben.common.exception.CustomerNotFoundException;


@Controller
public class CustomerController {
	private String defaultRedirectURL = "redirect:/customers/page/1?sortField=id&sortDir=desc";

	@Autowired
	private CustomerService customerService;

	@GetMapping("/customers")
	public String listFirstPage (Model model) {
		return defaultRedirectURL;
	}


	@GetMapping("/customers/page/{pageNum}")
	public String listByPage (@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper, @PathVariable(name = "pageNum") int pageNum) {

		customerService.listByPage(pageNum, helper);

		return "customers/customers";
	}


	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus (@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
		Customer customer = customerService.get(id);
		customerService.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "activé(e)" : "désactivé(e)";
		String message = "Le client '" + customer.getFullName() + "' a été " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return defaultRedirectURL;
	}


	@GetMapping("/customers/detail/{id}")
	public String viewCustomer (@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = customerService.get(id);
			model.addAttribute("customer", customer);

			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}


	@GetMapping("/customers/edit/{id}")
	public String editCustomer (@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = customerService.get(id);
			List<Country> listCountries = customerService.listAllCountries();
			List<State> listStates = customerService.listAllStates();
			List<City> listCities = customerService.listAllCities();

			model.addAttribute("listCountries", listCountries);
			model.addAttribute("listStates", listStates);
			model.addAttribute("listCities", listCities);
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", String.format("Modifier le client '" + customer.getFullName() + "' (ID: %d)", id));

			return "customers/customer_form";

		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}


	@PostMapping("/customers/save")
	public String saveCustomer (Customer customer, Model model, RedirectAttributes ra) {

		customer.setEnabled(true);
		customerService.save(customer);
		ra.addFlashAttribute("message", "Le client avec ID " + customer.getId() + " a été modifié avec succès.");
		return defaultRedirectURL;
	}


	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer (@PathVariable Integer id, RedirectAttributes ra) {
		try {
			Customer customer = customerService.get(id);
			customerService.delete(id);
			ra.addFlashAttribute("message", "Le client '" + customer.getFullName() + "' avec ID: " + customer.getId() + "  a été supprimé avec succès !");

		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}

		return defaultRedirectURL;
	}

}
