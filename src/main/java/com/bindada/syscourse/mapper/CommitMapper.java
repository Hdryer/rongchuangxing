package com.bindada.syscourse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bindada.syscourse.entity.Commit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommitMapper extends BaseMapper<Commit> {

    public String getCommit(String id);
}
