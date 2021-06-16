/**
 * 
 */
$(document).ready(function() {
	
	var result;
	
	$("#loginnext-btn").prop("disabled", true);
	
	hideInput();
		
	$(document).on("blur", "#swuititle", function(){
		var title = $.trim($("#swuititle").val());
		try{
			if(title.length > 20 || title.length == 0){
				throw "Please enter a Customer Name not greater than 20 characters";
			} 
			$("#swuititlerror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("#swuititlerror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
	
	$(document).on("blur", "#cdsadlfld", function(){
		var info = $.trim($("#cdsadlfld").val());
		try{
			if(info.length > 20){
				throw "Additional Info cannot be greater than 20 characters";
			}
			$("#cdsadlflderror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		} catch(msg) {
			$("#cdsadlflderror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
	
	$(document).on("change", ".clrntid, .clrntdefault", function(){
		try{
			if($("#CCEdefault").is(":checked") && !$("#CCE").is(":checked")){
				throw "Please choose CCE colorant system before selecting the default";
			}
			if($("#BACdefault").is(":checked") && !$("#BAC").is(":checked")){
				throw "Please choose BAC colorant system before selecting the default";
			}
			if($("#844default").is(":checked") && !$("#844").is(":checked")){
				throw "Please choose 844 colorant system before selecting the default";
			}
			if($("#CCEdefault").is(":checked") && !$("#CCE").is(":checked")){
				throw "Please choose CCE colorant system";
			}
			if($("#BACdefault").is(":checked") && !$("#BAC").is(":checked")){
				throw "Please choose BAC colorant system";
			}
			if($("#844default").is(":checked") && !$("#844").is(":checked")){
				throw "Please choose 844 colorant system";
			}
			$("#clrntsyserror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
			$("#loginnext-btn").prop("disabled", false);
		}catch(msg){
			$("#clrntsyserror").text(msg);
			$(this).addClass("border-danger");
			$(this).prop("checked", false);
		}
	});
		
	$(document).on("blur", "#acceptcode", function(){
		try{
			var acceptcodeval = $.trim($("#acceptcode").val());
			if(acceptcodeval.length != 6){
				throw "Acceptace code is 6 digits";
			}
			$("#eulaerror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("#eulaerror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
	
	$(document).on("click", "#loginnext-btn", function(){
		try{
			if((!$("#CCE").is(":checked") && !$("#BAC").is(":checked") && !$("#844").is(":checked")) || 
					(!$("#CCEdefault").is(":checked") && !$("#BACdefault").is(":checked") && !$("#844default").is(":checked"))){
				throw "Please correct the colorant system(s)";
			}
			var acceptcodeval = $.trim($("#acceptcode").val());
			var eula = $("#eulalist").val();
			if(eula != 'None' && !acceptcodeval){
				throw "Please enter an Acceptance Code";
			}
			if(eula == 'None' && acceptcodeval){
				throw "Please choose a EULA";
			}
			if($("#ntlaccterror").text()!="" || $("#intntlaccterror").text()!="" || $("#swuititlerror").text()!="" 
				|| $("#cdsadlflderror").text()!="" || $("#clrntsyserror").text()!="" || $("#eulaerror").text()!=""){
				throw "Please fix form error(s)";
			}
			$("#formerror").text("");
		}catch(msg){
			event.preventDefault();
			$("#formerror").text(msg);
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
		}
	});
	
});

function validateAcctNbr(selector) {
	var acctnbr = selector.val();
	var selectorError;
	try{
		if(selector.attr('id') == "ntlacctnbr") {
			selectorError = $("#ntlaccterror");
			if(selector.is(":visible") && (acctnbr.length!=9 || isNaN(acctnbr))){
				throw "Please enter a 9 digit account number";
			}
		} else {
			selectorError = $("#intntlaccterror");
			if(selector.is(":visible") && (acctnbr.length!=7 || isNaN(acctnbr))){
				throw "Please enter a 7 digit account number";
			}
		}
		if(selector.is(":visible") && result=="true"){
			throw "Account Number already exists";
		}
		$(selector).removeClass("border-danger");
		$(selectorError).text("");
		$("#formerror").text("");
		showHiddenInput();
		showHideCustTypeOptions(acctnbr);
	}catch(msg){
		$(selector).addClass("border-danger");
		$(selector).select();
		$(selectorError).text(msg);
	}
}

function checkAccountNbr() {
	var selector = $(".acctnbr:visible");
	var value = selector.val().trim();
	console.log("account number is " + value);
	$.ajax({
		url:"ajaxAcctNbrResult.action",
		data:{acctNbr: value},
		dataType:"json",
		async: false,
		success:function(data){
			result = data.result;
			console.log("result is " + data.result);
			console.log("Ajax success: custId is " + value);
			validateAcctNbr(selector);
		},
		error:function(request, status){
			console.log("Ajax error: " + status);
			alert(status + ": could not validate account number. Please retry.");
		}
	});
}

function showHideCustTypeOptions(custId) {
	console.log("show/hide cust type option(s)");
	if(custId != null && custId != 'undefined') {
		console.log("custId is not null or undefined");
		if(custId.length == 6) {
			if(!custId.startsWith('7')) {
				$("#typelist option[value='STORE']").hide();
				console.log("custId is 6 digits, but is not a cost center");
				console.log("STORE option hidden");
			}
		} else {
			$("#typelist option[value='STORE']").hide();
			console.log("custId not 6 digit cost center");
			console.log("STORE option hidden");
		}
	} else {
		console.log("custId is null or undefined");
	}
}

function showHideInput(value) {
	clearAllErrors();
	switch(value) {
	case 'natlWdigits':
		hideInput();
		showNtlAcctInput();
		break;
	case 'intnatlWdigits':
		hideInput();
		showIntnatlAcctInput();
		break;
	case 'natlWOdigits':
	case 'intnatlWOdigits':
		clearAcctNbr();
		showHiddenInput();
		break;
	default:
		hideInput();
	}
}

function showNtlAcctInput() {
	$(".ntlacct").show();
	$("#intntlacct").hide();
	$(".intntlacct").val('');
	$("#cont").show();
	$("#ntlacctnbr").focus();
}

function showIntnatlAcctInput() {
	$(".ntlacct").hide();
	$("#ntlacct").val('');
	$(".intntlacct").show();
	$("#cont").show();
	$("#intntlacctnbr").focus();
}

function showHiddenInput() {
	$(".cstmrnm").show();
	$("#cdsadlfld").show();
	$(".clrnt").show();
	$("#loginnext-btn").show();
	$("#custtype").show();
	$(".eula").show();
	$("#cont").hide();
	$("#swuititle").focus();
}

function hideInput() {
	$(".ntlacct").hide();
	$(".intntlacct").hide();
	$("#cont").hide();
	$(".cstmrnm").hide();
	$("#cdsadlfld").hide();
	$(".clrnt").hide();
	$("#loginnext-btn").hide();
	$("#custtype").hide();
	$(".rmbyrm").hide();
	$(".locid").hide();
	$(".eula").hide();
}

function clearAcctNbr() {
	$("#ntlacct").hide();
	$("#ntlacct").val('');
	$("#intntlacct").hide();
	$("#intntlacct").val('');
	$("#cont").hide();
}

function clearAllErrors() {
	$('.errorfld').each(function(){
		$(this).text('');
	});
	
	$('input:text').each(function(){
		$(this).removeClass('border-danger');
	});
}

function toggleProfileInput(value){
	if(value == 'CUSTOMER') {
		$(".rmbyrm").hide();
		$(".locid").hide();
	} else {
		$(".rmbyrm").show();
		$(".locid").show();
	}
}