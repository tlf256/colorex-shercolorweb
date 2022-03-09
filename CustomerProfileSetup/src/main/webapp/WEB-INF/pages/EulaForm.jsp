<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<script type="text/javascript" charset="utf-8" src="script/CustomerForm.js"></script>
<title>Eula Form</title>
</head>
<body>
<!-- including Header -->
<s:include value="Header.jsp"></s:include>
<div class="container-fluid">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
	</div>
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
	</div>
	<br>
	<div class="row" id="activateEula">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<strong>Activate Eula?</strong><br>
			<div class="custom-radio ml-1">
				<input type="radio" id="yes" name="activate" class="mt-1 mb-1" value="yes" /><br>
				<input type="radio" id="no" name="activate" class="mt-1 mb-1" value="no" /><br>
			</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<s:form id="customerInfo" action="getCustomerInfo" method="post">
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="eulaerror" class="text-danger"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row d-none" id="eula">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:select list=""></s:select>
			<s:textfield label="Acceptance Code" name="customer.acceptCode" id="acceptCode" requiredLabel="true" />
			<s:textfield label="Website" name="customer.website" id="website" requiredLabel="true" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<br>
	<br>
	<div class="row" id="nextform">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<s:submit cssClass="btn btn-primary pull-right ml-2" id="loginnext-btn" value="Next" />
			<s:submit cssClass="btn btn-secondary pull-right" id="cancel-btn" value="Cancel" action="resetAction" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	</s:form>
</div>
<br>
<br>
<br>
<!-- Including footer -->
<s:include value="Footer.jsp"></s:include>
</body>
</html>