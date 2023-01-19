package com.bindada.syscourse.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.common.PageBuilder;
import com.bindada.syscourse.common.weixin.Interaction;
import com.bindada.syscourse.common.weixin.TemplateMessage.FinishClassMessage;
import com.bindada.syscourse.dto.*;
import com.bindada.syscourse.entity.Commit;
import com.bindada.syscourse.entity.Course;
import com.bindada.syscourse.entity.Record;
import com.bindada.syscourse.entity.Student;
import com.bindada.syscourse.mapper.CommitMapper;
import com.bindada.syscourse.mapper.CourseMapper;
import com.bindada.syscourse.mapper.RecordMapper;
import com.bindada.syscourse.mapper.StudentMapper;
import com.bindada.syscourse.service.CourseService;
import com.bindada.syscourse.util.DayMapUtil;
import com.bindada.syscourse.util.UUidUtil;
import com.bindada.syscourse.util.UserThreadLocal;
import com.bindada.syscourse.vo.StudentJson;
import com.bindada.syscourse.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private CommitMapper commitMapper;

    /**
     * 查询所有课表
     *
     * @return*/
    @Override
    public BasePageList<Course> queryCourse(CourseQueryDTO dto) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (dto.getCurrent()==0)
            dto.setCurrent(1);
        if (dto.getSize()==0)
            dto.setSize(10);
        //鉴定用户权限
        if(UserThreadLocal.get().getType()!=0)
            queryWrapper.lambda().eq(!dto.getTeacherAccount().isEmpty(),Course::getTeacherAccount,dto.getTeacherAccount());
        else
            queryWrapper.lambda().eq(Course::getTeacherAccount,UserThreadLocal.get().getAccountUserName());

        //判断起止时间
        if(!StringUtils.isEmpty(dto.getBeginTime()) && !StringUtils.isEmpty(dto.getEndTime())){
            queryWrapper.lambda().between(Course::getClassDate,dto.getBeginTime(),dto.getEndTime());
        } else {
            queryWrapper.lambda().between(Course::getClassDate,DayMapUtil.getStartDay(),DayMapUtil.getEndDay());
        }
        Page<Course> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<Course> result = courseMapper.selectPage(page, queryWrapper);
        return PageBuilder.copyAndConvert(page,result.getRecords());
    }

    /**
     *
     * 添加一节临时课程
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCourse(CourseDTO courseDTO) throws Exception {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Course::getTeacherAccount,courseDTO.getTeacherAccount());
        queryWrapper.lambda().eq(Course::getClassDate,courseDTO.getClassDate());
        queryWrapper.lambda().eq(Course::getClassTime,courseDTO.getClassTime());
        if (courseMapper.selectOne(queryWrapper)!=null)
            throw new Exception("该老师当前时间段已有课程安排，请确认后再安排");

        Course course = new Course();
        BeanUtils.copyProperties(courseDTO,course);
        List<StudentVO> list = courseDTO.getStudentList();
        ArrayList<StudentJson> jsons = new ArrayList<>();
        for (StudentVO studentVO : list) {
            StudentJson studentJson = new StudentJson();
            BeanUtils.copyProperties(studentVO,studentJson);
            studentJson.setIsCommit(0);
            studentJson.setIsRemove(0);
            jsons.add(studentJson);
        }
        course.setStudentJson(JSON.toJSONString(jsons));
        courseMapper.insert(course);
    }


    /**
     *
     * 修改学生数组
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStu(CourseUpdateStuDTO dto) {
        UpdateWrapper<Course> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Course::getId,dto.getId())
                .set(Course::getStudentJson,JSON.toJSONString(dto.getStudentList()));

        /**删除学生后重新检测是否全部消课完*/
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Course::getId,dto.getId());
        Course course = courseMapper.selectOne(queryWrapper);
        if (course.getFlag()==0){
            List<StudentJson> studentList = dto.getStudentList();
            int flag=1;
            for (StudentJson studentJson : studentList) {
                if (studentJson.getIsRemove()==0)
                    flag=0;
            }
            updateWrapper.lambda().set(Course::getFlag,flag);
        }

        courseMapper.update(null,updateWrapper);
    }



    /**
    * 查询待消课程
    * */
    @Override
    public BasePageList queryRemove(QueryRemoveDTO dto) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (dto.getCurrent()==0)
            dto.setCurrent(1);
        if (dto.getSize()==0)
            dto.setSize(10);
        queryWrapper.lambda().eq(Course::getFlag,0);
        if (UserThreadLocal.get().getType()==0)
            queryWrapper.lambda().eq(Course::getTeacherAccount,UserThreadLocal.get().getAccountUserName());
        else
            queryWrapper.lambda().eq(!dto.getTeacherAccount().isEmpty(),Course::getTeacherAccount,dto.getTeacherAccount());
        Page<Course> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<Course> result = courseMapper.selectPage(page, queryWrapper);
        return PageBuilder.copyAndConvert(page,result.getRecords());
    }

    /**
     * 查询待评价课程
     * */
    @Override
    public BasePageList queryCommit(QueryCommitDTO dto) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (dto.getCurrent()==0)
            dto.setCurrent(1);
        if (dto.getSize()==0)
            dto.setSize(10);
        queryWrapper.lambda().eq(Course::getFlag,1);
        if (UserThreadLocal.get().getType()==0)
            queryWrapper.lambda().eq(Course::getTeacherAccount,UserThreadLocal.get().getAccountUserName());
        else
            queryWrapper.lambda().eq(!dto.getTeacherAccount().isEmpty(),Course::getTeacherAccount,dto.getTeacherAccount());
        Page<Course> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<Course> result = courseMapper.selectPage(page, queryWrapper);
        return PageBuilder.copyAndConvert(page,result.getRecords());
    }

    /**
     * 消课
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized String removeCourse(String id, String sid) throws Exception {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course course = courseMapper.selectOne(queryWrapper.lambda().eq(Course::getId, id));
        List<StudentJson> studentJsons = JSON.parseArray(course.getStudentJson(), StudentJson.class);
        for (StudentJson studentJson : studentJsons) {
            if (studentJson.getId().equals(sid)){
                if (studentJson.getIsRemove()==1){
                    throw new Exception(studentJson.getName()+" 学生已消课，请勿重复消课");
                }else if(studentJson.getIsRemove()==2){
                    throw new Exception(studentJson.getName()+" 学生已请假，请勿消课");
                }else {
                    studentJson.setIsRemove(1);
                    //学生剩余次数减一
                    Student student = studentMapper.selectOne(new QueryWrapper<Student>().lambda().eq(Student::getId, sid));
                    if (student.getRemainTimes()<=0){
                        throw new Exception(student.getName()+" 学生剩余上课次数为0,请联系教务老师删除该学生");
                    } else {
                        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.lambda().eq(Student::getId,sid)
                                .set(Student::getRemainTimes,student.getRemainTimes()-1);
                        studentMapper.update(null,updateWrapper);

                        //增加消课记录
                        Record record = new Record();
                        BeanUtils.copyProperties(course,record,"createTime","id");
                        StudentVO studentVO = new StudentVO();
                        studentVO.setId(student.getId());
                        studentVO.setName(student.getName());
                        record.setStudentvo(JSON.toJSONString(studentVO));
                        record.setCreatorId(UserThreadLocal.get().getId());
                        recordMapper.insert(record);
                    }
                    break;
                }
            }
        }
        //修改本课程学生数组
        UpdateWrapper<Course> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Course::getStudentJson,JSON.toJSONString(studentJsons))
                .eq(Course::getId,id);
        courseMapper.update(null,wrapper);

        int flag = 1;
        for (StudentJson studentJson : studentJsons)
            if (studentJson.getIsRemove()==0)
                flag=0;
        if (flag==1){
            UpdateWrapper<Course> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(Course::getFlag,1)
                    .eq(Course::getId,id);
            courseMapper.update(null,updateWrapper);
            return "消课成功,本节课学生已全部处理完成";
        }else {
            return "消课成功";
        }
    }


    /**
     * 请假
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String excuseCourse(String id, String sid) throws Exception {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course course = courseMapper.selectOne(queryWrapper.lambda().eq(Course::getId, id));
        List<StudentJson> studentJsons = JSON.parseArray(course.getStudentJson(), StudentJson.class);
        for (StudentJson studentJson : studentJsons) {
            if (studentJson.getId().equals(sid)){
                if (studentJson.getIsRemove()==1){
                    throw new Exception(studentJson.getName()+" 学生已消课，请勿请假");
                }else if(studentJson.getIsRemove()==2){
                    throw new Exception(studentJson.getName()+" 学生已请假，请勿重复操作");
                }else {
                    Student student = studentMapper.selectOne(new QueryWrapper<Student>().lambda().eq(Student::getId, sid));
                    if(student.getLeaveNum()>=3){
                        throw new Exception(student.getName()+" 学生请假操作次数已达三次，请进行消课处理");
                    }else {
                        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.lambda().eq(Student::getId,sid)
                                .set(Student::getLeaveNum,student.getLeaveNum()+1);
                        studentMapper.update(null,updateWrapper);
                    }
                    studentJson.setIsRemove(2);
                    break;
                }
            }
        }
        //修改学生数组
        UpdateWrapper<Course> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Course::getStudentJson,JSON.toJSONString(studentJsons))
                .eq(Course::getId,id);
        courseMapper.update(null,wrapper);

        int flag = 1;
        for (StudentJson studentJson : studentJsons)
            if (studentJson.getIsRemove()==0)
                flag=0;
        if (flag==1){
            UpdateWrapper<Course> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(Course::getFlag,1)
                    .eq(Course::getId,id);
            courseMapper.update(null,updateWrapper);
            return "请假成功,本节课学生已全部处理完成";
        }else {
            return "请假成功";
        }

    }


    /**
     * 评价
     *
     * @return*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String commitCourse(CommitDTO commitDTO) throws Exception {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        Course course = courseMapper.selectOne(queryWrapper.lambda().eq(Course::getId, commitDTO.getId()));
        List<StudentJson> studentJsons = JSON.parseArray(course.getStudentJson(), StudentJson.class);
        for (StudentJson studentJson : studentJsons) {
            if(studentJson.getId().equals(commitDTO.getSid())){
                if(studentJson.getIsCommit()==1){
                    throw new Exception(studentJson.getName()+" 学生已评价，请勿重复操作");
                }else{
                    studentJson.setIsCommit(1);
                    break;
                }
            }
        }
        //插入评价表
        Commit commit = new Commit();
        String uuid = UUidUtil.generateUUID();
        BeanUtils.copyProperties(commitDTO,commit);
        commit.setCid(commitDTO.getId());
        commit.setId(uuid);
        commit.setCreateTime(new Date());
        commitMapper.insert(commit);

        //修改学生json
        UpdateWrapper<Course> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(Course::getStudentJson,JSON.toJSONString(studentJsons))
                .eq(Course::getId,commitDTO.getId());
        courseMapper.update(null,updateWrapper);

        int flag = 2;
        for (StudentJson studentJson : studentJsons) {
            if (studentJson.getIsCommit()==0)
                flag=1;
        }
        if (flag==2){
            UpdateWrapper<Course> wrapper = new UpdateWrapper<>();
            wrapper.lambda().set(Course::getFlag,flag)
                    .eq(Course::getId,commitDTO.getId());
            courseMapper.update(null,wrapper);
        }

        //微信公众号推送消息
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String classTime = simpleDateFormat.format(commitDTO.getClassDate())+commitDTO.getClassTime();
        String[] strArray = commitDTO.getTeacherAccount().split("\\+");
        Student student = studentMapper.selectOne(new QueryWrapper<Student>().lambda().eq(Student::getId, commitDTO.getSid()));
        String openId = student.getAppId();
        String introduction = "您的孩子 "+student.getName()+" 完成了一节课";
        //String ip = "120.24.233.228";
        String ip = "47.98.33.252";

        FinishClassMessage finishClassMessage = new FinishClassMessage(openId,"http://"+ ip +":8080/commit?id="+uuid,commitDTO.getCourseType(), commitDTO.getCourseDetail(),classTime,introduction,strArray[0],student.getRemainTimes());
        boolean bool = Interaction.NotifyTemplateMessage(finishClassMessage);
        if (bool){
            return "评价成功";
        }else{
            throw new Exception("评价失败,推送消息不成功");
        }
    }
}
