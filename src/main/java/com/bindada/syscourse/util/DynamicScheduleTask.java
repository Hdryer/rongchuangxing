package com.bindada.syscourse.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bindada.syscourse.entity.Course;
import com.bindada.syscourse.entity.MyClass;
import com.bindada.syscourse.mapper.CourseMapper;
import com.bindada.syscourse.mapper.CronMapper;
import com.bindada.syscourse.mapper.MyClassMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class DynamicScheduleTask implements SchedulingConfigurer {

    @Autowired      //注入mapper
    private  CronMapper cronMapper;

    @Autowired
    private MyClassMapper myClassMapper;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
     
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    System.out.println("开始生成课表");
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
                    String toDay = simpleDateFormat.format(date);
                    System.out.println("今天星期  "+toDay);
                    if (toDay.equals("Mon")){
                        log.info("开始星期一自动生成新课表: " +toDay + " "+LocalDateTime.now().toLocalTime());
                        List<MyClass> myClasses = myClassMapper.selectList(new QueryWrapper<MyClass>().lambda().eq(MyClass::getType,0));
                        for (MyClass myClass : myClasses) {
                            Course course = new Course();
                            BeanUtils.copyProperties(myClass, course, "createTime", "id");
                            String day = myClass.getClassTime().substring(0, 3);
                            Date nowDate = new Date();
                            GregorianCalendar calendar = new GregorianCalendar();
                            calendar.setTime(nowDate);
                            calendar.add(calendar.DATE, DayMapUtil.getDay(day));
                            Date newDate = calendar.getTime();
                            course.setClassDate(newDate);
                            courseMapper.insert(course);
                        }
                    }
                    List<MyClass> myClassList = myClassMapper.selectList(new QueryWrapper<MyClass>().lambda().eq(MyClass::getType, 1));
                    if (!CollectionUtils.isEmpty(myClassList)){
                        log.info("每天自动生成寒暑假课表: "+LocalDateTime.now().toLocalTime());
                        for (MyClass myClass : myClassList) {
                            GregorianCalendar calendar = new GregorianCalendar();
                            calendar.setTime(myClass.getEndDate());
                            calendar.add(calendar.DATE,1);
                            Date newEndDate = calendar.getTime();
                            Date newBeginDate = myClass.getBeginDate();
                            if (newBeginDate.after(date) || newEndDate.before(date))
                                continue;

                            Course course = new Course();
                            BeanUtils.copyProperties(myClass, course, "createTime", "id");
                            course.setClassDate(date);
                            courseMapper.insert(course);
                        }
                    }
                },

                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    String cron = cronMapper.getCron();
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // Omitted Code ..
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

}
