$.fn.dataTable.ext.order['dom-text'] = function(settings, col) {
	return this.api().column(col, { order: 'index' }).nodes().map(function(td, i) {
		return $('input', td).val();
	});
}

/* Create an array with the values of all the input boxes in a column, parsed as numbers */
$.fn.dataTable.ext.order['dom-text-numeric'] = function(settings, col) {
	return this.api().column(col, { order: 'index' }).nodes().map(function(td, i) {
		return $('input', td).val() * 1;
	});
}

/* Create an array with the values of all the select options in a column */
$.fn.dataTable.ext.order['dom-select'] = function(settings, col) {
	return this.api().column(col, { order: 'index' }).nodes().map(function(td, i) {
		return $('select', td).val();
	});
}

/* Create an array with the values of all the checkboxes in a column */
$.fn.dataTable.ext.order['dom-checkbox'] = function(settings, col) {
	return this.api().column(col, { order: 'index' }).nodes().map(function(td, i) {
		return $('input', td).prop('checked') ? '1' : '0';
	});
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
$.fn.dataTable.Api.register('order.neutral()', function() {
	return this.iterator('table', function(s) {
		s.aaSorting.length = 0;
		s.aiDisplay.sort(function(a, b) {
			return a - b;
		});
		s.aiDisplayMaster.sort(function(a, b) {
			return a - b;
		});
	});
}); 