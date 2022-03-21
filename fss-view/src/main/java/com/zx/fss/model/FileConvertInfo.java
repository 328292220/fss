package com.zx.fss.model;

public class FileConvertInfo {
    private String convertType;
    private int pageNum;
    private String fileGuid;
    private String fileDirPath;
    private String fileName;
    private String filePath;
    private String errorInfo;
    private String fileExt;
    private boolean haveConvert;

    public FileConvertInfo(){
        errorInfo = "";
        haveConvert = false;
    }

    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getFileGuid() {
        return fileGuid;
    }

    public void setFileGuid(String fileGuid) {
        this.fileGuid = fileGuid;
    }

    public String getFileDirPath() {
        return fileDirPath;
    }

    public void setFileDirPath(String fileDirPath) {
        this.fileDirPath = fileDirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public boolean isHaveConvert() {
        return haveConvert;
    }

    public void setHaveConvert(boolean haveConvert) {
        this.haveConvert = haveConvert;
    }
}
