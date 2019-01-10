package com.pass.service.common.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pass.service.common.util.MapUtil;
import com.pass.service.common.util.PoiExcelUtils;

/**
 *
 * @author: liangcm
 */
public class ExportExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);

    protected String errorDbCol;

    public String getErrorDbCol() {
        return "O_MESG";
    }

    public static String exportData(ExcelTplConfig tplCfg, List<Map<String, Object>> dataList,
            String sheetName, String tempfilePath) {

        FileOutputStream out;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String exportXlsName = "export-" + simpleDateFormat.format(new Date()) + ".xls";

        try {
            logger.info("tempfilePath:" + tempfilePath);
            File folder = new File(tempfilePath);
            if (!folder.exists()) {
                folder.setWritable(true, false);
                folder.mkdir();
            }
            String filePath = tempfilePath + exportXlsName;

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);

            List<ExcelColmnTplConfig> columnList =
                    new ArrayList<ExcelColmnTplConfig>(tplCfg.getColumnList());

            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < columnList.size(); i++) {
                ExcelColmnTplConfig column = columnList.get(i);

                HSSFCell cell = row.createCell(i);
                HSSFCellStyle headCellStyle = PoiExcelUtils.getHeadNormalStyle(wb);
                headCellStyle.setWrapText(true);
                cell.setCellStyle(headCellStyle);
                cell.setCellValue(column.getFileCol());
            }

            /*
             * HSSFFont font = wb.createFont(); font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
             * // font.setItalic(true); // 是否使用斜体 // 设置单元格类型 HSSFCellStyle cellStyle =
             * wb.createCellStyle(); cellStyle.setFont(font);
             * cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
             * cellStyle.setWrapText(true);
             */

            for (int i = 0; i < dataList.size(); i++) {
                HSSFRow dataRow = sheet.createRow(1 + i);
                Map<String, Object> data = dataList.get(i);
                for (int j = 0; j < columnList.size(); j++) {
                    ExcelColmnTplConfig column = columnList.get(j);
                    HSSFCell cell = dataRow.createCell(j);
                    cell.setCellValue(MapUtil.getStringValue(data, column.getDbCol()));
                    // cell.setCellStyle(cellStyle);// 设置单元格样式
                }
            }
            sheet.autoSizeColumn((short) 0); // 调整第一列宽度
            sheet.autoSizeColumn((short) 1); // 调整第二列宽度
            sheet.autoSizeColumn((short) 2); // 调整第三列宽度
            sheet.autoSizeColumn((short) 3); // 调整第四列宽度
            File file = new File(filePath);
            logger.info(filePath);
            out = new FileOutputStream(file);
            wb.write(out);
            out.close();
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException("文件写入异常！" + e.getMessage());
        } finally {
            out = null;
        }

        return exportXlsName;
    }

}
