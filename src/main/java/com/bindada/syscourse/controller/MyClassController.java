package com.bindada.syscourse.controller;

import com.bindada.syscourse.common.ApiResponse;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.dto.*;
import com.bindada.syscourse.entity.MyClass;
import com.bindada.syscourse.entity.Student;
import com.bindada.syscourse.mapper.CommitMapper;
import com.bindada.syscourse.service.MyClassService;
import com.bindada.syscourse.service.StudentService;
import com.bindada.syscourse.service.SubjectService;
import com.bindada.syscourse.vo.StudentVO;
import com.sun.org.apache.bcel.internal.util.ClassQueue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "班级管理")
@RequestMapping("/class")
@Slf4j
public class MyClassController {

    @Autowired
    private MyClassService myClassService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;


    @GetMapping("/queryStudent")
    @ApiOperation(value = "查询符合条件的学生")
    public ApiResponse queryStudent(@RequestParam String schoolArea, @RequestParam String teacherAccount){
        List<Student> studentList = studentService.getStudent(schoolArea, teacherAccount);
        List<StudentVO> studentVOS = new ArrayList<>();
        for (Student student : studentList) {
            StudentVO studentVO = new StudentVO();
            BeanUtils.copyProperties(student,studentVO);
            studentVOS.add(studentVO);
        }
        return ApiResponse.success("获取成功",studentVOS);
    }

    @ApiOperation(value = "获取班级类型")
    @PostMapping("/queryClassType")
    public ApiResponse queryClassType(@RequestBody ClassTypeDTO classTypeDTO){
        return ApiResponse.success("获取成功",subjectService.queryClassType(classTypeDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "开设班级")
    public ApiResponse addClass(@RequestBody MyClassDTO myClassDTO){
        try {
            myClassService.addClass(myClassDTO);
            return ApiResponse.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(e.getMessage());
        }
    }

    @PostMapping("/query")
    @ApiOperation(value = "查询班级(教务)")
    public ApiResponse queryClass(@RequestBody MyClassQueryDTO myClassQueryDTO){
        BasePageList pageList = myClassService.queryClass(myClassQueryDTO);
        return ApiResponse.success("查询成功",pageList);
    }

    @PostMapping("/queryByTea")
    @ApiOperation(value = "查询班级(授课)")
    public ApiResponse queryClassByTea(){
        List<MyClass> myClasses = myClassService.queryClassByTea();
        return ApiResponse.success("查询成功",myClasses);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改班级(时间+课类)")
    public ApiResponse updateClass(@RequestBody MyClassUpdateDTO myClassUpdateDTO){
        try {
            myClassService.updateClass(myClassUpdateDTO);
            return ApiResponse.success("修改成功");
        } catch (Exception e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @PutMapping("/updateStu")
    @ApiOperation(value = "修改班级(上课学生)")
    public ApiResponse updateStuClass(@RequestBody MyClassUpdateStuDTO myClassUpdateStuDTO){
        try {
            myClassService.updateStuClass(myClassUpdateStuDTO);
            return ApiResponse.success("修改成功");
        } catch (Exception e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @DeleteMapping("/del")
    @ApiOperation(value = "删除班级")
    public ApiResponse delClass(String id){
        try {
            myClassService.delClass(id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            return ApiResponse.fail(e);
        }
    }

}
