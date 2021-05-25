var jobTable;
var valid = true;

$(document).ready(function() {
	
	var match = $.urlParam('match');
	//$("#listJobsAction_formulaUserCorrectAction")
	var exportColList = $("#listJobsAction_exportColList").val();
	var columnList = exportColList.split(',').map(function(item) {
		var result = parseInt(item, 10);
		if (isNaN(result)) {
			return 0;
		} else {
			return result;
		}
	});
	
	$('#fdate').datepicker({
		defaultDate: null
	});
	
	$('#tdate').datepicker({
		defaultDate: null
	});
	
	jobTable = $('#job_table').DataTable({
		columnDefs: [
			{
				targets: [columnList[columnList.length-1]], render: function(data, type, row){
					return data.split(" | ").join("  <br/>");
				}
			}
		],
		dom: 'ifBrtp',
		buttons : [
			{ extend: 'copy',
            	exportOptions: {
            		stripHtml: true,
            		columns: columnList
            	},
            },
			{ extend: 'csv',
            	exportOptions: {
            		stripHtml: true,
            		columns: columnList
            	},
            },
			{ extend: 'excel',
            	exportOptions: {
            		stripHtml: true,
            		columns: columnList
            	},
            },
            { extend: 'print',
            	exportOptions: {
            		stripHtml: false,
            		columns: columnList
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
        /*"columnDefs": [
        	{
        		"targets": "_all",
        		"data": null,
				"render": function(data, type, row){
					console.log(data);
					console.log(type);
					console.log(row);
					if(type === 'display'){
						var str = $('td').text();
						return $.parseHTML(str);
					}
					return data;
				}
            }
        ],*/
        "language": {
        	"emptyTable" : i18n['displayJobs.noJobsAvailable']
        },
        "ordering": true,
        "order": [ 0, 'desc' ],
        "paginate": false,
        "scrollY" : 500,
        "scrollX": true,
        "pagingType": "full",
    });
    
    console.log('match is ' + match);
    
    // display the job search filter modal unless match is true
    // and in this case the jobs have already been filtered
    if(match != null && match == "true"){
    	$('#mainForm').attr('action', 'selectColorMatchAction');
    	$('#title').text(i18n['compareColors.chooseFirstSample']);
    	$('#searchmodal').modal('hide');
    } else {
    	console.log("pathname is " + window.location.pathname);
    	if(window.location.pathname == '/CustomerSherColorWeb/startNewJob.action' || 
    			window.location.pathname == '/CustomerSherColorWeb/searchJobsAction.action'){
    		$('#searchmodal').modal('show');
    	}
    }
	
	/*var cell = jobTable.cell(this);
	var celldata = cell.data();
	var enteredValue = jobTable.cells(".idNumber").data();
	var index = 0;*/
	
	/*$(".idNumber").each(function(){
		$(this).html($(this).text());
	});*/
	
	/*$.fn.dataTable.render.encodedHtml = function(){
		return function(data, type, row){
			console.log(data);
			console.log(type);
			console.log(row);
			if(type === 'display'){
				var str = data.toString();
				return $('td').html(str);
			}
			return data;
		};
	};*/
    
    /*validation for job search form*/
    $('#cntrlnbr').on('blur focusout', function(){
    	var controlNbr = $(this).val();
    	var parsedCntrlNbr = parseInt(controlNbr);
    	//console.log("parsed control number is " + parsedCntrlNbr);
    	try {
    		if(controlNbr && Number.isNaN(parsedCntrlNbr)) {
    			console.log('Control number is Not a Number');
    			throw i18n['displayJobs.controlNbrMustBeInteger'];
    		}
    		removeWarningPopover();
    		$('#cntrlnbr').removeClass('border-danger');
    		$('#searchError').text('');
    	} catch(msg) {
    		addWarningPopover('#cntrlnbr', msg);
    		valid = false;
    	}
    });
    
    $('#job_table tbody').on('click','tr',function(event){
    	//window.alert("row clicked ");
    	var lookupControlNbr = jobTable.row(this).data()[0];
    	//window.alert("job number clicked is " + lookupControlNbr);
    	document.getElementById('controlNbr').value = lookupControlNbr;
    	document.getElementById('mainForm').setAttribute('action', 'selectJobAction');
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
			success:function(data){
				console.log("Success");
				if (data != null){
					$('#dltmsg').removeClass('d-none');
					$('#dltmsg').text(data.deleteSuccessMsg);
				}
			},
			error:function(request, status, error){
				console.log("Error: " + status + " (" + error + ")");
			}
		});
    	jobTable.row(deleteRow).remove().draw();
    });
    
    $('#searchmodal').on('shown.bs.modal', function(){
    	$('.container-fluid').hide();
    });
    
    $('#searchmodal').on('hidden.bs.modal', function(){
    	$('.container-fluid').show();
    	if($('.popover').is(':visible')) {
    		//console.log("popover is visible");
    		removeWarningPopover();
    	}
    });
    
});

//function parses url to get value of specified param name
$.urlParam = function(name){
var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null){
       return null;
    } else{
       return results[1] || 0;
    }
}

//function toggles other textfield
function toggleOther(val){
	if(val == "Other") {
		$('#other').removeClass('d-none');
		$('#roomlist').attr('name', '');
		$('#other').attr('name', 'js.roomUse');
	} else {
		$('#other').addClass('d-none');
		$('#roomlist').attr('name', 'js.roomUse');
		$('#other').attr('name', '');
	}
}

function addWarningPopover(selector,msg){
	$(selector).attr("data-toggle", "popover");
	$(selector).attr("data-placement","left");
	$(selector).attr("data-content", msg);
	$(selector).popover({trigger : 'manual'});
	$(selector).popover('show');
	$('.popover').addClass('border-danger');
	$('.popover-body').addClass('text-danger');
	$(selector).addClass('border-danger');
	$(selector).select();
}

function removeWarningPopover(){
	$('.popover').each(function(){
    	$(this).popover('hide');
        $('input[data-toggle="popover"]').each(function(){
        	$(this).removeAttr('data-placement');
            $(this).removeAttr('data-content');
            $(this).removeAttr('data-toggle');
        });
	});
}

function validate() {
	if(valid) {
		$('#jobSearchForm').submit();
	} else {
		removeWarningPopover();
		$('#searchError').text(i18n['displayJobs.pleaseFixErrors']);
	}
}

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
