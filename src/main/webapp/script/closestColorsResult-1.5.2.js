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
        	"emptyTable" : 'No closest colors found'
        },
        "ordering": true,
        "order": [ 2, "asc" ],
	    "paginate": false,
	    "scrollY" : '50vh',
	    "pagingType": "full",
		"scrollCollapse": true
	});
}

function showCmptColorTable(){
	cmptColorTable = $("#closestCmptColor_table").DataTable({
		dom: 'ifBrtp',
		"language": {
        	"emptyTable" : 'No closest colors found'
        },
        "ordering": true,
        "order": [ 4, "asc" ],
	    "paginate": false,
	    "scrollY" : '50vh',
	    "pagingType": "full",
		"scrollCollapse": true
	});
}