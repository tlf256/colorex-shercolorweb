/**
 * 
 */
$(document).ready(function() {
	
	$("#yes").click(function(){
		$("#keyfld-hidden").removeClass("d-none");
		$("#acctcmt-hidden").removeClass("d-none");
		$("#logininfo_btn-hidden").removeClass("d-none");
		$("#keyfld").focus();
		$("#forminfo-hidden").addClass("d-none");
	});
	
	$(document).on("click", "#logininfo_add", function(){
		$("#logininfo_add").attr("id", "logininfo1_add");
		$("#keyfld1-hidden").removeClass("d-none");
		$("#acctcmt1-hidden").removeClass("d-none");
		$("#keyfld1").focus();
	});
	
	$(document).on("click", "#logininfo1_add", function(){
		$("#logininfo1_add").attr("id", "logininfo2_add");
		$("#keyfld2-hidden").removeClass("d-none");
		$("#acctcmt2-hidden").removeClass("d-none");
		$("#keyfld2").focus();
	});
	
	$(document).on("click", "#logininfo2_add", function(){
		$("#logininfo2_add").attr("id", "logininfo3_add");
		$("#keyfld3-hidden").removeClass("d-none");
		$("#acctcmt3-hidden").removeClass("d-none");
		$("#keyfld3").focus();
	});
	
	$("#logininfo3_add").click(function(){
		$("#logininfo3_add").attr("id", "logininfo4_add");
		$("#keyfld4-hidden").removeClass("d-none");
		$("#acctcmt4-hidden").removeClass("d-none");
		$("#keyfld4").focus();
	});
	
	$("#logininfo4_add").click(function(){
		$("#logininfo4_add").attr("id", "logininfo5_add");
		$("#keyfld5-hidden").removeClass("d-none");
		$("#acctcmt5-hidden").removeClass("d-none");
		$("#keyfld5").focus();
	});
	
	$("#logininfo5_add").click(function(){
		$("#logininfo5_add").attr("id", "logininfo6_add");
		$("#keyfld6-hidden").removeClass("d-none");
		$("#acctcmt6-hidden").removeClass("d-none");
		$("#keyfld6").focus();
	});
	
	$("#logininfo6_add").click(function(){
		$("#logininfo6_add").attr("id", "logininfo7_add");
		$("#keyfld7-hidden").removeClass("d-none");
		$("#acctcmt7-hidden").removeClass("d-none");
		$("#keyfld7").focus();
	});
	
	$("#logininfo7_add").click(function(){
		$("#logininfo7_add").attr("id", "logininfo8_add");
		$("#keyfld8-hidden").removeClass("d-none");
		$("#acctcmt8-hidden").removeClass("d-none");
		$("#keyfld8").focus();
	});
	
	$("#logininfo8_add").click(function(){
		//$("#logininfo8_add").attr("id", "logininfo9_add");
		$("#keyfld9-hidden").removeClass("d-none");
		$("#acctcmt9-hidden").removeClass("d-none");
		$("#keyfld9").focus();
		$("#logininfo8_add").addClass("d-none");
	});
	
	$("#logininfo_next").click(function(){
		//$("#forminfo-hidden").removeClass("d-none");
		$("#logininfo_btn-hidden").addClass("d-none");
	});
	
	$("#no").click(function(){
		$("#keyfld-hidden").addClass("d-none");
		$("#acctcmt-hidden").addClass("d-none");
		$("#logininfo_btn-hidden").addClass("d-none");
		//$("#forminfo-hidden").removeClass("d-none");
	});
	
});