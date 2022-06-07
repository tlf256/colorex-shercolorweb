<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<%@ taglib prefix="s" uri="/struts-tags" %>
 
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="closestColors.closestColors"/></title>
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
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.2.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/closestColor-1.5.2.js"></script>
		<style>
		  .sw-bg-main {
	          background-color: ${sessionScope[reqGuid].rgbHex};
	       }
		  .chip {
			  position: relative;
			  display: -webkit-box;
			  display: -ms-flexbox;
			  display: flex;
			  -webkit-box-orient: vertical;
			  -webkit-box-direction: normal;
			  -ms-flex-direction: column;
			  flex-direction: column;
			  min-width: 10px;
			  min-height: 10px;
			  height: 52px;
			  width: 52px;
			  border-radius: 50%;
			  border: 1px solid rgba(0, 0, 0, 0.125);
			}
		</style>
		<script type="text/javascript" charset="utf-8">
			$(function(){
				
			});
		</script>
	</head>
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		<%-- <s:set var="thisGuid" value="reqGuid" /> --%>
		<div class="container-fluid">
			<div class="row mt-4">
				<div class="col-sm-2"></div>
				<div class="col-sm-8"><h3><s:text name="closestColors.closestColors"/></h3></div>
				<div class="col-sm-2"></div>
			</div>
			<br>
			<br>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
				</div>
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<strong><s:text name="global.colorCompanyColon"/></strong>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
					<s:text name="processColorAction.custom"/>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
				</div>
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
				</div>
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<strong><s:text name="global.colorIdColon"/></strong>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
					<s:text name="processColorAction.match"/>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
				</div>
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
				</div>
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<strong><s:text name="global.colorNameColon"/></strong>
				</div>
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<s:text name="closestColors.measuredColor"/>
					<span class="chip sw-bg-main mt-1"></span>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-1 col-xs-0">
				</div>
			</div>
			<br>
			<br>
			<br>
			<br>
			<s:form id="" action="" validate="true"  theme="bootstrap">
			<div class="form-row">
				<s:hidden name="reqGuid" value="%{reqGuid}" />
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1">
				</div>
				<div class="col-lg-7 col-md-7 col-sm-10">
					<button type="button" id="" class="btn btn-primary mb-5 mt-2 mx-1" onclick="showIntExtModal()"><s:text name="global.next"></s:text></button>
					<s:submit class="btn btn-secondary pull-right mb-5 mt-2 mx-1" value="%{getText('global.cancel')}" action="userCancelAction"/>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-1">
				</div>
			</div>
			</s:form>
		</div>
		<div class="modal fade" aria-labelledby="" aria-hidden="true"  id="intExtModal" role="dialog">
	    	<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content">
				  <s:form id="intExtForm" action="closestColorResultAction" validate="true" focusElement="" theme="bootstrap">
					<div class="modal-header">
						<h5 class="modal-title"><s:text name="closestColors.colorUse"/></h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
					  <div class="row">
						<div class="col-lg-1 col-md-1 col-sm-1">
							<s:hidden name="reqGuid" value="%{reqGuid}" />
						</div>
						<div class="col-lg-10 col-md-10 col-sm-10">
							<p><s:text name="closestColors.colorUseBaseRecommendation"/></p>
            			</div>
						<div class="col-lg-1 col-md-1 col-sm-1">
						</div>
					  </div>
					  <div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1">
						</div>
						<div class="col-lg-7 col-md-7 col-sm-10">
						  <div class="form-group">
            				<h6><s:text name="closestColors.chooseProjectTypeColon"/></h6>
           					<div class="form-check">
           					  <input class="form-check-input" type="radio" name="intExt" value="I" id="interior" checked>
           					  <label class="form-check-label" for="interior">
							    <s:text name="getPercentageIntExt.interior"/>
							  </label>
							  <br>
           					  <input class="form-check-input" type="radio" name="intExt" value="E" id="exterior">
							  <label class="form-check-label" for="exterior">
							    <s:text name="getPercentageIntExt.exterior"/>
							  </label>
							</div>
            			</div>
            			<div class="form-check">
	         			  <input class="form-check-input form-control-lg" type="checkbox" name="swactive" id="swActiveChk" value="true" checked>
	         			  <label class="form-check-label" for="swActiveChk">
						    <s:text name="closestColors.displaySwActiveOnly"/>
						  </label>
						</div>
						</div>
						<div class="col-lg-3 col-md-3 col-sm-1">
						</div>
					  </div>
					</div>
					<div class="modal-footer">
						<button type="button" id="btnNxtIntExt" class="btn btn-primary"><s:text name="global.next"></s:text></button>
						<s:submit class="btn btn-secondary pull-right" id="btnCancel" data-dismiss="modal" value="%{getText('global.cancel')}"/>
					</div>
				  </s:form>
				</div>
			</div>
		</div>
		<div class="modal fade" aria-labelledby="processReqModal"
			aria-hidden="true" id="processReqModal" role="dialog"
			data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<em id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></em>
						<h5 class="modal-title"><s:text name="closestColors.pleaseWait"/></h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="%{getText('global.close')}">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<p id="process-message" style="white-space:pre-line"><s:text name="closestColors.locatingColors"/></p>
					</div>
					<div class="modal-footer">
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