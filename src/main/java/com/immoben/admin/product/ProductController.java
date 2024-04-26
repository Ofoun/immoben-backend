package com.immoben.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.immoben.admin.FileUploadUtil;
import com.immoben.admin.category.CategoryService;
import com.immoben.admin.city.CityService;
import com.immoben.admin.customer.CustomerService;
import com.immoben.admin.paging.PagingAndSortingHelper;
import com.immoben.admin.paging.PagingAndSortingParam;
import com.immoben.admin.security.ImmobenCustomerDetails;
import com.immoben.admin.security.ImmobenUserDetails;
import com.immoben.common.entity.Category;
import com.immoben.common.entity.City;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.product.Product;
import com.immoben.common.exception.ProductNotFoundException;


@Controller
public class ProductController {
	private String defaultRedirectURL = "redirect:/products/page/1?sortField=id&sortDir=desc&categoryId=0&cityId=0";
	@Autowired
	private ProductService productService;
	@Autowired
	private CityService cityService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CustomerService customerService;

	@GetMapping("/products")
	public String listFirstPage (Model model) {
		return defaultRedirectURL;
	}

//	@GetMapping("/products/page/{pageNum}")
//	public String listByPage(
//			@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
//			@PathVariable(name = "pageNum") int pageNum, Model model,
//			Integer categoryId
//			) {
//		
//		productService.listByPage(pageNum, helper, categoryId);
//		
//		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//		
//		if (categoryId != null) model.addAttribute("categoryId", categoryId);
//		model.addAttribute("listCategories", listCategories);
//		
//		return "products/products";		
//	}


	@GetMapping("/products/page/{pageNum}")
	public String listByPage (@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper, @PathVariable(name = "pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword, @Param("categoryId") Integer categoryId, @Param("cityId") Integer cityId, @Param("customerId") Integer customerId) {
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId, cityId, customerId);
		List<Product> listProducts = page.getContent();

//		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		List<Category> listCategories = categoryService.listNoChildrenCategories();

		List<City> listCities = cityService.listAll();

		List<Customer> listCustomers = customerService.listAll();

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		if (categoryId != null) model.addAttribute("categoryId", categoryId);
		if (cityId != null) model.addAttribute("cityId", cityId);
		if (customerId != null) model.addAttribute("customerId", customerId);

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listCities", listCities);
		model.addAttribute("listCustomers", listCustomers);

		return "products/products";
	}


	@GetMapping("/products/new")
	public String newProduct (Model model) {
		List<City> listCities = cityService.listAll();

		Product product = new Product();
		Customer customer = product.getCustomer();

		product.setEnabled(true);
		product.setInStock(true);
		product.setCustomer(customer);

		model.addAttribute("product", product);
		model.addAttribute("listCities", listCities);
		model.addAttribute("pageTitle", "Créer une nouvelle annonce");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}


	@PostMapping("/products/save")
	public String saveProduct (Product product, RedirectAttributes ra, @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart, @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts, @RequestParam(name = "detailIDs", required = false) String[] detailIDs, @RequestParam(name = "detailNames", required = false) String[] detailNames, @RequestParam(name = "detailValues", required = false) String[] detailValues, @RequestParam(name = "imageIDs", required = false) String[] imageIDs, @RequestParam(name = "imageNames", required = false) String[] imageNames, @AuthenticationPrincipal ImmobenUserDetails loggedUser, @AuthenticationPrincipal ImmobenCustomerDetails loggedCustomer) throws IOException {

//		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
//			if (loggedUser.hasRole("Salesperson")) {
//				productService.saveProductPrice(product);
//				ra.addFlashAttribute("message", "Le produit '" + product.getShortName() + "' a été enregistré avec succès ! ");			
//				return defaultRedirectURL;
//			}
//		}

//		if (!loggedUser.hasRole("Admin") && (!loggedUser.hasRole("Editor")) || !loggedCustomer.hasRole("Admin")) {
//			if (loggedUser.hasRole("Salesperson") || loggedCustomer.hasRole("Editor")) {
//				productService.saveProductPrice(product);
//				ra.addFlashAttribute("message", "Le produit '" + product.getShortName() + "' a été enregistré avec succès ! ");			
//				return defaultRedirectURL;
//			}
//		}

//		if (!loggedUser.hasRole("Admin") && (!loggedUser.hasRole("Editor")) || !loggedCustomer.hasRole("Admin")) {
//			if (loggedCustomer.hasRole("Editor") || loggedUser.hasRole("Salesperson")) {
//				productService.saveProductPrice(product);
//				ra.addFlashAttribute("message", "Le produit '" + product.getShortName() + "' a été enregistré avec succès ! ");			
//				return defaultRedirectURL;
//			}
//		}  

		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);

		Product savedProduct = this.productService.save(product);

		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);

		ra.addFlashAttribute("message", "L" + "'" + "annonce '" + product.getShortName() + "' avec le numéro Id: " + product.getId() + "  a été modifié avec succès ! ");

		return defaultRedirectURL;
	}


	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus (@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) throws ProductNotFoundException {
		Product product = productService.get(id);
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "activé(e)" : "désactivé(e)";
		String message = "Le produit    '" + product.getShortName() + "'  a été " + status + " avec succès !";
		redirectAttributes.addFlashAttribute("message", message);

		return defaultRedirectURL;
	}


	@GetMapping("/products/delete/{id}")
	public String deleteProduct (@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);
			productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;

			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);

			redirectAttributes.addFlashAttribute("message", "Le produit '" + product.getShortName() + "' a été supprimé avec succès !");
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return defaultRedirectURL;
	}


	@GetMapping("/products/edit/{id}")
	public String editProduct (@PathVariable("id") Integer id, Model model, RedirectAttributes ra, @AuthenticationPrincipal ImmobenUserDetails loggedUser, @AuthenticationPrincipal ImmobenCustomerDetails loggedCustomer) {
		try {
			Product product = productService.get(id);
			List<City> listCities = cityService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();

			boolean isReadOnlyForSalesperson = false;

//			if (!loggedUser.hasRole("Admin") || !loggedCustomer.hasRole("Editor")) {
//				if (loggedUser.hasRole("Salesperson") || loggedCustomer.hasRole("Salesperson")) {
//					isReadOnlyForSalesperson = true;						
//				}
//			} 
//			
//			else if (!loggedCustomer.hasRole("Admin") && !loggedCustomer.hasRole("Salesperson")) {
//				if (loggedCustomer.hasRole("Editor")) {
//					isReadOnlyForSalesperson = true;
//				}
//			}

			model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
			model.addAttribute("product", product);
			model.addAttribute("listCities", listCities);
			model.addAttribute("pageTitle", "Modifier cette annonce ( '" + product.getShortName() + "' avec ID: " + id + " )");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_form";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return defaultRedirectURL;
		}
	}


	@GetMapping("/products/detail/{id}")
	public String viewProductDetails (@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			model.addAttribute("product", product);

			return "products/product_detail_modal";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return defaultRedirectURL;
		}
	}
}
