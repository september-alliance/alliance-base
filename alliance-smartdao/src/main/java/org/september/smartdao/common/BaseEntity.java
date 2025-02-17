package org.september.smartdao.common;

import java.util.Date;

import org.september.smartdao.anno.OptimisticLock;
import org.springframework.format.annotation.DateTimeFormat;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.DefaultValue;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;

public abstract class BaseEntity {

    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "addtime", type = MySqlTypeConstant.DATETIME)
    protected Date addtime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time", type = MySqlTypeConstant.DATETIME)
    protected Date updateTime;

    /**
     *
     */
    @Column(name = "adduid", length = 20)
    protected Long adduid;

    /**
     *
     */
    @Column(name = "delete_flag", type = MySqlTypeConstant.SMALLINT, length = 6, isNull = false)
    @DefaultValue("0")
    protected Integer deleteFlag;

    /**
     *
     */
    @Column(name = "delete_uid", length = 20)
    protected Long deleteUid;

    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "delete_time", type = MySqlTypeConstant.DATETIME)
    protected Date deleteTime;
    
    
    @OptimisticLock
    @Column(name = "data_version", type = MySqlTypeConstant.INT, length = 6, isNull = false)
    @DefaultValue("0")
    protected Integer dataVersion;

    public abstract Long getId();

    public abstract void setId(Long l);

    public void setAddtime(Date date) {
        this.addtime = date;
    }

    public void setUpdateTime(Date date) {
        this.updateTime = date;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getAdduid() {
        return adduid;
    }

    public void setAdduid(Long adduid) {
        this.adduid = adduid;
    }

    public Long getDeleteUid() {
        return deleteUid;
    }

    public void setDeleteUid(Long deleteUid) {
        this.deleteUid = deleteUid;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Date getAddtime() {
        return addtime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

	public Integer getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(Integer dataVersion) {
		this.dataVersion = dataVersion;
	}

}
