<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>SWDeviceHandler Svc Installation</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.2.js"></script>
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
						<h2>To install the SWDeviceHandler service:</h2>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						<h2>1.  Click the Download button below to download the Setup.exe to the local PC.</h2>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						<h2>2.  Double click on the setup.exe to install the SWDeviceHandler as a service.</h2>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						   
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						   
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						    
					</div>
				</div>
						
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						   
					</div>
				</div>	
				<div class="row">
					
						<div class="col-sm-2">
						</div>
						<div class="col-sm-4">
							<s:submit cssClass="btn btn-primary center-block btn-lg" value="Download" action="downloadExeAction"/>
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
		 $(window).on('load', function () {

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