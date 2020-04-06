<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Choose Product</title>
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
		<script type="text/javascript" charset="utf-8"	src="script/GetProductAutoComplete.js"></script>
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
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
				</div>
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<strong><s:property value="screenLabel"/>:</strong><br>
					</s:iterator>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<s:property value="enteredValue" escapeHtml="false"/><br>
					</s:iterator>	
				</div>
				<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
				</div>
			</div>
			<br>

			<s:form action="productUserNextAction" validate="true" focusElement="partialProductNameOrId" theme="bootstrap">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong>Color Company:</strong>
					</div>
					<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
						${sessionScope[thisGuid].colorComp}
					</div>
					<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong>Color ID:</strong>
					</div>
					<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
						${sessionScope[thisGuid].colorID}
					</div>
					<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong>Color Name:</strong>
					</div>
					<div class="col-lg-2 col-md-3 col-sm-4 col-xs-6">
						${sessionScope[thisGuid].colorName}<br>
						<div class="card card-body sw-bg-main"></div>
					</div>
					<div class="col-lg-6 col-md-5 col-sm-4 col-xs-2">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong>Interior Bases:</strong>
					</div>
					<div class="col-lg-2 col-md-3 col-sm-4 col-xs-6">
						${sessionScope[thisGuid].intBases}<br>
					</div>
					<div class="col-lg-6 col-md-5 col-sm-4 col-xs-2">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong>Exterior Bases:</strong>
					</div>
					<div class="col-lg-2 col-md-3 col-sm-4 col-xs-6">
						${sessionScope[thisGuid].extBases}<br>
					</div>
					<div class="col-lg-6 col-md-5 col-sm-4 col-xs-2">
					</div>
				</div>

				<br>
				<br>
				<div class="row">
            		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-8 col-md-8 col-sm-10 col-xs-12">
						<s:if test="hasActionMessages()">
						      <s:actionmessage/>
						</s:if>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
            		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-8 col-md-8 col-sm-10 col-xs-12">
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
						<s:textfield name="partialProductNameOrId" id="partialProductNameOrId" label="Product" placeholder="Scan UPC or enter Rex, Sales Number or Product Description" size="30" maxlength="20" cssStyle="font-size: 16px;" autofocus="autofocus" />
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
						</div>
						<div class="col-lg-1 col-md-1 col-sm-1 col-xs-3">
							<s:submit cssClass="btn btn-primary pull-left" value="Next" action="productUserNextAction"/>
						</div>
						<div class="col-lg-7 col-md-7 col-sm-9 col-xs-9">
							<s:submit cssClass="btn btn-secondary" value="Back" action="productUserBackAction"/>
							<s:submit cssClass="btn btn-secondary pull-right" value="Cancel" action="userCancelAction"/>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">	
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