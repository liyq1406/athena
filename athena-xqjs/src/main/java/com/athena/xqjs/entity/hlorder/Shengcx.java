package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class Shengcx {
    private String chanxzbh;//产线组编号

    private BigDecimal tessjxq1;//特殊时间1星期

    private BigDecimal tessjxs1;//特殊时间1小时

    private BigDecimal tessjxq2;//特殊时间2星期

    private BigDecimal tessjxs2;//特殊时间2小时

    private BigDecimal tessjxq3;//特殊时间3星期

    private BigDecimal tessjxs3;//特殊时间3小时

    private BigDecimal shengcjp;//整车生产节拍

    private BigDecimal weilscjp;//整车未来生产节拍

    private Date qiehsj;//整车生产节拍切换时间

    private BigDecimal chults;//车位数

    private String biaos;//标识 1有效 0失效

    private String creator;//创建人

    private Date createTime;//创建时间

    private String editor;//修改时间

    private Date editTime;//修改人

    private BigDecimal cpshengcjp;//成品生产节拍

    private String flag;//1:车间,2：成品

    private BigDecimal anqkctsmrz;//安全天数默认值
    
    private String shengcxbh;//生产线编号

    private String usercenter;//用户中心

    public String getShengcxbh() {
        return shengcxbh;
    }

    public void setShengcxbh(String shengcxbh) {
        this.shengcxbh = shengcxbh == null ? null : shengcxbh.trim();
    }

    public String getUsercenter() {
        return usercenter;
    }

    public void setUsercenter(String usercenter) {
        this.usercenter = usercenter == null ? null : usercenter.trim();
    }

    public String getChanxzbh() {
        return chanxzbh;
    }

    public void setChanxzbh(String chanxzbh) {
        this.chanxzbh = chanxzbh == null ? null : chanxzbh.trim();
    }

    public BigDecimal getTessjxq1() {
        return tessjxq1;
    }

    public void setTessjxq1(BigDecimal tessjxq1) {
        this.tessjxq1 = tessjxq1;
    }

    public BigDecimal getTessjxs1() {
        return tessjxs1;
    }

    public void setTessjxs1(BigDecimal tessjxs1) {
        this.tessjxs1 = tessjxs1;
    }

    public BigDecimal getTessjxq2() {
        return tessjxq2;
    }

    public void setTessjxq2(BigDecimal tessjxq2) {
        this.tessjxq2 = tessjxq2;
    }

    public BigDecimal getTessjxs2() {
        return tessjxs2;
    }

    public void setTessjxs2(BigDecimal tessjxs2) {
        this.tessjxs2 = tessjxs2;
    }

    public BigDecimal getTessjxq3() {
        return tessjxq3;
    }

    public void setTessjxq3(BigDecimal tessjxq3) {
        this.tessjxq3 = tessjxq3;
    }

    public BigDecimal getTessjxs3() {
        return tessjxs3;
    }

    public void setTessjxs3(BigDecimal tessjxs3) {
        this.tessjxs3 = tessjxs3;
    }

    public BigDecimal getShengcjp() {
        return shengcjp;
    }

    public void setShengcjp(BigDecimal shengcjp) {
        this.shengcjp = shengcjp;
    }

    public BigDecimal getWeilscjp() {
        return weilscjp;
    }

    public void setWeilscjp(BigDecimal weilscjp) {
        this.weilscjp = weilscjp;
    }

    public Date getQiehsj() {
        return qiehsj;
    }

    public void setQiehsj(Date qiehsj) {
        this.qiehsj = qiehsj;
    }

    public BigDecimal getChults() {
        return chults;
    }

    public void setChults(BigDecimal chults) {
        this.chults = chults;
    }

    public String getBiaos() {
        return biaos;
    }

    public void setBiaos(String biaos) {
        this.biaos = biaos == null ? null : biaos.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor == null ? null : editor.trim();
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public BigDecimal getCpshengcjp() {
        return cpshengcjp;
    }

    public void setCpshengcjp(BigDecimal cpshengcjp) {
        this.cpshengcjp = cpshengcjp;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public BigDecimal getAnqkctsmrz() {
        return anqkctsmrz;
    }

    public void setAnqkctsmrz(BigDecimal anqkctsmrz) {
        this.anqkctsmrz = anqkctsmrz;
    }
}