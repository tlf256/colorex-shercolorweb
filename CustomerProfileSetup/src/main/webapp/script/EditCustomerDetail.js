/**
 * 
 */
$(document).ready(function() {
	
	$("#code").hide();
	$(".eulaTemp").hide();
	$('#uploadEula').hide();
	
	if($('#custProfile').is(':visible')) {
		var selectList = $('#typelist');
		var selectedCustType = $('#typelist option:selected').val();
		
		buildSelectList($('#customerid').text(), $('#acctType').val(), selectList, selectedCustType);
		
		if($('#typelist option:selected').val() == 'CUSTOMER') {
			$("#rmbyrm").hide();
			$("#locid").hide();
		} else {
			$("#rmbyrm").show();
			$("#locid").show();
		}
	}
	
	if($('#prodCompAccess').is(':visible')) {
		$(".prodcomps").hide();
	}
	
	var existinglogins = $(".kyfld").map(function(index){
		if($(this).val()!=""){
			return $(this).val();
		}
	}).toArray();
	var jsvalue = String($.trim($("#customerid").text()));
	//job rows + cloned rows not to exceed 10
	var jobrows = $(".jobrow").length; //length of job rows when page renders
	var clonedjobrows = 10-$(".jobrow").length; //max length of cloned (empty) rows
	var firstclonedjobrow = jobrows+1;
	var b = 0;
	
	if(jobrows<10){
		var firstrow = $("#newjobrow");
		firstrow.removeClass("d-none");
		firstrow.children("td").eq(0).text(firstclonedjobrow);
		firstrow.children("td").eq(3).find("input").attr("value", firstrow.index()-1);
		firstrow.children("td").eq(4).find("input").attr("value", firstrow.index()-1);
	}
	
	//add cloned job rows
	while(b<clonedjobrows-1){
		var clone = $("#newjobrow").clone(true);
		var rowid = "newjobrow"+b;
		var lastrow = $("#job_detail").find("tr:last");
		
		clone.attr("id", rowid);
		$("#job_detail").append(clone);
		$("#"+rowid).find("input").each(function(){
			$(this).attr("id", $(this).attr("id")+b);
		});
		$("#"+rowid).find("input:checkbox").each(function(){
			$(this).attr("value", lastrow.index());
		});
		$("#"+rowid).find("label").each(function(){
			$(this).attr("for", $(this).attr("for")+b);
		});
		$("#"+rowid).find("td").eq(0).each(function(){
			$(this).text(firstclonedjobrow+(b+1));
		});
		b++;
	}
	
	//login rows + cloned rows not to exceed 10
	var loginrows = $(".loginrow").length; //length of login rows when page renders
	var clonedloginrows = 10-$(".loginrow").length; //max length of cloned (empty) rows
	
	if(loginrows>=10){
		$("#newloginrow").addClass("d-none");
	}
	
	$(document).on("click", ".dltloginrow", function(){	
		var input = $(this).parent('td').parent('tr').find("input");
		var clonedRowLength = $(".cloned-loginrow").length;
		//window.alert($(".cloned-loginrow").length);
		deleteClonedRow(input, clonedRowLength);
	});
	
	$(document).on("click", ".deleterow", function(){	
		var input = $(this).parent('td').parent('tr').find("input");
		input.val("");
		$(this).parent('td').parent('tr').remove();
	});
	
	$(document).on("click", ".addrow", function(){
		var a = 0;
		var clone = $("#newloginrow").clone(true);
		var rowid = "newloginrow"+a;
		if($("#loginTrans_detail tr").length < 11){
			clone.attr("id", rowid);
			$("#loginTrans_detail").append(clone);
			clone.find("input").val("");
			$("#"+rowid).find("input").each(function(){
				$(this).attr("id", $(this).attr("id")+(a+1));
			});
		} else {
			$(this).addClass("d-none");
		}
		a++;
	});
	
	$(document).on("click", ".addpcrow", function(){
		var i = 0;
		var prodCompRow = $('#newProdCompRow');
		var clone = prodCompRow.clone(true);
		var rowId = prodCompRow + i;
		
		clone.attr("id", rowid);
		$("#prodcomp_detail").append(clone);
		clone.find("input").val("");
		$("#"+rowid).find("input").each(function(){
			$(this).attr("id", $(this).attr("id")+(i+1));
		});
		i++;
	});
	
	$(document).on("click", ".dltpcclonerow", function(){	
		var input = $(this).parent('td').parent('tr').find("input");
		var clonedRowLength = $(".cloned-pcrow").length;
		//console.log("cloned prod comp row length is " + clonedRowLength);
		deleteClonedRow(input, clonedRowLength);
	});
	
	$(document).on("click", ".dltpcrow", function(){
		var input = $(this).parent('td').parent('tr').find("input");
		var prodCompVal = input.eq(0).val();
		var result;
		console.log("deleting prod comp " + prodCompVal);
		
		$.ajax({
			url:"ajaxDeleteProdComp.action",
			dataType:"json",
			data: {prodComp: prodCompVal},
			async: true,
			success:function(data){
				result = data.prodCompDeleted;
				console.log("Result of prod comp delete is " + result);
			},
			error:function(request, status){
				alert(status + ": could not delete prod comp. Please retry.");
			}
		}).done(function(){
			if(result) {
				console.log("deleting prod comp record...")
				input.val("");
				input.parent('td').parent('tr').remove();
			}
		});
	});
	
	$("#edt").on("click", function(){
		$("#customername").removeAttr("readonly");
		$("#customername").focus();
		$("#customername").select();
	});
	
	$("#edt1").on("click", function(){
		$("#cdsadlfld").removeAttr("readonly");
		$("#cdsadlfld").focus();
		$("#cdsadlfld").select();
	});
	
	$("#edt2").on("click", function(){
		$("#acceptcode").removeAttr("readonly");
		$("#acceptcode").focus();
		$("#acceptcode").select();
	});
	
	$(document).on("click", ".edtrow", function(){
		var input = $(this).parent('td').parent('tr').find("input");
		input.removeAttr("readonly");
		input.eq(0).focus();
		input.eq(0).select();
	});
		
	$("#customername").on("blur", function(){
		try{
			var swuititle = $.trim($(this).val());
			if(swuititle.length > 20){
				throw "Customer Name cannot be greater than 20 characters";
			}
			if(!swuititle){
				throw "Please enter a customer name";
			}
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
		
	$("#cdsadlfld").on("blur", function(){
		try{
			var cdsadlfld = $.trim($(this).val());
			if(cdsadlfld.length > 20){
				throw "Additional info cannot be greater than 20 characters";
			}
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
		
	$("#acceptcode").on("blur", function(){
		try{
			var acceptcode = $.trim($(this).val());
			if(acceptcode.length != 6){
				throw "Acceptace code is 6 digits";
			}
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
		
	$("#eulafile").on("change", function(){
		try{
			var eula = $("#eulafile").val();
			var ext = eula.split('.').pop().toLowerCase();
			if(eula){
				if(ext != 'pdf'){
					throw "Invalid file extension";
				}
			}
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
	
	var prodComps = $.ajax({
		url:"ajaxProdCompResult.action",
		dataType:"json",
		async: true,
		success:function(data){
			console.log("Ajax success: received prod comps");
			var prodComps = data.prodCompList;
			console.log("prod comps json result: " + prodComps);
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
			
			$('#custediterror').text('');
			$(this).removeClass("border-danger");
		} catch(msg) {
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
	
	$(document).on("click", ".primprdcmp", function(){
		var chkbx = $(this);
		if(chkbx.is(":checked")) {
			var chkbxGroup = $("input:checkbox[name='" + chkbx.attr('name') + "']");
			var txtVal = chkbx.parent('td').parent('tr').find('input:text').val();
			console.log("prod comp text input value is " + txtVal);
			chkbxGroup.prop("checked", false);
			if(txtVal != null && txtVal != "") {
				chkbx.prop("checked", true);
				chkbx.attr("value", txtVal);
			} else {
				chkbx.prop("checked", false);
			}
		} else {
			chkbx.prop("checked", false);
		}
	});
	
	var logins;
	
	$(document).on({
		"focusin":function(){
			logins = $(".kyfld").map(function(index){
				if(!$(this).is($(".kyfld:last"))){
					return $(this).val();
				}
			}).toArray();
		},
		"blur":function(e){
			var keyfldval = $.trim($(this).val());
			var keyfld = $(this);
			/*var result;
			$.ajax({
				url:"ajaxKeyfieldResult.action",
				data:{keyfield: keyfldval},
				dataType:"json",
				async: false,
				success:function(data){
					result = data.result;
					console.log(result);
				},
				error:function(request, status){
					alert(status + ": could not validate login ID. Please retry.");
				}
			});*/
			try{
				if(keyfldval.length > 20){
					throw "Login ID cannot be greater than 20 characters";
				}
				if($.inArray(keyfldval,logins)!=-1){
					throw "Please enter unique values for Login ID";
				}
				/*if(result=="true" && $.inArray(keyfldval,existinglogins)==-1){
					throw "Login ID already exists";
				}*/
				$("#custediterror").text("");
				$("#formerror").text("");
				keyfld.removeClass("border-danger");
			}catch(msg){
				keyfieldvalid = false;
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
				$("#custediterror").text(msg);
				keyfld.addClass("border-danger");
				keyfld.select();
			}
		}
	},".kyfld");
	
	var mstraccts;
	
	$(document).on("blur", ".mstraccntnm", function(){
		try{
			var mstracct = $.trim($(this).val());
			if(mstracct.length > 50){
				throw "Master Account Name cannot be greater than 50 characters";
			}
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
		
	$(document).on("blur", ".scrnlbl", function(){
		try{
			var scrnlbl = $.trim($(this).val());
			if(scrnlbl.length > 15){
				throw "Screen Label cannot be greater than 15 characters";
			}
			if($(this).parents("tr").index()==0 && !$(this).parents("tr").find("input:text").val()){
				throw "Please enter Job Field information";
			}
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
		
	$(document).on("blur", ".flddflt", function(){
		try{
			var flddflt = $.trim($(this).val());
			if(flddflt.length > 15){
				throw "Field Default cannot be greater than 15 characters";
			}
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).select();
		}
	});
	
	var d = new Date();
	var dd = String(d.getDate()).padStart(2, '0');
	var mm = String(d.getMonth() + 1).padStart(2, '0');
	var yyyy = String(d.getFullYear());
	var today = mm + "/" + dd + "/" + yyyy;
	console.log("today's date = " + today);
	
	$(document).on("click", "#submitchng", function(){
		try{
			if($("#custediterror").text()!=""){
				throw "Please fix form error(s):";
			}
			if(!$(".clrntid:checked").length) {
				throw "Please choose colorant system(s)";
			}
			if(!$(".clrntdefault:checked").length) {
				throw "Please choose default colorant system";
			}
			$(".scrnlbl").each(function(i){
				if(!$(this).val() && $(".flddflt").eq(i).val()!=""){
					$(this).addClass("border-danger");
					throw "Please enter a value for Screen Label";
				}else{
					$(this).removeClass("border-danger");
				}
			});
			$(".kyfld").each(function(i){
				if((!jsvalue.startsWith("99") || jsvalue.length!=9) && existinglogins.length>0){
					if(!$(this).val() && $(".kyfld").length==1){
						$(this).addClass("border-danger");
						throw "Please enter a value for Login ID";
					}else{
						$(this).removeClass("border-danger");
					}
				}
			});
			
			if(!$(".primprdcmp:checked").val() && $("#prodcomp_detail").is(":visible")) {
				throw "Please choose a Primary Prod Comp";
			}
			
			var acceptcode = $.trim($("#acceptcode").val());
			var eulaws = $("#eulalist").val();
			var eula = $("#eulafile").val();
			var eulatext = $("#eulatext").val();
			
			if(eula && !eulatext){
				throw "Please enter EULA text";
			}
			if(!eula && eulatext){
				throw "Please choose a EULA pdf";
			}
			if($("#acceptcode").is(":visible") && !acceptcode){
				throw "Please enter an Acceptance Code";
			}
			if((eulaws == 'None' && acceptcode) || (eulaws == 'None' && eula)){
				throw "Please choose a EULA";
			}
			if($("#expDate") != null && $("#expDate").val() != ""){
				if(($("#expDate").val() < $("#effDate").val()) || ($("#expDate").val() <= today)){
					throw "Invalid Expiration Date";
				}
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

function verifyEffDate(value) {
	try{
		var effdate = $.trim(value);
		var eula = $("#eulafile").val();
		if(eula && !effdate){
			throw "Please enter an Effective Date";
		}
		if(!eula && effdate){
			throw "Please choose a EULA pdf";
		}
		if(effdate){
			if(!/^(0?[1-9]|1[0-2])\/(0?[1-9]|[1-2][0-9]|3[0-1])\/(2\d\d\d)$/.test(effdate)){
				throw "Please enter valid date in mm/dd/yyyy format";
			}
		}
		$("#custediterror").text("");
		$("#formerror").text("");
		$(this).removeClass("border-danger");
	}catch(msg){
		$("html, body").animate({
			scrollTop: $(document.body).offset().top
		}, 1500);
		$("#custediterror").text(msg);
		$(this).addClass("border-danger");
		$(this).select();
	}
}

function deleteClonedRow(input, clonedRowLength) {
	console.log("cloned row length is " + clonedRowLength);
	if(clonedRowLength < 2){ 
		input.removeAttr("readonly");
		input.val("");
		if(input.is(":checked")) {
			input.prop("checked", false);
		}
		input.eq(0).focus();
		input.eq(0).select();
	}else{
		input.val("");
		input.parent('td').parent('tr').remove();
	}
}

function buildSelectList(custId, accttype, selectList, selectedCustType) {
	customerId = custId.trim();
	console.log("setting options for select list, custId is " + customerId);
	var custTypes = [];
	console.log("accttype is " + accttype);
	switch(accttype) {
	case 'natlWdigits':
		if(customerId >= '400000000' && customerId <= '400000012') {
			console.log("internal 9 digit account");
			custTypes = ["CUSTOMER", "DRAWDOWN"];
		} else {
			console.log("national 9 digit account");
			custTypes = ["CUSTOMER"];
		}
		break;
	case 'intnatlWdigits':
		console.log("international 7 digit account");
		custTypes = ["CUSTOMER"];
		break;
	case 'intnatlCostCntr':
		console.log("6 digit cost center");
		custTypes = ["STORE"];
		break;
	case 'natlWOdigits':
		console.log("national 6 digit account")
		custTypes = ["CUSTOMER", "DRAWDOWN"];
		break;
	case 'intnatlWOdigits':
		console.log("international 8 digit account");
		custTypes = ["CUSTOMER"];
		break;
	default:
		console.log("account type is undefined")
	}
	
	buildCustTypesList(custTypes, selectList, selectedCustType);
}

function toggleSelectList(value) {
	console.log("EULA type is " + value);
	switch(value) {
	case 'None':
		$('.eulaTemp').hide();
		$('#code').hide();
		$('#uploadEula').hide();
		removeInputValues($('#uploadEula'));
		break;
	case 'Custom EULA':
		$('.eulaTemp').hide();
		$('#code').hide();
		$('#uploadEula').show();
		break;
	case 'SherColor Web EULA':
		$('.eulaTemp').hide();
		$('#code').show();
		$('#uploadEula').hide();
		removeInputValues($('#uploadEula'));
		break;
	case 'Custom EULA Template':
		$('.eulaTemp').show();
		$('#code').show();
		$('#uploadEula').hide();
		removeInputValues($('#uploadEula'));
		break;
	}
}

function removeInputValues(parentSelector) {
	parentSelector.find(":input").each(function(){
		$(this).val("");
	});
}