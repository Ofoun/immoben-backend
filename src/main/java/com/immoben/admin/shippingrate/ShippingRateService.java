package com.immoben.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.immoben.admin.paging.PagingAndSortingHelper;
import com.immoben.admin.setting.country.CountryRepository;
import com.immoben.common.entity.Country;
import com.immoben.common.entity.ShippingRate;


@Service
@Transactional
public class ShippingRateService {
	public static final int RATES_PER_PAGE = 10;
//	private static final int DIM_DIVISOR = 139;	
	@Autowired
	private ShippingRateRepository shipRepo;
	@Autowired
	private CountryRepository countryRepo;
//	@Autowired private ProductRepository productRepo;

	public void listByPage (int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
	}


	public List<Country> listAllCountries () {
		return countryRepo.findAllByOrderByNameAsc();
	}


	public void save (ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDB = shipRepo.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);

		if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("Il y a déjà un tarif pour la destination " + rateInForm.getCountry().getName() + ", " + rateInForm.getState());
		}
		shipRepo.save(rateInForm);
	}


	public ShippingRate get (Integer id) throws ShippingRateNotFoundException {
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Impossible de trouver le tarif d'expédition avec l'ID: " + id);
		}
	}


	public void updateCODSupport (Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Impossible de trouver le tarif d'expédition avec l'ID: " + id);
		}

		shipRepo.updateCODSupport(id, codSupported);
	}


	public void delete (Integer id) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Impossible de trouver le tarif d'expédition avec l'ID: " + id);

		}
		shipRepo.deleteById(id);
	}

	/*
	 * public float calculateShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
	 * ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);
	 * 
	 * if (shippingRate == null) { throw new ShippingRateNotFoundException("Aucun tarif d'expédition trouvé pour cette " +
	 * " destination. Vous devez saisir les frais de port manuellement."); }
	 * 
	 * Product product = productRepo.findById(productId).get();
	 * 
	 * 
	 * float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR; float finalWeight =
	 * product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
	 * 
	 * return finalWeight * shippingRate.getRate(); }
	 */
}
