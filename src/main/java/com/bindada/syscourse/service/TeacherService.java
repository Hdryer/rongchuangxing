package com.bindada.syscourse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bindada.syscourse.entity.Teacher;

import java.util.List;

public interface TeacherService extends IService<Teacher> {

    public Teacher login(String account,String password);

    public List<String> getTeacherAccountName();

    public void updatePwd(String id,String password);
}
