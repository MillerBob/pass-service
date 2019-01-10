package com.pass.service.common.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportExcelUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImportExcelUtil.class);

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     * 
     * @param in ,fileName
     * @return
     * @throws IOException
     */
    public static List<Map<String, Object>> getBankListByExcel(InputStream in, String fileName,
            Map<String, String> map) throws Exception {
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
        	LOGGER.info("创建Excel工作薄为空！");
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        List<Map<String, Object>> dataListMaps = new ArrayList<Map<String, Object>>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            List<String> titleList = new ArrayList<String>();
            int colNum = 0; // 列数
            Row row0 = sheet.getRow(0);
            colNum = row0.getPhysicalNumberOfCells();
            for (int i1 = 0; i1 < colNum; i1++) {
                String nameString = (String) getCellValue(row0.getCell((short) i1));
                nameString = nameString.replace(" ", "");
                titleList.add(nameString);// 得到列名
            }

            // 遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }
                // 遍历所有的列
                Map<String, Object> mapCell = new HashMap<String, Object>();
                for (int y = row.getFirstCellNum(); y < titleList.size(); y++) {
                    cell = row.getCell(y);
                    if (null != cell) {
                        mapCell.put(titleList.get(y), getCellValue(cell));
                    }
                }
                Map<String, Object> mapNew = new HashMap<>();
                for (String key : map.keySet()) {
                    mapNew.put(map.get(key), mapCell.get(key));
                }
                dataListMaps.add(mapNew);
            }
        }
        // work.close();
        return dataListMaps;
    }

    /**
     * 
     * @Description map配置，名称为value,key为字段名
     * @param in
     * @param fileName
     * @param map
     * @return
     * @throws Exception List<Map<String,Object>>
     * @exception:
     * @author: liangcm
     * @time:2017年5月3日 下午8:27:10
     */
    public static List<Map<String, Object>> getListByExcel(InputStream in, String fileName,
            Map<String, String> map) throws Exception {
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        List<Map<String, Object>> dataListMaps = new ArrayList<Map<String, Object>>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            List<String> titleList = new ArrayList<String>();
            int colNum = 0; // 列数
            Row row0 = sheet.getRow(0);
            if (row0 == null) {
                throw new RuntimeException("导入模版不正确，请下载正确的模版导入");
            }

            colNum = row0.getPhysicalNumberOfCells();
            for (int i1 = 0; i1 < colNum; i1++) {
                String nameString = (String) getCellValue(row0.getCell((short) i1));
                if (StringUtils.isNotBlank(nameString) && !map.containsValue(nameString)) {
                    throw new RuntimeException("导入模版不正确，请下载正确的模版导入");
                }
                nameString = nameString.replace(" ", "");
                titleList.add(nameString);// 得到列名
            }

            // 遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }
                // 遍历所有的列
                Map<String, Object> mapCell = new HashMap<String, Object>();
                for (int y = row.getFirstCellNum(); y <= row.getPhysicalNumberOfCells(); y++) {
                    cell = row.getCell(y);
                    if (null != cell) {
                        mapCell.put(titleList.get(y), getCellValue(cell));
                    }
                }
                Map<String, Object> mapNew = new HashMap<>();
                for (String key : map.keySet()) {
                    Object value = mapCell.get(map.get(key));
                    if (value != null) {
                        mapNew.put(key, value);
                    }
                }
                dataListMaps.add(mapNew);
            }
        }
        // work.close();
        return dataListMaps;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * 
     * @param inStr ,fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        if (!inStr.markSupported()) {
            inStr = new PushbackInputStream(inStr, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(inStr)) {
            return new HSSFWorkbook(inStr);
        }
        if (POIXMLDocument.hasOOXMLHeader(inStr)) {
            return new XSSFWorkbook(OPCPackage.open(inStr));
        }
        throw new Exception("解析的文件格式有误！");

    }

    /**
     * 描述：对表格中数值进行格式化
     * 
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        Object value = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); // 日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00"); // 格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = cell.getNumericCellValue() + "";
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

}
