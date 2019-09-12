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
	
});