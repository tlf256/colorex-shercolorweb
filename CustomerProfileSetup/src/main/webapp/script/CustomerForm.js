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
	
	var valid = false;
	var result;
	
	$("#ntlacctnbr").on("change", function(){
		var ntl = $.trim($("#ntlacctnbr").val());
		$.ajax({
			url:"ajaxAcctNbrResult.action",
			data:{acctNbr: ntl},
			dataType:"json",
			async:false,
			success:function(data){
				result = data.result;
			}
		});
		try{
			if(!ntl){
				throw "Please enter an account number";
			}
			if(ntl.length!=9) {
				throw "National account number is 9 digits";
			}
			if(isNaN(ntl)) {
				throw "Account number must be a number";
			}
			if(result=="true"){
				throw "Account Number already exists";
			}
			valid = true;
			$("#ntlaccterror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("#ntlaccterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
		
	
	$("#intntlacctnbr").on("change", function(){
		var intntl = $.trim($("#intntlacctnbr").val());
		$.ajax({
			url:"ajaxAcctNbrResult.action",
			data:{acctNbr: intntl},
			dataType:"json",
			async:false,
			success:function(data){
				result = data.result;
			}
		});
		try{
			if(!intntl){
				throw "Please enter an account number";
			}
			if(intntl.length!=7) {
				throw "International account number is 7 digits";
			}
			if(isNaN(intntl)) {
				throw "Account number must be a number";
			}
			if(result=="true"){
				throw "Account Number already exists";
			}
			valid = true;
			$("#intntlaccterror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("#intntlaccterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	$("#swuititle").on("blur", function(){
		var title = $.trim($("#swuititle").val());
		try{
			if(title.length > 20){
				throw "Customer Name cannot be greater than 20 characters"
			} 
			if(!title){
				throw "Please enter a Customer Name";
			}
			valid = true;
			$("#swuititlerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("#swuititlerror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});

	$("#cdsadlfld").on("blur", function(){
		var info = $.trim($("#cdsadlfld").val());
		try{
			if(info.length > 20){
				throw "Additional Info cannot be greater than 20 characters";
			}
			valid = true;
			$("#cdsadlflderror").text("");
			$(this).removeClass("border-danger");
		} catch(msg) {
			valid = false;
			$("#cdsadlflderror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	$(document).on("change", "input:checkbox", function(){
		try{
			if(($("#ccedefault").is(":checked") && !$("#cce").is(":checked")) || ($("#bacdefault").is(":checked") && !$("#bac").is(":checked")) 
					|| ($("#844default").is(":checked") && !$("#844").is(":checked"))){
				throw "Please choose the correct default colorant system";
			}
			valid = true;
			$("#clrntsyserror").text("");
		}catch(msg){
			valid = false;
			$("#clrntsyserror").text(msg);
			$(this).focus();
		}
	});
	
	$(document).on("change", "[name='customer.defaultClrntSys']", function(){
		try{
			if($("#ccedefault").is(":checked") && !$("#cce").is(":checked")){
				throw "Please choose CCE colorant system before selecting it as the default";
			}
			if($("#bacdefault").is(":checked") && !$("#bac").is(":checked")){
				throw "Please choose BAC colorant system before selecting it as the default";
			}
			if($("#844default").is(":checked") && !$("#844").is(":checked")){
				throw "Please choose 844 colorant system before selecting it as the default";
			}
			valid = true;
			$("#clrntsyserror").text("");
			//$("#loginnext-btn").removeClass("d-none");
		}catch(msg){
			valid = false;
			$("#clrntsyserror").text(msg);
			$(this).prop("checked", false);
			//$(this).focus();
			//$("#loginnext-btn").addClass("d-none");
		}
	});
	
	$("#acceptcode").on("blur", function(){
		try{
			var acceptcode = $.trim($(this).val());
			var eula = $("#eulalist").val();
			if(acceptcode.length != 6){
				throw "Acceptace code is 6 digits";
			}
			if(eula && !acceptcode){
				throw "Please enter an Acceptance Code";
			}
			if(eula == 'None' && acceptcode){
				throw "Please choose a EULA";
			}
			valid = true;
			$("#custformerror").text("");
			$("#eulaerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			
			$("#eulaerror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	$("#eulalist").on("change", function(){
		var ws = $("#eulalist").val();
		if(ws != "None"){
			$("#eulahist").removeClass("d-none");
		} else {
			$("#eulahist").addClass("d-none");
		}
	});
	
	$("#loginnext-btn").click(function(){
		try{
			if((!$("#cce").is(":checked") && !$("#bac").is(":checked") && !$("#844").is(":checked")) || 
					(!$("#ccedefault").is(":checked") && !$("#bacdefault").is(":checked") && !$("#844default").is(":checked"))){
				throw "Please choose colorant system(s) and select a default";
			}
			if(valid===false){
				throw "Please fix form error(s):";
			}
		}catch(msg){
			event.preventDefault();
			$("#formerror").text(msg);
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
		}
	});
	
});