package com.hisoft.hscloud.web.util; 

public interface DBConstants {
    public final static String QUERY_ACCESS_KEY = "QUERY_ACCESS_KEY";
    
    //通过accessid查询ip
    public final static String GET_ALL_IP_BY_ACCESSID="GET_IP_BY_ACCESSID";
    public final static String QUERY_FACTORY_SENQUENCE = "QUERY_FACTORY_SENQUENCE";
    public final static String QUERY_USER_ID = "QUERY_USER_ID";
    
    public final static String GETALL_PLANS4INFO_SQL = "GETALL_PLANS4INFO_SQL";
    
    public final static String GET_ALL_VMINFO_BY_MACHINENO = "GET_ALL_VMINFO_BY_MACHINENO";
    public final static String GET_ALL_VMINFO_BY_VMID="GET_ALL_VMINFO_BY_VMID";
    
    public final static String QUERY_ACCESS = "QUERY_ACCESS";
    
    public final static String QUERY_REFERENCEID_BY_VMID = "QUERY_REFERENCEID_BY_VMID";
    
    public final static String QUERY_CREATEFLAG_BY_VMID = "QUERY_CREATEFLAG_BY_VMID";
    
    //查询用户所要虚拟机
    public final static String GET_ALL_VMINFO = "GET_ALL_VMINFO";
    
    //保存任务记录
    public final static String SAVE_TASK = "SAVE_TASK";
    //查询操作结果
    public final static String GET_OPERATION_RESULT="GET_OPERATION_RESULT";
    
    public final static String GET_OPERATION_RESOURCE_RESULT ="GET_OPERATION_RESOURCE_RESULT";
    public final static String QUERY_ACCESS_KEY_BY_EMAIL = "QUERY_ACCESS_KEY_BY_EMAIL";
    //查询外平台用户是否注册
    public final static String QUERY_PLATFORM_RELATION_BY_EUSERID = "QUERY_PLATFORM_RELATION_BY_EUSERID";
  //外平台用户注册hscloud
    public final static String SAVE_PLATFORM_RELATION = "SAVE_PLATFORM_RELATION";
}
