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
		<link rel="stylesheet" href="css/dataTables.bootstrap4.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/buttons.bootstrap4.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 	
  		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/dataTables.bootstrap4.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/dataTables.buttons.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/buttons.bootstrap4.min.js"></script> 
		<script type="text/javascript" charset="utf-8" src="js/buttons.colVis.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/buttons.html5.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/buttons.print.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.3.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/closestColorsResult-1.5.2.js"></script>
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
		<div class="container-fluid">
		   <div class="row">
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
				<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
					<s:actionerror/>
				</div>
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
			</div>
			<div class="row">
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
				<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
					<div class="card card-body bg-light mt-4">
					   <div class="row">
							<div class="col-lg-4 col-md-4 col-sm-3 col-xs-4">
								<span class="badge badge-secondary" style="font-size: 1.2rem;"><s:text name="closestColors.closestColors"/></span>
							</div>
							<div class="col-lg-3 col-md-3 col-sm-1 col-xs-0">
							</div>
							<div class="col-lg-5 col-md-5 col-sm-7 col-xs-8">
							</div>
						</div>
					</div>
				</div>
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
			</div>
			<div class="row">
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
				<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
					<div class="card card-body bg-light pb-4 mt-4">
						<div class="row">
							<div class="col-lg-1 col-md-1 col-sm-1 col-xs-0">
							</div>
							<div class="col-lg-3 col-md-3 col-sm-3 col-xs-4">
								<strong><s:text name="global.colorCompanyColon"/></strong>
							</div>
							<div class="col-lg-5 col-md-5 col-sm-7 col-xs-8">
								<s:text name="processColorAction.custom"/>
							</div>
							<div class="col-lg-3 col-md-3 col-sm-1 col-xs-0">
							</div>
						</div>
						<div class="row">
							<div class="col-lg-1 col-md-1 col-sm-1 col-xs-0">
							</div>
							<div class="col-lg-3 col-md-3 col-sm-3 col-xs-4">
								<strong><s:text name="global.colorIdColon"/></strong>
							</div>
							<div class="col-lg-5 col-md-5 col-sm-7 col-xs-8">
								<s:text name="processColorAction.match"/>
							</div>
							<div class="col-lg-3 col-md-3 col-sm-1 col-xs-0">
							</div>
						</div>
						<div class="row">
							<div class="col-lg-1 col-md-1 col-sm-1 col-xs-0">
							</div>
							<div class="col-lg-3 col-md-3 col-sm-3 col-xs-4">
								<strong><s:text name="global.colorNameColon"/></strong>
							</div>
							<div class="col-lg-5 col-md-5 col-sm-3 col-xs-4">
								<s:text name="closestColors.measuredColor"/>
								<span class="chip sw-bg-main mt-2"></span>
							</div>
							<div class="col-lg-3 col-md-3 col-sm-1 col-xs-0">
							</div>
						</div>
					</div>
				</div>
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
			</div>
			<br>
			<br>
			<div class="row" id="swResults">
				<div class="col-lg-2 col-md-2 col-sm-1"></div>
           		<div class="col-lg-8 col-md-8 col-sm-10">
           		  <h6><strong><s:text name="closestColors.sherwinColors"/></strong></h6>
           			<table id="closestSwColor_table" class="table table-striped table-bordered" style="width:100%">
           			  <caption><s:text name="closestColors.swColorsTableCaption"/></caption>
						<thead>
							<tr>
								<th id="sw_colorId"><s:text name="global.colorId"/></th>
								<th id="sw_colorName"><s:text name="global.colorName"/></th>
								<th id="sw_intBase"><s:text name="closestColors.intBase"/></th>
								<th id="sw_extBase"><s:text name="closestColors.extBase"/></th>
								<th id="sw_deltaE"><s:text name="getProdFamily.deltaE"/></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="closestSwColors" status="i">
								<tr class="">
									<td><s:property value="colorId"/></td>
									<td><s:property value="colorName"/></td>
									<td><s:property value="intBase"/></td>
									<td><s:property value="extBase"/></td>
									<td><s:property value="getText('global.numFormatTwoDecPlaces', {deltaE})" /></td> 
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
				<div class="col-lg-2 col-md-2 col-sm-1"></div>
			</div>
			<s:if test="closestCmptColors != null">
			<br>
			  <div class="row" id="cmptResults">
				<div class="col-lg-2 col-md-2 col-sm-1"></div>
           		<div class="col-lg-8 col-md-8 col-sm-10">
           		  <h6><strong><s:text name="closestColors.competitiveColors"/></strong></h6>
           			<table id="closestCmptColor_table" class="table table-striped table-bordered" style="width:100%">
           			  <caption><s:text name="closestColors.competColorsTableCaption"/></caption>
						<thead>
							<tr>
								<th id="compet_colorComp"><s:text name="global.colorCompany"/></th>
								<th id="compet_colorId"><s:text name="global.colorId"/></th>						
								<th id="compet_colorName"><s:text name="global.colorName"/></th>
								<th id="compet_colorPal"><s:text name="closestColors.colorPalette"/></th>
								<th id="compet_deltaE"><s:text name="getProdFamily.deltaE"/></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="closestCmptColors" status="">
								<tr class="">
									<td><s:property value="colorComp"/></td>
									<td><s:property value="colorId"/></td> 
									<td><s:property value="colorName"/></td>
									<td><s:property value="palette"/></td>
									<td><s:property value="getText('global.numFormatTwoDecPlaces', {deltaE})" /></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
				<div class="col-lg-2 col-md-2 col-sm-1"></div>
			</div>
			</s:if>
			<br>
			<s:form id="" action="" validate="true"  theme="bootstrap">
			<div class="form-row">
				<s:hidden name="reqGuid" value="%{reqGuid}" />
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1">
				</div>
				<div class="col-lg-8 col-md-8 col-sm-10">
					<s:submit class="btn btn-primary mb-5 mt-2 mx-1" value="%{getText('closestColors.newMeasurement')}" action="closestColorsMeasureAction"/>
					<s:submit class="btn btn-secondary pull-right mb-5 mt-2 mx-1" value="%{getText('global.done')}" action="userCancelAction"/>
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