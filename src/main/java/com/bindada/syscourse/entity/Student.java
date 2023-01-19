package com.bindada.syscourse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bindada.syscourse.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName(value = "student")
@ApiModel(value = "学生信息")
public class Student extends BaseEntity {

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private int age;
    @ApiModelProperty(value = "学校")
    private String school;
    @ApiModelProperty(value = "校区")
    private String schoolArea;
    @ApiModelProperty(value = "家长微信ID")
    private String appId;
    @ApiModelProperty(value = "父母名字")
    private String parentName;
    @ApiModelProperty(value = "父母电话")
    private String parentPhone;
    @ApiModelProperty(value = "在学状态")
    private String learnState;  //在学状态
    @ApiModelProperty(value = "是否体验生")
    private int isExperiment;  //是否体验生
    @ApiModelProperty(value = "课程类型")
    private String courseType;
    @ApiModelProperty(value = "课程详情")
    private String courseDetail;
    @ApiModelProperty(value = "老师ID")
    private String teacherAccount;
    @ApiModelProperty(value = "上课时间")
    private String classTime;
    @ApiModelProperty(value = "缴费金额")
    private int amount;   //缴费金额
    @ApiModelProperty(value = "剩余次数")
    private int remainTimes;  //剩余次数
    @ApiModelProperty(value = "请假次数")
    private int leaveNum;
    @ApiModelProperty(value = "备注")
    private String description;  //备注
}
