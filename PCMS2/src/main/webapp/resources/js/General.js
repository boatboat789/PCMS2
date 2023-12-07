function preLoaderHandler (x){         
	   x.style.display = 'none';  
	}              
function trim(str) {
    if (str == null) { // test for null or undefined
        return "";  
    }
	str = str.replace(/^\s+/, '');
	for (var i = str.length - 1; i >= 0; i--) {
		if (/\S/.test(str.charAt(i))) {
			str = str.substring(0, i + 1);
			break;
		}
	}
	return str;
	}

function dateHeight(dateStr){
	if (trim(dateStr) != '') {
		var frDate = trim(dateStr).split(' ');
	// 	var frTime = frDate[1].split(':');
		var frDateParts = frDate[0].split('/');
		var day = frDateParts[0] * 60 * 24;
		var month = frDateParts[1] * 60 * 24 * 31;
		var year = frDateParts[2] * 60 * 24 * 366;
	// 	var hour = frTime[0] * 60;
	// 	var minutes = frTime[1];
	// 	var x = day+month+year+hour+minutes;
		var x = day+month+year;
	} else {
		var x = 99999999999999999; //GoHorse!
	}
	return x;
}
function checkString(str) {  
    var regex=/^[a-zA-Z]+$/;
	if(str.match(regex)) { return true; }
  	else { return false; }
}
function checkInt(str) {         
    var regex=/^[0-9]+$/;
	if(regex.test(str)) { return true; }
  	else { return false; }
}
function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}   
function isNumeric(value) {
    return /^\d+$/.test(value);
}   
function stringToDate(dateString){ 
    var date = new Date();         
    var datearray = dateString.split("/");  
    date = new Date(datearray[1] + '-' + datearray[0] + '-' + datearray[2]) ;   
	return  date ; 	             
}    

function dateDDMMYYYToDDMM(dateStr){       
	var datearray = dateStr.split("/");   
	if(datearray.length > 1 ){
		dateStr = datearray[0] + '/'+datearray[1] 
	}else{
		dateStr = ''
	}
    return dateStr;
}