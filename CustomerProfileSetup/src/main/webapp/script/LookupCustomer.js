/**
 * 
 */
$(document).ready(function() {
	var customer = $('#customer').DataTable({
	dom: 'iBrtp',
	buttons : [
	    {
	    	text: 'Edit',
			action: function ( e, dt, node, config ) {
				window.location.assign("populateCustomerForm.action");
			}
	    },
	    {
	    	text: 'Delete',
			action: function ( e, dt, node, config ) {
				alert( this.text() );
			}
	    }
	],
	"emptyTable" : "Customer does not exist",
	"ordering": false,
	"paginate": false,
	"scrollY" : 500,
	"pagingType": "full"
});

/*$("#customer").addClass("table-hover");
$('#customer tbody').on('click','tr',function(event){
	var lookupCustomer = customer.row(this).data()[0];
	document.forms[0].lookupCustomer.value = lookupCustomer;
	document.forms[0].submit();
});*/

});