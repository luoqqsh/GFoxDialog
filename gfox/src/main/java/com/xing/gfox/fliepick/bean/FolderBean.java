package com.xing.gfox.fliepick.bean;

import java.io.Serializable;
import java.util.List;

public class FolderBean implements Serializable {
    private static final long serialVersionUID = 7523187287116967946L;

    private List<FileBean> mediaFileBeanList;
    private String folderName;
    private String folderIcon;

    public List<FileBean> getMediaFileBeanList() {
        return mediaFileBeanList;
    }

    public void setMediaFileBeanList(List<FileBean> mediaFileBeanList) {
        this.mediaFileBeanList = mediaFileBeanList;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    private String folderPath;

    public String getFolderIcon() {
        return folderIcon;
    }

    public void setFolderIcon(String folderIcon) {
        this.folderIcon = folderIcon;
    }
}
