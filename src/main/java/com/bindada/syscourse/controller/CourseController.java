package com.bindada.syscourse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bindada.syscourse.common.ApiResponse;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.dto.*;
import com.bindada.syscourse.entity.Commit;
import com.bindada.syscourse.entity.Course;
import com.bindada.syscourse.entity.Student;
import com.bindada.syscourse.mapper.CommitMapper;
import com.bindada.syscourse.service.CourseService;
import com.bindada.syscourse.service.StudentService;
import com.bindada.syscourse.vo.StudentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.scene.chart.ValueAxis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "课程模块")
@RequestMapping("/course")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;


    @Autowired
    private CommitMapper commitMapper;


    @PostMapping("/query")
    @ApiOperation(value = "获取所有课程")
    public ApiResponse queryAllCourse(@RequestBody  CourseQueryDTO courseQueryDTO){
        BasePageList<Course> pageList = courseService.queryCourse(courseQueryDTO);
        return ApiResponse.success("获取成功",pageList);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加临时课程")
    public ApiResponse addCourse(@RequestBody CourseDTO courseDTO){
        try {
            courseService.addCourse(courseDTO);
            return ApiResponse.success("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(e.getMessage());
        }
    }

    @DeleteMapping("/del")
    @ApiOperation(value = "删除临时课程")
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse deleteCourse(String id){
        try {
            courseService.remove(new QueryWrapper<Course>().lambda().eq(Course::getId,id));
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail("删除失败,请重试");
        }
    }

    @PutMapping("/updateStu")
    @ApiOperation(value = "修改学生数组")
    public ApiResponse updateStu(@RequestBody  CourseUpdateStuDTO courseUpdateStuDTO){
        try {
            courseService.updateStu(courseUpdateStuDTO);
            return ApiResponse.success("修改成功");
        } catch (Exception e) {
            return ApiResponse.fail("修改失败,请重试");
        }
    }



    /***
     * 消课与评价操作
     *
     * */
    @PostMapping("/queryRemove")
    @ApiOperation(value = "查询待消课课表")
    public ApiResponse queryRemove(@RequestBody QueryRemoveDTO queryRemoveDTO){
        BasePageList pageList = courseService.queryRemove(queryRemoveDTO);
        return ApiResponse.success("查询成功",pageList);
    }

    @PostMapping("/queryCommit")
    @ApiOperation(value = "查询待评价课表")
    public ApiResponse queryCommit(@RequestBody  QueryCommitDTO queryCommitDTO){
        BasePageList pageList = courseService.queryCommit(queryCommitDTO);
        return ApiResponse.success("查询成功",pageList);
    }

    @ApiOperation(value = "消课")
    @PutMapping("/remove")
    public ApiResponse removeCourse(String id,String sid){
        try {
            String s = courseService.removeCourse(id, sid);
            return ApiResponse.success(s);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "请假")
    @PutMapping("/excuse")
    public ApiResponse excuseCourse(String id,String sid){
        try {
            String s = courseService.excuseCourse(id, sid);
            return ApiResponse.success(s);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "评价")
    @PostMapping("/commit")
    public ApiResponse commitCourse(@RequestBody  CommitDTO commitDTO){
        try {
            String str = courseService.commitCourse(commitDTO);
            return ApiResponse.success(str);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping("/getCommit/{id}")
    @ApiOperation(value = "查看评价(家长)")
    public String getCommit(@PathVariable String id){
        return commitMapper.getCommit(id);
    }

    @GetMapping("/getCommit2")
    @ApiOperation(value = "查看评价(老师)")
    public ApiResponse getCommit2(String cid, String sid){
        QueryWrapper<Commit> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Commit::getCid,cid)
                .eq(Commit::getSid,sid);
        Commit commit = commitMapper.selectOne(queryWrapper);
        if(commit!=null)
            return ApiResponse.success("获取成功",commit);
        else
            return ApiResponse.fail("该学生暂未评价");
    }
}
