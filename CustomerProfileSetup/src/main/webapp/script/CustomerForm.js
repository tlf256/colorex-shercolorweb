/**
 * 
 */
$(document).ready(function() {
	
	$("#ntlacct").hide();
	$("#intntlacct").hide();
	$("#cstmrnm").hide();
	$("#cdsadlfld").hide();
	$("#clrnt").hide();
	$("#loginnext-btn").hide();
	$("#eula").hide();
	
	$("#loginnext-btn").prop("disabled", true);
	
	function natlWdigits(){
		$("#ntlacct").show();
		$("#intntlacct").hide();
		$("#cstmrnm").show();
		$("#cdsadlfld").show();
		$("#clrnt").show();
		$("#loginnext-btn").show();
		$("#eula").show();
	}
	
	function intnatlWdigits(){
		$("#intntlacct").show();
		$("#ntlacct").hide();
		$("#cstmrnm").show();
		$("#cdsadlfld").show();
		$("#clrnt").show();
		$("#loginnext-btn").show();
		$("#eula").show();
	}
	
	function acctWOdigits(){
		$("#ntlacct").hide();
		$("#intntlacct").hide();
		$("#cstmrnm").show();
		$("#cdsadlfld").show();
		$("#clrnt").show();
		$("#loginnext-btn").show();
		$("#eula").show();
	}
	
	$("#selectedAccttype-0").click(function(){
		natlWdigits();
		$("#ntlacctnbr").focus();
		$("html, body").animate({
			scrollTop: $("#ntlacct").offset().top
		}, 1000);
	});
	
	$("#selectedAccttype-1").click(function(){
		acctWOdigits();
		$("#swuititle").focus();
		$("html, body").animate({
			scrollTop: $("#cstmrnm").offset().top
		}, 1000);
	});
	
	$("#selectedAccttype-2").click(function(){
		intnatlWdigits();
		$("#intntlacctnbr").focus();
		$("html, body").animate({
			scrollTop: $("#intntlacct").offset().top
		}, 1000);
	});
	
	$("#selectedAccttype-3").click(function(){
		acctWOdigits();
		$("#swuititle").focus();
		$("html, body").animate({
			scrollTop: $("#cstmrnm").offset().top
		}, 1000);
	});
		
	$(document).on("blur change", "#ntlacctnbr", function(){
		var ntlval = $.trim($("#ntlacctnbr").val());
		var ntl = $("#ntlacctnbr");
		var result;
		$.ajax({
			url:"ajaxAcctNbrResult.action",
			data:{acctNbr: ntlval},
			dataType:"json",
			success:function(data){
				result = data.result;
			},
			error:function(request, status){
				alert(status + ": could not validate account number. Please retry.");
			}
		});
		try{
			if(ntl.is(":visible") && ntlval.length!=9){
				throw "Please enter a 9 digit account number";
			}
			if(ntl.is(":visible") && isNaN(ntlval)) {
				throw "Account number must be a number";
			}
			if(ntl.is(":visible") && result=="true"){
				throw "Account Number already exists";
			}
			$(this).removeClass("border-danger");
			$("#ntlaccterror").text("");
			$("#formerror").text("");
		}catch(msg){
			$(this).addClass("border-danger");
			$(this).focus();
			$("#ntlaccterror").text(msg);
		}
	});
		
	$(document).on("blur change", "#intntlacctnbr", function(){
		var intntlval = $.trim($("#intntlacctnbr").val());
		var intntl = $("#intntlacctnbr");
		var result;
		$.ajax({
			url:"ajaxAcctNbrResult.action",
			data:{acctNbr: intntlval},
			dataType:"json",
			success:function(data){
				result = data.result;
			},
			error:function(request, status){
				alert(status + ": could not validate account number. Please retry.");
			}
		});
		try{
			if(intntl.is(":visible") && intntlval.length!=7){
				throw "Please enter a 7 digit account number";
			}
			if(intntl.is(":visible") && isNaN(intntlval)) {
				throw "Account number must be a number";
			}
			if(intntl.is(":visible") && result=="true"){
				throw "Account Number already exists";
			}
			$("#intntlaccterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("#intntlaccterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
		
	$(document).on("blur change", "#swuititle", function(){
		var title = $.trim($("#swuititle").val());
		try{
			if(title.length > 20 || title.length == 0){
				throw "Please enter a Customer Name not greater than 20 characters"
			} 
			$("#swuititlerror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("#swuititlerror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	$(document).on("blur change", "#cdsadlfld", function(){
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
			$(this).focus();
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
		
	$(document).on("blur change", "#acceptcode", function(){
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
			$(this).focus();
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