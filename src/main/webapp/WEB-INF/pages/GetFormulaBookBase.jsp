<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!doctype html>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="global.confirmColorAndBase" /></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 	
		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.1.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/BasicFieldValidator.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-3">
				</div>
				<div class="col-sm-6">
				</div>
				<div class="col-sm-3">
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-sm-2">
				</div>
			</div>

	
			<s:form id="GetPctForm" action="ProcessFormulaBookBaseAction" validate="true" focusElement="percentOfFormula" theme="bootstrap">
				<div class="row">
	            
						<div class="col-sm-2">
						</div>
	            		<div class="col-sm-4">
	            			<s:select id="formulasList" label="%{getText('global.selectFormulaToUse')}" 
	            				headerKey="-1" headerValue="%{getText('global.confirmColorAndBase')}"
								list="colorBases" name="selectedColorBase"  autofocus="autofocus" required="required" />
							<div id="formulasDropdownErrorText" style="color:red" class="d-none">
								<s:text name="global.pleaseSelectAFormula"/>
							</div>
							<br>
						</div>
				
				</div>
				<div class="row">
					
						<div class="col-sm-2">
							<s:hidden name="reqGuid" value="%{reqGuid}"/>
						</div>
						<div class="col-sm-2">
						</div>
					
				</div>
			
				<div class="row">
						<div class="col-sm-2">
						</div>	
						<div class="col-sm-2">
							<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}"
									  onclick="return verifyFormulaSelected();" action="fbBase2UserNextAction"/>
						</div>
						<div class="col-sm-2">	
						</div>
						<div class="col-sm-2">
						</div>
						<div class="col-sm-2">
			    			<s:submit cssClass="btn btn-secondary" value="%{getText('global.cancel')}" action="userCancelAction"/>
			    		</div>
		    	</div>
			</s:form>
		</div>
		
		<br>
		<br>
		<br>
		<script>
			function verifyFormulaSelected(){
				// require formula choice if user hasn't already 
				var chooseFormulaText = '<s:text name="global.confirmColorAndBase"/>'
				var formulaText = $("select[id='formulasList'] option:selected").text();
				if (formulaText == null || formulaText == "" || formulaText == chooseFormulaText){
					$("#formulasList").focus();
					$("#formulasDropdownErrorText").removeClass("d-none");
					return false;
				} else {
					return true;
				}
			}
		</script>
  
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>