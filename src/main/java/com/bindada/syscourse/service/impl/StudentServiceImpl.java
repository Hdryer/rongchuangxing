package com.bindada.syscourse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.common.PageBuilder;
import com.bindada.syscourse.dto.StudentDTO;
import com.bindada.syscourse.dto.StudentQueryDTO;
import com.bindada.syscourse.entity.Student;
import com.bindada.syscourse.mapper.StudentMapper;
import com.bindada.syscourse.service.StudentService;
import com.bindada.syscourse.util.UserThreadLocal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 添加学生
     * */
    @Override
    @Transactional
    public void addStudent(StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO,student);
        student.setClassTime("[]");
        student.setLeaveNum(0);
        this.save(student);
    }

    /**
    * 查询所有学生
    * */
    @Override
    public BasePageList<Student> queryAllStudent(StudentQueryDTO dto) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        if(dto.getCurrent()==0)
            dto.setCurrent(1);
        if (dto.getSize()==0)
            dto.setSize(10);
        if(!StringUtils.isEmpty(dto.getName()))
            wrapper.lambda().like(Student::getName,dto.getName());
        if(!StringUtils.isEmpty(dto.getSchoolArea()))
            wrapper.lambda().eq(Student::getSchoolArea,dto.getSchoolArea());
        if(!StringUtils.isEmpty(dto.getState()))
            wrapper.lambda().eq(Student::getLearnState,dto.getState());
        if(UserThreadLocal.get().getType()==0)
            wrapper.lambda().eq(Student::getTeacherAccount,UserThreadLocal.get().getAccountUserName());
        if(dto.getIsExperiment()!=2)
            wrapper.lambda().eq(Student::getIsExperiment,dto.getIsExperiment());
        if(dto.getRemainTimes()==1)
            wrapper.lambda().le(Student::getRemainTimes,5);


        Page<Student> page = new Page<>(dto.getCurrent(),dto.getSize());
        Page<Student> result = studentMapper.selectPage(page, wrapper);
        return PageBuilder.copyAndConvert(page,result.getRecords());
    }


    /**
     * 修改学生信息
     * */
    @Override
    @Transactional
    public void updateStudent(Student student) throws Exception{
         studentMapper.update(student,new QueryWrapper<Student>().lambda().eq(Student::getId,student.getId()));
    }

    /**
     * 删除学生
     */
    @Override
    @Transactional
    public void delStudent(String id) throws Exception {
        studentMapper.delete(new QueryWrapper<Student>().lambda().eq(Student::getId,id));
    }

    /**
     * 查询符合条件的学生
     * */
    @Override
    public List<Student> getStudent(String schoolArea, String teacherAccount) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(!schoolArea.isEmpty() ,Student::getSchoolArea,schoolArea);
        queryWrapper.lambda().eq(!teacherAccount.isEmpty(),Student::getTeacherAccount,teacherAccount);
        return studentMapper.selectList(queryWrapper);
    }


    /**
     * appId绑定接口
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean bindAppID(String phone, String appID) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Student::getParentPhone,phone);
        List<Student> list = studentMapper.selectList(queryWrapper);
        if (list.isEmpty())
            return false;
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Student::getParentPhone,phone)
                .set(Student::getAppId,appID);
        try {
            studentMapper.update(null,updateWrapper);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
