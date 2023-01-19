package com.bindada.syscourse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bindada.syscourse.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "subject")
public class Subject  implements Serializable {

    @Id
    @TableId(type = IdType.ASSIGN_UUID)
    @Column(length = 64)
    private String id;

    private String schoolArea;

    private String course;

    private String detail;
}
