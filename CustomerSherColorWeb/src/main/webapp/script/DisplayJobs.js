var jobTable;

$(document).ready(function() {
	jobTable = $('#job_table').DataTable({
		dom: 'ifBrtp',
		buttons : [
			{ extend: 'copy',
            	exportOptions: {
            		stripHtml: true,
            		columns: [0,1,2,3,4,5,6,7,8,10,11,12,14]
            	},
            },
			{ extend: 'csv',
            	exportOptions: {
            		stripHtml: true,
            		columns: [0,1,2,3,4,5,6,7,8,10,11,12,14]
            	},
            },
			{ extend: 'excel',
            	exportOptions: {
            		stripHtml: true,
            		columns: [0,1,2,3,4,5,6,7,8,10,11,12,14]
            	},
            },
            { extend: 'print',
            	exportOptions: {
            		stripHtml: false,
            		columns: [0,1,2,3,4,5,6,7,8,10,11,12,13]
            	},
            },
        ],
        
		"emptyTable" : "No jobs available",
        "ordering": true,
        "order": [ 0, 'desc' ],
        "paginate": false,
        "scrollY" : 500,
        "scrollX": true,
        "pagingType": "full",
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
