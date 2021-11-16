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
  		<link rel="stylesheet" href="css/font-awesome.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
 		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
  		<script type="text/javascript" charset="utf-8"	src="js/moment-with-locales.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/getcolorautocomplete-1.4.4.js"></script>
		<style>
		.ui-autocomplete {
		    max-height: 300px;
		    overflow-y: auto;
		    overflow-x: hidden;
		}
		</style>
		<script type="text/javascript" charset="utf-8">
			$(function(){
				var selectedValue;
				$("[id^=selectedCoTypes]").change(function(){
					selectedValue = $("[id^=selectedCoTypes]:checked").val();
					//console.log("selected value - " + selectedValue);

					switch(selectedValue){
						case "SW":
							enableTextInput();
							$('#colorCompanies').addClass('d-none');
							break;
						case "COMPET":
							$('#colorCompanies').removeClass('d-none');
							enableTextInput();
							break;
						case "CUSTOMMATCH":
							disableTextInput();
							$('#colorCompanies').addClass('d-none');
							break;
						case "MEASURE":
							disableTextInput();
							$('#colorCompanies').addClass('d-none');
							break;
						default:
							enableTextInput();
							$('#colorCompanies').addClass('d-none');
							break;
					}
					
				});
			});

			function disableTextInput(){
				$('#partialColorNameOrId').val('MANUAL');
				$('#partialColorNameOrId').prop('disabled', true);
			}

			function enableTextInput(){
				$('#partialColorNameOrId').val('');
				$('#partialColorNameOrId').prop('disabled', false);
			}
		</script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			<div class="row mt-4">
				<div class="col-sm-2"></div>
				<div class="col-sm-8"><h3><s:text name="compareColors.compareColors"/></h3></div>
				<div class="col-sm-2"></div>
			</div>
			<br>
			<s:form id="compareColorsForm" action="" validate="true" focusElement="partialColorNameOrId" theme="bootstrap">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1">
					</div>
            		<div class="col-lg-8 col-md-8 col-sm-10">
            			<div class="form-group">
            				<strong><s:text name="compareColors.chooseStandardSourceColon"/></strong>
            				<div class="controls">
            					<s:iterator value="sourceOptions" status="i">
	            					<div class="form-check">
	            					  <s:if test="%{#i.index == 0}">
	            					  	<input class="form-check-input" type="radio" name="selectedCoTypes" value='<s:property value="key"/>' id="selectedCoTypes-<s:property value="%{#i.index}"/>" checked>
	            					  </s:if>
	            					  <s:else>
	            					  	<input class="form-check-input" type="radio" name="selectedCoTypes" value='<s:property value="key"/>' id="selectedCoTypes-<s:property value="%{#i.index}"/>">
	            					  </s:else>
									  <label class="form-check-label font-weight-normal" for="selectedCoTypes-<s:property value="%{#i.index}"/>">
									    <s:property value="value"/>
									  </label>
									</div>
	            				</s:iterator>
            				</div>
            			</div>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1"></div>
				</div>
				<div class="row mt-sm-2 d-none" id="colorCompanies">
					<div class="col-lg-2 col-md-2 col-sm-2"></div>
					<div class="col-lg-4 col-md-4 col-sm-6">
						<s:select label="%{getText('getColor.companyName')}" id="companiesList" list="colorCompanies"  />
					</div>
					<div class="col-lg-6 col-md-6 col-sm-4"></div>
				</div>
				<br>
				<div>
					<div class="row" id="colorEntry">
						<div class="col-lg-2 col-md-2 col-sm-1">
							<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
							<s:hidden name="colorData" id="colorData" value=""/>
							<s:hidden name="compare" id="compareColors" value="%{compare}"/>
						</div>
						<div class="col-lg-8 col-md-8 col-sm-10">
							<s:textfield name="partialColorNameOrId" id="partialColorNameOrId" label="%{getText('getColor.enterColorNameOrNumber')}" 
								placeholder="%{getText('getColor.chooseColorType')}" size="30" maxlength="30" 
								cssStyle="font-size: 16px;" autofocus="autofocus"  />
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1"></div>
					</div>
					<div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>	
						<div class="col-lg-1 col-md-1 col-sm-1 col-xs-3" id="nextBtnDiv">
							<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}" action="spectroCompareColorsNextAction"/>
						</div>
						<div class="col-lg-7 col-md-7 col-sm-9 col-xs-9">
							<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.cancel')}" action="userCancelAction"/>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
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