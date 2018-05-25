var jobTable;

$(document).ready(function() {
	jobTable = $('#job_table').DataTable({
		dom: 'ifBrtp',
		buttons : [
            'copy', 'csv', 'excel', 'print'
        ],
		"emptyTable" : "No jobs available",
        "ordering": true,
        "order": [ 0, 'desc' ],
        "paginate": false,
        "scrollY" : 500,
        "pagingType": "full"
    });
    
    $('#job_table tbody').on('click','tr',function(event){
    	//window.alert("row clicked ");
    	var lookupControlNbr = jobTable.row(this).data()[0];
    	//window.alert("job number clicked is " + lookupControlNbr);
    	document.forms[0].lookupControlNbr.value = lookupControlNbr;
    	document.forms[0].submit();
    });
    $("#job_table").addClass("table-hover");
});



//function displayJobTable(){
//	$('#job_table').dataTable({
//			"emptyTable" : "No jobs available",
//	        "ordering": true,
//	        "paginate": false,
//	        "pagingType": "full"
//	});
// 
//}

//$(document).ready(function() {
//	displayJobTable()
//});

