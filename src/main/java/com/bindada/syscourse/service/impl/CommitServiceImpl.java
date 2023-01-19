package com.bindada.syscourse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bindada.syscourse.entity.Commit;
import com.bindada.syscourse.mapper.CommitMapper;
import com.bindada.syscourse.service.CommitService;
import org.springframework.stereotype.Service;

@Service
public class CommitServiceImpl extends ServiceImpl<CommitMapper, Commit> implements CommitService {
}
