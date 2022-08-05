const debugging = false;
var colorListTableVar;
var isSearchSW = true;
var colorTypeValue;

$( document ).ready(function() {
	
	$('#searchBtnSW').on('click', function() {
		$('#searchModalSW').modal('hide');
		$('#colorLookupResultModal').modal('show');
		
		var locId = $('#locatorId').val();
		var colorId = $('#clridSW').val();
		var colorName = $('#clrnmSW').val();
		var colorType = $('input[name|=selectedCoTypes]');
		colorTypeValue = colorType.filter(':checked').val();
		isSearchSW = true;
		
		if(typeof colorListTableVar !== 'undefined') {
			$('#colorListTable').DataTable().clear();
			$('#colorListTable').DataTable().destroy();
		} else {
			$('#colorListTableDiv').append('<table id="colorListTable" class="table center table-striped table-bordered w-auto"></table>');
		}
		
		if($('#loadingMsg').is(':hidden')) {
			$('#loadingMsg').show();
		}
		
		// DataTable's header, when using the ScrollY attribute for initialization, will cause
		// the header to be misaligned for an element thats initially hidden. DataTables cannot
		// determine the width of the hidden element when it draws the table.
		// A slight delay allows the modal to be displayed so this width can be calculated.
		setTimeout(function(){
			$.ajax({
				url: 'colorLookupSearchAction.action',
				dataType : "json",
				data: {
					'locatorId' : encodeURIComponent(locId.trim()),
					'colorID' : encodeURIComponent(colorId.trim()),
					'colorName' : encodeURIComponent(colorName.trim()),
					'selectedCoTypes' : encodeURIComponent(colorTypeValue),
					'reqGuid' : $('#reqGuid').val()
				},
				success: function(result){
					log(result);
					createColorSearchResultTable(result);				
				},
				error: function(xhr, statusTxt) {
					log(xhr.status);
					log(xhr.statusText);
				}
			});			
			clearSearchValues();
		}, 200);
		
	});
	
	$('#searchBtnCPNA').on('click', function() {
	    $('#searchModalCPNA').modal('hide');
		$('#colorLookupResultModal').modal('show');
		var colorCompany;
		if($('#competList').is(':visible')) {
			colorCompany = $('#modalCompetList').val();
		} else {
			colorCompany = $('#modalNAList').val();
		}
		var colorId = $('#clridCPNA').val();
		var colorName = $('#clrnmCPNA').val();
		var colorType = $('input[name|=selectedCoTypes]');
		colorTypeValue = colorType.filter(':checked').val();
		isSearchSW = false;
		
		if(typeof colorListTableVar !== 'undefined') {
			$('#colorListTable').DataTable().clear();
			$('#colorListTable').DataTable().destroy();
		} else {
			$('#colorListTableDiv').append('<table id="colorListTable" class="table center table-striped table-bordered w-auto"></table>');
		}
		
		if($('#loadingMsg').is(':hidden')) {
			$('#loadingMsg').show();
		}
		
		setTimeout(function(){
			$.ajax({
				url: 'colorLookupSearchAction.action',
				dataType : "json",
				data: {
					'selectedCompany' : encodeURIComponent(colorCompany.trim()),
					'colorID' : encodeURIComponent(colorId.trim()),
					'colorName' : encodeURIComponent(colorName.trim()),
					'selectedCoTypes' : encodeURIComponent(colorTypeValue),
					'reqGuid' : $('#reqGuid').val()
				},
				success: function(result){
					log(result);
					createColorSearchResultTable(result);				
				},
				error: function(xhr, statusTxt) {
					log(xhr.status);
					log(xhr.statusText);
				}
			});	
			clearSearchValues();
		}, 200);
		
	});
	
});

function createColorSearchResultTable(result) {
	
	if (isSearchSW === true) {
		
		log("Loading SW data table");
		colorListTableVar = $('#colorListTable').DataTable({
		    "scrollY": "50vh",
			"scrollX": true,
			"scrollCollapse": true,
			"paging": false,
			"order": [[0, 'desc']],
			"language": {
				"emptyTable" : i18n['getColor.noColorsAvailable']
				},
				"data": result,
				"columns" : [
					{ "title": i18n['getColor.locatorId'], "data" : "locId" },
					{ "title": i18n['global.colorId'], "data" : "colorId" },
					{ "title": i18n['global.colorName'], "data" : "colorName" },
					{ "title": i18n['getColor.palette'], "data" : "palette" },
					{ "title": i18n['getColor.swatchId'], "data" : "swatchId" },
					{ "title": i18n['getColor.primerId'], "data" : "primerId" }
				]
		});
		
		$('#colorListTable tbody').on('click','tr',function(event){
			var selectedColor = colorListTableVar.row(this).data().colorId;
			log("Color clicked is " + selectedColor);
			$('#partialColorNameOrId').val(selectedColor);
			$('#colorUserNextActionForm').submit();
		});
			
	} else {
		
		log("Loading Compet/Nat. Acct. data table");		
	    colorListTableVar = $('#colorListTable').DataTable({
			"scrollY": "50vh",
			"scrollX": true,
			"scrollCollapse": true,
			"paging": false,
			"language": {
	        	"emptyTable" : i18n['getColor.noColorsAvailable']
				},
			"data": result,
			"columns" : [
				 { "title": i18n['getColor.companyName'], "data" : "colorComp" },
				 { "title": i18n['global.colorId'], "data" : "colorId" },
				 { "title": i18n['global.colorName'], "data" : "colorName" },
				 { "title": i18n['getColor.palette'], "data" : "palette" },
				 { "title": i18n['getColor.swatchId'], "data" : "swatchId" },
				 { "title": i18n['getColor.primerId'], "data" : "primerId" }
			]
		});	    
	    
		$('#colorListTable tbody').on('click','tr',function(event){
			var selectedColor = colorListTableVar.row(this).data().colorId;
			var selectedCompany = colorListTableVar.row(this).data().colorComp;
			log("Company/Color clicked is " + selectedCompany + "/" + selectedColor);
			$("#selectedCompany").attr("value",encodeURIComponent(selectedCompany));
			$('#partialColorNameOrId').val(selectedColor);
			$('#colorUserNextActionForm').submit();
		});
	}
	
	$("#colorListTable").addClass("table-hover");	
	if($('#loadingMsg').is(':visible')) {
		$('#loadingMsg').hide();
	}
	
}

function log(toLog){
	if(debugging) {
		console.log(toLog);
	}
}

function clearSearchValues() {
	$('#locatorId').val('');
	$('#clridSW').val('');
	$('#clrnmSW').val('');
	$('#clridCPNA').val('');
	$('#clrnmCPNA').val('');
	$('#modalCompetList').val('ALL');
	$('#modalNAList').val('ALL');
}