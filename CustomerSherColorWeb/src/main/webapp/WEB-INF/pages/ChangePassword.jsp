<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Change Password</title>
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 	
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
	</head>
	<body>
<!-- Fixed navbar -->
		<!-- Fixed navbar -->
		<nav class="navbar navbar-dark bg-dark navbar-expand-md" style="padding-top: 0px; padding-bottom: 0px;">
			   <a href='#'><img src="graphics/shercolor-sm.jpg" alt="Sher-Color" style="height: 3.44rem;"/></a>
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
									Your password has expired.
								 </c:when>
							 	<c:otherwise>
									Please change your password.
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
						  For your security, please change your password using the following criteria:<br><br>
						  <ul>
						  	<li>Minimum of eight characters</li>
						  	<li>Must have at least one capital letter and one lower-case letter</li>
						  	<li>Must have at least one number and one special character (!@#$%^&amp;*)</li>
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
						<s:password name="userPass" id="userPass" label="New Password:" placeholder="Enter password here" size="30" maxlength="30"></s:password>
		    			<s:password name="userPassConfirm" id="userPassConfirm" label="Confirm:" placeholder="Confirm password here" size="30" maxlength="30"></s:password>
		    			<s:submit cssClass="btn btn-primary btn-lg active" id="LoginFocus" autofocus="autofocus" value="Reset Password" action="passwordChangeAction"/>
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