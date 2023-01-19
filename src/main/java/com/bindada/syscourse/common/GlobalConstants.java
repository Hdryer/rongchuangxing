package com.bindada.syscourse.common;


/**
 * 全局常量
 *
 */
public interface GlobalConstants {

    // token
    String AUTHORIZATION = "Authorization";
    // 树顶部节点ID
    String TREE_TOP_ID = "0";
    // 树类型ID
    String DEFAULT_TREE_CATEGORY_ID = "1";
    // 超级管理员角色ID
    String SUPER_ADMIN_ROLE_ID = "1";
    // 超级管理员ID
    String SUPER_ADMIN_ID = "1";

    //全局code
    //方法成功
    Integer SUCCESS = 0;
    //参数异常
    Integer FAILD = 1000;
    //参数异常
    Integer PARAMS_ERROR = 1001;
    //数据已存在
    Integer DATA_EXIST = 1002;
    //未登录或登录异常
    Integer NO_LOGIN = 2000;
}
