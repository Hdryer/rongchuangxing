package com.bindada.syscourse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.common.PageBuilder;
import com.bindada.syscourse.dto.*;
import com.bindada.syscourse.entity.Course;
import com.bindada.syscourse.vo.StudentVO;
import io.swagger.annotations.ApiOperation;

public interface CourseService extends IService<Course> {

    public BasePageList<Course> queryCourse(CourseQueryDTO courseQueryDTO);

    public void addCourse(CourseDTO courseDTO) throws Exception;

    public void updateStu(CourseUpdateStuDTO courseUpdateStuDTO);

    public BasePageList queryRemove(QueryRemoveDTO dto);

    public BasePageList queryCommit(QueryCommitDTO dto);

    public String removeCourse(String id, String sid) throws Exception;

    public String excuseCourse(String id,String sid) throws Exception;

    public String commitCourse(CommitDTO commitDTO) throws Exception;
}
