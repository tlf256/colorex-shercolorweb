$(document).ready(function() {

	measurementTable = $("#measurement_table").DataTable({
		dom: 'ifBrtp',
		"emptyTable" : "No measurements available",
	    "ordering": true,
	    "order": [ 0, 'desc' ],
	    "paginate": false,
	    "scrollY" : '50vh',
	    "scrollX": true,
	    "pagingType": "full",
	    "aoColumnDefs":[
	    	{ "bVisible": false, "aTargets": [7,8]}
	    ]

	});
	$("#measurement_table").addClass("table-hover");

});