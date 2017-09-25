package com.project.community.callback;

import com.baidu.platform.comapi.map.B;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.model.HttpParams;
import com.project.community.model.AgreementResponse;
import com.project.community.model.ArticleModel;
import com.project.community.model.AuditStatusModel;
import com.project.community.model.BannerResponse;
import com.project.community.model.CommentModel;
import com.project.community.model.CommunityCensusModel;
import com.project.community.model.CommunityDeviceFilterModel;
import com.project.community.model.CommunityFamilyModel;
import com.project.community.model.CommunityModel;
import com.project.community.model.CommunityPersonFilterModel;
import com.project.community.model.CommunityShopFilterModel;
import com.project.community.model.DeviceModel;
import com.project.community.model.DictionaryResponse;
import com.project.community.model.DistModel;
import com.project.community.model.FamilyModel;
import com.project.community.model.FamilyPersonModel;
import com.project.community.model.FileUploadModel;
import com.project.community.model.GuideModel;
import com.project.community.model.HotlineModel;
import com.project.community.model.HouseModel;
import com.project.community.model.NewsModel;
import com.project.community.model.PaymentDetailModel;
import com.project.community.model.PaymentHouseHistroyModel;
import com.project.community.model.PaymentInfoModel;
import com.project.community.model.PaymentWayModel;
import com.project.community.model.SearchModel;
import com.project.community.model.ShopModel;
import com.project.community.model.UserModel;
import com.project.community.model.WuyeIndexResponse;
import com.project.community.model.ZhengwuIndexResponse;

import java.io.File;
import java.util.List;

/**
 * Created by qizfeng on 17/7/3.
 */

public interface ServerDao {

    /**
     * 新闻列表
     *
     * @param callback
     */
    void getNewsList(JsonCallback<BaseResponse<List<NewsModel>>> callback);

    /**
     * 新闻列表
     *
     * @param type     新闻类型
     * @param callback
     */
    void getNewsList(String type, JsonCallback<BaseResponse<List<NewsModel>>> callback);


    /**
     * 获取验证码
     *
     * @param telphone
     * @param callback
     */
    void getCode(String telphone, JsonCallback<BaseResponse<List>> callback);

    /**
     * 注册
     *
     * @param username     用户名
     * @param phone        手机号
     * @param code         验证码
     * @param password     密码
     * @param prepPassowrd 确认密码
     * @param idCard       身份证号
     * @param orgCode      小区
     * @param callback
     */
    void doRegister(String username, String phone, String code, String password, String prepPassowrd, String idCard, String orgCode, JsonCallback<BaseResponse<UserModel>> callback);


    /**
     * 登录
     *
     * @param username 用户名(手机号)
     * @param password 密码
     * @param callback
     */
    void doLogin(String username, String password, JsonCallback<BaseResponse<UserModel>> callback);


    /**
     * 忘记密码
     *
     * @param phone        手机号
     * @param code         验证码
     * @param password     密码
     * @param prepPassword 确认密码
     * @param callback
     */

    void doForgetPass(String phone, String code, String password, String prepPassword, JsonCallback<BaseResponse<List>> callback);


    /**
     * 政务首页
     *
     * @param userId   用户id
     * @param pageNo   当前页
     * @param pageSize 条数
     * @param callback
     */
    void getZhengwuIndexData(String userId, int pageNo, int pageSize, JsonCallback<BaseResponse<ZhengwuIndexResponse>> callback);

    /**
     * 服务协议
     *
     * @param callback
     */
    void getAgreement(JsonCallback<BaseResponse<AgreementResponse>> callback);


    /**
     * 获取个人资料
     *
     * @param id
     * @param callback
     */
    void getUserInfo(String id, JsonCallback<BaseResponse<UserModel>> callback);

    /**
     * 修改个人资料
     *
     * @param id         用户id
     * @param photo      用户头像
     * @param loginName  用户昵称
     * @param name       用户姓名
     * @param idCard     身份证号
     * @param roomNo     房屋编号
     * @param isOwner    是否是业主
     * @param occupation 职业
     * @param nation     民族
     * @param religion   宗教信仰
     * @param party      党派
     * @param callback   回调
     */
    void doEditUserInfo(String id, String photo, String loginName, String name,
                        String idCard, String roomNo, String isOwner, String occupation,
                        String nation, String religion, String party, JsonCallback<BaseResponse<List>> callback);


    /**
     * 修改头像
     *
     * @param id
     * @param photo
     * @param callback
     */
    void uploadUserPhoto(String id, String photo, JsonCallback<BaseResponse<List>> callback);

    /**
     * 数据字典
     *
     * @param dictType 类型 性别sex
     *                 是否业主 sys_user_is_owner
     *                 民族 sys_user_nation
     *                 宗教 sys_user_religion
     *                 党派 sys_user_party
     *                 部门 gov_part
     *                 主题 gov_theme
     * @param callback
     */
    void getDictionaryData(String dictType, JsonCallback<BaseResponse<DictionaryResponse>> callback);

    /**
     * 修改密码
     *
     * @param id
     * @param oldPassword  原密码
     * @param password     新密码
     * @param prepPassword 确认新密码
     * @param callback     回调
     */
    void doModifyPwd(String id, String oldPassword, String password, String prepPassword, JsonCallback<BaseResponse<List>> callback);


    /**
     * 上传文件
     *
     * @param file     文件流
     * @param callback
     */
    void uploadFile(File file, JsonCallback<BaseResponse<FileUploadModel>> callback);

    /**
     * 轮播图
     *
     * @param imageType    1轮播图 2社区首页
     * @param imageAddress 1首页 2物业
     * @param callback
     */
    void getBannerData(String imageType, String imageAddress, JsonCallback<BaseResponse<BannerResponse>> callback);

    /**
     * 提交居民意见
     *
     * @param userId   用户id
     * @param orgCode
     * @param phone    手机号
     * @param title    标题
     * @param context  内容
     * @param callback
     */
    void submitSuggest(String userId, String orgCode, String phone, String title, String context, JsonCallback<BaseResponse<List>> callback);

    /**
     * 办事指南列表
     *
     * @param guideTheme 主题
     * @param guidePart  部门
     * @param callback
     */
    void getWorkGuide(String guideTheme, String guidePart, JsonCallback<BaseResponse<List<GuideModel>>> callback);

    /**
     * 办事指南搜索
     * @param keywords
     * @param callback
     */
    void searchGuide(String keywords,JsonCallback<BaseResponse<List<GuideModel>>>callback);

    /**
     * 热线电话
     *
     * @param type     1政务 2物业
     * @param orgCode  机构代码
     * @param callback
     */
    void getHotLine(String type, String orgCode, JsonCallback<BaseResponse<List<HotlineModel>>> callback);

    /**
     * 根据类别获取文章
     *
     * @param type     类别id
     * @param callback
     */
    void getTypeTopic(String uesrId, int pageNo, int pageSize, String type, JsonCallback<BaseResponse<List<ArticleModel>>> callback);

    /**
     * @param userId
     * @param artId
     * @param callback
     */
    void getTopicDetail(String userId, String artId, JsonCallback<BaseResponse<ArticleModel>> callback);

    /**
     * 收藏文章
     *
     * @param artId    文章id
     * @param callback
     */
    void doCollectTopic(String userId, String artId, JsonCallback<BaseResponse<List>> callback);

    /**
     * 评论列表
     *
     * @param artId    文章id
     * @param callback
     */
    void getComments(String artId, JsonCallback<BaseResponse<List<CommentModel>>> callback);

    /**
     * 发评论
     *
     * @param userId   用户id
     * @param artId    文章id
     * @param content  评论内容
     * @param targetId 回复人id
     * @param callback
     */
    void doComment(String userId, String artId, String targetId, String content, JsonCallback<BaseResponse<List>> callback);


    /**
     * 小区列表
     *
     * @param callback
     */
    void getDistList(JsonCallback<BaseResponse<List<DistModel>>> callback);

    /**
     * 删除评论
     *
     * @param userId
     * @param commentId
     * @param type
     * @param callback
     */
    void doDeleteComment(String userId, String commentId, int type, JsonCallback<BaseResponse<List>> callback);

    /**
     * 搜索
     *
     * @param type     0-政务 1-物业 2-民生
     * @param keywords 关键字
     * @param callback
     */
    void doSearch(String type, String keywords, JsonCallback<BaseResponse<List<SearchModel>>> callback);

    /**
     * 物业首页数据
     *
     * @param type
     * @param callback
     */
    void getWuyeIndexData(String userId, int pageNo, int pageSize, int type, JsonCallback<BaseResponse<WuyeIndexResponse>> callback);


    /**
     * 获取缴费方式
     *
     * @param callback
     */
    void getPaymentWay(JsonCallback<BaseResponse<List<PaymentWayModel>>> callback);

    /**
     * 获取缴费编号列表
     *
     * @param userId   用户id
     * @param callback
     */
    void getPaymentNoData(String userId, JsonCallback<BaseResponse<List<PaymentHouseHistroyModel>>> callback);


    /**
     * 查询房屋信息
     *
     * @param roomNo   房屋编号
     * @param callback
     */
    void selectHouseInfo(String roomNo, JsonCallback<BaseResponse<HouseModel>> callback);


    /**
     * 添加房屋
     *
     * @param userId   用户id
     * @param roomNo   房屋编号
     * @param callback
     */
    void addHouse(String userId, String roomNo, JsonCallback<BaseResponse<HouseModel>> callback);


    /**
     * 删除缴费房屋
     *
     * @param userId    用户id
     * @param payRoomId 缴费房屋id
     * @param callback
     */
    void deleteHouse(String userId, String payRoomId, JsonCallback<BaseResponse<List>> callback);

    /**
     * 获取缴费信息
     *
     * @param paymentType
     * @param roomNo
     * @param callback
     */
    void getPaymentDetailInfo(String paymentType, String roomNo, JsonCallback<BaseResponse<PaymentInfoModel>> callback);


    /**
     * 家庭信息 列表
     *
     * @param userId
     * @param phone    手机号
     * @param roomNo   个人信息房屋编号
     * @param callback
     */
    void getFamilyListInfo(String userId, String phone, String roomNo, JsonCallback<BaseResponse<List<FamilyModel>>> callback);

    /**
     * 家庭信息
     * @param userId 用户id
     * @param phone 手机号
     * @param roomNo 个人信息房屋编号
     * @param familyId 家庭id
     * @param callback
     */
    void getFamilyInfo(String userId,String phone,String roomNo,String familyId,JsonCallback<BaseResponse<List<FamilyModel>>>callback);

    /**
     * 检查业主状态
     *
     * @param roomNo
     * @param userId
     * @param callback
     */
    void checkOwner(String roomNo, String userId, String phone, JsonCallback<BaseResponse<AuditStatusModel>> callback);

    /**
     * 添加家庭
     *
     * @param userId     用户id
     * @param roomNo     房屋编号
     * @param familyName 家庭名称
     * @param callback
     */
    void addFamily(String userId, String roomNo, String familyName, JsonCallback<BaseResponse<HouseModel>> callback);

    /**
     * 添加人口
     *
     * @param params   参数集合
     *                 roomNo 房屋编号
     *                 userId 用户id
     *                 familyId 家庭编号
     *                 memberId 成员id 为空则为新增 编辑时传入
     *                 realName 真实姓名
     *                 photo 头像
     *                 phone 手机号
     *                 headRelation 和户主关系
     *                 occupation 职业
     *                 sex 性别 1-男 2-女 0-未填写
     *                 nation 民族
     *                 religion 宗教信仰
     *                 party 党派
     *                 dateOfBirth 出生日期
     *                 idNumber 身份证号
     *                 roomAddress房屋地址
     * @param callback
     */
    void addPerson(HttpParams params, JsonCallback<BaseResponse<List>> callback);

    /**
     * 删除家庭成员
     *
     * @param userId   用户id
     * @param memberId 删除的成员id
     * @param roomNo 房屋编号
     * @param callback
     */
    void deletePerson(String userId, String memberId, String roomNo, JsonCallback<BaseResponse<List>> callback);


    /**
     * 获取家庭成员信息
     *
     * @param memberId 家庭成员id
     * @param callback
     */
    void getPerson(String memberId, JsonCallback<BaseResponse<FamilyPersonModel>> callback);

    /**
     * 删除家庭
     *
     * @param userId 用户id
     * @param roomNo 房屋编号
     * @param familyId 家庭id
     * @param callback
     */
    void deleteFamily(String userId, String roomNo, String familyId, JsonCallback<BaseResponse<List>> callback);

    /**
     * 提交房屋业主审核
     *
     * @param userId     用户id
     * @param familyName 家庭名称
     * @param familyNo   房屋编号
     * @param callback
     */
    void auditFamily(String userId, String familyName, String familyNo, JsonCallback<BaseResponse<List>> callback);


    /**
     * 获取支付订单信息
     * @param userId 用户id
     * @param parmentId 订单id
     * @param callback
     */
    void getPayOrderInfo(String userId,String parmentId,JsonCallback<BaseResponse<String>>callback);

    /**
     * 小区列表
     * @param userId 用户id
     * @param orgCode 组织机构
     * @param callback
     */
    void getCommunityList(String userId, String orgCode, JsonCallback<BaseResponse<List<CommunityModel>>>callback);

    /**
     * 获取小区统计信息
     * @param userId 用户id
     * @param orgCode 组织机构
     * @param callback
     */
    void getCommunityCensusInfo(String userId, String orgCode, JsonCallback<BaseResponse<List<CommunityCensusModel>>>callback);

    /**
     * 小区楼栋成员列表
     * @param userId 用户id
     * @param unit 单元
     * @param floor 楼栋
     * @param callback
     */
    void getCommunityFamilyPersonList(String userId, String unit, String floor, JsonCallback<BaseResponse<List<CommunityFamilyModel>>>callback);

    /**
     * 获取小区楼栋成员信息
     * @param memberId
     * @param callback
     */
    void getCommunityFamilyPersonInfo(String memberId,JsonCallback<BaseResponse<FamilyPersonModel>> callback);

    /**
     * 获取商铺坐标
     * @param userId 用户id
     * @param coordinate 定位坐标
     * @param distance 筛选距离
     * @param auditStatus 审核状态
     * @param callback
     */
    void getCommunityShopList(String userId, String coordinate, String distance, String auditStatus, JsonCallback<BaseResponse<List<ShopModel>>>callback);

    /**
     * 获取商铺筛选条件
     * @param callback
     */
    void getCommunityShopFilter(JsonCallback<BaseResponse<CommunityShopFilterModel>>callback);

    /**
     * 商铺详情
     * @param userId
     * @param shopId
     * @param callback
     */
    void getCommunityShopDetail(String userId,String shopId,JsonCallback<BaseResponse<ShopModel>>callback);

    /**
     * 获取居民筛选条件
     * @param callback
     */
    void getCommunityPersonFilter(JsonCallback<BaseResponse<CommunityPersonFilterModel>>callback);

    /**
     * 获取居民坐标
     * @param userId 用户id
     * @param coordinate 定位坐标
     * @param distance 筛选距离
     * @param memberTag 居民标签
     * @param callback
     */
    void getCommunityPersonList(String userId,String coordinate,String distance,String memberTag,JsonCallback<BaseResponse<List<FamilyPersonModel>>>callback);

    /**
     * 居民详情
     * @param userId
     * @param id
     * @param callback
     */
    void getCommunityPersonDetail(String userId,String id,JsonCallback<BaseResponse<FamilyPersonModel>>callback);

    /**
     * 获取设施筛选条件
     * @param callback
     */
    void getCommunityDeviceFilter(JsonCallback<BaseResponse<CommunityDeviceFilterModel>>callback);

    /**
     * 获取物业设施坐标
     * @param userId 用户id
     * @param coordinate 定位坐标
     * @param orgCode 组织机构
     * @param distance 筛选距离
     * @param facilitiesType 设施类型
     * @param callback
     */
    void getCommunityDeviceList(String userId, String coordinate, String orgCode, String distance,
                                String facilitiesType, JsonCallback<BaseResponse<List<DeviceModel>>>callback);

    /**
     * 物业设施详情
     * @param userId 用户id
     * @param faiId 设备id
     * @param callback
     */
    void getCommunityDeviceDetail(String userId,String faiId,JsonCallback<BaseResponse<DeviceModel>>callback);

    /**
     *上传定位表座
     * @param userId 用户id
     * @param coordinate 经度,纬度
     * @param callback
     */
    void doUploadLocation(String userId,String coordinate,JsonCallback<BaseResponse<List>>callback );
}
