package com.bindada.syscourse;

<<<<<<< HEAD
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
=======
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bindada.syscourse.common.aliyun.SmsService;
import com.bindada.syscourse.common.weixin.Interaction;
import com.bindada.syscourse.common.weixin.QRCode;
import com.bindada.syscourse.common.weixin.TemplateMessage;
import com.bindada.syscourse.entity.Commit;
import com.bindada.syscourse.entity.MyClass;
import com.bindada.syscourse.entity.Student;
import com.bindada.syscourse.entity.Subject;
import com.bindada.syscourse.mapper.*;
import com.bindada.syscourse.service.SubjectService;
import com.bindada.syscourse.util.DayMapUtil;
import com.bindada.syscourse.util.RedisUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;
>>>>>>> e153035 (融创星项目服务端code第一版)

@SpringBootTest
class SyscourseApplicationTests {

<<<<<<< HEAD
    @Test
    void contextLoads() {
    }

=======
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MyClassMapper myClassMapper;

    @Test
    void contextLoads() {
        System.out.println(1233);
    }

    @Test
    public void test1() {
        List<MyClass> myClassList = myClassMapper.selectList(new QueryWrapper<MyClass>().lambda().eq(MyClass::getType, 1));
        if (!CollectionUtils.isEmpty(myClassList)) {
            for (MyClass myClass : myClassList) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(myClass.getEndDate());
                calendar.add(calendar.DATE, 1);
                Date newEndDate = calendar.getTime();

                System.out.println(newEndDate.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                System.out.println(simpleDateFormat.format(newEndDate));

                Date date = new Date();
                System.out.println(date.before(newEndDate));
                System.out.println(date.after(newEndDate));
            }
        }
    }


    @Test
    public void test2() {
        Page<Student> studentPage = new Page<>(2, 2);
        studentPage = studentMapper.selectPage(studentPage, null);
        for (Student record : studentPage.getRecords()) {
            System.out.println(record);
        }
    }

    @Test
    public void test21() {
        String str = "服气+13334578";
        String[] split = str.split("\\+");
        for (String s : split) {
            System.out.println(s);
        }
        System.out.println(split[0]);
        System.out.println(split.length);
    }

    @Autowired
    private CommitMapper commitMapper;


    @Test
    public void test3() {
        Commit commit = new Commit();
        commit.setId("12121545");
        commit.setSid("11111");
        commit.setCourseType("dafaf");
        commit.setCourseDetail("dagfa");
        commit.setClassDate(new Date());
        commit.setClassTime("十点");
        commit.setTeacherAccount("45454");
        commit.setCommit("HHHHHHHHHHHHHHHH");
        commitMapper.insert(commit);
    }


    @Test
    public void test4() {
        //System.out.println(DayMapUtil.getDay("星期日"));
        //String string ="星期六 10：00";
        //System.out.println(string.substring(0, 3));
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,E");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("E");
        System.out.println(simpleDateFormat.format(date));
        System.out.println(simpleDateFormat1.format(date));
    }

    @Test
    public void test5() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(simpleDateFormat.format(date));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 20);
        Date newTime = calendar.getTime();
        System.out.println(simpleDateFormat.format(newTime));
    }


    @Test
    public void test6() throws ParseException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        String toDay = simpleDateFormat.format(date);
        System.out.println(toDay);
    }

    @Test
    public void TestGetQRCodeTicket() {
        System.out.println(Interaction.getQRCodeTicket(new QRCode.CreateData(600, 123)));
    }

    @Test
    public void TestFinishClassMessage() {
        TemplateMessage.FinishClassMessage message = new TemplateMessage.FinishClassMessage(
                "o0DR_57mSc5a5VaDeHgfnGMxasHA",
                "https://www.baidu.com",
                "高级折磨",
                "还得是鹏爷666",
                "2019年10月1日 15:01~2020年10月1日 15:01",
                "鹏爷",
                666
        );

        Assertions.assertTrue(Interaction.NotifyTemplateMessage(message));
    }

    @Test
    public void TestSendVerificationCode() {
        String phone = "16670889918";
        String code = "123456";
        Assertions.assertTrue(SmsService.SendVerificationCode(phone, code));
    }

    @Test
    public void TestPhoneNumberPattern() {
        String pattern = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        Assertions.assertTrue(Pattern.matches(pattern, "16670889918"));
        Assertions.assertFalse(Pattern.matches(pattern, "1667088991"));
        Assertions.assertTrue(Pattern.matches(pattern, "13469246065"));
    }

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectService subjectService;

    @Test
    public void test7() {
        //subjectMapper.insert()
        List<Subject> list = new ArrayList<>();
        Subject subject = new Subject("", "工业园校区", "模特课程", "初级班4-6岁");
        list.add(subject);

        Subject subject1 = new Subject("", "工业园校区", "模特课程", "中级班6-8岁");
        list.add(subject1);

        Subject subject2 = new Subject("", "工业园校区", "模特课程", "高级班8岁以上");
        list.add(subject2);

        Subject subject3 = new Subject("", "工业园校区", "舞蹈课程", "中级班7-9岁");
        list.add(subject3);

        Subject subject4 = new Subject("", "工业园校区", "舞蹈课程", "高级班8-12岁");
        list.add(subject4);

        Subject subject5 = new Subject("", "工业园校区", "舞蹈课程", "精英班7岁以上");
        list.add(subject5);

        Subject subject6 = new Subject("", "工业园校区", "舞蹈课程", "1对1大师班");
        list.add(subject6);

        Subject subject7 = new Subject("", "工业园校区", "舞蹈课程", "1对1大师班");
        list.add(subject7);

        Subject subject8 = new Subject("", "碧桂园校区", "舞蹈课程", "1对1大师班");
        list.add(subject8);

        Subject subject9 = new Subject("", "嘉悦城校区", "舞蹈课程", "1对1大师班");
        list.add(subject9);

        Subject subject10 = new Subject("", "嘉悦城校区", "舞蹈课程", "游学提升营地");
        list.add(subject10);

    }
>>>>>>> e153035 (融创星项目服务端code第一版)
}
