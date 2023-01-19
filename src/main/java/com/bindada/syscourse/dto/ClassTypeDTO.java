package com.bindada.syscourse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "获取班级类型")
public class ClassTypeDTO  implements Serializable {

    @ApiModelProperty(value = "校区")
    private String areaSchool;

    @ApiModelProperty(value = "科目")
    private String course;
}
