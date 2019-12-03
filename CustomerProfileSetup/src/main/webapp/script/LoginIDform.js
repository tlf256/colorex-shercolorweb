	/**
 * 
 */
$(document).ready(function(){
	var jsvalue = String($.trim($("#customerid").text()));
	$("#jobnext-btn").addClass("d-none");
	
	$("#loginInfo").on("click", "#btnAdd", function(){
		$(".cloned-row:last").clone(true).appendTo(".cloned-row:last");
		$(".cloned-row:last").find("input:first").select();
		$(".cloned-row:last").find("input").removeClass("border-danger");
		$(".cloned-row:last").find("input:first").val("");
		$(".cloned-row:last").find("input:last").val("");
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
	
	var valid;
	var logins;
	var result;
	
	$("#loginInfo").on({
		"focusin":function(){
			logins = $(".keyfield").map(function(index){
				return $(this).val();
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
			$.ajax({
				url:"ajaxKeyfieldResult.action",
				data:{keyfield: keyfldval},
				dataType:"json",
				async:false,
				success:function(data){
					result = data.result;
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
						valid = true;
						$("#jobnext-btn").show();
						$("#loginformerror").text("");
						$("#formerror").text("");
						keyfld.removeClass("border-danger");
					}catch(msg){
						valid = false;
						keyfld.focus();
						keyfld.addClass("border-danger");
						$("#loginformerror").text(msg);
						$("html, body").animate({
							scrollTop: $("#required-hidden").offset().top
						}, 1500);
					}
				}
			});
		}
	},".keyfield");
	
	var mstraccts;
	
	$("#loginInfo").on({
		"change":function(){
			try{
				var mstracct = $.trim($(this).val());
				if(mstracct.length > 50){
					throw "Master Account Name cannot be greater than 50 characters";
				}
				valid = true;
				$(this).removeClass("border-danger");
				$("#loginformerror").text("");
				$("#formerror").text("");
			}catch(msg){
				valid = false;
				$(this).focus();
				$(this).addClass("border-danger");
				$("#loginformerror").text(msg);
				$("html, body").animate({
					scrollTop: $("#required").offset().top
				}, 1500);
			}
		}
	}, ".mstracctnm");
		
	//character counter for account comment field
	$("#loginInfo").on("keyup", "input[id^='acctcomm']", function(){
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
	});
		
	$("#jobnext-btn").click(function(){
		try{
			if(valid===false){
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
				scrollTop: $("#required").offset().top
			}, 1500);
		}
	});
});