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
@ApiModel(value = "开班参数")
public class MyClassDTO implements Serializable {

    @ApiModelProperty(value = "校区")
    private String schoolArea;

    @ApiModelProperty(value = "课程类型")
    private String courseType;

    @ApiModelProperty(value = "课程详细")
    private String courseDetail;

    @ApiModelProperty(value = "上课时间")
    private String classTime;

    @ApiModelProperty(value = "班级类型 0学期班,1寒暑班")
    private int type;

    @ApiModelProperty(value = "开班日期")
    private Date beginDate;

    @ApiModelProperty(value = "结班日期")
    private Date endDate;

    @ApiModelProperty(value = "老师ID")
    private String teacherAccount;

    @ApiModelProperty(value = "学生数组")
    private List<StudentVO> StudentList;
}
