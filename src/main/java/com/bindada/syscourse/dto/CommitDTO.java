package com.bindada.syscourse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "评价参数")
public class CommitDTO implements Serializable {

    @ApiModelProperty(value = "课程id")
    private String id;

    @ApiModelProperty(value = "学生Id")
    private String sid;

    @ApiModelProperty(value = "课程类型")
    private String courseType;

    @ApiModelProperty(value = "课程详情")
    private String courseDetail;

    @ApiModelProperty(value = "上课日期")
    private Date classDate;

    @ApiModelProperty(value = "上课时间")
    private String classTime;

    @ApiModelProperty(value = "老师ID")
    private String teacherAccount;

    @ApiModelProperty(value = "评价内容")
    private String commit;
}
