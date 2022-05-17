/**
 * 
 */
$(document).ready(function(){
	closestColorTable = $("#closestColor_table").DataTable({
		dom: 'ifBrtp',
		"language": {
        	"emptyTable" : i18n['displayStoredMeasurements.noMeasurementsAvailable'],
        	"url" : langUrl
        },
        "ordering": true,
        "order": [ 4, "asc" ],
	    "paginate": false,
	    "scrollY" : '50vh',
	    "scrollX": true,
	    "pagingType": "full"
	});
});