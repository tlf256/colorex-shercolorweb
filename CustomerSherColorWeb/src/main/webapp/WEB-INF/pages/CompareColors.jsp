<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<%@ taglib prefix="s" uri="/struts-tags" %>
 
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name=""/>Compare Colors</title>
		<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 	
  		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
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
					console.log("selected value - " + selectedValue);
					
					if (selectedValue === "COMPET"){
						$('#colorCompanies').removeClass('d-none');
					} else {
						$('#colorCompanies').addClass('d-none');
					}
					
					if(this.checked) {
						$('.form-control-feedback, .help-block').remove();
						$('.has-feedback').removeClass('has-error has-feedback');
						$('#partialColorNameOrId').val('');
				    }
					
				});
				
				//validate partialColorNameOrId if custom manual or custom match
				//prevent special characters < or > from being entered
				$(document).on({
					'keypress blur': function(){
						if(selectedValue == "CUSTOMMATCH"){
							try{
								if(event.key == ">" || event.key == "<"){
									//console.log("< or > keypress");
									throw '<s:text name="global.noLtOrGt"/>'; 
								}
								if($(this).val().includes(">") || $(this).val().includes("<")){
									throw '<s:text name="global.invalidEntryLtGt"/>';
								}
								$(document).find('#errortxt').remove();
								$('input:submit').attr('disabled', false);
							} catch(msg){
								if(event.type=="keypress"){
									event.preventDefault();
								}
								if(!$(document).find('#errortxt').is(':visible')){
									$(this).parent().append('<div id="errortxt" class="text-danger mt-2"></div>');
								}
								$(document).find('#errortxt').text(msg);
								if(event.type=="blur"){
									$(this).focus();
									$('input:submit').attr('disabled', true);
								}
							}
						}
					}
				}, '#partialColorNameOrId');
				
			});
		</script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			<div class="row mt-3">
				<div class="col-sm-2"></div>
				<div class="col-sm-8"><h3>Compare Two Colors</h3></div>
				<div class="col-sm-2"></div>
			</div>
			<br>
			<s:form id="compareColorsForm" action="" validate="true" focusElement="partialColorNameOrId" theme="bootstrap">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1">
					</div>
            		<div class="col-lg-8 col-md-8 col-sm-10">
            			<div class="form-group">
            				<strong><s:text name=""/>Choose Standard Source:</strong>
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
							<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}" action="compareColorsNextAction"/>
						</div>
						<div class="col-lg-7 col-md-7 col-sm-9 col-xs-9">
							<s:submit cssClass="btn btn-secondary" action="" value="%{getText('global.back')}" />
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