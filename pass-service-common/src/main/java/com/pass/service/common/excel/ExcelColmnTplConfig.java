package com.pass.service.common.excel;

import java.io.Serializable;

public class ExcelColmnTplConfig implements Serializable {
    private String fileCol;
    private String dbCol;
    private Boolean isNotNull = false;

    public String getFileCol() {
        return fileCol;
    }

    public void setFileCol(String fileCol) {
        this.fileCol = fileCol;
    }

    public String getDbCol() {
        return dbCol;
    }

    public void setDbCol(String dbCol) {
        this.dbCol = dbCol;
    }

    public Boolean getIsNotNull() {
        return isNotNull;
    }

    public void setIsNotNull(Boolean isNotNull) {
        this.isNotNull = isNotNull;
    }
}
