<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<%@ taglib prefix="s" uri="/struts-tags" %>
 
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="compareColors.compareColors"/></title>
		<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 	
  		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
 		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
  		<script type="text/javascript" charset="utf-8"	src="js/moment-with-locales.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.2.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/getcolorautocomplete-1.5.1.js"></script>
		<style>
		.ui-autocomplete {
		    max-height: 300px;
		    overflow-y: auto;
		    overflow-x: hidden;
		}
		</style>
		<script type="text/javascript" charset="utf-8">
		$(document).ready(function(){
			$('#cmpColorsNxt').on('click', function(){
				
				var selectedValue;
				$("[id^=selectedCoTypes]").change(function(){
					selectedValue = $("[id^=selectedCoTypes]:checked").val();
					console.log("selected value - " + selectedValue);
					
				
				$('#compareColorsForm').submit();
				
				var selected = $('#existingSrc').getAttribute('selected');
				console.log("color match selected: " + selected);
				
				if(selected == "true"){
					var displayMessage = i18n['closestColors.loadingColorMatches'];
					pleaseWaitModal_show(displayMessage, null);
				}
			});
		});
		</script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
				<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
					<div class="card card-body bg-light mt-4">
					   <div class="row">
							<div class="col-lg-4 col-md-4 col-sm-3 col-xs-4">
								<span class="badge badge-secondary" style="font-size: 1.2rem;"><s:text name="compareColors.compareColors"/></span>
							</div>
							<div class="col-lg-3 col-md-3 col-sm-1 col-xs-0"></div>
							<div class="col-lg-5 col-md-5 col-sm-7 col-xs-8"></div>
						</div>
					</div>
				</div>
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1">
				</div>
				<div class="col-lg-8 col-md-8 col-sm-10">
					<s:actionerror/>
				</div>
				<div class="col-lg-2 col-md-2 col-sm-1">
				</div>
			</div>
			<s:form id="compareColorsForm" action="spectroCompareColorsNextAction" validate="true" focusElement="partialColorNameOrId" theme="bootstrap">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1">
					</div>
            		<div class="col-lg-8 col-md-8 col-sm-10">
            			<div class="form-group mt-4">
            				<strong><s:text name="compareColors.chooseStandardSourceColon"/></strong>
            			</div>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1"></div>
				</div>
	    		<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1">
					</div>
					<div class="col-lg-8 col-md-8 col-sm-10">
						<div class="form-check ml-3">
						  <input class="form-check-input" type="radio" name="standardSource" value="new" id="newSrc" checked/> New/Saved Color <br>
            			  <input class="form-check-input" type="radio" name="standardSource" value="existing" id="existingSrc" /> Existing Color Match
						</div>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1">
					</div>
				</div>
				<br>
				<br>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1">
					</div>
					<div class="col-lg-8 col-md-8 col-sm-10">
						<s:hidden name="reqGuid" value="%{reqGuid}" />
						<button class="btn btn-primary" id="cmpColorsNxt"><s:text name="global.next"/></button>
						<s:submit class="btn btn-secondary pull-right" value="%{getText('global.cancel')}" action="userCancelAction"/>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1">
					</div>
				</div>
			</s:form>
		</div>
		<br>
		<br>
		<br>
		<br>
  
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>