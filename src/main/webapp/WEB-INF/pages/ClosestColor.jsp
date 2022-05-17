<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<%@ taglib prefix="s" uri="/struts-tags" %>
 
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Closest Color</title>
		<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 	
  		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
 		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
  		<script type="text/javascript" charset="utf-8"	src="js/moment-with-locales.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.1.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/closestColor-1.5.1.js"></script>
		<style>
			
		</style>
		<script type="text/javascript" charset="utf-8">
			$(function(){
				
			});
		</script>
	</head>
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		<s:set var="thisGuid" value="reqGuid" />
		<div class="container-fluid">
			<div class="row mt-4">
				<div class="col-sm-2"></div>
				<div class="col-sm-8"><h3>Closest Color</h3></div>
				<div class="col-sm-2"></div>
			</div>
			<br>
			<s:form id="" action="" validate="true" focusElement="partialColorNameOrId" theme="bootstrap">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1">
						<s:hidden name="reqGuid" value="thisGuid" />
					</div>
            		<div class="col-lg-8 col-md-8 col-sm-10">
            			<div class="form-group">
           					<div class="form-check">
         					  <input class="form-check-input form-control-lg" type="checkbox" name="closestColorRs" value="swactive" id="swResults" checked>
							  <label class="form-check-label" for="swResults">
							    Show SW Active Colors Only
							  </label>
							</div>
            			</div>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1"></div>
				</div>
			</s:form>
			<br>
			<s:form id="" action="" validate="true"  theme="bootstrap">
			<div class="form-row">
				<s:hidden name="reqGuid" value="thisGuid" />
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1">
				</div>
				<div class="col-lg-8 col-md-8 col-sm-10">
					<button type="button" id="" class="btn btn-secondary mb-5 mt-2 mx-1" title="%{getText('manageStoredMeasurements.currentLoadingRemoteMeas')}">Next</button>
					<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2 mx-1" value="%{getText('global.cancel')}" action="userCancelAction"/>
				</div>
				<div class="col-lg-2 col-md-2 col-sm-1">
				</div>
			</div>
			</s:form>
		</div>
		<br>
		<br>
		<br>
		<br>
  
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>