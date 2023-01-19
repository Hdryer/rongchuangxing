package com.bindada.syscourse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bindada.syscourse.common.ApiResponse;
import com.bindada.syscourse.entity.CourseEntity;
import com.bindada.syscourse.mapper.CourseMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "测试模块")
@RequestMapping("/test")
public class TestController {

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/test1")
    @ApiOperation("测试1")
    public String test1(){
        return "test";
    }

    @GetMapping("/getList")
    @ApiOperation("查询所有的课程")
    public ApiResponse listCourse(){
        List<CourseEntity> entities = courseMapper.selectList(null);
        return ApiResponse.success("查询成功",entities);
    }
}
