package com.bindada.syscourse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bindada.syscourse.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@Data
@TableName(value = "record")
@AllArgsConstructor
@NoArgsConstructor
public class Record extends BaseEntity {

    private Date classDate;

    private String classTime;

    private String courseType;

    private String schoolArea;

    private String teacherAccount;

    private String studentvo;

}
