<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
 
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name=""/>Compare Results</title>
		<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 	
  		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
 		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
  		<script type="text/javascript" charset="utf-8"	src="js/moment-with-locales.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CompareColorsResults.js"></script>
		<style>
		
		</style>
		<script type="text/javascript" charset="utf-8">
			
		</script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			<div class="row mt-4">
				<div class="col-sm-1"></div>
				<div class="col-sm-10"><h3><s:text name=""/>Color Comparison Results</h3></div>
				<div class="col-sm-1"></div>
			</div>
			<br>
			<div class="row">
				<div class="col-sm-1"></div>
				<div class="col-sm-10">
					<table id="compareResults_table" class="table table-striped table-bordered">
						<thead>
							<tr>
								<th>Scale</th>
								<th>Standard</th>
								<th>Sample</th>
								<th>Difference</th>
								<th>Color Range</th>
								<th>Sample Description</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>L</td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.standLval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.trialLval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.lDiff})" /></td>
								<td><s:text name="" />Lightness</td>
								<td>
									<s:if test="colorDiff.trialLdescr == 'lighter'">
										<s:text name=""></s:text>Lighter than standard
									</s:if>
									<s:if test="colorDiff.trialLdescr == 'darker'">
										<strong><s:text name=""></s:text>Darker than standard</strong>
									</s:if>
									<s:if test="colorDiff.trialLdescr == 'nodiff'">
										<s:text name=""></s:text>No difference
									</s:if>
								</td>
							</tr>
							<tr>
								<td>A</td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.standAval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.trialAval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.aDiff})" /></td>
								<td><s:text name="" />Red/Green</td>
								<td>
									<s:if test="colorDiff.trialAdescr == 'more red'">
										<span style="color:red"><s:text name=""></s:text>More red than standard</span>
									</s:if>
									<s:if test="colorDiff.trialAdescr == 'more green'">
										<span style="color:green"><s:text name=""></s:text>More green than standard</span>
									</s:if>
									<s:if test="colorDiff.trialAdescr == 'nodiff'">
										<s:text name=""></s:text>No difference
									</s:if>
								</td>
							</tr>
							<tr>
								<td>B</td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.standBval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.trialBval})" /></td>
								<td><s:property value="getText('{0, number, #.##}', {colorDiff.bDiff})" /></td>
								<td><s:text name="" />Yellow/Blue</td>
								<td>
									<s:if test="colorDiff.trialBdescr == 'more yellow'">
										<span style="color:yellow"><s:text name=""></s:text>More yellow than standard</span>
									</s:if>
									<s:if test="colorDiff.trialBdescr == 'more blue'">
										<span style="color:blue"><s:text name=""></s:text>More blue than standard</span>
									</s:if>
									<s:if test="colorDiff.trialBdescr == 'nodiff'">
										<s:text name=""></s:text>No difference
									</s:if>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-sm-1"></div>
			</div>
			<br>
			<div class="row">
				<div class="col-sm-1"></div>
				<div class="col-sm-10">
					<strong><s:text name=""/>Delta E: </strong>
					<span class="badge badge-secondary"style="font-size: .9rem;" id="delta-e">
						<s:property value="getText('{0, number, #.##}', {colorDiff.deDiff})" />
					</span>
				</div>
				<div class="col-sm-1"></div>
			</div>
			<div class="row">
				<div class="col-sm-1"></div>
				<div class="col-sm-10">
					<strong class="" id="deMsg">
						<s:if test="colorDiff.deDiff > 1">
							<s:text name=""></s:text>(A Delta E greater than 1.00 produces a visible difference)
						</s:if>
					</strong>
				</div>
				<div class="col-sm-1"></div>
			</div>
			<br>
			<s:form id="" action="" theme="bootstrap">
				<div class="row">
					<div class="col-sm-1">
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
					</div>
					<div class="col-sm-10">
						<s:submit class="btn btn-primary pull-right" value="%{getText('global.done')}" action="userCancelAction"/>
					</div>
					<div class="col-sm-1"></div>
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