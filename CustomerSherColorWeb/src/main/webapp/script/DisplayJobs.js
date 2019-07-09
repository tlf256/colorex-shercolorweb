var jobTable;

$(document).ready(function() {
	jobTable = $('#job_table').DataTable({
		dom: 'ifBrtp',
		buttons : [
			{ extend: 'copy',
            	exportOptions: {
            		stripHtml: true,
            		columns: [0,1,2,3,4,5,6,7,8,10,11,12,13,14]
            	},
            },
			{ extend: 'csv',
            	exportOptions: {
            		stripHtml: true,
            		columns: [0,1,2,3,4,5,6,7,8,10,11,12,13,14]
            	},
            },
			{ extend: 'excel',
            	exportOptions: {
            		stripHtml: true,
            		columns: [0,1,2,3,4,5,6,7,8,10,11,12,13,14]
            	},
            },
            { extend: 'print',
            	exportOptions: {
            		stripHtml: false,
            		columns: [0,1,2,3,4,5,6,7,8,10,11,12,13,14]
            	},
            	customize: function(win)
                {
     
                    var last = null;
                    var current = null;
                    var bod = [];
     
                    var css = '@page { size: landscape; }',
                        head = win.document.head || win.document.getElementsByTagName('head')[0],
                        style = win.document.createElement('style');
     
                    style.type = 'text/css';
                    style.media = 'print';
     
                    if (style.styleSheet)
                    {
                      style.styleSheet.cssText = css;
                    }
                    else
                    {
                      style.appendChild(win.document.createTextNode(css));
                    }
     
                    head.appendChild(style);
             }
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
    
    $('#job_table tbody').on('click', '.dltrow', function(){
    	//var controlNbr = jobTable.row(this).data()[0];
    	var deleteRow = $(this).closest('tr');
    	var controlNbr = deleteRow.data()[0];
    	document.forms[0].lookupControlNbr.value = controlNbr;
    	document.forms[0].submit();
    	window.alert("controlNbr " + controlNbr);
    	jobTable.remove(deleteRow).draw();
    });
    
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
