package com.bindada.syscourse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bindada.syscourse.dto.ClassTypeDTO;
import com.bindada.syscourse.entity.Subject;

import java.util.List;

public interface SubjectService extends IService<Subject> {

    public List<Subject> queryClassType(ClassTypeDTO dto);
}
