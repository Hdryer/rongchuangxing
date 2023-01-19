package com.bindada.syscourse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bindada.syscourse.entity.Record;
import com.bindada.syscourse.mapper.RecordMapper;
import com.bindada.syscourse.service.RecordService;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
}
