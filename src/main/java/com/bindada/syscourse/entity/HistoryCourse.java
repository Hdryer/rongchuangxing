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
@TableName(value = "history_course")
public class HistoryCourse extends BaseEntity {

    private String schoolArea;

    private String courseType;

    private Date classTime;

    private String teacherAccount;

    private String StudentJson;
}
