package com.bindada.syscourse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bindada.syscourse.dto.ClassTypeDTO;
import com.bindada.syscourse.entity.Subject;
import com.bindada.syscourse.mapper.SubjectMapper;
import com.bindada.syscourse.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl  extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public List<Subject> queryClassType(ClassTypeDTO dto) {

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(!dto.getAreaSchool().isEmpty(),Subject::getSchoolArea,dto.getAreaSchool())
                .eq(!dto.getCourse().isEmpty(),Subject::getCourse,dto.getCourse());
        return subjectMapper.selectList(queryWrapper);
    }
}
