package com.bindada.syscourse.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "符合条件学生")
public class StudentVO {

    @ApiModelProperty(value = "学生ID")
    private String id;

    @ApiModelProperty(value = "学生名字")
    private String name;
}
