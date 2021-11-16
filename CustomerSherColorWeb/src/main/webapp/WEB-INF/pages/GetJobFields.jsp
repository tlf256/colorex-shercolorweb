<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags"%>

<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><s:text name="global.enterJobInfo" /></title>
<!-- JQuery -->
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css">
<link rel="stylesheet" href="css/font-awesome.css" type="text/css">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
<style>
.btn {
	margin-left: 3px;
	margin-right: 3px;
}
</style>
</head>

<body>
	<!-- including Header -->
	<s:include value="Header.jsp"></s:include>

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3"></div>
			<div class="col-sm-6"></div>
			<div class="col-sm-3">
				<s:set var="thisGuid" value="reqGuid" />
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-sm-2"></div>
		</div>
		
		<s:form action="processJobFieldsAction" validate="true"
			focusElement="processJobFieldsAction_jobFieldList_0__enteredValue"
			theme="bootstrap">

			<div class="row">
				<div class="col-sm-2">
					<s:hidden name="reqGuid" value="%{reqGuid}" />
					<s:hidden name="updateMode" value="%{updateMode}" />
					<s:set var="setFirstFieldFocus" value="%{'false'}" />
				</div>
			</div>
			<s:iterator value="jobFieldList" status="outerStat">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1"></div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-3">
						<strong><s:property value="screenLabel" /><s:text name="global.colonDelimiter"/></strong>
					</div>
					<div class="col-lg-2 col-md-4 col-sm-6 col-xs-6">
						<%-- 							<s:if test="%{#setFirstFieldFocus==%{'false'}}"> --%>
						<%--     							<s:textfield name="jobFieldList[%{#outerStat.index}].enteredValue" autofocus="autofocus"/> --%>
						<%--     							<s:set var="setFirstFieldFocus" value="%{'true'}"/> --%>
						<%-- 							</s:if> --%>
						<%-- 							<s:else> --%>
						<s:textfield class="entval" name="jobFieldList[%{#outerStat.index}].enteredValue" />
						<%-- 							</s:else> --%>

					</div>
					<div class="col-lg-5 col-md-3 col-sm-1 col-xs-1" id="errormsg">
						<strong><s:property value="requiredText" /></strong>
					</div>
					<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1"></div>
				</div>
			</s:iterator>

			<div class="row mt-2">

				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1"></div>
				<s:if test="updateMode==1">
					<div class="col-lg-4 col-md-4 col-sm-10 col-xs-1">
						<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}"
							action="processJobFieldUpdateAction" />
						<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.cancel')}"
							action="userCancelAction" />
					</div>
				</s:if>
				<s:else>
					<div class="col-lg-4 col-md-6 col-sm-10 col-xs-10">
						<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}"
							action="processJobFieldsAction" />
						<s:if test = "%{accountIsDrawdownCenter==true}">
							<s:submit cssClass="btn btn-secondary" value="%{getText('getJobFields.copyFromExistingJob')}"
								action="copyExistingJobFieldsAction" />
						</s:if>
						<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.cancel')}"
							action="userCancelAction" />
					</div>
				</s:else>
				<div class="col-lg-6 col-md-4 col-sm-1 col-xs-1"></div>
			</div>
			
		</s:form>
	</div>

	<br>
	<br>
	<br>
	<script>
		$(document).ready(function () {
			// if fields have been copied in, highlight Next
			if ("${lookupControlNbr}" > 0){
				$(".btn-primary").first().focus();
			// otherwise highlight first field
			} else {
				var txtBox=document.getElementById("processJobFieldsAction_jobFieldList_0__enteredValue" );
				txtBox.focus();
			}
			 
			//validate jobFields enteredValue
			//prevent special characters < or > from being entered
			$(document).on({
				'keypress blur':function(){
					try{
						if(event.key == ">" || event.key == "<"){
							throw '<s:text name="global.noLtOrGt"/>';
						}
						if($(this).val().includes(">") || $(this).val().includes("<")){
							throw '<s:text name="global.invalidEntryLtGt"/>';
						}
						$('.entval').each(function(){
							$(this).parents('.row').find('#errortxt').remove();
							$(this).removeClass('border-danger');
						});
						$('input:submit').attr('disabled', false);
					} catch(msg){
						if(event.type=="keypress"){
							event.preventDefault();
						}
						if(!$(document).find('#errortxt').is(':visible')){
							$(this).parents('.row').find('#errormsg').append('<div id="errortxt" class="text-danger"></div>');
						}
						$(this).parents('.row').find('#errortxt').text(msg)
						$(this).addClass('border-danger');
						if(event.type=="blur"){
							$(this).focus();
							$('input:submit').attr('disabled', true);
						}
					}
				}
			}, '.entval');
			
		 });
		<!--
		  function HF_openSherwin() {
		    var popupWin = window.open("http://www.sherwin-williams.com", "Sherwin", "resizable=yes,toolbar=yes,menubar=yes,statusbar=yes,directories=no,location=yes,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }  
		  function HF_openLegal() {
		    var popupWin = window.open("http://www.sherwin-williams.com/terms/", "legal", "resizable=no,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }
		  function HF_openPrivacy() {
		    var popupWin = window.open("http://privacy.sherwin-williams.com/", "privacy", "resizable=yes,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=640,height=480,left=10,top=10");
		    popupWin.focus();
		  }
		//-->
		</script>

	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>

</body>
</html>