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
			<div class="row mt-5">
				<div class="col-sm-2"></div>
				<div class="col-sm-7"><h3>Closest Color</h3></div>
				<div class="col-sm-2"></div>
			</div>
			<br>
			<s:form id="" action="" validate="true" focusElement="partialColorNameOrId" theme="bootstrap">
				<s:hidden name="reqGuid" value="#thisGuid" />
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1">
					</div>
            		<div class="col-lg-7 col-md-7 col-sm-8">
            			<div class="form-group">
            				<h6><strong>Display SW Active Colors Only?</strong></h6>
           					<div class="form-check">
           					  <input class="form-check-input" type="radio" name="swactive" value="true" id="swActiveYes">
           					  <label class="form-check-label" for="swActiveYes">
							    Yes
							  </label>
							  <br>
           					  <input class="form-check-input" type="radio" name="swactive" value="false" id="swActiveNo" checked>
							  <label class="form-check-label" for="swActiveNo">
							    No
							  </label>
							</div>
            			</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-3"></div>
				</div>
			</s:form>
			<br>
			<s:form id="" action="" validate="true"  theme="bootstrap">
			<div class="form-row">
				<s:hidden name="reqGuid" value="#thisGuid" />
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1">
				</div>
				<div class="col-lg-7 col-md-7 col-sm-8">
					<s:submit class="btn btn-primary" value="%{getText('global.next')}" action="closestColorMeasureAction" autofocus="autofocus"/>
					<s:submit class="btn btn-secondary pull-right" value="%{getText('global.cancel')}" action="userCancelAction"/>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-3">
				</div>
			</div>
			</s:form>
		</div>
		<div class="modal" aria-labelledby="" aria-hidden="true"  id="intExtModal" role="dialog">
	    	<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title">Color Use</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<p>Base recommendations for colors are dependent on product type. It is important to determine the project type for this matched color.</p>
            				<h6>Choose project type:</h6>
           					<div class="form-check">
           					  <input class="form-check-input" type="radio" name="intExt" value="int" id="interior" checked>
           					  <label class="form-check-label" for="interior">
							    Interior
							  </label>
							  <br>
           					  <input class="form-check-input" type="radio" name="intExt" value="ext" id="exterior">
							  <label class="form-check-label" for="exterior">
							    Exterior
							  </label>
							</div>
            			</div>
					</div>
					<div class="modal-footer">
						<s:submit class="btn btn-primary" value="%{getText('global.next')}" onclick="" action="" autofocus="autofocus" />
						<s:submit class="btn btn-secondary" id="btnCancel" data-dismiss="modal" value="%{getText('global.cancel')}"/>
					</div>
				</div>
			</div>
		</div>
		<br>
		<br>
		<br>
		<br>
  
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>