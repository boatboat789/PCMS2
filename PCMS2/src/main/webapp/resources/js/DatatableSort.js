$.fn.dataTable.ext.order['dom-text'] = function  ( settings, col )
{
    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
        return $('input', td).val();
    } );
}
 
/* Create an array with the values of all the input boxes in a column, parsed as numbers */
$.fn.dataTable.ext.order['dom-text-numeric'] = function  ( settings, col )
{
    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
        return $('input', td).val() * 1;
    } );
}      
 
/* Create an array with the values of all the select options in a column */
$.fn.dataTable.ext.order['dom-select'] = function  ( settings, col )
{
    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
        return $('select', td).val();
    } );
}
 
/* Create an array with the values of all the checkboxes in a column */
$.fn.dataTable.ext.order['dom-checkbox'] = function  ( settings, col )
{
    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
        return $('input', td).prop('checked') ? '1' : '0';
    } );
}
jQuery.fn.dataTableExt.oSort['date-euro-asc'] = function(a, b) {
	var x = dateHeight(a);
	var y = dateHeight(b);
	var z = ((x < y) ? -1 : ((x > y) ? 1 : 0));
	return z;
};
	jQuery.fn.dataTableExt.oSort['date-euro-desc'] = function(a, b) {
	var x = dateHeight(a);
	var y = dateHeight(b);
	var z = ((x < y) ? 1 : ((x > y) ? -1 : 0));
	return z;
};
$.fn.dataTable.Api.register( 'order.neutral()', function () {
	return this.iterator( 'table', function ( s ) {
		s.aaSorting.length = 0;
		s.aiDisplay.sort( function (a,b) {
			return a-b;
		} );
		s.aiDisplayMaster.sort( function (a,b) {
			return a-b;
		} );
	} );
} );
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