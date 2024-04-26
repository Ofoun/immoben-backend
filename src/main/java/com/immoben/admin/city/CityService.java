package com.immoben.admin.city;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.immoben.admin.paging.PagingAndSortingHelper;
import com.immoben.common.entity.City;


@Service
public class CityService {
	public static final int CITIES_PER_PAGE = 50;

	@Autowired
	private CityRepository repo;

	public List<City> listAll () {
		return (List<City>) repo.findAll();
	}


	public void listByPage (int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CITIES_PER_PAGE, repo);
	}


	public City save (City city) {
		return repo.save(city);
	}


	public City get (String name) throws CityNotFoundException {
		try {
			return repo.findByName(name);
		} catch (NoSuchElementException ex) {
			throw new CityNotFoundException("Aucune ville comme: " + name + " n'est trouvable.");
		}
	}


	public City get (Integer id) throws CityNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CityNotFoundException("Aucune ville avec ID: " + id + " n'est trouvable.");
		}
	}


	public void delete (Integer id) throws CityNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new CityNotFoundException("Aucune ville avec ID: " + id + " n'est trouvable.");
		}

		repo.deleteById(id);
	}


	public String checkUnique (Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		City cityByName = repo.findByName(name);

		if (isCreatingNew) {
			if (cityByName != null) return "Duplicate";
		} else {
			if (cityByName != null && cityByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
}
