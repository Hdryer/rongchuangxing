package com.bindada.syscourse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bindada.syscourse.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "my_class")
public class MyClass extends BaseEntity {

    private String schoolArea;

    private String courseType;

    private String courseDetail;

    private String classTime;

    private int type;

    private Date beginDate;

    private Date endDate;

    private String teacherAccount;

    private String StudentJson;
}
