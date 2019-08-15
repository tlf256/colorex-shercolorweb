<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery -->
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.0/jquery.validate.js"></script>
<script type="text/javascript" charset="utf-8" src="script/LoginIDform.js"></script>
<title>Login Form</title>
</head>
<body>
<!-- including Header -->
<s:include value="Header.jsp"></s:include>

<div class="container-fluid">
	<br>
	<div class="row">
		<div class="col-md-3 col-sm-2"></div>
		<div class="col-sm-2">
			<strong>Customer ID:</strong>
		</div>
		<div class="col-sm-2" id="customerid">
			${sessionScope["CustomerDetail"].customerId}
		</div>
	</div>
	<div class="row">
		<div class="col-md-3 col-sm-2"></div>
		<div class="col-sm-2">
			<strong>Customer Name:</strong>
		</div>
		<div class="col-sm-2">
			${sessionScope["CustomerDetail"].swuiTitle}
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-md-3 col-sm-2"></div>
		<div class="col-md-4 col-sm-2">
			<h3>Enter Login Information</h3>
		</div>
		<div class="col-md-3 col-sm-2"></div>
	</div>
	<br>
	<s:form id="loginInfo" action="getLoginInfo" method="post">
	<div class="row" id="required">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="formerror" class="text-danger"></div><br>
			<div id="loginformerror" name="loginerror" class="text-danger"></div>
			<strong>* Denotes Required Field</strong>
		</div>
		<!-- <div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div> -->
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<div class="cloned-row">
		<div class="row">
			<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
			<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
				<div id="keyflderror" class="text-danger"></div>
				<div id="mstraccterror" class="text-danger"></div>
			</div>
			<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		</div>
		<div class="row" id="keyfld">
			<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
			<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
				<s:textfield label="Login ID" name="login.keyField" class="keyfield" id="keyfld0" requiredLabel="true" />
			</div>
			<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
				<s:textfield label="Employee Name" name="login.masterAcctName" class="mstracctnm" id="mstracctname0" />
			</div>
			<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		</div>
		<div class="row" id="acctcmt">
			<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
			<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
				<s:textfield label="Comment" name="login.acctComment" class="acctcomment" id="acctcomm0" placeholder="150 character limit" />
			</div>
			<!-- <div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div> -->
			<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
				<button type="button" id="btnAdd" class="btn btn-secondary">+</button>
				<button type="button" id="btnDel" class="btn btn-danger">x</button>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<span id="charcount"></span>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<br>
	<div class="row" id="logininfo_btn">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<s:submit cssClass="btn btn-primary pull-right ml-2" id="jobnext-btn" value="Next" />
			<s:submit cssClass="btn btn-secondary pull-right" id="cancel-btn" value="Cancel" action="resetAction" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	</s:form>
	<br>
</div>
<br>
<br>
<br>
<!-- Including footer -->
<s:include value="Footer.jsp"></s:include>

</body>
</html>