package com.bindada.syscourse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bindada.syscourse.common.ApiResponse;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.dto.StudentDTO;
import com.bindada.syscourse.dto.StudentQueryDTO;
import com.bindada.syscourse.entity.Student;
import com.bindada.syscourse.service.StudentService;
import com.bindada.syscourse.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api( tags = "学生模块")
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/add")
    @ApiOperation(value = "添加学生")
    public ApiResponse addStudent(@RequestBody StudentDTO studentDTO){
        studentService.addStudent(studentDTO);
        return ApiResponse.success("添加学生成功！");
    }


    @PostMapping("/queryAll")
    @ApiOperation(value = "查询所有学生(共用)")
    public ApiResponse queryAllStudent(@RequestBody StudentQueryDTO studentQueryDTO){
        BasePageList<Student> pageList = studentService.queryAllStudent(studentQueryDTO);
        return ApiResponse.success("获取成功",pageList);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "学生详情(共用)")
    public ApiResponse queryDetail(@RequestParam String id){
        Student student = studentService.getOne(new QueryWrapper<Student>().lambda().eq(Student::getId, id));
        return ApiResponse.success("获取成功",student);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改学生信息")
    public ApiResponse updateStudent(@RequestBody Student student){
        try {
            studentService.updateStudent(student);
            return ApiResponse.success("修改成功");
        } catch (Exception e) {
            return ApiResponse.fail("修改失败，请重试");
        }
    }


    @DeleteMapping("/delete")
    @ApiOperation(value = "删除学生信息")
    public ApiResponse delStudent(@RequestParam String id){
        try {
            studentService.delStudent(id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            return ApiResponse.fail("删除失败,请重试");
        }
    }
}
