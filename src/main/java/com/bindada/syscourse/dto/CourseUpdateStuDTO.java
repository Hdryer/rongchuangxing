package com.bindada.syscourse.dto;

import com.bindada.syscourse.vo.StudentJson;
import com.bindada.syscourse.vo.StudentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "修改学生数组")
public class CourseUpdateStuDTO implements Serializable {

    @ApiModelProperty("课程id")
    private String id;

    @ApiModelProperty(value = "学生数组")
    private List<StudentJson> StudentList;

}

