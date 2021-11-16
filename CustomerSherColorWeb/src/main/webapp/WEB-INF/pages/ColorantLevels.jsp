<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!doctype html>

<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="global.colorantLevels"/></title>
		<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.7.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/Colorant.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<s:hidden value="%{thisGuid}" id="reqGuid"></s:hidden>
		<style type="text/css">
		  .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
		.progress {
		    margin-bottom: 0px;
   			height: 2rem;
		}
		.mybuttongroup{
			width: 100%;
		}
		.btn-light {
		    color: #000000;
		    background-color: #ffffff;
		}
		</style>
	
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		<s:set var="thisGuid" value="reqGuid" />
		<s:hidden id="colorantLevels_tinterModel"   value="%{tinter.model}"/>
		<!-- ${sessionScope[thisGuid].tinter.model} />		-->
		<div class="container-fluid">
			<br>
			<div class="row">
				<div class="col-xl-1 col-lg-1 col-md-0 col-sm-0">
				</div>
				<div class="col-xl-10 col-lg-10 col-md-12 col-sm-12">
					<div class="card card-body bg-light mb-4">
						<div class="row">
							<div class="col-lg-3 col-md-3 col-sm-2 col-3">
								<span class="badge badge-secondary" style="font-size: 1.2rem;"><s:text name="global.colorantLevels"/></span>
								<h5 class="text-primary mt-3"><strong><s:property value="tinter.model" escapeHtml="true"></s:property></strong></h5>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-8 col-6">
								<div class="alert alert-warning">
								  <strong><s:text name="global.noteColon"/> </strong><s:text name="colorantLevels.shakeColorantMsg"/>
								</div>
							</div>
							<div class="col-lg-3 col-md-3 col-sm-2 col-3 text-center">
								<s:if test="%{tinter.model != null}"> 
								<s:url var="setFullURL" action="clrntLevelsUpdateAction" escapeAmp="false">
									<s:param name="reqGuid" value="%{thisGuid}"/>
									<s:param name="updateType">setAllFull</s:param>
								</s:url>
								<a href="<s:property value="setFullURL"/>" class="btn btn-primary" id="setallfull" style="margin-top: 10px; margin-left: 30px;"><s:text name="colorantLevels.setAllFull"/></a>
								</s:if>
								<s:url var="cancelURL" action="userCancelAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
								<a href='<s:property value="cancelURL"/>' class="btn btn-secondary" id="cancel" style="margin-top: 10px;margin-left: 30px;"><s:text name="global.done"/></a>
							</div>
						</div>
					</div>	
					<ul class="nav nav-tabs d-none">
					  <li class="nav-item"><p data-toggle="tab" class="nav-link active bg-light show mb-0"><strong><s:text name="colorantLevels.lowColorantWarnings"/> </strong><span id="warnCount" class="badge badge-warning" style="background-color: #f0ad4e;">0</span></p></li>
					</ul>
					  <table class="table table-bordered table-sm">
					    <thead>
					      <tr>
					      	<th class="bg-light" style="width: 10%;"><s:text name="colorantLevels.position"/></th>
					        <th class="bg-light" style="width: 10%;"><s:text name="colorantLevels.color"/></th>
					        <th class="bg-light" style="width: 10%;"><s:text name="colorantLevels.ouncesCurrentMax"/></th>
					        <th class="bg-light" style="width: 10%;"><s:text name="colorantLevels.quartsCurrentMax"/></th>
					        <th class="bg-light" style="width: 10%;"><s:text name="colorantLevels.actions"/></th>
					        <th class="bg-light" style="width: 50%;"><s:text name="colorantLevels.containerLevel"/></th>
					      </tr>
					    </thead>
					    <tbody>
					    <s:iterator value="tinter.canisterList" status="i">
					    	<s:if test="%{clrntCode != 'NA'}">
						      <tr id="row<s:property value="%{#i.index}"/>">
								<s:set var="percent" value="(currentClrntAmount/maxCanisterFill) * 100"/>
								<s:set var="maxQt" value="maxCanisterFill/32"/>
								<s:set var="curQt" value="currentClrntAmount/32"/>
								<td id="position"><s:property value="position"/></td>
						        <td id="color<s:property value="%{#i.index}"/>"><s:property value="clrntCode"/> - <s:property value="clrntName"/></td>
						        <td id="ounces<s:property value="%{#i.index}"/>"><s:property value="currentClrntAmount"/>/<s:property value="maxCanisterFill"/></td>
						        <td id="quarts<s:property value="%{#i.index}"/>"><s:property value="curQt"/>/<s:property value="maxQt"/></td>
						        <td class="actions">
						        	<div class="btn-group-vertical mybuttongroup p-1" role="group">
						        		<s:if test="%{tinter.model != null && tinter.model.startsWith('FM X')}"> 
							        	<button type="button" class="btn btn-light btn-sm border" id="moveto<s:property value="%{#i.index}"/>">
							        	  <s:hidden name="key" value="%{position}" id="key"/>
										  <span class="fa fa-arrow-circle-o-right" aria-hidden="true"></span>&nbsp;<s:text name="colorantLevels.moveTo"/>&nbsp;&nbsp;
										</button>
										</s:if>
							        	<button type="button" class="btn btn-light btn-sm border" id="addqt<s:property value="%{#i.index}"/>">
							        	  <s:hidden name="key" value="%{position}" id="key"/>
										  <span class="fa fa-plus" aria-hidden="true"></span>&nbsp;<s:text name="colorantLevels.addQuart"/>
										</button>
										<button type="button" class="btn btn-light btn-sm border" id="subqt<s:property value="%{#i.index}"/>">
										  <s:hidden name="key" value="%{position}" id="key"/>
										  <span class="fa fa-minus" aria-hidden="true"></span>&nbsp;<s:text name="colorantLevels.subQuart"/>
										</button>
										<button type="button" class="btn btn-light btn-sm border" id="setfl<s:property value="%{#i.index}"/>">
										  <s:hidden name="key" value="%{position}" id="key"/>
										  <span class="fa fa-upload" aria-hidden="true"></span>&nbsp;&nbsp;<s:text name="colorantLevels.setFull"/> &nbsp;&nbsp;
										</button>
									</div>
						        </td>
					        	<td class="level" id="level<s:property value="%{#i.index}"/>">
					        		<div class="row">
					        			<div class="col-xl-12 mb-2">
						        			<div id='warningdiv' class="pull-left"><span class='badge badge-warning warning d-none' id='warning'><s:text name="global.warningClrntLow"/></span></div>
											<span class="hfive badge bg-white ml-auto mb-1 pull-right" id="<s:property value="%{#i.index}"/>" style="font-size: 1rem;"><s:property value="percent"/>%</span><s:hidden name="fillAlarm" value="%{fillAlarmLevel}" cssClass="fillAlarm"/>
					        			</div>
					        		</div>
					        		<div class="row">
					        			<div class="col-xl-12">
						        			<div class="progress">
								  			<div class="progress-bar" role="progressbar" aria-valuenow="<s:property value="percent"/>"
								  			aria-valuemin="0" aria-valuemax="100" style="width: <s:property value="percent"/>%; background-color: <s:property value="rgbHex"/>;">
								  			</div>
							  			</div>
					        		</div>
								</div>
								</td>
						      </tr>
					     	</s:if>
					     </s:iterator>
					    </tbody>
					  </table>
					<br>
				</div>
				<div class="col-xl-1 col-lg-1 col-md-0 col-sm-0">
				</div>
			</div>
		</div>
		<br>
		<br>
		<br>
		<br>
		
		<!-- Modals -->
		<div class="modal fade" aria-labelledby="addModal" aria-hidden="true"  id="addModal" role="dialog">
		    <div class="modal-dialog modal-sm">
		      <div class="modal-content">
		        <div class="modal-header">
		        <h6 class="modal-title"><s:text name="colorantLevels.message"/></h6>
		          <button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
		        </div>
		        <div class="modal-body">
		          <p id="addmsg"><s:text name="global.containerFull"/></p>
		        </div>
		        <div class="modal-footer">
		          <button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="%{getText('global.close')}" ><s:text name="global.close"/></button>
		        </div>
		      </div>
		    </div>
		  </div>
		  <div class="modal fade" aria-labelledby="subModal" aria-hidden="true"  id="subModal" role="dialog">
		    <div class="modal-dialog modal-sm">
		      <div class="modal-content">
		        <div class="modal-header">
		        <h6 class="modal-title"><s:text name="colorantLevels.message"/></h6>
		          <button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
		        </div>
		        <div class="modal-body">
		          <p id="submsg"><s:text name="colorantLevels.containerEmpty"/></p>
		        </div>
		        <div class="modal-footer">
		          <button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="%{getText('global.close')}" ><s:text name="global.close"/></button>
		        </div>
		      </div>
		    </div>
		  </div>
		  			    <!-- Move in Progress Modal Window -->
			    <div class="modal fade" aria-labelledby="moveInProgressModal" aria-hidden="true"  id="moveInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
								<h5 class="modal-title"><s:text name="colorantLevels.movingCarousel"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="progress-message" font-size="4"><s:text name="colorantLevels.pleaseWaitCarouselRotates"/></p>
								<ul class="list-unstyled" id="tinterProgressList">
										</ul>
							</div>
							<div class="modal-footer">
							</div>
						</div>
					</div>
				</div>			    
				
				<!--  Tinter Not Found Error Modal Window -->
				<div class="modal fade" aria-labelledby="tinterNotFoundModal" aria-hidden="true"  id="tinterNotFoundModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="global.tinterError"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="tinterNotFound" font-size="4"><s:text name="global.tinterNotFoundAdd"/></p>
							</div>
							<div class="modal-footer">
								<a href='<s:property value="cancelURL"/>' class="btn btn-secondary" id="cancel" style="margin-top: 10px;margin-left: 30px;"><s:text name="global.done"/></a>
							</div>
						</div>
					</div>
				</div>
				
			    <!-- Tinter Socket Error Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterSocketErrorModal" aria-hidden="true"  id="tinterSocketErrorModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="global.tinterError"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="tinterSocketError" font-size="4"></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterSocketErrorButton" data-dismiss="modal" aria-label="%{getText('global.close')}"><s:text name="global.close"/></button>
							</div>
						</div>
					</div>
				</div>			    
			    <!-- Tinter ErrorList Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="tinterErrorListTitle"><s:text name="global.tinterError"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<div>
									<ul class="list-unstyled" id="tinterErrorList">
									</ul>
								</div>
								<p id="tinterErrorListSummary" font-size="4"></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="%{getText('global.ok')}" ><s:text name="global.ok"/></button>
							</div>
						</div>
					</div>
				</div>
				
				<script>
				$(document).ready(function(){
					<s:if test="%{tinter.model == null}">
						$("#tinterNotFoundModal").modal('show');
					</s:if>
					
					$('#tinterNotFoundModal').on('hidden.bs.modal', function () {
						window.location.replace('<s:url action="loginAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>')
					});
				})
				
				</script>
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
	</body>
</html>