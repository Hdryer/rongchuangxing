package com.bindada.syscourse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bindada.syscourse.entity.Teacher;
import com.bindada.syscourse.mapper.TeacherMapper;
import com.bindada.syscourse.service.TeacherService;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 登录接口
     * */
    @Override
    public Teacher login(String account, String password) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        Teacher teacher = teacherMapper.selectOne(queryWrapper.lambda().eq(Teacher::getAccount, account).eq(Teacher::getPassword, password));
        if (teacher!=null)
            return teacher;
        throw new RuntimeException("账号或者密码错误");
    }

    /**
     * 获取所有教师ID
     * */
    @Override
    public List<String> getTeacherAccountName(){
        return  teacherMapper.getTeacherAccountName();
    }

    /**
     * 修改账户密码
     * */
    @Override
    @Transactional
    public void updatePwd(String id, String password) {
        UpdateWrapper<Teacher> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(Teacher::getPassword,password);
        updateWrapper.lambda().eq(Teacher::getId,id);
        this.update(null,updateWrapper);
    }
}
