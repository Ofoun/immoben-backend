package com.immoben.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.immoben.admin.AmazonS3Util;
import com.immoben.admin.paging.PagingAndSortingHelper;
import com.immoben.admin.paging.PagingAndSortingParam;
import com.immoben.admin.user.UserNotFoundException;
import com.immoben.admin.user.UserService;
import com.immoben.admin.user.export.UserCsvExporter;
import com.immoben.admin.user.export.UserExcelExporter;
import com.immoben.admin.user.export.UserPdfExporter;
import com.immoben.common.entity.Role;
import com.immoben.common.entity.User;


@Controller
public class UserController {
	private String defaultRedirectURL = "redirect:/users/page/1?sortField=id&sortDir=desc";
	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listFirstPage () {
		return defaultRedirectURL;
	}


	@GetMapping("/users/page/{pageNum}")
	public String listByPage (@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper, @PathVariable(name = "pageNum") int pageNum) {
		service.listByPage(pageNum, helper);

		return "users/users";
	}


	@GetMapping("/users/new")
	public String newUser (Model model) {
		List<Role> listRoles = service.listRoles();

		User user = new User();
		user.setEnabled(true);

		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Créer un nouvel utilisateur");

		return "users/user_form";
	}


	@PostMapping("/users/save")
	public String saveUser (User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);

			String uploadDir = "user-photos/" + savedUser.getId();

			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());

//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}

		redirectAttributes.addFlashAttribute("message", "L'utilisateur " + user.getFullName() + " a été enregistré avec succès.");

		return getRedirectURLtoAffectedUser(user);
	}


	private String getRedirectURLtoAffectedUser (User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}


	@GetMapping("/users/edit/{id}")
	public String editUser (@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Modifier l'utilisateur (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);

			return "users/user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}


	@GetMapping("/users/delete/{id}")
	public String deleteUser (@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			service.delete(id);
			String userPhotosDir = "user-photos/" + id;
			AmazonS3Util.removeFolder(userPhotosDir);

			redirectAttributes.addFlashAttribute("message", "L'utilisateur " + user.getFullName() + " a été supprimé avec succès.");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return defaultRedirectURL;
	}


	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus (@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) throws UserNotFoundException {
		User user = service.get(id);
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "activé(e)" : "désactivé(e)";
		String message = "L'utilisateur " + user.getFullName() + " a été " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return defaultRedirectURL;
	}


	@GetMapping("/users/export/csv")
	public void exportToCSV (HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}


	@GetMapping("/users/export/excel")
	public void exportToExcel (HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();

		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}


	@GetMapping("/users/export/pdf")
	public void exportToPDF (HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();

		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}
}
