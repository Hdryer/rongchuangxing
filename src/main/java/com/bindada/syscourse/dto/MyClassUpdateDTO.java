package com.bindada.syscourse.dto;

import com.bindada.syscourse.vo.StudentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "修改班级数据参数")
public class MyClassUpdateDTO implements Serializable {
    @ApiModelProperty(value = "班级id")
    private String id;

    @ApiModelProperty(value = "课程类型")
    private String courseType;

    @ApiModelProperty(value = "课程详情")
    private String courseDetail;

    @ApiModelProperty(value = "开班日期")
    private Date beginDate;

    @ApiModelProperty(value = "结班日期")
    private Date endDate;

    @ApiModelProperty(value = "上课时间")
    private String classTime;
}
