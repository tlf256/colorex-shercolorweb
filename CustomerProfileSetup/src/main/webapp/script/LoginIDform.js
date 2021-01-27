	/**
 * 
 */
$(document).ready(function(){
	
	var jsvalue = String($.trim($("#customerid").text()));
	
	$("#jobnext-btn").addClass("d-none");
	
	$("#loginInfo").on("click", "#btnAdd", function(){
		if($(".cloned-row").length < 10){
			$(".cloned-row:last").clone(true).appendTo(".cloned-row:last");
			$(".cloned-row:last").find("input:first").select();
			$(".cloned-row:last").find("input").removeClass("border-danger");
			$(".cloned-row:last").find("input").val("");
			//$(".cloned-row:last").find("input:last").val("");
			$("#charcount").text("");
			$(".cloned-row").each(function(i){
				$(this).attr("id", "clonedrow"+i);
				$(this).find("input:eq(0)").attr("id", "keyfld"+i);
				$(this).find("input:eq(1)").attr("id", "mstracctname"+i);
				$(this).find("input:eq(2)").attr("id", "acctcomm"+i);
				$(this).find("span").attr("id", "charcount"+i);
			});
			$("#jobnext-btn").hide();
			$("html, body").animate({
				scrollTop: $("#logininfo_btn").offset().top
			}, 2000);
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
		"focusout":function(){
			$("#jobnext-btn").removeClass("d-none");
		},
		"blur":function(){
			$("#jobnext-btn").removeClass("d-none");
			var keyfldval = $.trim($(this).val());
			var keyfld = $(this);
			var result;
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
			});
			try{
				if(keyfldval.length > 20){
					throw "Login ID cannot be greater than 20 characters";
				}
				if($.inArray(keyfldval,logins)!=-1){
					throw "Please enter unique values for Login ID";
				}
				if(result=="true"){
					throw "Login ID unavailable";
				}
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
				if(!jsvalue.startsWith("99")){
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