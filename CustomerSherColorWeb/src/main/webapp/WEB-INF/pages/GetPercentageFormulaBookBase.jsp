<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!doctype html>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="global.confirmColorAndBase"/></title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/getcolorautocomplete-1.4.4.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/BasicFieldValidator.js"></script>
		<script type="text/javascript" src="script/PctCancel.js"></script>
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

	
			<s:form id="GetPctForm" action="GetPercentageFormulaBookBaseAction" validate="true" focusElement="percentOfFormula" theme="bootstrap">
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
							<s:textfield name="percentOfFormula" id="percentOfFormula" label="%{getText('global.percentOfFormula')}" size="10" maxlength="3" cssStyle="font-size: 16px;"  
								onkeypress="return isNumber(event)" required="true" oninvalid="this.setCustomValidity('%{getText('global.pleaseFillOutThisField')}');" oninput="setCustomValidity('')" />
						</div>
				</div>
				<div class="row">
						<div class="col-sm-2">
						</div>	
						<div class="col-sm-2">
							<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}" 
									  onclick="return verifyFormulaSelected();" action="fbBaseUserNextAction"/>
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
			<script>
				function isNumber(evt) {
				    evt = (evt) ? evt : window.event;
				    var charCode = (evt.which) ? evt.which : evt.keyCode;
				    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
				        return false;
				    }
				    return true;
				}
				
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
		</div>
		
		<br>
		<br>
		<br>
		<script>
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