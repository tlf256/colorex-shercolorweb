/**
 * 
 */
$(document).ready(function(){
	showSwColorTable();
	if($('#cmptResults').is(':visible')){
		showCmptColorTable();
	}
});

function showSwColorTable(){
	swColorTable = $("#closestSwColor_table").DataTable({
		dom: 'iBrtp',
		"language": {
        	"emptyTable" : i18n['closestColors.noColorsFound']
        },
        "ordering": true,
        "order": [ 2, "asc" ],
	    "paginate": false,
	    "scrollY" : '50vh',
	    "pagingType": "full",
		"pageLength": 10,
		"scrollCollapse": true
	});
}

function showCmptColorTable(){
	cmptColorTable = $("#closestCmptColor_table").DataTable({
		dom: 'fiBrtp',
		"language": {
        	"emptyTable" : i18n['closestColors.noColorsFound']
        },
        "ordering": true,
        "order": [ 4, "asc" ],
	    "paginate": false,
	    "scrollY" : '50vh',
	    "pagingType": "full",
		"pageLength": 100,
		"scrollCollapse": true
	});
}