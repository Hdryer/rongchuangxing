package com.bindada.syscourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CronMapper {

    @Select("select cron from cron limit 1")
    public String getCron();
}
