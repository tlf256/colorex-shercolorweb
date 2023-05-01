<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!doctype html>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="getProdFamily.betterPerformanceWithDifferentBase"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
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
		<style type="text/css">
			.table-hover tbody tr:hover{
				background: #FFFF99;
				cursor: pointer;
			}
		</style>
		<script>
		// A Product should always be selected at start
		var selectedProdFamily;
		
		function processSelection(){
			// Do Action
			var myGuid = "${reqGuid}";
			console.log("myGUid")
			$.ajax({
				url : "prodFamilyUserNextAction.action",
				type : "POST",
				data : {
					reqGuid : myGuid,
					selectedProdFamily : selectedProdFamily
					
				},
				datatype : "json",
				success : function(data) {
					if (data.sessionStatus === "expired") {
						window.location.href = "./invalidLoginAction.action";
					} else {
						console.log("Action did not redirect...");
					}
				},
				error : function(err) {
					alert('<s:text name="global.failureColon"/>' + err);
				}
			});
		}
		
		function verifyUserSelection(overrideDeltaE) {
			if (overrideDeltaE) {
				return true;
			} else {
				var rows = document.querySelectorAll("tr");
				for (index=1; index<rows.length; index++) {
					var row = rows[index];
					var columns = row.querySelectorAll("td");
					var radio = row.querySelector("input");
					if (radio.checked == 1) {
						var colDeltaE = parseFloat(columns[4].textContent.trim());
						if (colDeltaE > 1) {
							//Show modal
							$('#highDeltaEValue').text(colDeltaE);
							$("#deltaEWarningModal").modal('show');
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		</script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-3">
				</div>
				<div class="col-sm-6">
				</div>
				<div class="col-sm-3">
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-sm-2">
				</div>
			</div>
			<s:form id="GetProdFamily" action="GetProductFamilyAction" validate="true" focusElement="prodFambly" theme="bootstrap">
				<div class="row">
					<div class="col-sm-2">
						<s:hidden name="reqGuid" value="%{reqGuid}"/>
						<s:hidden id="selectedProdFamily" name="selectedProdFamily" />
					</div>
					<div class="col-sm-2">
						<s:set var="reqGuid" value="reqGuid" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
					</div>
					<div class="col-sm-10">
						<h5><s:text name="getProdFamily.betterPerformanceFoundinDifferentBase"/></h5>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
					</div>
					<div class="col-sm-10">
					</div>
				</div>
				<br>
				<div id="prodFamily" class="row">
					<div class="col-sm-1"></div>
					<div class="col-sm-10">
					<table id="prodFamily_table" class="table table-striped table-bordered m-0">
						<caption style="caption-side:top;"><s:text name="global.chooseProduct"/></caption>
						<thead>
							<tr>
								<th></th>
								<th><s:text name="global.product"/></th>
								<th><s:text name="getProdFamily.quality"/></th>
								<th><s:text name="getProdFamily.base"/></th>
								<th><s:text name="getProdFamily.deltaE"/></th>
								<th><s:text name="getProdFamily.contrastRatio"/></th>
								<th><s:text name="global.comment"/></th>
								<th><s:text name="global.formula"/></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="entry" value="colorProdFamilies.entrySet()" status="i">
								<s:set var="index" value="%{#i.index}" />
								<tr class="border-bottom-1 border-dark">
									<td><input type="radio" class="prodFamRadio" name="prodFamily" /></td>
									<s:iterator var="item" value="colorProdFamilies[#index]">
										<td class="prodDetail">
											<s:property />
										</td>
									</s:iterator>
									<td class="pr-0">
										<s:iterator value="#session[reqGuid].formResponse.getFormulas().get(#index)">
											<div class="row">
												<div class="col-sm-6">
													<strong><s:property value="clrntSysId"/>*<s:text name="global.colorant"/></strong>
												</div>
												<div class="col-sm-1" align="center">
													<strong><s:property value="incrementHdr[0]"/></strong>
												</div>
												<div class="col-sm-1" align="center">
													<strong><s:property value="incrementHdr[1]"/></strong>
												</div>
												<div class="col-sm-1" align="center">
													<strong><s:property value="incrementHdr[2]"/></strong>
												</div>
												<div class="col-sm-1" align="center">
													<strong><s:property value="incrementHdr[3]"/></strong>
												</div>
											</div>
										</s:iterator>
										<s:iterator value="bothFormulas.get(#index).ingredients">
											<div class="row">
												<div class="col-sm-6">
													<s:property value="tintSysId"/>-<s:property value="name"/> 
												</div>
												<s:iterator value="increment" status="stat">
													<div class="col-sm-1" align="center">
														<s:property />
													</div>
												</s:iterator>
											</div>
										</s:iterator>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					</div>
					<div class="col-sm-1"></div>
				</div>
				<br>
				<div class="row">
					<div class="col-sm-1"></div>
					<div class="col-sm-10">
						<div id="noProductChoosenErrorText" style="color:red" class="d-none">
							<s:text name="global.pleaseSelectAProduct"/>
						</div>
					</div>
				</div>
				<br>
				<div class="row">
					<div class="col-sm-1"></div>
					<div class="col-sm-10">
						<div id="deltaEGreaterThanOne" class="alert alert-warning d-none"><s:text name="getProdFamily.deltaEGreaterThanOneWarning"/></div>
					</div>
					<div class="col-sm-1"></div>
				</div>
				<br>
				<div class="row">
					<div class="col-sm-1"></div>
					<div class="col-sm-2">
						<s:submit cssClass="btn btn-primary ml-3" id="submitNext" value="%{getText('global.next')}"
								  onclick="return verifyUserSelection(false);" action="prodFamilyUserNextAction"/>
					</div>
					<div class="col-sm-2">
						<s:submit cssClass="btn btn-secondary" value="%{getText('global.back')}" action="prodFamilyUserBackAction"/>
					</div>
					<div class="col-sm-6">
						<s:submit cssClass="btn btn-secondary pull-right mr-3" value="%{getText('global.cancel')}" action="userCancelAction"/>
					</div>
					<div class="col-sm-1"></div>
		    	</div>
			<br>
			
			<!-- Delta E Warning Modal -->
			<div class="modal" aria-labelledby="deltaEWarningModal"
				aria-hidden="true" id="deltaEWarningModal" role="dialog">
				<div class="modal-dialog modal-md">
					<div class="modal-content">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="getProdFamily.deltaEWarning"/></h5>
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<s:text name="getProdFamily.verifyDeltaEGreaterThanOne" />
								<br><br>
								<s:text name="getProdFamily.deltaEGreaterThanOneWarning" />
								<br><br>
								<s:text name="compareColorsResult.deltaEcolon" /><span id="highDeltaEValue" class="badge badge-danger ml-2"></span>
								<br><br>
								<s:text name="getProdFamily.verifyUserChoice" />
								
							</div>
							<div class="modal-footer">
								<s:submit cssClass="btn btn-primary ml-3" id="submitNext" value="%{getText('global.continue')}"
								  onclick="return verifyUserSelection(true);" action="prodFamilyUserNextAction"/>
								<button type="button" class="btn btn-secondary"
								id="overrideDeltaEClose" data-dismiss="modal" aria-label="Cancel"><s:text name="global.cancel"/></button>
							</div>
						</div>
					</div>
				</div>

			</div>
			</s:form>
			<script>
			$(document).ready(function() {
				
				$('#submitNext').focus();
				var prodTable = $('#prodFamily_table').DataTable({
					dom: 'rtp',
			        "language": {
			        	"emptyTable" : '<s:text name="getProdFamily.noProductsFound"/>'
			        },
			        "ordering": true,
			        "order": [ 4, 'asc' ],
			        "paginate": false,
			        "pagingType": "full",
			    });
				
				$('#prodFamily_table').addClass('table-hover');
				
				$('#prodFamily_table tbody').on('click','tr',function(event){
			    	var prodNbr = prodTable.row(this).data()[1];
			    	// table has been sorted by Delta-E value, so grab the updated row position
			    	var index = prodTable.rows( { order: 'applied' } ).nodes().indexOf(this);
			    	$("#selectedProdFamily").val(prodNbr);
			    	$(".prodFamRadio:eq("+index+")").prop('checked', true);
			    	userSelectedProduct = true;
			    });

				var rows = document.querySelectorAll("tr");
				for (index=1; index<rows.length; index++) {
					var row = rows[index];
					var columns = row.querySelectorAll("td");
					var colDeltaE = parseFloat(columns[4].textContent.trim());
					var colComment = columns[6].textContent.trim();
					if (colComment === "Best Performance") {
						row.style.background = "#b5e7a0";
						var radio = row.querySelector("input");
						radio.checked = true;
						var prodNbr = columns[1].textContent.trim();
						$("#selectedProdFamily").val(prodNbr);
						userSelectedProduct = true;
					}
					if (colDeltaE > 1) {
						columns[4].style.color = "red";
						columns[4].style.fontWeight = "bold";
						
					}
				}
				
	
			});
			</script>
	
		</div>
		
		<br>
		<br>
		<br>
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>