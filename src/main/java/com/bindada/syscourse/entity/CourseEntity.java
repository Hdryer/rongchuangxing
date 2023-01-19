package com.bindada.syscourse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "s_course")
public class CourseEntity {

    private int id;

    private String name;

    private int hours;

    private int sid;

    private String url;
}
