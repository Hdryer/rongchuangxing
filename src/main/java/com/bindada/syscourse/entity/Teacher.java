package com.bindada.syscourse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bindada.syscourse.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "teacher")
public class Teacher extends BaseEntity {

    private String accountUserName;

    private String account;

    private String password;

    private String username;

    private int type;
}
