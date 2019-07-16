<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!doctype html>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Better Performance With Different Base</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/GetColorAutoCompleteV1-3-0.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/BasicFieldValidator.js"></script>
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

	
			<s:form id="GetProdFamily" action="GetProductFamilyAction" validate="true" focusElement="prodFambly" theme="bootstrap">
			<div class="row">
						<div class="col-sm-2">
							<s:hidden name="reqGuid" value="%{reqGuid}"/>
						</div>
	            		<div class="col-sm-4">
	            			<s:select id="prodFambly" label="Better Performance Found in Different Base" 
	            				headerKey="-1" headerValue="Confirm Product"
								list="colorProdFamilies" name="selectedProdFamily"
								autofocus="autofocus" required="required" />
						</div>
				</div>
				<div class="row">
						<div class="col-sm-2">
						</div>
						<div class="col-sm-2">
						</div>
				</div>
				<div id="frstFormula" style="display: none;">
					<div  class="row">
						<div class="col-sm-2">
						</div>
						<div class="col-sm-2">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(0).clrntSysId}*COLORANT</Strong>
						</div>
						<div class="col-sm-1" align="center">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(0).incrementHdr[0]}</Strong>
						</div>
						<div class="col-sm-1" align="center">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(0).incrementHdr[1]}</Strong>
						</div>
						<div class="col-sm-1" align="center">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(0).incrementHdr[2]}</Strong>
						</div>
						<div class="col-sm-1" align="center">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(0).incrementHdr[3]}</Strong>
						</div>
					</div>
					<s:iterator value="bothFormulas.get(0).ingredients">	
						<div class="row">
							<div class="col-sm-2">
							</div>
							<div class="col-sm-2">
								<s:property value="tintSysId"/>-<s:property value="name"/> 
							</div>
							<s:iterator value="increment" status="stat">
								<div class="col-sm-1" align="center">
									<s:property value="top"/>
								</div>
							</s:iterator>
							</div>
				    </s:iterator>
			</div>
			<div id="scndFormula" style="display: none;">
				<div class="row">
						<div class="col-sm-2">
						</div>
						<div class="col-sm-2">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(1).clrntSysId}*COLORANT</Strong>
						</div>
						<div class="col-sm-1" align="center">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(1).incrementHdr[0]}</Strong>
						</div>
						<div class="col-sm-1" align="center">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(1).incrementHdr[1]}</Strong>
						</div>
						<div class="col-sm-1" align="center">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(1).incrementHdr[2]}</Strong>
						</div>
						<div class="col-sm-1" align="center">
							<Strong>${sessionScope[thisGuid].formResponse.getFormulas().get(1).incrementHdr[3]}</Strong>
						</div>
					</div>
	
	    			<s:iterator value="bothFormulas.get(1).ingredients">	
						<div class="row">
							<div class="col-sm-2">
							</div>
							<div class="col-sm-2">
								<s:property value="tintSysId"/>-<s:property value="name"/> 
							</div>
							<s:iterator value="increment" status="stat">
								<div class="col-sm-1" align="center">
									<s:property value="top"/>
								</div>
							</s:iterator>
							</div>
				    </s:iterator>
				</div>
				<br>
				<br>
				<div class="row">
						<div class="col-sm-2">
						</div>	
						<div class="col-sm-2">
							<s:submit cssClass="btn btn-primary" value="Next" action="prodFamilyUserNextAction"/>
						</div>
						<div class="col-sm-2">	
						</div>
						<div class="col-sm-2">
						</div>
						<div class="col-sm-2">
			    			<s:submit cssClass="btn btn-secondary" value="Cancel" action="userCancelAction"/>
			    		</div>
		    	</div>
			</s:form>
			<script>
			$(document).ready(function() {
				var firstFormula = '<s:property value="firstFormula"/>';
				$('#prodFambly').on('change',function(){
				    if( $(this).val()===firstFormula){
					    $("#frstFormula").show()
					    $("#scndFormula").hide()
				    }
				    else{
				    	if( $(this).val()===-1){
						    $("#frstFormula").hide()
						    $("#scndFormula").hide()
				    	}
				    	else {
						    $("#frstFormula").hide()
						    $("#scndFormula").show()
				    	}
				    }
				});
			});

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