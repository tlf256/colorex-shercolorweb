/**
 * 
 */
$(document).ready(function() {
	//sessionStorage.clear();
	var custTable = $('#customer_table').DataTable({
		dom: 'ifBrtp',
		buttons : [
            {
            	text: 'Create New',
        		action: function ( e, dt, node, config ) {
        			window.location.assign("openCustomerForm.action");
        			//window.open("createNewCustomer.action");
        		}            	
            }
        ],
		"emptyTable" : "No customers available",
        "ordering": true,
        "order": [ 1, 'asc' ],
        "paginate": false,
        "scrollY" : 500,
        "pagingType": "full"
    });
	
	$("#customer_table").addClass("table-hover");
	$('#customer_table tbody').on('click','tr',function(event){
		var lookupCustomerId = custTable.row(this).data()[0];
		//window.alert("row data at [0] is " + lookupCustomer[0]);
    	document.forms[0].lookupCustomerId.value = lookupCustomerId;
    	document.forms[0].submit();
    });
    
});