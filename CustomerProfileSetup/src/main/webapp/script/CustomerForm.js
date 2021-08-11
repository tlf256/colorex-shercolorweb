/**
 * 
 */

var valid;

$(document).ready(function() {
	
	var result;
	
	//$("#loginnext-btn").prop("disabled", true);
	
	hideInput();
	
	$(document).on({
		"blur": function(){
		var info = $.trim($("#cdsadlfld").val());
			try{
				if(info.length > 20){
					throw "Additional Info cannot be greater than 20 characters";
				}
				$("#cdsadlflderror").text("");
				$("#formerror").text("");
				$(this).removeClass("border-danger");
				valid = true;
			} catch(msg) {
				$("#cdsadlflderror").text(msg);
				$(this).addClass("border-danger");
				$(this).select();
				valid = false;
			}
		},
		"focusin": function(){
			var selector = $("#swuititle");
			var title = selector.val().trim();
			try{
				if(selector.is(':visible') && (title.length > 20 || title.length == 0)){
					throw "Please enter a Customer Name not greater than 20 characters";
				} 
				$("#swuititlerror").text("");
				$("#formerror").text("");
				selector.removeClass("border-danger");
				valid = true;
			}catch(msg){
				$("#swuititlerror").text(msg);
				selector.addClass("border-danger");
				selector.select();
				valid = false;
			}
		}
	}, "#cdsadlfld");
	
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
			valid = true;
		}catch(msg){
			$("#clrntsyserror").text(msg);
			$(this).addClass("border-danger");
			$(this).prop("checked", false);
			valid = false;
		}
	});
		
	$(document).on("blur", "#acceptcode", function(){
		try{
			var eula = $("#eulalist").val();
			var acceptcodeval = $.trim($("#acceptcode").val());
			if(eula != 'None' && acceptcodeval.length < 6){
				throw "Please enter a 6 digit Acceptance Code";
			}
			if(eula == 'None' && acceptcodeval){
				throw "Please choose a EULA";
			}
			$("#eulaerror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
			valid = true;
		}catch(msg){
			$("#eulaerror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
			valid = false;
		}
	});
	
	var prodComps = $.ajax({
		url:"ajaxProdCompResult.action",
		dataType:"json",
		async: true,
		success:function(data){
			console.log("Ajax success: received prod comps");
			var prodComps = data.prodCompList;
			console.log("prod comps json result: " + data.prodCompList);
			return prodComps;
		},
		error:function(request, status){
			console.log("Ajax error: " + status);
			alert(status + ": could not retrieve prod comps.");
		}
	});
	
	$(document).on("blur", "#prodcomps", function(){
		try {
			if(prodComps != null) {
				var prodCompStr = JSON.stringify(prodComps);
				var prodCompInput = $("#prodcomps").val();
				console.log('prod comp input: ' + prodCompInput);
				var prodCompArr = prodCompInput.split(',');
				
				for(var i = 0; i < prodCompArr.length; i++) {
					var enteredProdComp = prodCompArr[i].trim();
					var prodComp = enteredProdComp.toUpperCase();
					if(!prodCompStr.includes(prodComp)) {
						throw "Invalid prod comp \"" + enteredProdComp + "\", please check spelling and try again.";
					}
				}
			} else {
				throw "Could not validate prod comp(s), please refresh page and try again";
			}
			
			$('#prodcomperror').text('');
			$(this).removeClass("border-danger");
			valid = true;
		} catch(msg) {
			$('#prodcomperror').text(msg);
			$(this).addClass("border-danger");
			$(this).select();
			valid = false;
		}
	});
	
	/*$(document).on("click", "#loginnext-btn", function(){
		try{
			if($("#swuititle").val() == '') {
				throw "Customer Name is required";
			}
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
			$("#formerror").text(msg);
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
		}
	});*/
	
});

function validate() {
	try {
		if(!valid) {
			throw "Please fix form error(s)";
		}
		$("#formerror").text('');
		$('#customerInfo').submit();
	} catch(msg) {
		$("#formerror").text(msg);
		$("html, body").animate({
			scrollTop: $(document.body).offset().top
		}, 1500);
	}
}

function checkIfWarningNeeded(acctnbr) {
	console.log("checking if warning should be shown...");
	if(acctnbr >= '400000000' && acctnbr <= '400000012') {
		var selectList = $('#typelist');
		var custTypes = ["CUSTOMER", "DRAWDOWN"];
		var selectedCustType = custTypes[0];
		buildCustTypesList(custTypes, selectList, selectedCustType);
		$('#warn_modal').modal('show');
	} else {
		var selectedType = $('#typelist option:selected').val();
		showHiddenInput();
		toggleProfileInput(selectedType);
	}
}

function validateAcctNbr() {
	console.log("validating account number...");
	var selector = $(".acctnbr:visible");
	var acctnbr = selector.val().trim();
	var selectorError;
	try{
		if(selector.attr('id') == "ntlacctnbr") {
			selectorError = $("#ntlaccterror");
			if(acctnbr.length!=9 || isNaN(acctnbr)){
				throw "Please enter a 9 digit account number";
			}
		} else if(selector.attr('id') == "intntlacctnbr") {
			selectorError = $("#intntlaccterror");
			if(acctnbr.length!=7 || isNaN(acctnbr)){
				throw "Please enter a 7 digit account number";
			}
		} else {
			selectorError = $("#costcntrerror");
			if(acctnbr.length!=6 || isNaN(acctnbr)){
				throw "Please enter a 6 digit cost center";
			}
			if(acctnbr.startsWith('99')) {
				throw "Invalid cost center account";
			}
		}
		//account number is valid, check if it already exists
		checkAccountNbr(selector);
		if(result=="true"){
			throw "Account Number already exists";
		}
		$(selector).removeClass("border-danger");
		$(selectorError).text("");
		$("#formerror").text("");
		checkIfWarningNeeded(acctnbr);
		valid = true;
	}catch(msg){
		$(selector).addClass("border-danger");
		$(selector).select();
		$(selectorError).text(msg);
		valid = false;
	}
}

function checkAccountNbr(selector) {
	console.log("checking if the account number already exists...");
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
		},
		error:function(request, status){
			console.log("Ajax error: " + status);
			alert(status + ": could not validate account number. Please retry.");
		}
	});
}

function showHideInput(value) {
	console.log("account type is " + value);
	clearAllErrors();
	clearAllTextInput();
	var custTypes = [];
	var selectList = $('#typelist');
	var selectedCustType;
	switch(value) {
	case 'natlWdigits':
		console.log("internal SW account");
		custTypes = ["CUSTOMER"];
		selectedCustType = custTypes[0];
		hideInput();
		showNtlAcctInput();
		break;
	case 'intnatlWdigits':
		console.log("international account");
		custTypes = ["CUSTOMER"];
		selectedCustType = custTypes[0];
		hideInput();
		showIntnatlAcctInput();
		break;
	case 'intnatlCostCntr':
		console.log("store account");
		custTypes = ["STORE"];
		selectedCustType = custTypes[0];
		hideInput();
		showCostCenterInput();
		break;
	case 'natlWOdigits':
		console.log("generate national account number");
		custTypes = ["CUSTOMER", "DRAWDOWN"];
		selectedCustType = custTypes[0];
		hideAcctNbr();
		showHiddenInput();
		toggleProfileInput(selectedCustType);
		break;
	case 'intnatlWOdigits':
		console.log("generate international account number");
		custTypes = ["CUSTOMER"];
		selectedCustType = custTypes[0];
		hideAcctNbr();
		showHiddenInput();
		toggleProfileInput(selectedCustType);
		break;
	default:
		//unexpected value, do not allow user to proceed
		hideInput();
	}
		
	buildCustTypesList(custTypes, selectList, selectedCustType);
}

function showNtlAcctInput() {
	$(".ntlacct").show();
	$("#intntlacct").hide();
	$(".intntlacct").val('');
	$(".costcntr").hide();
	$("#cstcntr").val('');
	$("#cont").show();
	$("#ntlacctnbr").focus();
}

function showIntnatlAcctInput() {
	$(".ntlacct").hide();
	$("#ntlacct").val('');
	$(".costcntr").hide();
	$("#cstcntr").val('');
	$(".intntlacct").show();
	$("#cont").show();
	$("#intntlacctnbr").focus();
}

function showCostCenterInput() {
	$(".ntlacct").hide();
	$("#ntlacct").val('');
	$("#intntlacct").hide();
	$(".intntlacct").val('');
	$(".costcntr").show();
	$("#cont").show();
	$("#cstcntr").focus();
}

function showHiddenInput() {
	$(".cstmrnm").show();
	$("#cdsadlfld").show();
	$(".clrnt").show();
	$("#loginnext-btn").show();
	$(".custtype").show();
	$(".eula").show();
	$("#cont").hide();
	$(".prodaccess").show();
	$("#swuititle").focus();
}

function hideInput() {
	console.log("waiting for account type/number...");
	$(".ntlacct").hide();
	$(".intntlacct").hide();
	$(".costcntr").hide();
	$("#cont").hide();
	$(".cstmrnm").hide();
	$("#cdsadlfld").hide();
	$(".clrnt").hide();
	$("#loginnext-btn").hide();
	$(".custtype").hide();
	$(".rmbyrm").hide();
	$(".locid").hide();
	$(".eula").hide();
	$(".prodaccess").hide();
	$(".prodcomps").hide();
	$("#restrictno").attr("checked", true);
}

function hideAcctNbr() {
	$(".ntlacct").hide();
	$(".intntlacct").hide();
	$(".costcntr").hide();
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

function clearAllTextInput() {
	$('input:text').each(function(){
		$(this).val('');
	});
}

function proceedYes() {
	console.log("proceed with form completion");
	showHiddenInput();
	$('#warn_modal').modal('hide');
	$("#swuititle").focus();
}

function proceedNo() {
	console.log("reset form");
	clearAllErrors();
	clearAllTextInput();
	hideInput();
	$('#warn_modal').modal('hide');
}