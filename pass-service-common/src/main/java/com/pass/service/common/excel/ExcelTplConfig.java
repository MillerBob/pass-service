package com.pass.service.common.excel;

import java.io.Serializable;
import java.util.List;

public class ExcelTplConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String templateId;
	private int sheetNum = 0;

	private int titleRowNum = 0;

	private int dataRowNum = 1;

	/**
	 * 模板路径
	 */
	private String templatePath;

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * 自定义文件与数据库表列之间的映射关系
	 */
	private String columnDefineHandler;

	/**
	 * 数据入库处理逻辑,默认走ExcelDataHandlerBO
	 */
	private String fileInputHandler;

	public String getFileInputHandler() {
		return fileInputHandler;
	}

	public void setFileInputHandler(String fileInputHandler) {
		this.fileInputHandler = fileInputHandler;
	}

	/**
	 * 数据清洗校验逻辑
	 */
	private String dataCleanHandler;

	/**
	 * 数据入库逻辑
	 */
	private String dataDoneHandler;

	private String eltBoName;

	public String getEltBoName() {
		return eltBoName;
	}

	public void setEltBoName(String eltBoName) {
		this.eltBoName = eltBoName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	public int getTitleRowNum() {
		return titleRowNum;
	}

	public void setTitleRowNum(int titleRowNum) {
		this.titleRowNum = titleRowNum;
	}

	public int getDataRowNum() {
		return dataRowNum;
	}

	public void setDataRowNum(int dataRowNum) {
		this.dataRowNum = dataRowNum;
	}

	public String getColumnDefineHandler() {
		return columnDefineHandler;
	}

	public void setColumnDefineHandler(String columnDefineHandler) {
		this.columnDefineHandler = columnDefineHandler;
	}

	public String getDataCleanHandler() {
		return dataCleanHandler;
	}

	public void setDataCleanHandler(String dataCleanHandler) {
		this.dataCleanHandler = dataCleanHandler;
	}

	public String getDataDoneHandler() {
		return dataDoneHandler;
	}

	public void setDataDoneHandler(String dataDoneHandler) {
		this.dataDoneHandler = dataDoneHandler;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ExcelColmnTplConfig> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ExcelColmnTplConfig> columnList) {
		this.columnList = columnList;
	}

	private String tableName;

	private List<ExcelColmnTplConfig> columnList;

	/**
	 * 导入模板定义名
	 */
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
