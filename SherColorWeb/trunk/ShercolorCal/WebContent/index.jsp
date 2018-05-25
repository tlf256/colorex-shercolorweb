<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html">
<html>
   <head><title>Cal File Test</title>
    <script type="text/javascript" src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
    <script>
    function getCal(){
    var cal = $.json({
  	  url: "getCalTemplate",
  	  context: document.body,
  	  data: { 
    	  
    	    filename: "CCE_COROBD600.zip"
    },
   
    type: "POST"
  	});
  	console.log(cal);
    }
    $(document).ready(function() {
        getCal();	
    });
    	
    </script>
     
        <script type="text/javascript">
         
        </script>
    </head>
    <body id="mainBody" >
     Hello User!
    </body>

	
</html>