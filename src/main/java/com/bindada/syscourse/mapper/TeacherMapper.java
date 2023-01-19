package com.bindada.syscourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bindada.syscourse.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

    public List<String> getTeacherAccountName();
}
