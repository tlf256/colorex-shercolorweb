/**
 * 
 */
$(document).ready(function(){
	$(document).on({
		"keyup":function(){
			var length = $(this).val().length;
			var maxlimit = 150;
			var remaining;
			if(length > maxlimit){
				$(this).val($(this).val().substring(0, maxlimit));
				remaining = maxlimit - length + 1;
			}else{
				remaining = maxlimit - length;
			}
			$("#charcount").text(remaining + " characters remaining");
		},
		"change":function(){
			$(this).attr("placeholder", "150 Character Limit");
		}
		
	}, ".acctcmmnt");
	
	$('#effDate').datepicker({
		//dateFormat: "dd-M-y",
		changeMonth: true,
		changeYear: true,
		gotoCurrent: true
	});
	/*$('#expDate').datepicker({
		//dateFormat: "dd-M-y",
		changeMonth: true,
		changeYear: true,
		gotoCurrent: true
	});*/

});

function buildCustTypesList(custTypes, selectList, selectedOption) {
	console.log("building customer type selection");
	console.log("selected option is " + selectedOption);
	
	selectList.empty();
	
	$.each(custTypes, function(index, value){
		selectList.append('<option>' + value + '</option>');
	});
	
	selectList.val(selectedOption).find('option[value="' + selectedOption + '"]').attr('selected', true);
}

function toggleProfileInput(value){
	console.log("customer type select list value is " + value);
	if(value == 'CUSTOMER') {
		$(".rmbyrm").hide();
		$(".locid").hide();
	} else {
		$(".rmbyrm").show();
		$(".locid").show();
	}
}

function toggleTextArea(value){
	console.log("prod comp restriction " + value);
	if(value == 'true') {
		$(".prodcomps").show();
	} else {
		$(".prodcomps").hide();
		$("#prodcomps").val("");
	}
}

function toggleSelectList(value) {
	console.log("EULA type is " + value);
	switch(value) {
	case 'None':
	case 'Custom EULA':
		$('.eulaTemp').hide();
		$('#code').hide();
		break;
	case 'SherColor Web EULA':
		$('.eulaTemp').hide();
		$('#code').show();
		break;
	case 'Custom EULA Template':
		$('.eulaTemp').show();
		$('#code').show();
		break;
	}
}