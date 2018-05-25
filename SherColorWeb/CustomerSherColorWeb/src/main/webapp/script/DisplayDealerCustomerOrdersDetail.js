var orderDtlTable;

$(document).ready(function() {
	orderDtlTable = $('#orderdtl_table').dataTable({
		dom: 'ifBrtp',
		buttons : [
            'copy', 'csv', 'excel', 'print'
        ],
		"emptyTable" : "No customer orders available",
        "ordering": true,
        "order": [ 0, 'desc' ],
        "paginate": false,
        "scrollY" : 500,
        "pagingType": "full"
    });
    
    $('#orderdtl_table tbody').on('click','tr',function(event){
    	//window.alert("row clicked ");
    	var lookupDlrCustId  = orderDtlTable.fnGetData(this)[0];
       	var lookupControlNbr = orderDtlTable.fnGetData(this)[1];
       	var lookupLineNbr    = orderDtlTable.fnGetData(this)[2];
    	//window.alert("Customer id and Order Nbr clicked is " + lookupDlrCustId + " " + lookupControlNbr
    	//		+ " " + lookupLineNbr);
    	document.forms[0].lookupDlrCust.value = lookupDlrCustId;
    	document.forms[0].lookupControlNbr.value = lookupControlNbr;
    	document.forms[0].lookupLineNbr.value = lookupLineNbr;
    	document.forms[0].submit();
    });
    $("#orderdtl_table").addClass("table-hover");
});

