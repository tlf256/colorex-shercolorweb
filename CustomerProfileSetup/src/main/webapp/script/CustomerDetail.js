/**
 * 
 */
$(document).ready(function() {
	
	$("#delete").click(function(){
    	$("#deletemodal").modal('show');
	});
	
	$("#submitbtn").click(function(){
		$("#updatemodal").modal('show');
	});
	
	$("#inactivate").on("click", function(){
		$("#inactivemodal").modal('show');
	});
	
	$("#download_modal").on('show.bs.modal', function(){
		$("#eulapdf").html('<iframe src="downloadEula.action" class="embed-responsive-item" allowfullscreen></iframe>');
	});
	
});

function downloadPdf(){
	
	$("#download_modal").modal('show');
	
}