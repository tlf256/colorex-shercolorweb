var customerTable;

$(document).ready(function() {
	customerTable = $('#customer_table').dataTable({
		dom: 'ifBrtp',
		buttons : [
            'copy', 'csv', 'excel', 'print'
        ],
		"emptyTable" : "No customers available",
        "ordering": true,
        "order": [ 0, 'desc' ],
        "paginate": false,
        "scrollY" : 500,
        "pagingType": "full"
    });
    
    $('#customer_table tbody').on('click','tr',function(event){
    	//window.alert("row clicked ");
    	var lookupDlrCustId = customerTable.fnGetData(this)[0];
    	//window.alert("Customer id clicked is " + lookupDlrCustId);
    	document.forms[0].lookupDlrCustId.value = lookupDlrCustId;
    	document.forms[0].submit();
    });
    $("#customer_table").addClass("table-hover");
});

