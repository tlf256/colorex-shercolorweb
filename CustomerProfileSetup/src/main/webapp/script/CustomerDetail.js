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
	
	var eulaPdfTable = $('#eulapdf').DataTable({
		dom: 'ifBrtp',
		buttons : [
			'copy', 'print',
        ],
        "paginate": false,
        "scrollY" : 500,
        "pagingType": "full"
    });
	
});