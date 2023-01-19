package com.bindada.syscourse.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.common.PageBuilder;
import com.bindada.syscourse.dto.MyClassDTO;
import com.bindada.syscourse.dto.MyClassQueryDTO;
import com.bindada.syscourse.dto.MyClassUpdateDTO;
import com.bindada.syscourse.dto.MyClassUpdateStuDTO;
import com.bindada.syscourse.entity.MyClass;
import com.bindada.syscourse.entity.Student;
import com.bindada.syscourse.mapper.MyClassMapper;
import com.bindada.syscourse.mapper.StudentMapper;
import com.bindada.syscourse.service.MyClassService;
import com.bindada.syscourse.util.UserThreadLocal;
import com.bindada.syscourse.vo.StudentJson;
import com.bindada.syscourse.vo.StudentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyClassServiceImpl extends ServiceImpl<MyClassMapper, MyClass> implements MyClassService {

    @Autowired
    private MyClassMapper myClassMapper;

    @Autowired
    private StudentMapper studentMapper;


    /**
     * 新开设班级
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addClass(MyClassDTO myClassDTO) throws Exception {
        MyClass myClass = new MyClass();
        BeanUtils.copyProperties(myClassDTO,myClass);
        /**防止老师时间冲突*/
        QueryWrapper<MyClass> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MyClass::getTeacherAccount,myClass.getTeacherAccount())
                .eq(MyClass::getClassTime,myClass.getClassTime());
        if (myClassMapper.selectOne(wrapper)!=null){
            throw new Exception("该老师该时间段已有课程，请重新安排");
        }

        List<StudentVO> studentList = myClassDTO.getStudentList();
        List<StudentJson> jsons = new ArrayList<>();
        for (StudentVO studentVO : studentList) {
            StudentJson studentJson = new StudentJson();
            BeanUtils.copyProperties(studentVO, studentJson);
            studentJson.setIsCommit(0);
            studentJson.setIsRemove(0);
            jsons.add(studentJson);
        }
        String str = JSON.toJSONString(jsons);
        myClass.setStudentJson(str);
        //班级时间
        String classTime="";
        if (myClass.getType()==1){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String beginDate = simpleDateFormat.format(myClass.getBeginDate());
            String endDate = simpleDateFormat.format(myClass.getEndDate());
            classTime = beginDate +"至"+ endDate +" "+ myClass.getClassTime() ;
            myClass.setClassTime(classTime);
        }
        myClassMapper.insert(myClass);

        /**更新学生上课时间*/
        for (StudentVO studentVO : studentList) {
            Student student = studentMapper.selectOne(new QueryWrapper<Student>().lambda().eq(Student::getId, studentVO.getId()));
            if (myClass.getType()==0){
                this.addElse(student.getClassTime(),myClass.getClassTime(),studentVO);
            } else if (myClass.getType()==1){
                this.addElse(student.getClassTime(),classTime,studentVO);
            }
        }
    }


    /**
     * 分支方法 解决循环事务问题
     * 防止学生时间冲突
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addElse(String times, String time, StudentVO studentVO) throws Exception {
        List<String> list = JSON.parseObject(times, List.class);
        if (!list.contains(time)) {
            list.add(time);
            UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(Student::getId, studentVO.getId())
                    .set(Student::getClassTime, JSON.toJSONString(list));
            studentMapper.update(null, updateWrapper);
        } else {
            throw new Exception(studentVO.getName() + "学生该时间段已有课程安排，请确认再重新安排");
        }
    }

    /**
     * 查询本老师的班级
     * */
    @Override
    public List<MyClass> queryClassByTea() {
        QueryWrapper<MyClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MyClass::getTeacherAccount, UserThreadLocal.get().getAccountUserName());
        return myClassMapper.selectList(queryWrapper);
    }

    /**
     * 查询筛选所有班级
     * */
    @Override
    public BasePageList queryClass(MyClassQueryDTO dto) {
        QueryWrapper<MyClass> queryWrapper = new QueryWrapper<>();
        if (dto.getCurrent()==0)
            dto.setCurrent(1);
        if(dto.getSize()==0)
            dto.setSize(10);
        queryWrapper.lambda().eq(!dto.getSchoolArea().isEmpty(),MyClass::getSchoolArea,dto.getSchoolArea());
        queryWrapper.lambda().eq(!dto.getTeacherAccount().isEmpty(),MyClass::getTeacherAccount,dto.getTeacherAccount());
        queryWrapper.lambda().like(!dto.getDay().isEmpty(),MyClass::getClassTime,dto.getDay());

        Page<MyClass> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<MyClass> result = myClassMapper.selectPage(page, queryWrapper);
        return PageBuilder.copyAndConvert(page,result.getRecords());
    }

    /**
     * 修改班级属性(时间和类型)
     * 课表时间待修改
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClass(MyClassUpdateDTO dto) throws Exception {
        QueryWrapper<MyClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MyClass::getId,dto.getId());
        MyClass myClass = myClassMapper.selectOne(queryWrapper);
        UpdateWrapper<MyClass> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(MyClass::getId,dto.getId());
        updateWrapper.lambda().set(!dto.getCourseType().isEmpty(),MyClass::getCourseType,dto.getCourseType());
        updateWrapper.lambda().set(!dto.getCourseDetail().isEmpty(),MyClass::getCourseDetail,dto.getCourseDetail());
        updateWrapper.lambda().set(!StringUtils.isEmpty(dto.getBeginDate()),MyClass::getBeginDate,dto.getBeginDate());
        updateWrapper.lambda().set(!StringUtils.isEmpty(dto.getEndDate()),MyClass::getEndDate,dto.getEndDate());

        String time = myClass.getClassTime();
        if (!dto.getClassTime().equals(time)){
            updateWrapper.lambda().set(!dto.getClassTime().isEmpty(),MyClass::getClassTime,dto.getClassTime());
            String studentJson = myClass.getStudentJson();
            List<StudentJson> studentJsons = JSON.parseArray(studentJson, StudentJson.class);
            for (StudentJson json : studentJsons) {
                Student student = studentMapper.selectOne(new QueryWrapper<Student>().lambda().eq(Student::getId, json.getId()));
                List list = JSON.parseObject(student.getClassTime(), List.class);
                list.remove(time);
                if (!list.contains(dto.getClassTime())){
                    list.add(dto.getClassTime());
                }else {
                    throw new Exception(student.getName()+"学生该时间段已有课程安排，请确认再重新安排");
                }
                UpdateWrapper<Student> updateWrapper1 = new UpdateWrapper<>();
                updateWrapper1.lambda().eq(Student::getId,json.getId())
                        .set(Student::getClassTime,JSON.toJSONString(list));
                studentMapper.update(null,updateWrapper1);
            }
        }
        myClassMapper.update(null,updateWrapper);

        /**修改已生成课表  */

    }

    /**
     * 修改班级学生人员
     *
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStuClass(MyClassUpdateStuDTO dto) throws Exception {
        QueryWrapper<MyClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MyClass::getId,dto.getId());
        MyClass myClass = myClassMapper.selectOne(queryWrapper);
        String time = myClass.getClassTime();
        List<String> list = dto.getStudentList();
        List<String> stuIdList = new ArrayList<>();
        List<StudentJson> studentJsons = JSON.parseArray(myClass.getStudentJson(), StudentJson.class);
        /**修改学生们的上课时间字段*/
        for (StudentJson studentJson : studentJsons) {
            stuIdList.add(studentJson.getId());
            if (!list.contains(studentJson.getId())) {
                Student student = studentMapper.selectOne(new QueryWrapper<Student>().lambda().eq(Student::getId, studentJson.getId()));
                List newList = JSON.parseObject(student.getClassTime(), List.class);
                newList.remove(time);
                UpdateWrapper<Student> updateWrapper = new UpdateWrapper<Student>();
                updateWrapper.lambda().eq(Student::getId, studentJson.getId())
                        .set(Student::getClassTime, JSON.toJSONString(newList));
                studentMapper.update(null,updateWrapper);
            }
        }
        List<StudentJson> studentJsons1 = new ArrayList<>();
        for (String stuId : list) {
            Student student = studentMapper.selectOne(new QueryWrapper<Student>().lambda().eq(Student::getId, stuId));
            StudentJson studentJson = new StudentJson();
            studentJson.setId(student.getId());
            studentJson.setName(student.getName());
            studentJson.setIsRemove(0);
            studentJson.setIsCommit(0);
            studentJsons1.add(studentJson);
            if (!stuIdList.contains(stuId)){
                UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
                List newList = JSON.parseObject(student.getClassTime(), List.class);
                if(!newList.contains(time)){
                    newList.add(time);
                }else {
                    throw new Exception(student.getName()+"学生该时间段已有课程安排，请确认再重新安排");
                }
                updateWrapper.lambda().eq(Student::getId,stuId)
                        .set(Student::getClassTime,JSON.toJSONString(newList));
                studentMapper.update(null,updateWrapper);
            }
        }
        UpdateWrapper<MyClass> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(MyClass::getId,dto.getId())
                .set(MyClass::getStudentJson,JSON.toJSONString(studentJsons1));
        myClassMapper.update(null,updateWrapper);

        /**待完成 修改已生成课表*/
    }

    /**
     * 删除班级
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delClass(String id) throws Exception{
        QueryWrapper<MyClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MyClass::getId,id);
        MyClass myClass = myClassMapper.selectOne(queryWrapper);

        /**删除所在班级学生的时间记录*/
        String studentJson = myClass.getStudentJson();
        List<StudentJson> list = JSON.parseArray(studentJson, StudentJson.class);
        for (StudentJson json : list) {
            UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(Student::getId,json.getId());
            Student student = studentMapper.selectOne(new QueryWrapper<Student>().lambda().eq(Student::getId, json.getId()));
            String times = student.getClassTime();
            List<String> list1 = JSON.parseObject(times, List.class);
            list1.remove(myClass.getClassTime());
            String classTime = JSON.toJSONString(list1);
            updateWrapper.lambda().set(Student::getClassTime,classTime);
            studentMapper.update(null,updateWrapper);
        }
        //删除该班级
        myClassMapper.delete(queryWrapper);
    }

}
