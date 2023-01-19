package com.bindada.syscourse.dto;

import com.bindada.syscourse.common.BasePageList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查询待评价")
public class QueryCommitDTO extends BasePageList {

    @ApiModelProperty("老师ID")
    private String teacherAccount;
}
