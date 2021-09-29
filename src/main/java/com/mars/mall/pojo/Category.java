package com.mars.mall.pojo;

import java.util.Date;

/**
 * 类别类 —— 类别表
 */
public class Category {
    private Integer id;//类别Id

    private Integer parentId;//父类别id当id=0时说明是根节点,一级类别

    private String name;//类别名称

    private Boolean status;//类别状态1-正常,2-已废弃

    private Integer sortOrder;//排序编号,同类展示顺序,数值相等则自然排序

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}