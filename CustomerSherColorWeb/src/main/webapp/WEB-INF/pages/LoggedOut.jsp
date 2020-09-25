<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!doctype html>

<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><s:text name="global.loggedOut"/></title>
<link rel=StyleSheet href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="${pageContext.request.contextPath}/css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="${pageContext.request.contextPath}/js/smoothness/jquery-ui.css" type="text/css">
<link rel=StyleSheet href="${pageContext.request.contextPath}/css/CustomerSherColorWeb.css" type="text/css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/script/customershercolorweb-1.4.2.js"></script>
</head>

<body>
	<!-- Fixed navbar -->
	<nav class="navbar navbar-dark bg-dark navbar-expand-md pt-0 pb-0">
		   <img src="${pageContext.request.contextPath}/graphics/shercolor-sm.jpg" alt="Sher-Color" style="height: 3.44rem;"/>
		   <s:set var="thisGuid" value="reqGuid" />
	</nav>
	<div class="container center-form-parent">
		<s:form class="form-sw-centered" id="loginForm"
			action="loginUserAction" method="post" align="center"
			theme="bootstrap">
			<div class="text-center mb-4">
				<!-- 	<img class="mb-4"
				src="graphics/shercolor-lg.jpg"
				alt="" width="72" height="72">
				-->
				<h1 class="h3 mb-5 mt-5 font-weight-normal">${loMessage}</h1>

			</div>



			<div class="form-row">
				<s:submit cssClass="btn btn-primary btn-lg btn-block active"
					id="LoginFocus" autofocus="autofocus" value="%{getText('loggedOut.logInAgain')}"
					action="loginAgainAction" />

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
