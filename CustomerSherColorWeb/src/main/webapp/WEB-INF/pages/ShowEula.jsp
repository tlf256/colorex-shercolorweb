<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>

<!doctype html>
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title><s:text name="global.endUserLicenseAgreement"/></title>
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.5.js"></script>

	
	<style>
		.dropdown-submenu {
		    position: relative;
		}
		.dropdown-submenu>.dropdown-menu {
		    top: 0;
		    left: 100%;
		    margin-top: -6px;
		    margin-left: -1px;
		    -webkit-border-radius: 0 6px 6px 6px;
		    -moz-border-radius: 0 6px 6px;
		    border-radius: 0 6px 6px 6px;
		}
		.dropdown-submenu:hover>.dropdown-menu {
	 	   display: block;
		}
		.dropdown-submenu>a:after {
		    display: block;
		    content: " ";
		    float: right;
		    width: 0;
		    height: 0;
		    border-color: transparent;
		    border-style: solid;
		    border-width: 5px 0 5px 5px;
		    border-left-color: #ccc;
		    margin-top: 5px;
		    margin-right: -10px;
		}
		.dropdown-submenu:hover>a:after {
		    border-left-color: #fff;
		}
		.dropdown-submenu.pull-left {
		    float: none;
		}
		.dropdown-submenu.pull-left>.dropdown-menu {
		    left: -100%;
		    margin-left: 10px;
		    -webkit-border-radius: 6px 0 6px 6px;
		    -moz-border-radius: 6px 0 6px 6px;
		    border-radius: 6px 0 6px 6px;
		}
		.bar {
		    margin-bottom: 14px;
		    margin-top: 13.5px;
		}
	 	.dropdown-toggle::after {
		    display: none;
		}
	</style>
	<script type="text/javascript">

  			$(function(){
 				$('#t1').keyup(function(){
		    		if($(this).val() != ""){
		        		$("button[type='button']").removeAttr("disabled");
		    		}  else {
		                $("button[type='button']").attr('disabled', true); //disable input
		            }
				});
 			});


	
		//Enable popovers
		$(function(){
		    $("[data-toggle=popover]").popover({
		        html : true,
		        content: function() {
		          var content = $(this).attr("data-popover-content");
		          return $(content).children(".popover-body").html();
		        },
		        title: function() {
		          var title = $(this).attr("data-popover-content");
		          return $(title).children(".popover-heading").html();
		        }
		    });
		});
		
		//Used to rotate loader icon in modals
		function rotateIcon(){
			let n = 0;
			console.log(status);
			$('#spinner').removeClass('d-none');
			let interval = setInterval(function(){
		    	n += 1;
		    	if(n >= 60000){
		            $('#spinner').addClass('d-none');
		        	clearInterval(interval);
		        }else{
		        	$('#spinner').css("transform","rotate(" + n + "deg)");
		        }
			},5);
			
			$('#initTinterInProgressModal').one('hide.bs.modal',function(){
				$('#spinner').addClass('d-none');
	        	if(interval){clearInterval(interval);}
			});
		}
		
		function wssBrowserCheck(){
			if(wssCount === 0){
				if (ws_tinter || ws_spectro && $("#startNewJob_newSession").val() === "true") {
					if(!ws_tinter.validBrowser || !ws_spectro.validBrowser){
						wssCount++;
						$("#unsupportedBrowserModal").modal('show');
					}
				}
			}
		}
		
		
		//update user's language preference
		function updateLanguage(){
			var selectedLang = $("select[id='languageList'] option:selected").val();
			console.log(selectedLang);
			
			$.ajax({
				url : "updateLocale.action",
				type : "POST",
				data : {
					request_locale : selectedLang,
				},
				datatype : "json",
				async : true,
				success : function(data) {
					if (data.sessionStatus === "expired") {
						window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
					} else {
						// reload page to update the language
						location.reload();
					}
				},
				error : function(err) {
					alert('<s:text name="global.failureColon"/>' + err);
				}
			});
		}

		$(document).ready(function() {
		    // update dropdown to display the language that the user picked if they have done so
		    var userLanguage = "${session['WW_TRANS_I18N_LOCALE']}";
		    if (userLanguage != null && userLanguage != ""){
			    $("#languageList").val(userLanguage);
		    } else {
		    	$("#languageList").val("en_US");
		    }
		});

	</script>
  </head>
		    
	    <nav class="navbar navbar-dark bg-dark navbar-expand-md">			 
			   <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false">
		      		<span class="navbar-toggler-icon"></span>
		   		</button>
			   <div class="collapse navbar-collapse" id="navbarContent"> 
<!-- 			   		<ul id="menuPanelL" class="nav navbar-nav navbar-left"> -->
<!-- 	         			<li class="nav-item dropdown"> -->
<!-- 	         				<button class="btn dropdown-toggle bg-dark pt-2" data-toggle="dropdown" type="button" aria-haspopup="true" -->
<!-- 	        				aria-expanded="false"><i class="fa fa-bars" style="color: white;font-size: 1.4rem;"></i></button> -->
	        				
<!-- 	        			</li> -->
<!-- 	        				Popover links -->
	        			
	        			
<!-- 	        			Content for Popovers -->
						
	
<!-- 			      	</ul> -->
			   		<s:set var="thisGuid" value="reqGuid" />
			     	<ul class="navbar-nav ml-auto">
			     		<li class="nav-item"><span class='navbar-text'>
				     		<s:text name="global.loggedInAsFirstNameLastName">
			     				<s:param>${sessionScope[thisGuid].firstName}</s:param>
			     				<s:param>${sessionScope[thisGuid].lastName}</s:param>
							</s:text></span>
			     		</li>
			     		<li class="nav-item p-2 pl-3 pr-3"><span id="bar"><strong style="color: dimgrey;">|</strong></span></li>
			     		<li class="nav-item"><span class='navbar-text'>${sessionScope[thisGuid].customerName}</span></li>
			     		<li class="nav-item p-2 pl-3 pr-3"><span id="bar"><strong style="color: dimgrey;">|</strong></span></li>
			     		<li class="nav-item"><select class="bg-dark navbar-text" id="languageList" onchange="updateLanguage();">
							    <option value="en_US">English</option>
							    <option value="es_ES">Español</option>
							    <option value="zh_CN">中文</option>
						    </select>
						</li>
			     		<s:url var="loUrl" action="logoutAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
			     		<li class="nav-item pl-3"><a class="nav-link" href="<s:property value="loUrl" />"><s:text name="global.logout"/> <span class='fa fa-sign-out' style="font-size: 18px;"></span></a></li> 
			     	</ul>
			   </div><!--/.nav-collapse -->
		</nav>

	  	</div>
	</div>
	
	<body>
		<div class="container-fluid">
		<div class="row mt-5">
				<div class="col-lg-2 col-md-2 col-xs-2 col-xs-0">
					
				</div>
				<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
						<div class="text-center"><img id="shercolorimg" src="graphics/shercolor-lg.jpg" class="img-fluid" style="width: 33rem;"/></div>
				</div>
				<div class="col-lg-2 col-md-2 col-xs-2 col-xs-0">

				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 col-md-3 col-xs-2 col-xs-0">
					
				</div>
				<div class="col-lg-4 col-md-6 col-sm-8 col-xs-12">

				</div>
				<div class="col-lg-4 col-md-3 col-xs-2 col-xs-0">

				</div>
			</div>
			<br>
			<!-- Display first EULA section, which is always present -->
			<div class="row">
				<div class="col-lg-3 col-md-3 col-xs-2 col-xs-0">
					
				</div>
				<div class="col-lg-7 col-md-6 col-sm-8 col-xs-12">
					<p><s:text name="showEula.mustAccept"/> </p>  
					<p><s:text name="showEula.pleaseEnterAgreementCode"/> </p>
				</div>
				<div class="col-lg-2 col-md-3 col-xs-2 col-xs-0">

				</div>
			</div>
 			<s:form action="startEulaAction" method="post" align="center" theme="bootstrap" >  
				<div class="row">
					<div class="col-lg-3 col-md-3 col-xs-2 col-xs-0">	
					</div>
					<div class="col-lg-5 col-md-6 col-sm-6 col-xs-9">
						<s:textfield id="eulaAgreementCode" name="eulaAgreementCode" autofocus="autofocus" />
					</div>
					<div class="col-lg-2 col-md-2 col-sm-2 col-xs-3">
						<s:submit cssClass="btn btn-primary btn-med pull-left" id="startEulaFocus" value="%{getText('global.continue')}" action="startEulaAction"/> 
					</div>
					<div class="col-lg-2 col-md-1 col-xs-2 col-xs-0">
						<s:hidden name="reqGuid" value="%{reqGuid}"/>			
	 				</div>
				</div>
			</s:form> 

			
			<!-- Display text box for signature and button to open next group if present. -->
			<div class="row" style="margin-bottom: 30px;">
				<div class="col-xl-4 col-lg-3 col-sm-1 col-xs-0">
				</div>
				<div class="col-xl-4 col-lg-6 col-sm-10 col-xs-12">
<%-- 					<s:form action="startNewJob" method="post" align="center" theme="bootstrap" focusElement="startNewJobFocus"> --%>
<!-- 						<div class="form-group" style="margin-top: 20px;"> -->
<%-- 							<s:hidden name="reqGuid" value="%{reqGuid}"/> --%>

<%-- 							<s:submit cssClass="btn btn-primary btn-lg pull-left" id="acceptEulaFocus" autofocus="autofocus" value="Accept Agreement" action="acceptEula"/> --%>
<!-- 						</div>   -->
<!-- 		    			Unsupported Browser Modal Window -->
					    <div class="modal fade" aria-labelledby="unsupportedBrowserModal" aria-hidden="true"  id="unsupportedBrowserModal" role="dialog">
					    	<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<h5 class="modal-title" id="unsupportedBrowserTitle"><s:text name="global.unsupportedBrowser"/></h5>
										<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
									</div>
									<div class="modal-body">
										<div class="alert alert-danger" role="alert" id="wsserror"><s:text name="global.currentlyUsingUnsupportedBrowser"/></div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-success" id="unsupportedBrowserOK" data-dismiss="modal" aria-label="%{getText('global.ok')}" ><s:text name="global.ok"/></button>
									</div>
								</div>
							</div>
						</div>		    
<%-- 					</s:form> --%>
				</div>
				<div class="col-xl-4 col-lg-3 col-sm-1 col-xs-0">
					
				</div>
			</div>
		</div>
		<br>
		<br>
		<br>

		<script>

		<!--
		  function HF_openSherwin() {
		    var popupWin = window.open("http://www.sherwin-williams.com", "Sherwin", "resizable=yes,toolbar=yes,menubar=yes,statusbar=yes,directories=no,location=yes,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }  
		  function HF_openLegal() {
		    var popupWin = window.open("http://www.sherwin-williams.com/terms/", "legal", "resizable=no,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }
		  function HF_openPrivacy() {
		    var popupWin = window.open("http://privacy.sherwin-williams.com/", "privacy", "resizable=yes,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=640,height=480,left=10,top=10");
		    popupWin.focus();
		  }
		//-->
		</script>
		
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>	