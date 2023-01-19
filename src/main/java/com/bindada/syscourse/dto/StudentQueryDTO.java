package com.bindada.syscourse.dto;

import com.bindada.syscourse.common.BasePageList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "学生查询参数")
public class StudentQueryDTO extends BasePageList {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "所属校区")
    private String schoolArea;

    @ApiModelProperty(value = "在学状态")
    private String state;

    @ApiModelProperty(value = "是否为体验生 0否 1是")
    private int isExperiment;

    @ApiModelProperty(value = "剩余次数")
    private int remainTimes;
}
