let jobTable;
let valid = true;

$(document).ready(function() {
	const displayTintQueue = $("#listJobsAction_displayTintQueue").val();
	if (displayTintQueue) {
		
		const userLocale = "${session['WW_TRANS_I18N_LOCALE']}";
		let langUrl = null;
		// translate datatable text and make it sortable by that locale's date format 
		switch(userLocale){
			case("es_ES"):
				langUrl = "//cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json";
				$.fn.dataTable.moment('DD-MMM-YYYY H:mm:ss', 'es');
				break;
			case("zh_CN"):
				langUrl = "//cdn.datatables.net/plug-ins/1.10.21/i18n/Chinese.json";
				$.fn.dataTable.moment('YYYY-M-D H:mm:ss', 'zh-cn');
				break;
			default:
				$.fn.dataTable.moment('MMM D, YYYY h:mm:ss A', 'en');
		}
	}

	const exportColList = $("#listJobsAction_exportColList").val();
	const columnList = exportColList.split(',').map(function(item) {
		const result = parseInt(item, 10);
		if (isNaN(result)) {
			return 0;
		} else {
			return result;
		}
	});
	
	$('#fdate').datepicker({
		defaultDate: null,
		onClose: function(input, obj){
			const dateInput = $(this).val();
			console.log("from date is " + dateInput);
			validateDateFormat($(this), dateInput);
		}
	});
	
	$('#tdate').datepicker({
		defaultDate: null,
		onClose: function(input, obj){
			const dateInput = $(this).val();
			console.log("to date is " + dateInput);
			validateDateFormat($(this), dateInput);
		}
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
     
                    let last = null;
                    let current = null;
                    let bod = [];
     
                    let css = '@page { size: landscape; }',
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
            {
            	text: i18n['displayJobs.newSearch'],
            	action: function(){
            		showSearchModal()
            	},
            	attr:{
            		id: 'newSearch',
            		class: 'btn btn-primary'
            	}
            }
        ],
        "language": {
        	"emptyTable" : i18n['displayJobs.noJobsAvailable'],
        	"search": i18n['displayJobs.filterColon']
        },
        "ordering": true,
        "order": [ 0, 'desc' ],
        "paginate": false,
        "scrollY" : 500,
        "scrollX": true,
        "pagingType": "full"
    });
    
    let newSearchBtn = jobTable.buttons(['#newSearch']);
	const matchStandard = $.urlParam('matchStandard');
	
	console.log('matchStandard is ' + matchStandard);

    // display the job search filter modal unless matchStandard is true
    // and in this case the jobs have already been filtered
    if(matchStandard != null && matchStandard == "true"){
    	$('#searchmodal').modal('hide');
    	newSearchBtn.disable();
    }
    
    if (displayTintQueue) {
    	$(".dt-buttons").hide();
    }
    
    // prevent enter key from being used on text imput, except for scanner
    $(document).on({
    	keypress: function(event) {
    		console.log("enter keypress on text input");
	    	if (event.keyCode == 13) {
				event.preventDefault();
				console.log("this is " + $(this).attr("id"));
				if($(this).attr("id") == 'cntrlnbr') {
					const inputStr = $('#cntrlnbr').val();
		    		if(inputStr.includes("-")) {
		    			const strArr = inputStr.split("-");
		    			const controlNbr = strArr[0];
		            	const lineNbr = strArr[1];
			            $('#controlnbr').val(controlNbr);
			    		$('#linenbr').val(lineNbr);
			    		$('#jobSearchForm').submit();
		    		}
				}
			}
    	}
	}, "input:text");
    
    $(document).on('blur', "#cntrlnbr", function(){
		console.log("blur event");
		const controlNbr = $(this).val();
    	const parsedCntrlNbr = parseInt(controlNbr);
    	console.log("parsed control number is " + parsedCntrlNbr);
    	try {
    		if(controlNbr && Number.isNaN(parsedCntrlNbr)) {
    			console.log('Control number is Not a Number');
    			throw i18n['displayJobs.controlNbrMustBeInteger'];
    		}
    		removeWarningPopover();
    		$('#cntrlnbr').removeClass('border-danger');
    		$('#searchError').text('');
    		$('#controlnbr').val(controlNbr);
    		valid = true;
    	} catch(msg) {
    		addWarningPopover('#cntrlnbr', msg);
    		valid = false;
    	}
	});
    
    $('#job_table tbody').on('click','tr',function(event){
        const lookupControlNbr = jobTable.row(this).data()[0];
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
    
    let deleteRow;
    let controlNbr;
    
    $('#job_table tbody').on('click', '.dltrow', function(event){
    	deleteRow = $(this).closest('tr');
    	controlNbr = jobTable.row(deleteRow).data()[0];
    	$('#deletemodal').modal().show();
    	event.stopPropagation();
    });
    
    $('#yesbtn').on('click', function(){
    	const reqGuid = $('#guid').val();
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
    	$('#cntrlnbr').focus();
    });
    
    $('#searchmodal').on('hidden.bs.modal', function(){
    	if($('.popover').is(':visible')) {
    		removeWarningPopover();
    	}
    	// Forces a cancellation of the search if you exit out of the search filter modal
    	// User must use the Search button to continue further
    	$('#cancelNewSearch').click();
    });
    
});

//function parses url to get value of specified param name
$.urlParam = function(name){
const results = new RegExp('[?&]' + name + '=([^&#]*)').exec(window.location.href);
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
		$('#other').attr('name', 'thc.roomUse');
	} else {
		$('#other').addClass('d-none');
		$('#roomlist').attr('name', 'thc.roomUse');
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
	$(selector).focus();
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
		$('#searchError').text(i18n['displayJobs.pleaseFixErrors']);
		$('#searchmodal').animate({
			scrollTop: 0
		}, 1000);
	}
}

function showSearchModal() {
	$('#searchmodal').modal('show');
}

function validateDateFormat(selector, value) {
	const regex = new RegExp("[0-9]{2}\/[0-9]{2}\/[0-9]{4}");
	try {
		if(value && !regex.test(value)) {
			console.log('date is wrong format');
			throw i18n['displayJobs.dateMustBeCorrectFormat'];
		}
		removeWarningPopover();
		$(selector).removeClass('border-danger');
		$('#searchError').text('');
		valid = true;
	} catch(msg) {
		addWarningPopover($(selector), msg);
		valid = false;
	}
}