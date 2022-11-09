let intExtList = new Set();
let qualityList = new Set();
let sheenList = new Set();
let sizeList = new Set();

//Column indices of related values in the datatable
const SALESNBR_INDEX = 0;
const SIZE_INDEX = 7;
const SHEEN_INDEX = 6;
const COMPOSITE_INDEX = 5;
const QUALITY_INDEX = 4;
const INTEXT_INDEX = 3;
const BASE_INDEX = 2;
const PRODUCT_INDEX = 1;

$(document).ready(function() {

	//Create the datatable and call a service to load in all products for a given base
	let dt = initializeDataTable();
    loadProducts(dt);

    //Dropdown option list functionality
	intExtListSelected(dt);
	qualityListSelected(dt);
	sheenListSelected(dt);
	sizeListSelected(dt);

	productLookupModal();
});

/**
 * Initializes functionality of the datatable
 * @returns The datatable object
 */
function initializeDataTable() {

   let dt = $('#productLookupTable').DataTable({
    	"lengthMenu": [[5, 25, -1], [5, 25, "All"]],
//    	"lengthChange": false,
    	"info": false,
        'columnDefs': [{
            'targets': "_all",
	        'createdCell': function(td, cellData, rowData, row, col) {
	        	//Attach the salesNbrCell class to the cell that contains cell number, this makes it easy to grab the salesNbr when a row is selected
	            if (col === SALESNBR_INDEX) {
	                $(td).attr('class', 'salesNbrCell');
	            }
	          }
         }],
    });

    dt.on('click', 'tr', function () {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        } 
        else if (!$(this).hasClass('headerRow')){
        	dt.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
    		$('#productLookupModal').modal('hide');
    		$('#partialProductNameOrId').val(dt.$('tr.selected').find("td.salesNbrCell").html());
        }
    });

	return dt;
}

/**
 * IntExt Dropdown box functionality
 * @param dt The datatable related to the dropdown
 */
function intExtListSelected(dt) {
	$('#intExtList').change(function() {

		//Resets other lists as we are enforcing an order on the dropdown order
		$('#qualityList').prop("disabled", true);
		$('#sheenList').prop("disabled", true);
		$('#sizeList').prop("disabled", true);

		$('#qualityList option[value="ALL"]').prop('selected', true);
		$('#sheenList option[value="ALL"]').prop('selected', true);
		$('#sizeList option[value="ALL"]').prop('selected', true);

		let selectedIntExt = $(this).val();

		$.fn.dataTable.ext.search.pop();

	    $.fn.dataTable.ext.search.push(
	      function(settings, data, dataIndex) {
	          return (data[INTEXT_INDEX] == selectedIntExt || selectedIntExt == 'ALL');
	        }
	    );
	    dt.draw();

	    qualityList = new Set();

		dt.rows({filter: 'applied'}).every(function(){
		    let quality = this.data()[QUALITY_INDEX];
		    qualityList.add(quality);
		});

		createOptionsForList($("#qualityList"),qualityList);

		if (selectedIntExt != 'ALL') {
			$('#qualityList').prop("disabled", false);
		}
	});
}

/**
 * Quality Dropdown box functionality
 * @param dt The datatable related to the dropdown
 */
function qualityListSelected(dt) {
	$('#qualityList').change(function() {

		//Resets other lists as we are enforcing an order on the dropdown order
		$('#sheenList').prop("disabled", true);
		$('#sizeList').prop("disabled", true);

		$('#sheenList option[value="ALL"]').prop('selected', true);
		$('#sizeList option[value="ALL"]').prop('selected', true);

		let selectedQuality = $(this).val();
		let selectedIntExt = $('#intExtList').val();

		//Unfilter the table
		$.fn.dataTable.ext.search.pop();

		//Filter the table with the previously selected values + newly selected quality list
	    $.fn.dataTable.ext.search.push(
	      function(settings, data, dataIndex) {
	          return ((data[INTEXT_INDEX] == selectedIntExt) && (data[QUALITY_INDEX] == selectedQuality || selectedQuality == 'ALL'));
	      }
	    );
	    dt.draw();

	    sheenList = new Set();

		dt.rows({filter: 'applied'}).every(function(){
		    let sheen = this.data()[SHEEN_INDEX];
		    sheenList.add(sheen);
		});

		createOptionsForList($("#sheenList"),sheenList);

		if (selectedQuality != 'ALL') {
			$('#sheenList').prop("disabled", false);
		}
	});
}

/**
 * Sheen Dropdown box functionality
 * @param dt The datatable related to the dropdown
 */
function sheenListSelected(dt) {
	$('#sheenList').change(function() {

		//Resets other lists as we are enforcing an order on the dropdown order
		$('#sizeList').prop("disabled", true);
		$('#sizeList option[value="ALL"]').prop('selected', true);

		let selectedSheen = $(this).val();
		let selectedIntExt = $('#intExtList').val();
		let selectedQuality = $('#qualityList').val();

		//Unfilter the table
		$.fn.dataTable.ext.search.pop();

		//Filter the table with the previously selected values + newly selected sheen list
	    $.fn.dataTable.ext.search.push(
	      function(settings, data, dataIndex) {
	          return ((data[INTEXT_INDEX] == selectedIntExt) && (data[QUALITY_INDEX] == selectedQuality) && (data[SHEEN_INDEX] == selectedSheen || selectedSheen == 'ALL'));
	        }
	    );
	    dt.draw();

	    sizeList = new Set();
		dt.rows({filter: 'applied'}).every(function(){
		    let size = this.data()[SIZE_INDEX];
		    sizeList.add(size);
		});

		createOptionsForList($("#sizeList"),sizeList);

		if (selectedSheen != 'ALL') {
			$('#sizeList').prop("disabled", false);
		}
	});
}

/**
 * Size Dropdown box functionality
 * @param dt The datatable related to the dropdown
 */
function sizeListSelected(dt) {
	$('#sizeList').change(function() {

		let selectedSize = $(this).val();
		let selectedSheen = $('#sheenList').val();
		let selectedIntExt = $('#intExtList').val();
		let selectedQuality = $('#qualityList').val();

		//Unfilter the table
		$.fn.dataTable.ext.search.pop();

		//Filter the table with the previously selected values + newly selected size list
	    $.fn.dataTable.ext.search.push(
	      function(settings, data, dataIndex) {
	          return ((data[INTEXT_INDEX] == selectedIntExt) && (data[QUALITY_INDEX] == selectedQuality) && 
	        		  (data[SHEEN_INDEX] == selectedSheen) && (data[SIZE_INDEX] == selectedSize || selectedSize == 'ALL'));
	        }
	    );
	    dt.draw();
	});
}

/**
 * Adds functionality to the product lookup button to open the modal
 */
function productLookupModal() {
	$("#prodLookupBtn").click(function(){ 
		$('#productLookupModal').modal('show');
	});
}

/**
 * Load datatable with all products for given base
 * @param dt The datatable to load values into
 */
function loadProducts(dt){
	$.ajax({	
		url : "loadProductLookupAction.action",
		dataType : "json",
		data : {
			"reqGuid" : $('#reqGuid').val()  
			},
		success : function(data){
			if(data.sessionStatus === "expired") {
        		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
        	}

			$.each(data.productList, function(index, val) {
			    dt.row.add([
			    	val.salesNbr,
			    	val.prepComment.substring(0,9), 
			    	val.base,
			    	val.intExt,
			    	val.quality,
			    	val.composite,
			    	val.finish,
			    	val.prepComment.substring(10,12)
		    	]).draw();
			});

			//Load IntExt list for selection
			dt.rows().every(function(){
			    let intExt = this.data()[INTEXT_INDEX];
			    intExtList.add(intExt);
			});

			createOptionsForList($("#intExtList"),intExtList);
		}
	});
}

/**
 * Maps the values from the jsList to the DOM list by adding an <option> for each record
 * @param domList
 * @param jsList
 */
function createOptionsForList(domList, jsList) {

	//Resets the DOM list with the initial value of ALL
	domList
    .empty()
    .append('<option selected="selected" value="ALL">ALL</option>');

	//Convert the Set to a List in order to sort in Alphabetical order
	Array.from(jsList).sort().forEach(function (item) {
		domList.append($('<option>', { 
	        value: item,
	        text : item 
	    }));
	});
}