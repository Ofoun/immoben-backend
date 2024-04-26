function clearFilter() {
	window.location = moduleURL;	
}

function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	
	$("#yesButton").attr("href", link.attr("href"));	
	$("#confirmText").text("Êtes-vous sûr de vouloir supprimer ce/cette "
							 + entityName + " avec ID " + entityId + "?");
	$("#confirmModal").modal();	
}