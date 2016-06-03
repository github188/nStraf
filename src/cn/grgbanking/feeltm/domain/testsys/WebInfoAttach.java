package cn.grgbanking.feeltm.domain.testsys;


/**
 * 信息附件类
 * @author paladin
 * @version 1.0.0 
 */


public class WebInfoAttach  implements java.io.Serializable {


    // Fields
	/**
	 * 附件ID
	 */
     private String PInfoattachId;
     /**
      * 信息外键
      */
     private String FInfoinfoId;
     /**
      * 附件名称,一般是GUID名称+原来的扩展名
      */
     private String attachName;
     /**
      * 附件所在路径,相对路径不是绝对路径,路径位置是相对于ctxpath
      */
     private String attachPath;
     /**
      * 附件原来名称
      */
     private String attachOldname;
     /**
      * 附件的扩展名
      */
     private String attachExt;
     /**
      * 附件类型,0=未知类型附件, 1=图片,2=文本,3=影片,4=Flash,5=Word,6=Excel,7=PPT
      */
     private Short attchType=0;
     /**
      * 附件排序值,备用,从大到小排列
      */
     private Long attachIndex=0L;


    // Constructors

    /** default constructor */
    public WebInfoAttach() {
    }

	/** minimal constructor */
    public WebInfoAttach(String FInfoinfoId, String attachName, String attachPath) {
        this.FInfoinfoId = FInfoinfoId;
        this.attachName = attachName;
        this.attachPath = attachPath;
    }
    
    /** full constructor */
    public WebInfoAttach(String FInfoinfoId, String attachName, String attachPath, String attachOldname, String attachExt, Short attchType, Long attachIndex) {
        this.FInfoinfoId = FInfoinfoId;
        this.attachName = attachName;
        this.attachPath = attachPath;
        this.attachOldname = attachOldname;
        this.attachExt = attachExt;
        this.attchType = attchType;
        this.attachIndex = attachIndex;
    }

   
    // Property accessors

    public String getPInfoattachId() {
        return this.PInfoattachId;
    }
    
    public void setPInfoattachId(String PInfoattachId) {
        this.PInfoattachId = PInfoattachId;
    }

    public String getFInfoinfoId() {
        return this.FInfoinfoId;
    }
    
    public void setFInfoinfoId(String FInfoinfoId) {
        this.FInfoinfoId = FInfoinfoId;
    }

    public String getAttachName() {
        return this.attachName;
    }
    
    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachPath() {
        return this.attachPath;
    }
    
    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }

    public String getAttachOldname() {
        return this.attachOldname;
    }
    
    public void setAttachOldname(String attachOldname) {
        this.attachOldname = attachOldname;
    }

    public String getAttachExt() {
        return this.attachExt;
    }
    
    public void setAttachExt(String attachExt) {
        this.attachExt = attachExt;
    }

    public Short getAttchType() {
        return this.attchType;
    }
    
    public void setAttchType(Short attchType) {
        this.attchType = attchType;
    }

    public Long getAttachIndex() {
        return this.attachIndex;
    }
    
    public void setAttachIndex(Long attachIndex) {
        this.attachIndex = attachIndex;
    }
   








}