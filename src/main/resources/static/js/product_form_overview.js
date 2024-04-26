dropdownCities = $("#city");
dropdownCategories = $("#category");

$(document).ready(function() {
	
	$("#shortDescription").richText();
	$("#fullDescription").richText();
	
	dropdownCities.change(function() {
		dropdownCategories.empty();
		getCategories();
	});	
	
	getCategoriesForNewForm();

});

function getCategoriesForNewForm() {
	catIdField = $("#categoryId");
	editMode = false;
	
	if (catIdField.length) {
		editMode = true;
	}
	
	if (!editMode) getCategories();
}

function getCategories() {
	cityId = dropdownCities.val(); 
	url = cityModuleURL + "/" + cityId + "/categories";
	
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});			
	});
}

function checkUnique(form) {
	productId = $("#id").val();
	//productName = $("#name").val();
	
//	csrfValue = $("input[name='_csrf']").val();
	
//	params = {id: productId, name: productName, _csrf: csrfValue};
	params = {id: productId, _csrf: csrfValue};
	
	$.post(checkUniqueUrl, params, function(response) {
		if (response == "OK") {
			form.submit();
//		} else if (response == "Duplicate") {
//			showWarningModal("Il existe un autre produit portant le nom " + productName);	
		} else {
			showErrorModal("Réponse inconnue du serveur");
		}
		
	}).fail(function() {
		showErrorModal("Impossible de se connecter au serveur");
	});
	
	return false;
}	