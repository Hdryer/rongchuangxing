package com.bindada.syscourse.dto;

import com.bindada.syscourse.common.BasePageList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "班级查询参数")
public class MyClassQueryDTO extends BasePageList {

    @ApiModelProperty(value = "校区")
    private String schoolArea;

    @ApiModelProperty(value = "老师ID")
    private String teacherAccount;

    @ApiModelProperty(value = "星期几")
    private String day;
}
