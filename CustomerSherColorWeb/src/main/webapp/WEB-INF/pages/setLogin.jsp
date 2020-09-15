<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!doctype html>

<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><s:text name="global.login"/></title>
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css"
	type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8" src="js/popper.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>

</head>
<body>
	<!-- Fixed navbar -->
	<nav class="navbar navbar-dark bg-dark navbar-expand-md"
		style="padding-top: 0px; padding-bottom: 0px;">
		<a href='#'><img src="graphics/shercolor-sm.jpg" alt="Sher-Color"
			style="height: 3.44rem;" /></a>
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
				<h1 class="h3 mb-3 font-weight-normal"><s:text name="setLogin.signIn"/></h1>

			</div>
			<div class="row">
				<%-- 						<s:if test="hasActionMessages()"> --%>
				<%-- 						      <s:actionmessage/> --%>
				<%-- 						</s:if> --%>
			</div>



			<div class="form-label-group">
				<label class="sw-label" for="userID"><s:text name="setLogin.userID"/></label>
				<s:textfield name="userId" id="userID"
					placeholder="%{getText('setLogin.userIDPlaceholder')}" size="30" maxlength="30"
					cssStyle="font-size: 16px;" autofocus="autofocus"></s:textfield>
			</div>
			<div class="form-label-group">
				<label class="sw-label" for="userPass"><s:text name="setLogin.password"/></label>
				<s:password name="userPass" id="userPass"></s:password>
			</div>
			<div class="form-row">
				<s:submit cssClass="btn btn-primary btn-lg btn-block active"
					id="LoginFocus" autofocus="autofocus" value="%{getText('setLogin.logIn')}"
					action="loginUserAction" />

			</div>

		</s:form>
		<div class="center-text-parent">
			<p class="text-center sw-textbox" >
				<s:text name="setLogin.questions"/>
			</p>
		</div>
	</div>
	
	<script type="text/javascript">
// 			$("#LoginFocus").on("click", function() {
// 			    $(this).prop("disabled", true);
// 			});
        $("#loginForm").submit(function () {
            //disable the submit button
            $("#LoginFocus").attr("disabled", true);
            return true;

        });
		</script>
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