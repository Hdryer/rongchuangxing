package com.bindada.syscourse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.dto.StudentDTO;
import com.bindada.syscourse.dto.StudentQueryDTO;
import com.bindada.syscourse.entity.Student;

import java.util.List;

public interface StudentService extends IService<Student> {

    public void addStudent(StudentDTO studentDTO);

    public BasePageList<Student> queryAllStudent(StudentQueryDTO studentQueryDTO);

    public void updateStudent(Student student) throws Exception;

    public void delStudent(String id) throws Exception;

    public List<Student> getStudent(String schoolArea, String teacherAccount);

    public boolean bindAppID(String phone,String appID);
}
