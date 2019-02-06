
 var numbersOnly = /^\d+$/;
 var decimalOnly = /^\s*-?[1-9]\d*(\.\d{1,2})?\s*$/;
 var uppercaseOnly = /^[A-Z]+$/;
 var lowercaseOnly = /^[a-z]+$/;
 var stringOnly = /^[A-Za-z0-9]+$/;

 function testInputData(myfield, restrictionType) {

  var myData = document.getElementById(myfield).value;
  if(myData!==''){
   if(restrictionType.test(myData)){

   }else{
    alert('Your data input is invalid!');
   }
  }else{
   alert('Please enter data!');
  }
  return;
    
 }