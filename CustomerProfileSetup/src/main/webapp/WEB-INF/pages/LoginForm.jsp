<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
<script type="text/javascript" charset="utf-8" src="script/LoginForm.js"></script>
<title>Login Form</title>
</head>
<body>
<!-- including Header -->
<s:include value="Header.jsp"></s:include>

<div class="container-fluid">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6"></div>
		<div class="col-sm-3">
			<!--<s:set var="setFirstFieldFocus" value="%{'false'}" />-->
			<!--<s:set var="updateMode" value="%{updateMode}" />-->
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-2">
			<strong>Customer ID:</strong>
		</div>
		<div class="col-sm-2">
			${sessionScope["CustomerDetail"].custList[0].customerId}
		</div>
	</div>
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-2">
			<strong>Customer Name:</strong>
		</div>
		<div class="col-sm-2">
			${sessionScope["CustomerDetail"].custList[0].swuiTitle}
		</div>
	</div>
	<br>
	<br>
	<s:form id="loginInfo" method="post" validate="true" theme="bootstrap">
	<div class="row" id="createid-visible">
	<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
	<strong>* Denotes Required Field</strong>
	<br>
	<br>
	<strong>Create User ID?</strong>
		<div class="form-check">
			<input class="form-check-input" type="radio" name="createId" id="yes">
			<label class="form-check-label font-weight-normal" for="yes">
				Yes
			</label><br>
			<input class="form-check-input" type="radio" name="createId" id="no">
			<label class="form-check-label font-weight-normal" for="no">
				No
			</label>
		</div>
	</div>
	<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<div class="row d-none" id="keyfld-hidden">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield label="Login ID" name="keyfld" id="keyfld" requiredLabel="true" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield label="Master Account Name" name="mstracct" id="mstracct" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="acctcmt-hidden">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm" id="acctcomm" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="keyfld1-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld" id="keyfld1" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct" id="mstracct1" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt1-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm" id="acctcomm1" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="keyfld2-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld" id="keyfld2" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct" id="mstracct2" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt2-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm" id="acctcomm2" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="keyfld3-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld3" id="keyfld3" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct3" id="mstracct3" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt3-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm3" id="acctcomm3" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="keyfld4-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld4" id="keyfld4" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct4" id="mstracct4" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt4-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm4" id="acctcomm4" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="keyfld5-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld5" id="keyfld5" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct5" id="mstracct5" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt5-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm5" id="acctcomm5" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="keyfld6-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld6" id="keyfld6" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct6" id="mstracct6" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt6-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm6" id="acctcomm6" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="keyfld7-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld7" id="keyfld7" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct7" id="mstracct7" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt7-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm7" id="acctcomm7" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="keyfld8-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld8" id="keyfld8" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct8" id="mstracct8" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt8-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm8" id="acctcomm8" />
		</div>
		<div class="col-lg-0 col-md-0 col-sm-0 col-xs-0"></div>
	</div>
	<div class="row d-none" id="keyfld9-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Login ID" name="keyfld9" id="keyfld9" requiredLabel="true" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-1">
			<s:textfield label="Master Account Name" name="mstracct9" id="mstracct9" />
		</div>
	</div>
	<div class="row d-none" id="acctcmt9-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Account Comment" name="acctcomm9" id="acctcomm9" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="logininfo_btn-hidden">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<button type="button" id="logininfo_add" 
			class="btn btn-secondary mb-1 mt-2">+</button>
			<s:submit cssClass="btn btn-primary pull-right" value="Next"
				action="getLoginInfo" />
			<s:submit cssClass="btn btn-secondary pull-right" value="Cancel"
				action="userCancelAction" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<br>
	<br>
	</s:form>
</div>
	
<!-- Including footer -->
<s:include value="Footer.jsp"></s:include>

</body>
</html>