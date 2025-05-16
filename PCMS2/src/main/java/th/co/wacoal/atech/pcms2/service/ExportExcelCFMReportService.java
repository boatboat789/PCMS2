package th.co.wacoal.atech.pcms2.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;

import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;

@Controller
public class ExportExcelCFMReportService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int rowCount = 0;

    // Excel column headers (display names)
    private static final String[] HEADERS = {
        "Send Date", "No Per Day", "Reply Date", "CFM No", "Customer Name", 
        "SO", "SO Line", "Due Date", "PO", "Material", 
        "Product Name", "Lab No", "Color", "Prod ID", "Lot No", 
        "Dye L", "Dye Da", "Dye Db", "Dye St", "Dye DeltaE", 
        "Color Check L", "Color Check Da", "Color Check Db", "Color Check St", 
        "Color Check DeltaE", "CFM L", "CFM Da", "CFM Db", "CFM St", 
        "CFM DeltaE", "Color Check Date", "Color Check Status", "Color Check Remark", 
        "Result", "QC Comment", "Remark From Submit", "Next Lot", "Qty", "Unit Id"
    };

    // Corresponding Java field names (must match your entity)
    private static final String[] FIELDS = {
        "sendDate", "noPerDay", "replyDate", "cfmNo", "customerName", 
        "so", "soLine", "dueDate", "po", "material", 
        "productName", "labNo", "color", "prodID", "lotNo", 
        "dyeL", "dyeDa", "dyeDb", "dyeSt", "dyeDeltaE", 
        "colorCheckL", "colorCheckDa", "colorCheckDb", "colorCheckSt", 
        "colorCheckDeltaE", "cfmL", "cfmDa", "cfmDb", "cfmSt", 
        "cfmDeltaE", "colorCheckDate", "colorCheckStatus", "colorCheckRemark", 
        "result", "qcComment", "remarkFromSubmit", "nextLot", "qty", "unitId"
    };

    private CellStyle headerStyle;
    private CellStyle dateStyle;
    private CellStyle numberStyleTwoDecimal;
    private CellStyle defaultStyle;
    private CellStyle integerStyle; 
    public ExportExcelCFMReportService() {
        initializeWorkbook();
    }

    private void initializeWorkbook() {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("CFM_Report");
        
        // Create styles
        createStyles();
    }

    private void createStyles() {
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        // Header style
        headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(headerStyle);

        // Date style
        dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));
        setBorders(dateStyle);

        // Integer style (No decimal)
        integerStyle = workbook.createCellStyle();
        integerStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
        setBorders(integerStyle);

        // Number style with 2 decimal places
        numberStyleTwoDecimal = workbook.createCellStyle();
        numberStyleTwoDecimal.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        setBorders(numberStyleTwoDecimal);

        // Default style for empty cells
        defaultStyle = workbook.createCellStyle();
        setBorders(defaultStyle);
    }
 


    private void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    public void export(HttpServletResponse response, ArrayList<Z_ATT_CustomerConfirm2Detail> dataList) throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }

        try {
            writeHeader();
            writeData(dataList);
            
            // Auto-size columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Generate filename with timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
            String currentDateTime = sdf.format(new Date());
            String headerValue = "attachment; filename=CFM_Report_" + currentDateTime + ".xlsx";

            // Set content type and headers
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", headerValue);
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            // Write workbook to response
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                    IOUtils.copy(in, response.getOutputStream());
                    response.getOutputStream().flush();  // Ensure all data is sent
                }
            }
        } finally {
            workbook.close();
        }
    }


    private void writeHeader() {
        Row headerRow = sheet.createRow(rowCount++);
        
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeData(ArrayList<Z_ATT_CustomerConfirm2Detail> dataList) {
        for (Z_ATT_CustomerConfirm2Detail item : dataList) {
            Row row = sheet.createRow(rowCount++);
            
            for (int i = 0; i < FIELDS.length; i++) {
                Object value = getFieldValue(item, FIELDS[i]);
                createCell(row, i, value, FIELDS[i]);
            }
        }
    }
 private void createCell(Row row, int columnIndex, Object value, String fieldName) {
    Cell cell = row.createCell(columnIndex);

    if (value == null) {
        cell.setCellValue("");
        cell.setCellStyle(defaultStyle); // Ensure border is applied to empty cells
        return;
    }

    if (fieldName.equals("noPerDay") || fieldName.equals("soLine")) {
        // For noPerDay and soLine, display as integer without .00
        try {
            int intValue = Integer.parseInt(value.toString().replaceAll("\\.0+$", ""));
            cell.setCellValue(intValue);
        } catch (NumberFormatException e) {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(integerStyle); 
    } 
    else if (value instanceof BigDecimal) {
        BigDecimal bdValue = (BigDecimal) value;
        if (bdValue.compareTo(BigDecimal.ZERO) == 0) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(bdValue.doubleValue());
        }
        cell.setCellStyle(numberStyleTwoDecimal); 
    } 
    else if (value instanceof Date) {
        cell.setCellValue((Date) value);
        cell.setCellStyle(dateStyle);
    } 
    else {
        cell.setCellValue(value.toString());
        cell.setCellStyle(defaultStyle);
    }
}

//    private void createCell(Row row, int columnIndex, Object value) {
//        Cell cell = row.createCell(columnIndex);
//        
//        if (value == null) {
//            cell.setCellValue("");
//            cell.setCellStyle(defaultStyle);
//        } else if (value instanceof Date) {
//            cell.setCellValue((Date) value);
//            cell.setCellStyle(dateStyle);
//        } else if (value instanceof Number) {
//            cell.setCellValue(((Number) value).doubleValue());
//            cell.setCellStyle(numberStyle);
//        } else if (value instanceof Boolean) {
//            cell.setCellValue((Boolean) value);
//            cell.setCellStyle(defaultStyle);
//        } else {
//            cell.setCellValue(value.toString());
//            cell.setCellStyle(defaultStyle);
//        }
//    }

    private Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            System.err.println("Error accessing field " + fieldName + ": " + e.getMessage());
            return null;
        }
    }
}