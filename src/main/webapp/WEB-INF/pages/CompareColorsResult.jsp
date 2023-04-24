<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!doctype html>
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="compareColorsResult.compareResults"/></title>
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
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.3.js"></script>
		<style>
		.sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
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
					<div class="card card-body bg-light mt-4">
					   <div class="row">
							<div class="col-lg-4 col-md-4 col-sm-3 col-xs-4">
								<span class="badge badge-secondary" style="font-size: 1.2rem;"><s:text name="compareColorsResult.colorComparisonResults"/></span>
							</div>
							<div class="col-lg-3 col-md-3 col-sm-1 col-xs-0"></div>
							<div class="col-lg-5 col-md-5 col-sm-7 col-xs-8"></div>
						</div>
					</div>
				</div>
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
				<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
					<div class="card card-body bg-light pb-4">
					<p style="font-size: 1.4em;"><strong><s:text name="compareColorsResult.deltaEcolon"/></strong> 
						<span class="badge badge-secondary" style="font-size: 1em;" id="delta-e">
							<s:property value="getText('{0, number, #.##}', {colorDiff.deDiff})" />
						</span>
					</p>
				</div>
				</div>
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
			</div>
			<br>

				<div class="row">
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
				</div>
				<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
					<div class="card card-body bg-light pb-4">

				<div class="row">
				<div class="col-sm-1"></div>
					<div class="col-lg-5 col-md-5 col-sm-3 col-xs-4">
						<p style="font-size: 1.4em;"><strong><s:text name="compareColorsResult.standard" /></strong>
					</div>
				<div class="col-sm-1"></div>
				<div class="col-lg-5 col-md-5 col-sm-3 col-xs-4">
						<p style="font-size: 1.4em;"><strong><s:text name="compareColorsResult.sample"/></strong>
					</div>
			</div>
			<div class="row">
				<div class="col-sm-1"></div>
					<div class="col-lg-5 col-md-5 col-sm-3 col-xs-4">
						<s:property value="colorId"/>
						<s:property value="colorComp"/>
						<s:property value="colorName"/>
						<span class="chip mt-2" style="background: <s:property value="rgbHexStd"/>;"></span>
					</div>
				<div class="col-sm-1"></div>
				<div class="col-lg-5 col-md-5 col-sm-3 col-xs-4">
						<s:text name="closestColors.measuredColor"/>
						<span class="chip mt-3" style="background: <s:property value="rgbHexTrl"/>;"></span>
					</div>
				</div>
			</div>
		</div>
	</div>
			
			<br>
			<div class="row">
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0"></div>
				<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
					<table id="compareResults_table" class="table table-striped table-bordered">
					  <caption><s:text name="compareColorsResult.tableCaption"/></caption>
						<thead>
							<tr>
								<th><s:text name="compareColorsResult.scale"></s:text></th>
								<th><s:text name="compareColorsResult.standard"></s:text></th>
								<th><s:text name="compareColorsResult.sample"></s:text></th>
								<th><s:text name="compareColorsResult.difference"></s:text></th>
								<th><s:text name="compareColorsResult.colorRange"></s:text></th>
								<th><s:text name="compareColorsResult.sampleDescription"></s:text></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>L</td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.standLval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.trialLval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.lDiff})" /></td>
								<td><s:text name="compareColorsResult.lightness" /></td>
								<td>
									<s:if test="colorDiff.trialLdescr == 'lighter'">
										<s:text name="compareColorsResult.lighterThanStandard"></s:text>
									</s:if>
									<s:if test="colorDiff.trialLdescr == 'darker'">
										<s:text name="compareColorsResult.darkerThanStandard"></s:text>
									</s:if>
									<s:if test="colorDiff.trialLdescr == 'nodiff'">
										<s:text name="compareColorsResult.noDifference"></s:text>
									</s:if>
								</td>
							</tr>
							<tr>
								<td>A</td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.standAval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.trialAval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.aDiff})" /></td>
								<td><s:text name="compareColorsResult.redGreen" /></td>
								<td>
									<s:if test="colorDiff.trialAdescr == 'more red'">
										<s:text name="compareColorsResult.moreRedThanStandard"></s:text>
									</s:if>
									<s:if test="colorDiff.trialAdescr == 'more green'">
										<s:text name="compareColorsResult.moreGreenThanStandard"></s:text>
									</s:if>
									<s:if test="colorDiff.trialAdescr == 'nodiff'">
										<s:text name="compareColorsResult.noDifference"></s:text>
									</s:if>
								</td>
							</tr>
							<tr>
								<td>B</td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.standBval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.trialBval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.bDiff})" /></td>
								<td><s:text name="compareColorsResult.yellowBlue" /></td>
								<td>
									<s:if test="colorDiff.trialBdescr == 'more yellow'">
										<s:text name="compareColorsResult.moreYellowThanStandard"></s:text>
									</s:if>
									<s:if test="colorDiff.trialBdescr == 'more blue'">
										<s:text name="compareColorsResult.moreBlueThanStandard"></s:text>
									</s:if>
									<s:if test="colorDiff.trialBdescr == 'nodiff'">
										<s:text name="compareColorsResult.noDifference"></s:text>
									</s:if>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0"></div>
			</div>
			<div class="row">
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0"></div>
				<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
					<strong class="" id="deMsg">
						<s:text name="compareColorsResult.deltaEgreaterThanOneWarning"></s:text>
					</strong>
				</div>
				<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0"></div>
			</div>
			<br>
			<s:form id="" action="" theme="bootstrap">
				<div class="row">
					<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0">
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
					</div>
					<div class="col-xl-8 col-lg-8 col-md-10 col-sm-12">
						<s:submit class="btn btn-primary pull-right" value="%{getText('global.done')}" action="userCancelAction"/>
						<s:submit class="btn btn-secondary pull-left" value="%{getText('compareColorsResult.retry')}" action="spectroCompareColorsAction"/>
					</div>
					<div class="col-xl-2 col-lg-2 col-md-1 col-sm-0"></div>
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