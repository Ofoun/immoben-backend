package com.immoben.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.immoben.admin.FileUploadUtil;
import com.immoben.admin.customer.CustomerService;
import com.immoben.admin.security.ImmobenCustomerDetails;
import com.immoben.admin.security.ImmobenUserDetails;
import com.immoben.admin.user.UserService;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.User;


@RequestMapping("/")
@Controller
public class AccountController {

	@Autowired
	private UserService service;

	@Autowired
	private CustomerService customerService;

	@GetMapping("/account")
	public String viewDetails (@AuthenticationPrincipal ImmobenUserDetails loggedUser, @AuthenticationPrincipal ImmobenCustomerDetails loggedCustomer, Model model) {

		User user = new User();
		if (loggedUser != null) {
			String email = loggedUser.getUsername();

			User userEmail = service.getByEmail(email);
			model.addAttribute("user", user);
			model.addAttribute("userEmail", userEmail);

			user.setId(loggedUser.getId());
			user.setEmail(loggedUser.getUsername());
			user.setEmail(loggedUser.getEmail());
			user.setPassword(loggedUser.getPassword());
			user.setLastName(loggedUser.getLastName());
			user.setFirstName(loggedUser.getFirstName());
			user.setPhotos(loggedUser.getPhotos());

//		return "users/account_form";

		} else if (loggedCustomer != null) {

			Customer customer = new Customer();
//			String email = loggedCustomer.getUsername();
			Customer customerEmail = customerService.getByEmail(loggedCustomer.getEmail());
			model.addAttribute("customer", customer);
			model.addAttribute("customerEmail", customerEmail);

			customer.setId(loggedCustomer.getId());
			customer.setEmail(loggedCustomer.getUsername());
			customer.setEmail(loggedCustomer.getEmail());
			customer.setPassword(loggedCustomer.getPassword());
			customer.setLastName(loggedCustomer.getLastName());
			customer.setFirstName(loggedCustomer.getFirstName());
			customer.setPhoneNumber(loggedCustomer.getPhoneNumber());
			customer.setAddressLine1(loggedCustomer.getAddressLine1());
			customer.setAddressLine2(loggedCustomer.getAddressLine2());
			customer.setCity(loggedCustomer.getCity());
			customer.setCountry(loggedCustomer.getCountry());
			customer.setState(loggedCustomer.getState());
			customer.setPostalCode(loggedCustomer.getPostalCode());

			return "customers/customer_form";
		}
		return "users/account_form";
	}


	@PostMapping("/account/update")
	public String saveDetails (User user, RedirectAttributes redirectAttributes, @AuthenticationPrincipal ImmobenUserDetails loggedUser, @RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);

			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null);
			service.updateAccount(user);
		}

		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());

		redirectAttributes.addFlashAttribute("message", user.getFullName() + " , vos détails de compte ont été mis à jour avec succès.");

		return "redirect:/account";
	}
}
