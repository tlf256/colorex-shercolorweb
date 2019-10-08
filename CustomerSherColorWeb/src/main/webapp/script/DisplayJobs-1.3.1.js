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
    	document.getElementById('controlNbr').value = lookupControlNbr;
    	document.getElementById('mainForm').submit();
    });
    $("#job_table").addClass("table-hover");
    
    $('#job_table tbody').on({
    	mouseenter: function(){
    		$("#job_table").removeClass("table-hover");
    	},
    	mouseleave: function(){
    		$("#job_table").addClass("table-hover");
    	}
    }, '.dltrow');
    
    var deleteRow;
    var controlNbr;
    
    $('#job_table tbody').on('click', '.dltrow', function(event){
    	deleteRow = $(this).closest('tr');
    	controlNbr = jobTable.row(deleteRow).data()[0];
    	//console.log("controlNbr " + controlNbr);
    	$('#deletemodal').modal().show();
    	event.stopPropagation();
    });
    
    $('#yesbtn').on('click', function(){
    	var reqGuid = $('#guid').val();
    	$.ajax({
			url:"deleteJobAction.action",
			data:{"controlNbr": controlNbr, "reqGuid": reqGuid},
			type: "POST",
			dataType:"json",
			success:function(){
				console.log("Success");
			},
			error:function(request, status, error){
				console.log("Error: " + status + " (" + error + ")");
			}
		});
    	jobTable.row(deleteRow).remove().draw();
    	$('#dltmsg').removeClass('d-none');
    	$('#dltmsg').text("Job " + controlNbr + " deleted successfully");
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