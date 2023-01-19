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
@ApiModel(value = "添加临时一节课")
public class CourseDTO implements Serializable {
    @ApiModelProperty(value = "校区")
    private String schoolArea;

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

    @ApiModelProperty(value = "学生数组")
    private List<StudentVO> StudentList;
}
