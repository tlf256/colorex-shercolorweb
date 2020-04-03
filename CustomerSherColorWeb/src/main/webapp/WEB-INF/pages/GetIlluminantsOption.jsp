<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Primary Light Source</title>
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
		<s:set var="thisGuid" value="reqGuid" />
		<style>
	        .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
	    </style>
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

			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong>Color Company:</strong>
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].colorComp}
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong>Color ID:</strong>
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].colorID}
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong>Color Name:</strong>
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].colorName}
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
				</div>
				<div class="col-sm-4">
					<div class="card card-body sw-bg-main"></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong>Sales Number:</strong>
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].salesNbr} ${sessionScope[thisGuid].quality} ${sessionScope[thisGuid].composite} ${sessionScope[thisGuid].finish}
				</div>
			</div>
						<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].prodNbr} ${sessionScope[thisGuid].intExt} ${sessionScope[thisGuid].klass} 
				</div>
			</div>

			<br>
			<br>
			<br>
	
			<s:form action="GetLightSourceAction" validate="true" theme="bootstrap">
				<div class="row">
	            
  						<div class="col-sm-2">
						</div>
	            		<div class="col-sm-4">
						</div>					
				</div>
				<div class="row">
  						<div class="col-sm-2">
						</div>
	            		<div class="col-sm-4">
   								<s:hidden name="reqGuid" value="%{reqGuid}"/>
   								<div class="form-group">
			           				<strong>Choose Primary Light Source</strong>
			           				<div class="controls">
			           					<s:iterator value="lightSources" status="i">
			            					<div class="form-check">
			            					  <s:if test="%{#i.index == 0}">
			            					  	<input class="form-check-input" type="radio" name="selectedLightSources" value='<s:property value="key"/>' id="selectedLightSources-<s:property value="%{#i.index}"/>" checked>
			            					  </s:if>
			            					  <s:else>
			            					  	<input class="form-check-input" type="radio" name="selectedLightSources" value='<s:property value="key"/>' id="selectedLightSources-<s:property value="%{#i.index}"/>">
			            					  </s:else>
											  <label class="form-check-label font-weight-normal" for="selectedLightSources-<s:property value="%{#i.index}"/>">
											    <s:property value="value"/>
											  </label>
											</div>
			            				</s:iterator>
			           				</div>
			           			</div>
   								<%-- <s:radio label="Choose Primary Light Source" name="selectedLightSources" list="lightSources" value="defaultLightSourceValue" /> --%>
						</div>
				</div>
				
					
				<div class="row">				
						<div class="col-sm-2">
						</div>	
						<div class="col-sm-2">
							<s:submit cssClass="btn btn-primary" value="Next" action="lsUserNextAction" autofocus="autofocus"/>
						</div>
						<div class="col-sm-2">	
							<s:submit cssClass="btn btn-secondary" value="Back" action="lsUserBackAction"/>
   						</div>
						<div class="col-sm-2">
						</div>
						<div class="col-sm-2">
			    			<s:submit cssClass="btn btn-secondary" value="Cancel" action="userCancelAction"/>
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