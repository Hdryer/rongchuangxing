package com.bindada.syscourse.common;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bindada
 * @desc 所有实体类的基类
 *
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 7551485138236427661L;

    @Id
    @TableId(type = IdType.ASSIGN_UUID)
    @Column(name = "id",length = 64)
    protected String id;

    /**
     * 创建人id
     * */
    @Column(name = "creatorId",length = 64)
    protected String creatorId;

    /**
     * 修改人id
     * */
    @Column(name = "modifierId",length = 64)
    protected String modifierId;

    /**
     * 创建时间
     * */
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @TableField(fill = FieldFill.UPDATE)
    protected Date createTime;

    /**
     * 更新时间
     * */
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date modifyTime;

    /**
     * 是否删除： 1是，0否
     * */
    @Column
    protected int isDeleted;

    /**
     * 版本号
     * */
    @Version
    @TableField(fill = FieldFill.INSERT)
    protected int version;
}
