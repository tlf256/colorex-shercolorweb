<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><s:text name="changePassword.changePassword" /></title>
	<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
	<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
	<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 	
	<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
	<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
	<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
	<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
	<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.3.js"></script>
	
	<script type="text/javascript">
	//update user's language preference
	function updateLanguage(){
		var selectedLang = $("select[id='languageList'] option:selected").val();
		console.log(selectedLang);
		
		$.ajax({
			url : "updateLocale.action",
			type : "POST",
			data : {
				request_locale : selectedLang,
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					// reload page to update the language
					location.reload();
				}
			},
			error : function(err) {
				alert('<s:text name="global.failureColon"/>' + err);
			}
		});
	}
	
	$(document).ready(function() {
	    // update dropdown to display the language that the user picked if they have done so
	    var userLanguage = "${session['WW_TRANS_I18N_LOCALE']}";
	    if (userLanguage != null && userLanguage != ""){
		    $("#languageList").val(userLanguage);
	    } else {
	    	$("#languageList").val("en_US");
	    }
	});
</script>
</head>
<body>
<!-- Fixed navbar -->
		<!-- Fixed navbar -->
		<nav class="navbar navbar-dark bg-dark navbar-expand-md" style="padding-top: 0px; padding-bottom: 0px;">
			<a href='#'><img src="graphics/shercolor-sm.jpg" alt="Sher-Color" style="height: 3.44rem;"/></a>
			<ul class="navbar-nav ml-auto">
				<li class="nav-item">
			   		<select class="bg-dark navbar-text" id="languageList" onchange="updateLanguage();">
					    <option value="en_US">English</option>
					    <option value="es_ES" class="d-none">Español</option>
					    <option value="zh_CN">中文</option>
				    </select>
				</li>
			</ul>
		</nav>
		
<!-- 			<div class="container-fluid theme-showcase body"> -->
<!--     	<div class="form-group row"><div class="col-lg-4 col-md-6 col-sm-10 col-xs-12"> -->
<!--     		<div class="card panel-primary"> -->
<!-- 	    		<div class="card-header"> -->
<!-- 					  For your security, please change your password using the following criteria:<br><br> -->
<!-- 	                  - Minimum of eight characters<br> -->
<!-- 	                  - Must have at least one capital letter and one lower-case letter<br> -->
<!-- 	                  - Must have at least one number and one special character (i.e. !@#$%^&amp;*) -->
<!-- 				</div> -->
<!-- 				<div class="card-body"> -->
					
<!-- 						 <div class="alert alert-danger"> -->
<%-- 							<span class="errorMessage"><span class="errors">Login failed. Your password has expired.</span></span> --%>
<!-- 					   </div> -->
					
<%-- 			    	<s:form id="passwordForm" action="passwordChangeAction" method="post" align="center" theme="bootstrap"> --%>
<!-- 						<div class="form-group row"> -->
<!-- 							<div class="form-group col-sm-10"> -->
<!-- 							    <label for="searchLoginId" class="control-label">Login Id:</label> -->
<!-- 						    	<input class="form-control" type="text" name="loginId" id="loginId" value="shercolortest" READONLY /> -->
<!-- 							</div> -->
<!-- 						</div> -->
<%--  						<s:hidden name="guid1" id="guid1" value="%{guid1}"/> --%>
<%--  						<s:password name="userPass" id="userPass" label="New Password:" placeholder="Enter password here"></s:password>  --%>
<%--  		    			<s:password name="userPassConfirm" id="userPassConfirm" label="Confirm:" placeholder="Confirm password here"></s:password>  --%>
<%--  		    			<s:submit cssClass="btn btn-primary btn-lg active" id="LoginFocus" autofocus="autofocus" value="Reset Password" action="passwordChangeAction"/>  --%>
<%-- 					</s:form> --%>
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div></div> -->
<!-- 	</div> /container -->
		

		<div class="container-fluid">
  	    		<div class="form-group row mt-5">
					<div class="col-lg-4 col-md-4 col-sm-3 col-xs-0">
					</div>	
					<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
						<s:hidden name="whereFrom1" id="whereFrom" value="%{whereFrom}"/>	
						<div class="alert alert-danger" role="alert" align="center">
							<c:set var="whereFrom" value="${whereFrom1}"/>
							<c:choose>
								<c:when test="${whereFrom == 'EXPIRED'}">
									<s:text name="changePassword.yourPasswordHasExpired" />
								 </c:when>
							 	<c:otherwise>
									<s:text name="changePassword.pleaseChangePassword" />
							 	</c:otherwise>
						 	</c:choose>
					   	</div>
				   	</div>
				   	<div class="col-lg-4 col-md-4 col-sm-3 col-xs-0">
					</div>	
				</div>
			<br>
			<div class="form-group row">
					<div class="col-lg-4 col-md-4 col-sm-3 col-xs-0">
					</div>	
					<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
						  <s:text name="changePassword.passCriteriaColon" /><br><br>
						  <ul>
						  	<li><s:text name="changePassword.minOfEightChars" /></li>
						  	<li><s:text name="changePassword.mustHaveOneUcOneLc" /></li>
						  	<li><s:text name="changePassword.mustHaveOneNumOneSc" /></li>
						  </ul>
					</div>
					<div class="col-lg-4 col-md-4 col-sm-3 col-xs-0">
					</div>	
		    	</div>

			<s:form id="passwordForm" action="passwordChangeAction" method="post" align="center" theme="bootstrap">

				<div class="form-group row">
 					<div class="col-lg-4 col-md-4 col-sm-3 col-xs-0">
					</div>	
					<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
						<s:if test="hasActionMessages()">
						      <s:actionmessage/>
						</s:if>
					</div>
					<div class="col-lg-4 col-md-4 col-sm-3 col-xs-0">
					</div>	
				</div>
				   	
	    		<div class="form-group row text-center" style="margin-bottom: 7rem;">
					<div class="col-lg-4 col-md-4 col-sm-3 col-xs-0">
					</div>	
					<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
						<s:hidden name="guid1" id="guid1" value="%{guid1}"/>
						<s:password name="userPass" id="userPass" label="%{getText('changePassword.newPasswordColon')}" placeholder="%{getText('changePassword.enterPasswordHere')}" size="30" maxlength="30"></s:password>
		    			<s:password name="userPassConfirm" id="userPassConfirm" label="%{getText('changePassword.confirmColon')}" placeholder="%{getText('changePassword.confirmPassword')}" size="30" maxlength="30"></s:password>
		    			<s:submit cssClass="btn btn-primary pull-left btn-lg active" id="LoginFocus" autofocus="autofocus" value="%{getText('changePassword.resetPassword')}" action="passwordChangeAction"/>
		    			<s:if test="whereFrom != 'EXPIRED'">
		    				<s:submit cssClass="btn btn-secondary pull-right btn-lg" id="cancelChange" value="%{getText('global.cancel')}" action="cancelPasswordChangeAction"/>
		    			</s:if>
		    		</div>
					<div class="col-lg-4 col-md-4 col-sm-3 col-xs-0">
					</div>	
		    	</div>
		    	
			</s:form>
		</div>

		
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
	</body>
</html>