package com.bindada.syscourse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bindada.syscourse.common.BasePageList;
import com.bindada.syscourse.dto.MyClassDTO;
import com.bindada.syscourse.dto.MyClassQueryDTO;
import com.bindada.syscourse.dto.MyClassUpdateDTO;
import com.bindada.syscourse.dto.MyClassUpdateStuDTO;
import com.bindada.syscourse.entity.MyClass;
import com.bindada.syscourse.vo.StudentVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MyClassService extends IService<MyClass> {

    public void addClass(MyClassDTO myClassDTO) throws Exception;

    public void addElse(String times, String time, StudentVO studentVO) throws Exception;

    public BasePageList queryClass(MyClassQueryDTO myClassQueryDTO);

    public void delClass(String id) throws Exception;

    public void updateClass(MyClassUpdateDTO myClassUpdateDTO) throws Exception;

    public void updateStuClass(MyClassUpdateStuDTO myClassUpdateStuDTO) throws Exception;

    public List<MyClass> queryClassByTea();
}
