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
<title>Customer Form</title>
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
		<div class="col-sm-2">
			<%-- <s:if test="hasActionMessages()">
			    <s:actionmessage cssClass="alert-danger"/>
			</s:if> --%>
		</div>
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
	</div>
	<br>
	<div class="row">
		<div class="col-md-3 col-sm-2"></div>
		<div class="col-md-4 col-sm-3">
			<h3>Enter Customer Information</h3>
		</div>
		<div class="col-md-3 col-sm-2"></div>
	</div>
	<br>
	<s:form id="customerInfo" action="getCustomerInfo" method="post">
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="formerror" class="text-danger"></div>
			<div id="custformerror" class="text-danger"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row" id="acctype">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
		<strong>* Denotes Required Field</strong>
		<br>
		<br>
		<strong>Account Type *</strong>
			<div class="custom-radio ml-1">
			<input type="radio" id="selectedAccttype-0" name="customer.accttype" class="mt-1 mb-1" value="natlWdigits" />
				National with 9 digit account<br>
			<input type="radio" id="selectedAccttype-1" name="customer.accttype" class="mt-2 mb-1" value="natlWOdigits" />
				National without 9 digit account<br>
			<input type="radio" id="selectedAccttype-2" name="customer.accttype" class="mt-2 mb-1" value="intnatlWdigits" />
				International with 7 digit account<br>
			<input type="radio" id="selectedAccttype-3" name="customer.accttype" class="mt-2 mb-1" value="intnatlWOdigits" />
				International without 7 digit account<br>
			</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="ntlaccterror" class="text-danger"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row" id="ntlacct">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<!-- <div id="ntlaccterror" class="text-danger"></div> -->
			<s:textfield label="Account Number" name="customer.ntlacctnbr" id="ntlacctnbr" requiredLabel="true" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="intntlaccterror" class="text-danger"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row" id="intntlacct">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<!-- <div id="intntlaccterror" class="text-danger"></div> -->
			<s:textfield label="Account Number" name="customer.intntlacctnbr" id="intntlacctnbr" requiredLabel="true" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="swuititlerror" class="text-danger"></div>
			<div id="cdsadlflderror" class="text-danger"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	 <div class="row" id="cstmrnm">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<!-- <div id="swuititlerror" class="text-danger"></div> -->
			<s:textfield label="Customer Name" name="customer.swuiTitle" id="swuititle" requiredLabel="true" />
			<!-- <div id="cdsadlflderror" class="text-danger"></div> -->
			<s:textfield label="Additional Info (SherCust Database ID)" name="customer.cdsAdlFld" id="cdsadlfld" />
		</div>
		<div class="col-lg-5 col-md-5 col-sm-3 col-xs-3"></div>
	</div>
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="clrntsyserror" class="text-danger"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row" id="clrnt">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
		<strong>Colorant System *</strong><br>
		<div class="form-check">
			<input type="checkbox" id="cce" name="customer.cce" class="form-check-input clrntid" value="CCE"></input>
			<label class="form-check-label font-weight-normal" for="CCE">CCE</label><br>
			<input type="checkbox" id="bac" name="customer.bac" class="form-check-input clrntid" value="BAC"></input>
			<label class="form-check-label font-weight-normal" for="BAC">BAC</label><br>
			<input type="checkbox" id="844" name="customer.eff" class="form-check-input clrntid" value="844"></input>
			<label class="form-check-label font-weight-normal" for="884">844</label><br>
		</div>
		<!-- <div id="clrntsyserror" class="text-danger"></div> -->
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<!-- <div id="defaulterror" class="text-danger"></div> -->
			<strong>Default *</strong><br>
			<div class="custom-radio ml-1">
				<input type="radio" id="ccedefault" name="customer.defaultClrntSys" class="mt-1 mb-1" value="cce" /><br>
				<input type="radio" id="bacdefault" name="customer.defaultClrntSys" class="mt-2 mb-1" value="bac" /><br>
				<input type="radio" id="844default" name="customer.defaultClrntSys" class="mt-2 mb-1" value="844" /><br>
			</div>
		</div>
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