package com.bindada.syscourse.dto;

import com.bindada.syscourse.vo.StudentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "修改班级学生参数")
public class MyClassUpdateStuDTO implements Serializable {

    @ApiModelProperty(value = "课程id")
    private String id;

    @ApiModelProperty(value = "学生id数组")
    private List<String> StudentList;
}
