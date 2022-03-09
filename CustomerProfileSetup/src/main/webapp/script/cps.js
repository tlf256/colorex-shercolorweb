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
	
	$(document).on("change", ".clrntdefault, .clrntid", function(){
		try{
			var clrntSysIds = $('.clrntid:checked');
			var clrntSysDefault = $(".clrntdefault:checked").val();
			
			if(!clrntSysIds.length) {
				throw "Please choose colorant system(s)";
			}
			
			console.log("clrnt sys length is " + clrntSysIds.length);
			console.log("selected default is " + clrntSysDefault);
			
			var count = 0;
			
			clrntSysIds.each(function(){
				var clrntid = $(this).parent().text().trim();
				console.log("clrnt id is " + clrntid);
				if(clrntSysDefault != null && clrntid != clrntSysDefault) {
					count++;
				}
			});
			
			console.log("clrntsys id count is " + count);
			
			if(count == clrntSysIds.length) {
				// a default has not been chosen for
				// one of the selectd colorant systems
				count = 0; // reset counter
				throw "Please choose a default from selected colorant system(s)";
			}
			
			if($("#clrntsyserror").is(":visible")) {
				$("#clrntsyserror").text("");
			}
			$("#formerror").text("");
			$(this).removeClass("border-danger");
			valid = true;
		}catch(msg){
			if($("#clrntsyserror").is(":visible")) {
				$("#clrntsyserror").text(msg);
			} else {
				$("#formerror").text(msg);
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
			}
			$(this).addClass("border-danger");
			valid = false;
		}
	});
	
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