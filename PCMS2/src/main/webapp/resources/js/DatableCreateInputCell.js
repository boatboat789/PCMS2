//createTextInput(data, row.remarkChange , "dt-custom-td200 remarkChangeByInput", "remarkChange", 100 ,'remarkChange');
function createTextInput(data, value, inputClass, name, maxLength, fieldName) {
    return "<input data-search='" + data +
    "' data-field='" + fieldName + "' type='text' class='form-control " + inputClass +
    "' name='" + name + "' autocomplete='off' value='" + value +
    "' maxlength='" + maxLength + "' >";
}

//createDatetimeInput(data, 'inspectDate', 'inspectDateInput'); 
function createDatetimeInput(data, fieldName, inputClass) {
    let dateValue = "";

    if (data) {
        // สมมติ data เป็นรูปแบบ "DD/MM/YYYY HH:mm:ss"
        const parts = data.split(" ");
        if(parts.length === 2) {
            const dateParts = parts[0].split("/");
            const timeParts = parts[1].split(":");

            if(dateParts.length === 3 && timeParts.length >= 2) {
                const day = parseInt(dateParts[0], 10);
                const month = parseInt(dateParts[1], 10) - 1; // เดือนเริ่ม 0
                const year = parseInt(dateParts[2], 10);

                const hour = parseInt(timeParts[0], 10);
                const minute = parseInt(timeParts[1], 10);
                const second = timeParts.length > 2 ? parseInt(timeParts[2], 10) : 0;

                const parsed = new Date(year, month, day, hour, minute, second);
                if (!isNaN(parsed.getTime())) {
                    const pad = (n) => (n < 10 ? "0" + n : n);
                    dateValue =
                        pad(parsed.getDate()) + "/" +
                        pad(parsed.getMonth() + 1) + "/" +
                        parsed.getFullYear() + " " +
                        pad(parsed.getHours()) + ":" + 
                        pad(parsed.getMinutes()) + ":" +
                        pad(parsed.getSeconds());
                }
            }
        }
    }

    const safeField = typeof fieldName === "string" ? fieldName : "";
    const safeClass = typeof inputClass === "string" ? inputClass : "";

    return "<input class=\"form-control flatpickr " + safeClass + "\" " +
        "data-field=\"" + safeField + "\" " +
        "name=\"" + safeField + "\" " +
        "type=\"text\" " +
        "style=\"cursor: pointer; padding: 4px 2px;\" " +
        "value=\"" + dateValue + "\" " +
        "autocomplete=\"off\">";
}

//createSelectInput(data, row, meta, 'rollSplit', getSelectOptionsRollSplit)
function createSelectInput(data, row, meta, name, optionsFunction) {
	  var safeField = name;
	  var safeClass = name + "Input";

	  var html = "<select class='form-control " + safeClass + "' " +
	             "data-field='" + safeField + "' " +
	             "name='" + safeField + "'>" +
	             optionsFunction(data) +
	             "</select>";
	  return html;   
}      
////Create in jsp file
//function getSelectOptionsRollSplit(value) {       
//	let lv_html = "";  
//	lv_html += "<select class='form-control'>";         
//	lv_html += "<option value='1' selected>1</option>";
//	lv_html += "<option value='1R'>1R</option>" ; 
//	lv_html += "<option value='1L'>1L</option>" ;     
//	lv_html += "<option value='L'>L</option>" ; 
//	lv_html += "<option value='R'>R</option>" ; 
//	lv_html += "</select>";
//	var select = $(lv_html);      
//	if (value) { select.val(value).find(':selected').attr('selected', true); }
//	return select.html()
// } 
//function getSelectOptionsArticleReplaced(value) {
//	let lv_html = "";
//	lv_html += "<select class='form-control''>";   
//	lv_html += "<option value=''>Select</option>";
//	var size = ArticleList.length;
//	for (var i = 0; i < size; i++) {	
//		         
//		 var resultData = ArticleList[i]; 	 
//	  	lv_html += "<option value='"+resultData.article+"'>"+resultData.article+"</option>" ; 
//	}              
//	lv_html += "</select>";
//	var select = $(lv_html);  
//	if (value) { select.val(value).find(':selected').attr('selected', true); }
//	return select.html()
//}

//$('#MainTable').on('draw.dt', function () {
//    flatpickr(".flatpickr", {
//		enableTime: true,
//		time_24hr: true,
//		dateFormat: "d/m/Y H:i:S",
//		locale: "th",     
//		clickOpens: true,  
//		enableSeconds: true,       
//		clearBtn: true // This only works in some themes or custom builds
//    });
//});
//		$("#MainTable").on("change", "input[data-field], input.flatpickr, select[data-field]", function (e) {
//		    const $input = $(this);
//		    const $row = $input.closest("tr");
//		    const rowData = MainTable.row($row).data();
//		    const rowIndex = MainTable.row($row).index(); // index ใน DataTable
//		    const propertyName = $input.data("field") || $input.attr("name");
//		    let newValue;
//
//		    if ($input.hasClass("flatpickr")) {   
//		        const raw = $input.val().trim();    
//
//		        if (!raw) {
//		            // 🔴 Empty input: treat as null
//		            newValue = null;
//		        } else {
//		            const [datePart, timePart] = raw.split(" ");
//		            const [day, month, year] = datePart.split("/");
//		            const [hour, minute, second] = timePart.split(":"); 
//		            if (year && month && day && hour && minute) {
//		                const pad = function (n) {
//		                    return n.toString().padStart(2, "0");
//		                }; 
//		                newValue = 
//		                    year + "-" + pad(month) + "-" + pad(day) + "T" +
//		                    pad(hour) + ":" + pad(minute) + ":" + pad(second || 0);
//		            } else {
//		                newValue = null;   
//		            }
//		        } 
//		    } else {
//		        newValue = $input.val().trim();
//		    }
//         
//		    rowData[propertyName] = newValue;   
//// 		    rowData.dataStatus = 'C';
//// 		    rowData.isActive = true;
//// 		    MainTable.row($row).invalidate().draw(false);
//		});
//$("#MainTable tbody").on("click",".btn-splitRoll",function(){
//	var data = MainTable.row( $(this).parents('tr') ).data();       
//	var $row = $(this).parents("tr");   
//	var idx = MainTable.row($row).index();          
//    var rowData = MainTable.row($row).data() ;      
//    if(rowData.remarkChange.trim() == ''){
//	Swal.fire({
//		icon : "warning",
//		title : "Warning!",
//		text : "Remark need to input",
//	});
//    }
//    else{ 
//   	 Swal.fire({     
// 			  title: "Split roll : "+rowData.rollNumber+" ?",           
// 			  text: "Are you going to split roll number ?",
// 			  icon: "warning",          
// 			  showCancelButton: true,
// 			  confirmButtonColor: "#3085d6",
// 			  cancelButtonColor: "#d33",
// 			  confirmButtonText: "Yes, Split Roll !"
// 		})
// 		.then((result) => {   
// 			 if (result.isConfirmed) {
// 			  	var arrTmp = [];   
// 			  	arrTmp.push(rowData);     
// 			  splitRollNumber(arrTmp);
// 			} else { }     	 
//  		})  
//    }
//});




//handleInputChange(".DivisionInput", "division");
//handleInputChange(".CodeInput", "code");
//handleInputChange(".ArticleGreigeInput", "articleGreige");
//handleInputChange(".DesignGreigeInput", "designGreige");
//handleInputChange(".FiberInput", "fiber");
//handleInputChange(".IsCottonInput", "isCotton");    
//handleInputChange(".ArticleCommentInput", "specialCaseId");
//handleInputChange(".ArticleReplacedInput", "articleReplaced");

//handleNumericInputChange(".QtyGreigeMRInput", "qtyGreigeMR", checkIntOrDecimalThirteen);
//handleNumericInputChange(".FormulaPCInput", "formulaPC", checkIntOrDecimalThirteen);
//handleNumericInputChange(".FormulaKGInput", "formulaKG", checkIntOrDecimalThirteen);

//handleSingleCheckboxUpdate(".IsQtyGreigeForPOInput", "isQtyGreigeForPO");

//function handleSingleCheckboxUpdate(className, fieldName) {
//	  $("#MainTable").on("change", className, function () {    
//	    const $row = $(this).closest("tr");
//	    const rowIdx = MainTable.row($row).index();
//	    const row = MainTable.row($row);
//	    const rowData = MainTable.row($row).data();
//	    const isChecked = $(this).prop("checked"); 
//	    rowData[fieldName] = isChecked;     
//	    rowData.dataStatus = 'C';             
//	    MainTable.row(rowIdx).invalidate();          
//	  });
//	}
//function handleInputChange(className, fieldName) {    
//	  $("#MainTable").on("change", className, function (e) {
//	    var $row = $(this).closest("tr");
//	    var idx = MainTable.row($row).index();
//	    var rowData = MainTable.row($row).data();
//	    rowData[fieldName] = $(this).val();
//	    rowData.dataStatus = 'C';   
//	    MainTable.row(idx).invalidate();
//	  });
//	}
//function handleNumericInputChange(className, fieldName, validator) {
//	  $("#MainTable").on("change", className, function (e) {
//	    var $row = $(this).closest("tr");
//	    var idx = MainTable.row($row).index();
//	    var rowData = MainTable.row($row).data();
//	    let newValue = $(this).val().trim();
//	    let oldValue = rowData[fieldName];
//	    if (validator(newValue) || newValue === '') {
//	      rowData[fieldName] = newValue;
//	      rowData.dataStatus = 'C';
//	    } else {
//	      rowData[fieldName] = oldValue;
//	    }
//	    MainTable.row(idx).invalidate();
//	  });
//	}
//function handleEnterKeyUpdate(className, fieldName, validator) {
//	  $("#MainTable").on("keydown", className, function (e) {
//	    if (e.keyCode === 13) {
//	      $(this).off('blur');
//	      e.preventDefault();
//	      let newVal = $(this).val().trim();
//	      if (!validator(newVal) && newVal !== '') return;
//
//	      let tblData = MainTable.rows('.selected').data();
//	      let idxArray = MainTable.rows({ selected: true }).indexes();
//
//	      for (let i = 0; i < tblData.length; i++) {
//	        tblData[i][fieldName] = newVal;
//	        tblData[i].dataStatus = 'C';
//	        MainTable.row(idxArray[i]).invalidate();
//	      }
//	    }
//	  });
//	}