<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- Fixed navbar -->
<nav class="navbar navbar-dark bg-dark navbar-expand-md pt-0 pb-0">
	<ul id="menuPanelL" class="nav navbar-nav navbar-left">
		<li class="nav-item"><a href='<s:url action="loginAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><img src="graphics/shercolor-sm.jpg" alt="Sher-Color" style="height: 3.44rem;"/></a></li>
		<li class="nav-item pl-3 pt-2"><a class="navbar-text" href='<s:url action="listJobsAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="displayTintQueue" value="true"/></s:url>'>
	    	<s:text name="global.tintQueueColon"></s:text><span class="badge badge-secondary" id="tintQueueCountText"></span></a>
		</li>
	 </ul>
	   
	 
	   <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false">
      		<span class="navbar-toggler-icon"></span>
   		</button>
	   <div class="collapse navbar-collapse" id="navbarContent">
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
					    <option value="es_ES" class="d-none">Español</option>
					    <option value="zh_CN">中文</option>
				    </select>
				</li>
	     		<s:url var="loUrl" action="logoutAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
	     		<li class="nav-item pl-3"><a class="nav-link" href="<s:property value="loUrl" />"><s:text name="global.logout"/> <span class='fa fa-sign-out'></span></a></li> 
	     	</ul> 
	   </div><!--/.nav-collapse -->
</nav>

<script type="text/javascript">
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
				window.location.href = "./invalidLoginAction.action";
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
	$('#tintQueueCountText').text(${sessionScope[thisGuid].tintQueueCount});
    // update dropdown to display the language that the user picked if they have done so
    var userLanguage = "${session['WW_TRANS_I18N_LOCALE']}";
    if (userLanguage != null && userLanguage != ""){
	    $("#languageList").val(userLanguage);
    } else {
    	$("#languageList").val("en_US");
    }
});

</script>



