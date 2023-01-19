package com.bindada.syscourse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bindada.syscourse.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "course")
public class Course extends BaseEntity {

    private String schoolArea;

    private String courseType;

    private String courseDetail;

    private Date classDate;

    private String classTime;

    private String teacherAccount;

    private String StudentJson;

    private int flag;
}
