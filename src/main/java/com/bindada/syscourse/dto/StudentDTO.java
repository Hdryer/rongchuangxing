package com.bindada.syscourse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "新增学生参数")
public class StudentDTO implements Serializable {

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private int age;
    @ApiModelProperty(value = "学校")
    private String school;
    @ApiModelProperty(value = "校区")
    private String schoolArea;
    @ApiModelProperty(value = "家长姓名")
    private String parentName;
    @ApiModelProperty(value = "家长电话")
    private String parentPhone;
    @ApiModelProperty(value = "在学状态,下拉框")
    private String learnState;
    @ApiModelProperty(value = "是否体验生")
    private int isExperiment;
    @ApiModelProperty(value = "课程类型")
    private String courseType;
    @ApiModelProperty(value = "课程详情")
    private String courseDetail;
    @ApiModelProperty(value = "老师ID")
    private String teacherAccount;
    @ApiModelProperty(value = "上课时间")
    private String classTime;
    @ApiModelProperty(value = "缴费金额")
    private int amount;
    @ApiModelProperty(value = "剩余次数")
    private int remainTimes;
    @ApiModelProperty(value = "额外备注")
    private String description;
}
