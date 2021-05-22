package com.xing.gfox.rxHttp.test;

import java.util.List;

public class TvListData {

    /**
     * name : IPTV - ListFile
     * date : 2020-02-06
     * txtURL : rtsp://27.148.219.129:554/live/ch15102717162145789452.sdp?playtype=1&boid=001&backupagent=27.148.219.129:554&clienttype=1&time=20191117235603+08&life=172800&ifpricereqsnd=1&vcdnid=001&userid=5922751202156&mediaid=ch15102717162145789452&ctype=2&TSTVTimeLife=7200&contname=&authid=0&terminalflag=1&UserLiveType=0&stbid=651004910007034400000288CB89A9654&nodelevel=3&AuthInfo=BESeR9eMRDi8RsZJsJm5uQuYyDvjWwM%2FszT4e%2FVhQrlo5IKcht21qLksibFAyWsKtcJ7NgDL0pa6L%2FNky0Uo7A%3D%3D&bitrate=2000
     * links : [{"txtName":"福建电信 - IPTV","fileName":"ChinaNet-IPTV(XM)","fileType":"json"},{"txtName":"广西电信 - IPTV","fileName":"ChinaNet-IPTV(GX)","fileType":"json"},{"txtName":"国际电信 - IPTV","fileName":"ChinaNet-IPTV(GJ)","fileType":"json"},{"txtName":"直播中国 - Live","fileName":"ChinaNet-LiveCamera(XM)","fileType":"json"}]
     */

    private String name;
    private String date;
    private String txtURL;
    private List<LinksBean> links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTxtURL() {
        return txtURL;
    }

    public void setTxtURL(String txtURL) {
        this.txtURL = txtURL;
    }

    public List<LinksBean> getLinks() {
        return links;
    }

    public void setLinks(List<LinksBean> links) {
        this.links = links;
    }

    public static class LinksBean {
        /**
         * txtName : 福建电信 - IPTV
         * fileName : ChinaNet-IPTV(XM)
         * fileType : json
         */

        private String txtName;
        private String fileName;
        private String fileType;

        public String getTxtName() {
            return txtName;
        }

        public void setTxtName(String txtName) {
            this.txtName = txtName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }
    }
}
