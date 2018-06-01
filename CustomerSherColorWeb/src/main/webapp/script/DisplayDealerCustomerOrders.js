var orderTable;
//window.alert("order_table set-up ");

$(document).ready(function() {
	orderTable = $('#order_table').dataTable({
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
	
    $('#order_table tbody').on('click','tr',function(event){
    	//window.alert("row clicked ");
    	var lookupDlrCustId = "xxxxx";
    	var lookupControlNbr = 0;
    	lookupDlrCustId  = orderTable.fnGetData(this)[0];
       	lookupControlNbr = orderTable.fnGetData(this)[1];
    	//window.alert("Customer id and Order Nbr clicked is " + lookupDlrCustId + " Test2");
    	document.forms[0].lookupDlrCustId.value = lookupDlrCustId;
    	document.forms[0].lookupControlNbr.value = lookupControlNbr;
    	//window.alert("order_table submit " + lookupDlrCustId + " " + lookupControlNbr);
    	document.forms[0].submit();
    	//window.alert(" submit ");
    });
    $("#order_table").addClass("table-hover");
});

