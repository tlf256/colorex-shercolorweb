$(document).ready(function() {
	measurementTable = $("#measurement_table").DataTable({
		dom: 'ifBrtp',
		"language": {
        	"emptyTable" : i18n['displayStoredMeasurements.noMeasurementsAvailable']
        },
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