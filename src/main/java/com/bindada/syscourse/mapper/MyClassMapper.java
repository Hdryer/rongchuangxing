package com.bindada.syscourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bindada.syscourse.dto.MyClassDTO;
import com.bindada.syscourse.entity.MyClass;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface MyClassMapper extends BaseMapper<MyClass> {



}
