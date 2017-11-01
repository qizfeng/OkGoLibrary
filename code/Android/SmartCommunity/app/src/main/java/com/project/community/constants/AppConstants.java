package com.project.community.constants;

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
     * 办事指南搜索
     */
    public static final String URL_GUIDE_SEARCH = URL_BASE + "m/gov/guide/search";

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
     * 搜索民生
     */
    public static final String URL_SEARCH_PROPSHOPS = URL_BASE + "/m/propleLife/propShops/list";

    /**
     * 获取缴费类别
     */
    public static final String URL_JIAOFEI_TYPE = URL_BASE + "m/propPayment/menu";

    /**
     * 获取缴费编号列表
     */
    public static final String URL_JIAOFEI_NO_LIST = URL_BASE + "m/propPayRoom/index";

    /**
     * 添加房屋
     */
    public static final String URL_ADD_HOUSE = URL_BASE + "m/propPayRoom/save";

    /**
     * 查询房屋信息
     */
    public static final String URL_SELECT_HOUSE = URL_BASE + "m/propPayment/checkRoom";
    /**
     * 删除缴费房屋
     */
    public static final String URL_DELETE_HOUSE = URL_BASE + "m/propPayRoom/delete";


    /**
     * 查询缴费信息
     */
    public static final String URL_SELECT_PAY_INFO = URL_BASE + "m/propPayment/index";

    /**
     * 问卷列表url h5
     */
    public static final String URL_WENJUAN_LIST = URL_BASE + "surveyList/d9_questionnaire.html";

    /**
     * 问卷搜索
     */
    public static final String URL_WENJUAN_SEARCH = URL_BASE + "surveyList/d9_questionnaire.html";

    /**
     * 问卷详情页 h5
     */
    public static final String URL_WENJUAN_DETIAL = URL_BASE + "surveyList/d10_writeQuestionnaire.html";

    /**
     * 问卷结果页
     */
    public static final String URL_WENJUAN_RESULT = URL_BASE + "surveyList/d11_results.html";

    /**
     * 家庭列表
     */
    public static final String URL_FAMILY_LIST = URL_BASE + "m/propFamily/index";

    /**
     * 检查业主审核状态
     */
    public static final String URL_CHECK_OWNER = URL_BASE + "m/ownerAudit/checkOwner";

    /**
     * 添加家庭
     */
    public static final String URL_ADD_FAMILY = URL_BASE + "m/propFamily/save";

    /**
     * 删除家庭
     */
    public static final String URL_DELETE_FAMILY = URL_BASE + "m/propFamily/delete";

    /**
     * 添加人口
     */
    public static final String URL_ADD_PERSON = URL_BASE + "m/propMember/save";

    /**
     * 刪除人口
     */
    public static final String URL_DELETE_PERSON = URL_BASE + "m/propMember/delete";


    /**
     * 获取家庭成员信息
     */
    public static final String URL_GET_PERSON = URL_BASE + "m/propMember/getMember";

    /**
     * 提交房屋业主申请
     */
    public static final String URL_FAMILY_AUDIT = URL_BASE + "m/ownerAudit/audit";


    /**
     * 获取支付订单
     */
    public static final String URL_GET_PAY_ORDER = URL_BASE + "m/propPayment/aliPay";

    /**
     * 社区-小区列表census
     */
    public static final String URL_COMMUNITY_LIST = URL_BASE + "m/sys/communityList";

    /**
     * 社区-小区统计信息
     */
    public static final String URL_COMMUNITY_CENSUS = URL_BASE + "m/community/floor/getFloors";

    /**
     * 小区楼栋人员列表
     */
    public static final String URL_COMMUNITY_MEMBER_LIST = URL_BASE + "m/community/floor/getFloorMember";

    /**
     * 小区人员信息
     */
    public static final String URL_COMMUNITY_MEMBER_INFO = URL_BASE + "m/propMember/getMemberByMap";

    /**
     * 商铺坐标
     */
    public static final String URL_COMMUNITY_SHOP_LIST = URL_BASE + "m/community/shops/shopsList";

    /**
     * 商铺筛选
     */
    public static final String URL_COMMUNITY_SHOP_FILTER = URL_BASE + "m/community/shops/searchData";

    /**
     * 商铺详细信息
     */
    public static final String URL_COMMUNITY_SHOP_DETAIL = URL_BASE + "m/community/shops/get";

    /**
     * 居民筛选
     */
    public static final String URL_COMMUNITY_PERSON_FILTER = URL_BASE + "m/community/comyUser/searchData";

    /**
     * 居民坐标
     */
    public static final String URL_COMMUNITY_PERSON_LIST = URL_BASE + "m/community/comyUser/userList";

    /**
     * 居民详情
     */
    public static final String URL_COMMUNITY_PERSON_DETAIL = URL_BASE + "m/community/comyUser/get";

    /**
     * 设施坐标
     */
    public static final String URL_COMMUNITY_DEVICE_LIST = URL_BASE + "m/community/facilities/facilitiesList";

    /**
     * 设施筛选
     */
    public static final String URL_COMMUNITY_DEVICE_FILTER = URL_BASE + "m/community/facilities/searchData";

    /**
     * 设施详情
     */
    public static final String URL_COMMUNITY_DEVICE_DETAIL = URL_BASE + "m/community/facilities/get";

    /**
     * 上传用户坐标
     */
    public static final String URL_UPLOAD_LATLNG = URL_BASE + "m/community/comyUser/updateCoordinate";

    /**
     * 申请商铺
     */
    public static final String URL_APPLY_STORE = URL_BASE + "/m/propleLife/propShops/save";


    /**
     * 附近商家
     */
    public static final String URL_MINSHENG_INDEX = URL_BASE + "/m/propleLife/propShops/index";


    /**
     * 添加修改地址
     */
    public static final String URL_ADD_ADDRESS = URL_BASE + "/m/user/address/save";

    /**
     * 地址列表
     */
    public static final String URL_GET_ADDRESS_LIST = URL_BASE + "/m/user/address/index";
    /**
     * 删除地址
     */
    public static final String URL_DEL_ADDRESS = URL_BASE + "/m/user/address/delete";

    /**
     * 设为默认地址
     */
    public static final String URL_SET_DEFAULT = URL_BASE + "/m/user/address/setDefault";


    public static final String URL_GET_COLLECTION = URL_BASE + "/m/user/collection/index";


    /**
     * 申请商铺
     */
    public static final String URL_UPDATEISOPEN = URL_BASE + "/m/propleLife/propShops/updateIsOpen";

    /**
     * D75商铺信息首页
     */
    public static final String URL_SHOPINDEX = URL_BASE + "/m/propleLife/propShops/shopIndex";

    /**
     * D83商铺详情
     */
    public static final String URL_PROPSHOPS = URL_BASE + "/m/propleLife/propShops/get";

    /**
     * D77字账号列表
     */
    public static final String URL_SUBDOMAINACCOUNTLIST = URL_BASE + "/m/propleLife/shop/account/list";

    /**
     * D77子账号删除
     */
    public static final String DELETE_SUBDOMAINACCOUNT = URL_BASE + "/m/propleLife/shop/account/delete";

    /**
     * D78子账号详情
     */
    public static final String URL_GETSUBDOMAINACCOUNTDETAIL = URL_BASE + "/m/propleLife/shop/account/get";

    /**
     * D76添加商品
     */
    public static final String URL_ADDGOODS = URL_BASE + "/m/propleLife/shop/goods/save";

    /**
     * D78子账号添加
     */
    public static final String URL_ADDSUBDOMAINACCOUNT = URL_BASE + "/m/propleLife/shop/account/save";

    /**
     * D80商品管理
     */
    public static final String URL_GETGOODSMANAGER = URL_BASE + "/m/propleLife/shop/goods/list";


    /**
     * 获取论坛分类
     */
    public final static String URL_CLASSIFY_LIST = URL_BASE + "/m/sys/dict/list";

    /**
     * 获取论坛分类
     */
    public final static String URL_SENDMESSAGE = URL_BASE + "/m/propleLife/article/save";


    /**
     * 获取论坛分类
     */
    public final static String URL_GET_BBS_LIST = URL_BASE + "/m/propleLife/article/list";

    /**
     * 收藏或者取消收藏
     */
    public final static String URL_CLLECT_BBS = URL_BASE + "/m/propleLife/collection/collect";


    /**
     * 获取论坛评论列表
     */
    public final static String URL_GET_COMMENT_LIST = URL_BASE + "/m/propleLife/comment/list";


    /**
     * 发表评论或者评帖子
     */
    public final static String URL_SAVE_COMMENT = URL_BASE + "/m/propleLife/comment/save";

    /**
     * 删除所有评论
     */
    public final static String URL_DEL_ALL_COMMENT = URL_BASE + "/m/user/article/deleteAllComments";

    /**
     * 删除评论
     */
    public final static String URL_DEL_COMMENT = URL_BASE + "/m/propleLife/comment/delete";

    /**
     * 获取帖子详情
     */
    public final static String URL_GET_ARTICLE = URL_BASE + "/m/propleLife/article/get";

    /**
     * 搜索
     */
    public final static String URL_GET_BBS_SEARCH = URL_BASE + "/m/propleLife/article/get";

    /**
     * 发布的帖子
     */

    public final static String URL_GET_ARTICLE_INDEX = URL_BASE + "/m/user/article/index";

    /**
     * 删除帖子
     */
    public final static String URL_DEL_ARTICLE = URL_BASE + "/m/propleLife/article/delete";
    /**
     * 我的回帖
     */
    public final static String URL_ARTICLE_REPLIES = URL_BASE + "/m/user/article/replies";


    /**
     * 我的报修记录
     */
    public final static String URL_REPAIRS_RECORD = URL_BASE + "/m/propRepair/index";


    /**
     * 获取报修分类
     */
    public final static String URL_GET_ROOM_LIST = URL_BASE + "/m/proRepairRoom/index";

    /**
     * 添加房屋编码
     */
    public final static String URL_SEVA_HOUSE_NUMBER = URL_BASE + "/m/proRepairRoom/save";

    /**
     * 提价报修
     */
    public final static String URL_SAVE_PROP_REPAIR = URL_BASE + "/m/propRepair/save";

    /**
     * 评价报修
     */
    public final static String URL_PROREPAIR_COMMENT_SAVE = URL_BASE + "/m/proRepair/comment/save";

    /**
     * 取消订单
     */
    public final static String URL_PROREPAIR_CANCEL = URL_BASE + "/m/propRepair/cancel";

    /**
     * 完成订单
     */
    public final static String URL_PROREPAIR_COMPLETE = URL_BASE + "/m/propRepair/complete";

    /**
     * D80商品删除
     */
    public static final String URL_DELGOODS = URL_BASE + "/m/propleLife/shop/goods/delete";

    /**
     * D78上下架
     */
    public static final String URL_UPDOWNGOODS = URL_BASE + "/m/propleLife/shop/goods/upDownGoods";


    /**
     * D27购物车
     */
    public static final String URL_GETCARTLIST = URL_BASE + "/m/propleLife/shop/cart/list";

    /**
     * D29商家详情
     */
    public static final String URL_GETSHOPBYUSER = URL_BASE + "/m/propleLife/propShops/getShopByUser";

    /**
     * D27添加购物车
     */
    public static final String URL_ADDSHOPCART = URL_BASE + "/m/propleLife/shop/cart/save";

    /**
     * D27购物车删除
     */
    public static final String URL_DELCART = URL_BASE + "/m/propleLife/shop/cart/delete";

    /**
<<<<<<< .mine
     * <<<<<<< .mine
||||||| .r526
<<<<<<< .mine
=======
>>>>>>> .r531
     * D56订单提交
     */
    public static final String URL_GETORDER = URL_BASE + "/m/propleLife/shop/order/save";

    /**
     * D确认订单
     */
    public static final String URL_SUBMIT = URL_BASE + "/m/propleLife/shop/order/submit";

    /**
     * D58评价
     */
    public static final String URL_COMMENTGOODS=URL_BASE+"/m/propleLife/order/comment/save";

    /**
     * D56订单提交
     */
    public static final String URL_GETDETAIL = URL_BASE + "/m/propleLife/order/sale/getDetail";

    /**
     * D55取消订单
     */
    public static final String URL_CACELORDER = URL_BASE + "/m/propleLife/shop/order/cancel";

    /**
     * D55删除订单
     */
    public static final String URL_DELETORDER = URL_BASE + "/m/propleLife/shop/order/delete";

    /**
     * D57订单列表
     */
    public static final String URL_GETORDERLIST = URL_BASE + "/m/propleLife/shop/order/list";

    /**
     * D88商家订单列表
     */
    public static final String URL_GETLISTBYSHOP=URL_BASE+"/m/propleLife/shop/order/listByShop";

    /**
     * D59申请售后
     */
    public static final String URL_APPLYORDER=URL_BASE+"/m/propleLife/order/sale/save";

    /**
     * D55确认收货
     */
    public static final String URL_COMPLETE = URL_BASE + "/m/propleLife/shop/order/complete";

    /**
     * 发货
     */
    public static final String URL_SEND=URL_BASE+"/m/propleLife/shop/order/send";

    /**

     * 维修人员维修单
     */
    public static final String URL_REPAIR_LIST = URL_BASE + "/m/propRepair/repairList";

    /**
     * 获取维修人员详情
     */
    public static final String URL_GET_REPAIR = URL_BASE + "/m/propRepair/getRepair";

    /**
     * 上传设备唯一标识符
     */
    public static final String URL_UPLOAD_UDID = URL_BASE + "m/sys/machineCode";


    /**
     * 获取报修详情
     */
    public static final String URL_GET_PROPREPAIR = URL_BASE + "/m/propRepair/get";



    public  static  final  String  URL_SET_PROPREPAIR_HANDLE=URL_BASE + "/m/propRepair/handle";


    /**
     * 维修人员回复
     */
    public static final  String  URL_REPLAY_SAVE=URL_BASE + "/m/proRepair/replay/save";

    /**
     * 分页大小
     */
    public static final int PAGE_SIZE = 15;

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


    public static String LAST_WEB_URL = "";

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
