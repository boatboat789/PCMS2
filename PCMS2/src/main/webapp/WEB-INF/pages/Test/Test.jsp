<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%> 
<!DOCTYPE html>
<html>       
<head>   
	<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>  
	<title>PCMS - Detail</title>            	               
	<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>          
	<link href="<c:url value="/resources/css/style_overide.css" />" rel="stylesheet" type="text/css">    
	<link href="<c:url value="/resources/css/datatable.overide.css" />" rel="stylesheet" type="text/css">       
</head>          
<body>       	        
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>    
	<button id="addRow">Add new row</button>        
	<button id="reSet">Re set</button>        
	<div id="wrapper-center" class="row" style="margin: 0 5px;">      
		
	    <table id="example" class="display nowrap" style="width:100%">
	        <thead>
	            <tr>
	                <th>Column 1</th> 
	                <th>Column 2</th>
	                <th>Column 3</th>
	                <th>Column 4</th>
	                <th>Column 5</th>
	            </tr>
	        </thead>  
	        <tbody>
	        <tr>
            <td>Tiger Nixon</td>
            <td>System Architect</td>
            <td>Edinburgh</td>
            <td>61</td>
            <td>2011/04/25</td> 
          </tr>
          <tr>
            <td>Garrett Winters</td>
            <td>Director</td>
            <td>Edinburgh</td>
            <td>63</td>
            <td>2011/07/25</td>    
          </tr>
          <tr>
            <td>Ashton Cox</td>
            <td>Technical Author</td>
            <td>San Francisco</td>
            <td>66</td>
            <td>2009/01/12</td> 
          </tr>
          <tr>
            <td>Cedric Kelly</td>
            <td>Javascript Developer</td>
            <td>Edinburgh</td>
            <td>22</td>
            <td>2012/03/29</td> 
          </tr>
          <tr>
            <td>Jenna Elliott</td>
            <td>Financial Controller</td>
            <td>Edinburgh</td>
            <td>33</td>
            <td>2008/11/28</td> 
          </tr>
          <tr>
            <td>Brielle Williamson</td>
            <td>Integration Specialist</td>
            <td>New York</td>
            <td>61</td>
            <td>2012/12/02</td>    
          </tr>
	        </tbody>   
	    </table> 
	</div >
    <jsp:include page="/WEB-INF/pages/config/footer.jsp"></jsp:include>   
</body>               
<script src="<c:url value="/resources/js/DatatableSort.js" />"></script>  
<script src="<c:url value="/resources/js/General.js" />"></script>     
<style>        
.p-r-15 {padding-right: 15px !important; }      
/* tfoot .dtfc-fixed-left{   */
/*     background-color: white;    */  
/* }      */
</style> 
<script>      
var dragSrc = null;  //Globally track source cell
var cells = null;  // All cells in table
$(document).ready(function () {
    var t = $('#example').DataTable({
    	 pageLength: 100,
    	    columnDefs: [ {
    	      targets: '_all',    
    	      // Set HTML5 draggable for all cells
    	      createdCell: function (td, cellData, rowData, row, col) {
    	        $(td).attr('draggable', 'true');
    	      }
    	    } ],
    	    drawCallback: function () {
    	      // Apply HTML5 drag and drop listeners to all cells
    	      cells = document.querySelectorAll('#example td');
    	        [].forEach.call(cells, function(cell) {
    	          cell.addEventListener('dragstart', handleDragStart, false);
    	          cell.addEventListener('dragenter', handleDragEnter, false)
    	          cell.addEventListener('dragover', handleDragOver, false);
    	          cell.addEventListener('dragleave', handleDragLeave, false);
    	          cell.addEventListener('drop', handleDrop, false);
    	          cell.addEventListener('dragend', handleDragEnd, false);
    	        });
    	    }
    });
    var counter = 1;
 
    $('#addRow').on('click', function () {
        t.row.add([counter + '.1', counter + '.2', counter + '.3', counter + '.4', counter + '.5']).draw(false); 
        counter++;     
//         alert(   
//         	    'Number of row entries: '+
//         	    t
//         	        .column( 0 )
//         	        .data()
//         	        .length
//         	);
    });
    $('#reSet').on('click', function () {
// //         t.row.add([counter + '.1', counter + '.2', counter + '.3', counter + '.4', counter + '.5']).draw(false);  
		console.log("Column Count : ",t.columns(':visible').count())    
		console.log("Row Count : ",t.column( 0 ).data().length)
		// ROW 
       	 let size = t.column( 0 ).data().length;
		let valBase = '';
        for (let i = 0; i <  size; i++) {
        	// COL
//         	for (let j = 0; j <  t.columns(':visible').count(); j++) {
//             	t .cell( idx, 0 ).data( 'Updated' ).draw(); //         dataTable.cell( idx, 2 ).data();
//            	} 
        	 let val = '';   
        	 console.log(i,size)         
        	 if(i == 0){          
            	 valBase = t.cell( i, 1 ).data( );
        	 }            
        	if(i < size-1){ 
        		val = t.cell( i+1, 1 ).data( );   
        		t.cell( i, 1 ).data( val  ).draw(false);   
        	}        
        	else{     
        		t.cell( i, 1 ).data( valBase ).draw(false);   
        	}     
    		 
       	} 
		
		
    	 $('#example').DataTable().cells().every( function (rowIndex, colIndex) {
    	        var data = this.data(); 
    	        console.log("Row:", rowIndex, "Col:", colIndex, "Data:", data);
    	    });   
    });   
    
    // Automatically add a first row of data
    $('#addRow').click();
    function handleDragStart(e) {
   	  this.style.opacity = '0.4';  // this / e.target is the source node.
   	  dragSrc = this;  // Keep track of source cell 
   	  // Allow moves
   	  e.dataTransfer.effectAllowed = 'move'; 
   	  // Get the cell data and store in the transfer data object
   	  e.dataTransfer.setData('text/html', this.innerHTML);
   	}

   	function handleDragOver(e) {
   	  if (e.preventDefault) {
   	    e.preventDefault(); // Necessary. Allows us to drop.
   	  } 
   	  // Allow moves
   	  e.dataTransfer.dropEffect = 'move';  // See the section on the DataTransfer object.
	  return false;
   	} 
   	function handleDragEnter(e) {
   	  // this / e.target is the current hover target. 
   	  // Apply drop zone visual
   	  this.classList.add('over');
   	} 
   	function handleDragLeave(e) {
   	  // this / e.target is previous target element. 
   	  // Remove drop zone visual
   	  this.classList.remove('over');  
   	}
   	function handleDrop(e) {
   	  // this / e.target is current target element. 
   	  if (e.stopPropagation) {
   	    e.stopPropagation(); // stops the browser from redirecting.
   	  } 
   	  // Don't do anything if dropping the same column we're dragging.
   	  if (dragSrc != this) {
   	    // Set the source column's HTML to the HTML of the column we dropped on.
   	    dragSrc.innerHTML = this.innerHTML; 
   	    // Set the distination cell to the transfer data from the source
   	    this.innerHTML = e.dataTransfer.getData('text/html'); 
   	    // Invalidate the src cell and dst cell to have DT update its cache then draw
   	    console.log(dragSrc)   
   	    console.log(this)
   	    t.cell(dragSrc).invalidate();    	
   	    t.cell(this).invalidate().draw(false);
   	  } 
   	  return false;
   	} 
   	function handleDragEnd(e) {
   	  // this/e.target is the source node.
   	  this.style.opacity = '1.0';
   	  [].forEach.call(cells, function (cell) {
   	    // Make sure to remove drop zone visual class
   	    cell.classList.remove('over');
   	  });
   	}

});
</script> 
</html>