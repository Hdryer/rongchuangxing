package com.bindada.syscourse.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "commit")
public class Commit implements Serializable  {

    private String id;

    private String cid;

    private String sid;

    private String courseType;

    private String courseDetail;

    private Date classDate;

    private String classTime;

    private String teacherAccount;

    private String commit;

    private Date createTime;
}
