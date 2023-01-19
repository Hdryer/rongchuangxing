package com.bindada.syscourse.dto;

import com.bindada.syscourse.common.BasePageList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "课程查询参数")
public class CourseQueryDTO extends BasePageList {

    @ApiModelProperty(value = "老师ID")
    private String teacherAccount;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
