/**
 * 
 */
$(document).ready(function() {
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
		$("#newjobrow").removeClass("d-none");
		$("#newjobrow").children("td").eq(0).text(firstclonedjobrow);
	}
	
	//add unique ids to cloned rows
	while(b<clonedjobrows-1){
		var clone = $("#newjobrow").clone(true);
		var rowid = "newjobrow"+b;
		
		clone.attr("id", rowid);
		$("#job_detail").append(clone);
		$("#"+rowid).find("input").each(function(){
			$(this).attr("id", $(this).attr("id")+b);
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
	
	$(".dltrow").click(function(){	
		var input = $(this).parent('td').parent('tr').find("input");
		//window.alert($(".cloned-loginrow").length);
		if($(".cloned-loginrow").length==1){ 
			input.removeAttr("readonly");
			input.val("");
			input.eq(0).focus();
			input.eq(0).select();
		}else{
			input.val("");
			$(this).parent('td').parent('tr').remove();
		}
	});
	
	$(".deleterow").click(function(){	
		var input = $(this).parent('td').parent('tr').find("input");
		input.val("");
		$(this).parent('td').parent('tr').remove();
	});
	
	var a = 0;
	$(".addrow").click(function(){
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
	
	$("#edt").click(function(){
		$("#customername").removeAttr("readonly");
		$("#customername").focus();
		$("#customername").select();
	});
	
	$("#edt1").click(function(){
		$("#cdsadlfld").removeAttr("readonly");
		$("#cdsadlfld").focus();
		$("#cdsadlfld").select();
	});
	
	$("#edt2").click(function(){
		$("#acceptcode").removeAttr("readonly");
		$("#acceptcode").focus();
		$("#acceptcode").select();
	});
	
	$(".edtrow").click(function(){
		var input = $(this).parent('td').parent('tr').find("input");
		input.removeAttr("readonly");
		input.eq(0).focus();
		input.eq(0).select();
	});
	
	var jsvalue = String($.trim($("#customerid").text()));
	var valid;
	
	$("#customername").on("blur", function(){
		try{
			var swuititle = $.trim($(this).val());
			if(swuititle.length > 20){
				throw "Customer Name cannot be greater than 20 characters";
			}
			if(!swuititle){
				throw "Please enter a customer name";
			}
			valid = true;
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	$("#cdsadlfld").on("blur", function(){
		try{
			var cdsadlfld = $.trim($(this).val());
			if(cdsadlfld.length > 20){
				throw "Additional info cannot be greater than 20 characters";
			}
			valid = true;
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	$("#acceptcode").on("blur", function(){
		try{
			var acceptcode = $.trim($(this).val());
			if(acceptcode.length != 6){
				throw "Acceptace code is 6 digits";
			}
			valid = true;
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
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
			valid = true;
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	$("#effDate").on("change", function(){
		try{
			var effdate = $.trim($(this).val());
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
			valid = true;
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	$("#expDate").on("change", function(){
		try{
			var expdate = $.trim($(this).val());
			if(expdate){
				if(!/^(0?[1-9]|1[0-2])\/(0?[1-9]|[1-2][0-9]|3[0-1])\/(2\d\d\d)$/.test(expdate)){
					throw "Please enter valid date in mm/dd/yyyy format";
				}
			}
			valid = true;
			$("#custediterror").text("");
			$("#formerror").text("");
			$(this).removeClass("border-danger");
		}catch(msg){
			valid = false;
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
			$("#custediterror").text(msg);
			$(this).addClass("border-danger");
			$(this).focus();
		}
	});
	
	var logins;
	var result;
	
	$(document).on({
		"focusin":function(){
			logins = $(".kyfld").map(function(index){
				if(!$(this).is($(".kyfld:last"))){
					return $(this).val();
				}
			}).toArray();
		},
		"change":function(){
			var keyfld = $.trim($(this).val());
			$.ajax({
				url:"ajaxKeyfieldResult.action",
				data:{keyfield: keyfld},
				dataType:"json",
				async:false,
				success:function(data){
					result = data.result;
				}
			});
			try{
				if(keyfld.length > 20){
					throw "Login ID cannot be greater than 20 characters";
				}
				if($.inArray(keyfld,logins)!=-1){
					throw "Please enter unique values for Login ID";
				}
				if(result=="true" && $.inArray(keyfld,existinglogins)==-1){
					throw "Login ID already exists";
				}
				valid = true;
				$("#custediterror").text("");
				$("#formerror").text("");
				$(this).removeClass("border-danger");
			}catch(msg){
				valid = false;
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
				$("#custediterror").text(msg);
				$(this).addClass("border-danger");
				$(this).focus();
			}
		}
	},".kyfld");
	
	var mstraccts;
	
	$(document).on({
		"change":function(){
			try{
				var mstracct = $.trim($(this).val());
				if(mstracct.length > 50){
					throw "Master Account Name cannot be greater than 50 characters";
				}
				valid = true;
				$("#custediterror").text("");
				$("#formerror").text("");
				$(this).removeClass("border-danger");
			}catch(msg){
				valid = false;
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
				$("#custediterror").text(msg);
				$(this).addClass("border-danger");
				$(this).focus();
			}
		}
	}, ".mstraccntnm");
	
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
		},
		"change":function(){
			$(this).attr("placeholder", "150 Character Limit");
		}
		
	}, ".acctcmmnt");
	
	$(document).on({
		"blur":function(){
			try{
				var scrnlbl = $.trim($(this).val());
				if(scrnlbl.length > 15){
					throw "Screen Label cannot be greater than 15 characters";
				}
				if($(this).parents("tr").index()==0 && !$(this).parents("tr").find("input:text").val()){
					throw "Please enter Job Field information";
				}
				valid = true;
				$("#custediterror").text("");
				$("#formerror").text("");
				$(this).removeClass("border-danger");
			}catch(msg){
				valid = false;
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
				$("#custediterror").text(msg);
				$(this).addClass("border-danger");
				$(this).focus();
			}
		}
	}, ".scrnlbl");
	
	$(document).on({
		"blur":function(){
			try{
				var flddflt = $.trim($(this).val());
				if(flddflt.length > 15){
					throw "Field Default cannot be greater than 15 characters";
				}
				valid = true;
				$("#custediterror").text("");
				$("#formerror").text("");
				$(this).removeClass("border-danger");
			}catch(msg){
				valid = false;
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
				$("#custediterror").text(msg);
				$(this).addClass("border-danger");
				$(this).focus();
			}
		}
	}, ".flddflt");
	
	$(document).on({
		"change":function(){
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
				valid = true;
				$("#custediterror").text("");
				$("#formerror").text("");
				$(this).removeClass("border-danger");
			}catch(msg){
				valid = false;
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
				$("#custediterror").text(msg);
				$(this).addClass("border-danger");
				$(this).prop("checked", false);
			}
		}
	}, $(".clrntsys"));
	
	/*$(document).on({
		"change":function(){
			try{
				if($("#CCEdefault").is(":checked") && !$("#CCE").is(":checked")){
					throw "Please choose CCE colorant system";
				}
				if($("#BACdefault").is(":checked") && !$("#BAC").is(":checked")){
					throw "Please choose BAC colorant system";
				}
				if($("#844default").is(":checked") && !$("#844").is(":checked")){
					throw "Please choose 844 colorant system";
				}
				valid = true;
				$("#custediterror").text("");
				$("#formerror").text("");
				$(this).removeClass("border-danger");
			}catch(msg){
				valid = false;
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
				$("#custediterror").text(msg);
				$(this).addClass("border-danger");
				//$(this).prop("checked", false);
			}
		}
	}, $(".clrnts"));*/
	
	$(document).on("click", "#submitchng", function(e){
		try{
			if(valid===false){
				throw "Please fix form error(s):";
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
			var acceptcode = $.trim($("#acceptcode").val());
			var eulaws = $("#eulalist").val();
			var eula = $("#eulafile").val();
			var eulatext = $("#eulatext").val();
			var d = new Date();
			var dd = String(d.getDate()).padStart(2, '0');
			var mm = String(d.getMonth() + 1).padStart(2, '0');
			var yyyy = String(d.getFullYear());
			var today = mm + "/" + dd + "/" + yyyy;
			console.log("today's date = " + today);
			if(eula && !eulatext){
				throw "Please enter EULA text";
			}
			if(!eula && eulatext){
				throw "Please choose a EULA pdf";
			}
			if($("#eulalist").is(":visible") && eulaws != 'None' && !acceptcode){
				throw "Please enter an Acceptance Code";
			}
			if(eulaws == 'None' && acceptcode){
				throw "Please choose a EULA";
			}
			if($("#expDate") != null && $("#expDate").val() != ""){
				if($("#expDate").val() < $("#effDate").val()){
					throw "Invalid Expiration Date";
				}
			}
			$("#formerror").text("");
			return true;
		}catch(msg){
			e.preventDefault();
			$("#formerror").text(msg);
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
		}
	});
	
	$('#effDate').datepicker({
		//dateFormat: "dd-M-y",
		changeMonth: true,
		changeYear: true,
		gotoCurrent: true
	});
	$('#expDate').datepicker({
		//dateFormat: "dd-M-y",
		changeMonth: true,
		changeYear: true,
		gotoCurrent: true
	});
	
});