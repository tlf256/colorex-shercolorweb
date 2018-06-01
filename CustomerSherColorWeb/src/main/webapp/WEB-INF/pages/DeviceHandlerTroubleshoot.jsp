<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Enter Job Info</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
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
					<s:set var="thisGuid" value="reqGuid" />
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-sm-2">
				</div>
			</div>

	
			<s:form action="swdhInstallAction" validate="true"  theme="bootstrap">
			
				<div class="row">
					<div class="col-sm-2">
						<s:hidden name="reqGuid" value="%{reqGuid}"/>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						<h2>To troubleshoot the SWDeviceHandler service:</h2>
					</div>
				</div>
<br>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						<h3>1.  Restart the PC.</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						<h3>2.  Open the browser and log into the Customer SherColor Web application.</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						<h3>3.  At the welcome page, if there were devices (tinter, color eye) previously configured,
						there should be a display in the header showing the device status.</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						    
					</div>
				</div>

<br>
<br>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						  <h3>If not, please contact the Sherwin-Williams Help Desk.</h3>  
					</div>
				</div>
						
<br>
<br>
				<div class="row">
				
						<div class="col-sm-2">
						</div>
						<div class="col-sm-4">
							<s:submit cssClass="btn btn-primary center-block btn-lg" value="Next" action="userCancelAction"/>
						</div>
						<div class="col-sm-4">
			    			<s:submit cssClass="btn btn-secondary center-block btn-lg" value="Cancel" action="userCancelAction"/>
			    		</div>
			    	</div>
		    	
			</s:form>
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