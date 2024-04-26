package com.immoben.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.immoben.admin.paging.PagingAndSortingHelper;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.product.Product;
import com.immoben.common.exception.ProductNotFoundException;


@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 50;

	@Autowired
	private ProductRepository repo;

	public List<Product> listAll () {
		return (List<Product>) this.repo.findAll();
	}


	public Page<Product> listByPage (int pageNum, String sortField, String sortDir, String keyword, Integer categoryId, Integer cityId, Integer customerId) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";

				if (cityId != null && cityId > 0) {
					String cityIdMatch = "-" + String.valueOf(cityId) + "-";

					this.repo.searchInCategoryAndCity(keyword, categoryIdMatch, cityIdMatch, pageable);

				} else {
					this.repo.searchInCategory(keyword, categoryIdMatch, pageable);
				}
			} else {

				if (cityId != null && cityId > 0) {
					String cityIdMatch = "-" + String.valueOf(cityId) + "-";

					this.repo.searchInCity(keyword, cityIdMatch, pageable);

				} else {
					this.repo.findAll(keyword, pageable);
				}
			}
		} else {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";

				if (cityId != null && cityId > 0) {
					String cityIdMatch = "-" + String.valueOf(cityId) + "-";

					this.repo.findAllInCategoryAndCity(categoryIdMatch, cityIdMatch, pageable);
				} else {
					this.repo.findAllInCategory(categoryIdMatch, pageable);
				}
			} else {
				if (cityId != null && cityId > 0) {
					String cityIdMatch = "-" + String.valueOf(cityId) + "-";

					this.repo.findAllInCity(cityIdMatch, pageable);
				} else {
					this.repo.findAll(pageable);
				}
			}
		}

		return this.repo.findAll(pageable);
	}


	public void searchProducts (int pageNum, PagingAndSortingHelper helper) {
		Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		Page<Product> page = this.repo.searchProductsByName(keyword, pageable);
		helper.updateModelAttributes(pageNum, page);
	}


	public Product save (Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}

		product.setUpdatedTime(new Date());

		final Customer owner = this.repo.findCustomerByEmail(product.getOwnerEmail());
		product.setCustomer(owner);

		return this.repo.save(product);
	}


	public void saveProductPrice (Product productInForm) {
		Product productInDB = this.repo.findById(productInForm.getId()).get();
		productInDB.setCost(productInForm.getCost());
		productInDB.setPrice(productInForm.getPrice());
		productInDB.setDiscountPercent(productInForm.getDiscountPercent());

		this.repo.save(productInDB);
	}


	/*
	 * public String checkUnique(Integer id, String name) { boolean isCreatingNew = (id == null || id == 0); Product productByName =
	 * this.repo.findByName(name);
	 * 
	 * if (isCreatingNew) { if (productByName != null) return "Duplicate"; } else { if (productByName != null &&
	 * productByName.getId() != id) { return "Duplicate"; } }
	 * 
	 * return "OK"; }
	 * 
	 */
	public void updateProductEnabledStatus (Integer id, boolean enabled) {
		this.repo.updateEnabledStatus(id, enabled);
	}


	public void delete (Integer id) throws ProductNotFoundException {
		Long countById = this.repo.countById(id);

		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Aucun produit avec ID: " + id + " n'est trouvable.");
		}

		this.repo.deleteById(id);
	}


	public Product get (Integer id) throws ProductNotFoundException {
		try {
			return this.repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Aucun produit avec ID: " + id + " n'est trouvable.");
		}
	}
}
