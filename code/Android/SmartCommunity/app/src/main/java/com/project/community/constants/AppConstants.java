package com.project.community.constants;

import com.baidu.platform.comapi.map.B;

/**
 * Created by qizfeng on 17/7/4.
 * 常量字段存储
 */

public class AppConstants {

    public static final String HOST = "http://zhihuishequ.zpftech.com";

    /**
     * 服务器地址
     */
    public static final String URL_BASE = HOST + "/";

    /**
     * 政务首页
     */
    public static final String URL_ZHENGWU_INDEX = URL_BASE + "m/gov";

    /**
     * 获取验证码
     */
    public static final String URL_CODE = URL_BASE + "m/sys/getCode";

    /**
     * 注册
     */
    public static final String URL_REGISTER = URL_BASE + "m/sys/register";

    /**
     * 登录
     */
    public static final String URL_LOGIN = URL_BASE + "m/sys/login";

    /**
     * 忘记密码
     */
    public static final String URL_FORGET_PASS = URL_BASE + "m/sys/forget";

    /**
     * 服务条款
     */
    public static final String URL_AGREEMENT = URL_BASE + "m/sys/agreement";


    /**
     * 获取个人资料
     */
    public static final String URL_USER_INFO = URL_BASE + "m/sys/info";

    /**
     * 修改个人资料
     */
    public static final String URL_EDIT_USER_INFO = URL_BASE + "m/sys/edit";

    /**
     * 数据字典
     */
    public static final String URL_DICTIONARY = URL_BASE + "m/sys/dict/list";

    /**
     * 修改密码
     */
    public static final String URL_MODIFY_PWD = URL_BASE + "m/sys/updatePwd";

    /**
     * 文件上传
     */
    public static final String URL_UPLOAD_FILE = URL_BASE + "m/sys/upload";

    /**
     * 问卷列表
     */
    public static final String URL_QUESTION_LIST = URL_BASE + "m/gov/questions";

    /**
     * 轮播图
     */
    public static final String URL_BANNER = URL_BASE + "m/sys/image/list";

    /**
     * 居民意见
     */
    public static final String URL_SUGGEST = URL_BASE + "m/gov/opinion";


    /**
     * 办事指南列表
     */
    public static final String URL_GUIDE_LIST = URL_BASE + "m/gov/guide";

    /**
     * 政务热线
     */
    public static final String URL_ZHENGWU_HOTLINE = URL_BASE + "m/gov/hotline";

    /**
     * 物业客服电话
     */
    public static final String URL_WUYE_HOTLINE = URL_BASE + "m/prop/hotline";

    /**
     * 类别文章列表
     */
    public static final String URL_TYPE_TOPIC = URL_BASE + "m/gov/art";

    /**
     * 文章正文
     */
    public static final String URL_TOPIC_DETAIL = URL_BASE + "m/gov/art/info";

    /**
     * 文章收藏
     */
    public static final String URL_COLLECT_TOPIC = URL_BASE + "m/gov/art/collection";

    /**
     * 评论列表
     */
    public static final String URL_COMMENT_LIST = URL_BASE + "m/gov/art/commentList";

    /**
     * 发布评论
     */
    public static final String URL_COMMENT = URL_BASE + "m/gov/art/comment";


    /**
     * 删除评论
     */
    public static final String URL_COMMENT_DELETE = URL_BASE + "m/gov/art/commentDel";

    /**
     * 小区列表
     */
    public static final String URL_DIST_LIST = URL_BASE + "m/sys/orgList";

    /**
     * 搜索-政务
     */
    public static final String URL_SEARCH_ZHENGWU = URL_BASE + "m/gov/search";

    /**
     * 物业首页
     */
    public static final String URL_WUYE_INDEX = URL_BASE + "m/prop";

    /**
     * 搜索物业
     */
    public static final String URL_SEARCH_WUYE = URL_BASE + "m/prop/search";

    /**
     * 获取缴费类别
     */
    public static final String URL_JIAOFEI_TYPE=URL_BASE+"m/propPayment/menu";

    /*==========================*/

    public static final int PAGE_SIZE = 10;

    /**
     * 政务公告type
     */
    public static final int ZHENGWU_GONGGAO_TYPE = 3;

    /**
     * 政务宣传type
     */
    public static final int ZHENGWU_XUANCHUAN_TYPE = 4;

    /**
     * 社区快讯type
     */
    public static final int WUYE_KUAIXUN = 7;

    /**
     * 社区公告type
     */
    public static final int WUYE_GONGGAO = 6;

    /**
     * 是否是第一次打开APP
     */
    public static final String FIRST_OPEN = "first_open";
    /**
     * GPS定位结果
     */
    public static final int LOCATION_GPS = 61;

    /**
     * 网络定位结果
     */
    public static final int LOCATION_NETWORK = 161;

    /**
     * 网络异常
     */
    public static final int LOCATION_NETWORK_EXCEPTION = 63;

    /**
     * 网络连接失败
     */
    public static final int LOCATION_NETWORK_CONNECT_FAIL = 68;

    /**
     * 第一次检查网络完毕
     */
    public static final int CHECK_NETWORK_CONNECT_FLAG = 1100;

    /**
     * 传给Service
     */
    public static final int SEND_LOCATION_TO_SERVICE = 1101;
    /**
     * 上传定位结果到服务器成功
     */
    public static final int UPLOAD_LOACTION_SUCCESS = 1102;

    /**
     * 上传定位结果到服务器失败
     */
    public static final int UPLOAD_LOACTION_FAIL = 1103;

    /**
     * 网络异常通知的唯一标识
     */
    public final static int NOTIFICATIO_NETWORK_NOT_OPEN = 1104;

    /**
     * GPS未开启通知的唯一标识
     */
    public final static int NOTIFICATIO_GPS_NOT_OPEN = 1105;

    /**
     * 客户端绑定Service
     */
    public static final int MSG_REGISTER_CLIENT = 1106;
    /**
     * 客户端解绑Service
     */
    public static final int MSG_UNREGISTER_CLIENT = 1107;
    /**
     * 服务发送指令，可以在客户端和服务直接交流
     */
    public static final int MSG_SET_VALUE = 1108;

    /**
     * 以未知启动方式打开Service
     */
    public final static int UNKNOWN_START_SERVICE = -1;
    /**
     * 以开机自启动方式打开Service
     */
    public final static int BOOT_START_SERVICE = 1;

    /**
     * 以手动启动方式打开Service
     */
    public final static int HANDLER_START_SERVICE = 2;


}
