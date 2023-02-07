var valid = true;
const debugging = false;
var spectroTableVar;

$( document ).ready(function() {
	$('#spectroSearchModal').modal('show');
	
	//Initialize date pickers
	$('#fdate').datepicker({
		defaultDate: "-1m",
		onClose: function(){
			var dateInput = $(this).val();
			log("from date is " + dateInput);
			validateDateFormat($(this), dateInput, 'fdate');
		}
	});
	
	$('#tdate').datepicker({
		defaultDate: 0,
		onClose: function(){
			var dateInput = $(this).val();
			log("to date is " + dateInput);
			validateDateFormat($(this), dateInput, 'tdate');
		}
	});
	
	//Set default dates in the to/from fields.
	$('#fdate').datepicker({
		dateFormat: "mm/dd/yyyy"
	}).datepicker("setDate", "-1m");
	
	$('#tdate').datepicker({
		dateFormat: "mm/dd/yyyy"	
	}).datepicker("setDate", "0");
	
	$('#modalCloseBtn').on('click', function(){
		if(typeof spectroTableVar !== 'undefined') {
			$('#spectroSearchModal').modal('hide');
		} else {
			$('#cancelBtn').click();
		}	
	});
	
	$('#searchbtn').on('click', function(){
		var cmd = $('#spectroCommands').val();
		var fromDate = $('#fdate').val();
		var toDate = $('#tdate').val();
		if(valid) {
			
			if(typeof spectroTableVar !== 'undefined') {
				$('#spectroListTable').DataTable().clear();
				$('#spectroListTable').DataTable().destroy();
				$('#spectroListTable').empty();
			} else {
				$('#spectroLogTableContainer').append('<table id="spectroListTable" class="table center table-striped table-bordered w-auto"></table>');
			}
			
			if($('#loadingMsg').css("visibility") === "hidden") {
				$('#loadingMsg').css('visibility', 'visible');
			}
			
		    log("spectroCommand: " + cmd.trim());
			log("fromDate: " + fromDate.trim());
			log("toDate: " + toDate.trim());
			log("reqGuid: " + $('#guid').val());
			$.ajax({
				url: 'spectroLogLookupAction.action',
				dataType : "json",
				data: {
					'spectroCommand' : encodeURIComponent(cmd.trim()),
					'fromDate' : encodeURIComponent(fromDate.trim()),
					'toDate' : encodeURIComponent(toDate.trim()),
					'reqGuid' : $('#guid').val()
				},
				success: function(result){
					log(result);
					createSpectroSearchResultTable(result);				
				},
				error: function(xhr, statusTxt) {
					log(xhr.status);
					log(xhr.statusText);
					log(statusTxt);
				}
			});
			$('#spectroSearchModal').modal('hide');
		}
	});	
});

function createSpectroSearchResultTable(result) {
	log("Loading Spectro data table");
		spectroTableVar = $('#spectroListTable').DataTable({
			dom: 'Bftip',
			buttons : [ 
				{
            		text: i18n['displaySpectroLog.newSearch'],
            		action: function(){
            			$('#spectroSearchModal').modal('show');
            		},
            		attr:{
            			id: 'newSearch',
            			class: 'btn btn-primary'
            		}
            	},
            	{
            		text: i18n['global.back'],
            		action: function(){
            			$('#cancelBtn').click();
            		},
            		attr:{
            			id: 'backBtn',
            			class: 'btn btn-secondary'
            		}
            	}
			],
			"scrollY": "50vh",
			"scrollX": true,
			"scrollCollapse": true,
			"paging": true,
			"language": {
				"emptyTable" : i18n['displaySpectroLog.noSpectroEventsAvailable']
				},
				"data": result,
				"columns" : [
					{ "title": i18n['displaySpectroLog.spectroModel'], "data" : "spectroModel" },
					{ "title": i18n['displaySpectroLog.spectroSerialNbr'], "data" : "spectroSerialNbr" },
					{ "title": i18n['displaySpectroLog.spectroCommand'], "data" : "spectroCommand" },
					{ "title": i18n['displaySpectroLog.requestTime'], "data" : "requestTime" },
					{ "title": i18n['displaySpectroLog.responseCode'], "data" : "responseCode" },
					{ "title": i18n['displaySpectroLog.responseMessage'], "data" : "responseMsg" }
				]
		});
		
		if($('#loadingMsg').css("visibility") === "visible") {
			$('#loadingMsg').css('visibility', 'hidden');
		}

}

function validateDateFormat(selector, value, id) {
	log('id is ' + id);
	var regex = new RegExp("[0-9]{2}\/[0-9]{2}\/[0-9]{4}");
	try {
		if(value && !regex.test(value)) {
			log('date is wrong format');
			throw i18n['displayJobs.dateMustBeCorrectFormat'];
		}
		
		if(id === 'fdate') {
			log('from date was edited');
			if(value.trim() !== '') {
				$('#tdate').datepicker('option', 'minDate', value);
			} else {
				$('#tdate').datepicker('option', 'minDate', null);
			}
		}
		
		if(id === 'tdate') {
			log('to date was edited');
			if(value.trim() !== '') {
				$('#fdate').datepicker('option', 'maxDate', value);
			} else {
				$('#fdate').datepicker('option', 'maxDate', null);
			}
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

function log(toLog){
	if(debugging) {
		console.log(toLog);
	}
}