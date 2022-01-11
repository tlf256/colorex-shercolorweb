	/**
 * 
 */
$(document).ready(function(){
	
	//$("#jobnext-btn").addClass("d-none");
	$("#loginInfo").find("input:eq(0)").focus();
	
	var jsvalue = String($.trim($("#customerid").text()));
	var req = true;
	if(jsvalue.length == 9){
		if(jsvalue.startsWith("4")){
			req = false;
		}
	} else {
		if(!jsvalue.startsWith("99") && !jsvalue.startsWith("INTL")){
			req = false;
		}
	}
	//console.log("required = " + req);
	
	$("#loginInfo").on("click", "#btnAdd", function(){
		var rowLength = $(".cloned-row").length;
		//console.log("row length is " + rowLength);
		if(rowLength < 20){
			var cloneRow = $(".cloned-row:first");
			//console.log("clone index is " + cloneRow.index());
			
			var thisRow = cloneRow.clone(true);
			var rowIndex = rowLength;
			//console.log("this row index is " + rowIndex);
			thisRow.find("input").removeClass("border-danger").val("");
			thisRow.find("input[id*='keyfld']").attr("id", "keyfld"+rowIndex).select();
			thisRow.find("input[id*='mstracctname']").attr("id", "mstracctname"+rowIndex);
			thisRow.find("input[id*='acctcomm']").attr("id", "acctcomm"+rowIndex);
			thisRow.find("span").attr("id", "charcount"+rowIndex);
			if(rowLength == 19) {
				// remove add button from this row
				thisRow.find("input[id*='btnAdd']").addClass("d-none");
			}
			
			thisRow.appendTo(".row-container");
			
			$("#jobnext-btn").hide();
			$("html, body").animate({
				scrollTop: $("#logininfo_btn").offset().top
			}, 1000);
		} else {
			$(this).addClass("d-none");
		}
	});
	
	$("#loginInfo").on("click", "#btnDel", function(){
		if($(".cloned-row").length>1){
			$(this).parent().parent().parent().remove();
			$("#jobnext-btn").show();
			$("#charcount").text("");
		}else{
			$(this).parent().parent().parent().find("input").val("");
			$(this).parent().parent().parent().find("input:first").select();
			$(this).parent().parent().parent().find("input:first").focus();
		}
	});
	
	var logins;
	
	$(document).on({
		"focusin":function(){
			logins = $(".keyfield").map(function(index){
				if($(this).val()!=null && $(this).val()!=""){
					return $(this).val();
				}
			}).toArray();
			logins.slice(-1);
		},
		"blur":function(){
			var keyfldval = $.trim($(this).val());
			var keyfld = $(this);
			// 9/14/2021 TLF - removed ajax keyfield validation since it is checking login ID
			// against all recorded IDs instead of by customer ID, but even 
			// this isn't necessary since the above array checks the 
			// uniqueness of the login IDs entered for that customer ID
			/*var result;
			$.ajax({
				url:"ajaxKeyfieldResult.action",
				data:{keyfield: keyfldval},
				dataType:"json",
				async:false,
				success:function(data){
					result = data.result;
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
				/*if(result=="true"){
					throw "Login ID unavailable";
				}*/
				$("#jobnext-btn").show();
				$("#loginformerror").text("");
				$("#formerror").text("");
				keyfld.removeClass("border-danger");
			}catch(msg){
				keyfld.select();
				keyfld.addClass("border-danger");
				$("#loginformerror").text(msg);
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 1500);
			}
		}
	},".keyfield");
	
	var mstraccts;
	
	$(document).on("blur", ".mstracctnm", function(){
		try{
			var mstracct = $.trim($(this).val());
			if(mstracct.length > 50){
				throw "Master Account Name cannot be greater than 50 characters";
			}
			$(this).removeClass("border-danger");
			$("#loginformerror").text("");
			$("#formerror").text("");
		}catch(msg){
			$(this).select();
			$(this).addClass("border-danger");
			$("#loginformerror").text(msg);
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
		}
	});
		
	$("#jobnext-btn").click(function(){
		try{
			if($("#loginformerror").text()!=""){
				throw "Please fix form error(s):";
			}
			$(".keyfield").each(function(i){
				if(req){
					if(!$(this).val() && $(this).is(":visible")){
						$(this).addClass("border-danger");
						throw "Please enter a value for Login ID";
					}else{
						$(this).removeClass("border-danger");
					}
				}
			});
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