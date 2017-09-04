package com.project.community.callback;

import com.baidu.platform.comapi.map.B;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.model.AgreementResponse;
import com.project.community.model.ArticleModel;
import com.project.community.model.BannerResponse;
import com.project.community.model.CommentModel;
import com.project.community.model.DictionaryResponse;
import com.project.community.model.DistModel;
import com.project.community.model.FileUploadModel;
import com.project.community.model.GuideModel;
import com.project.community.model.NewsModel;
import com.project.community.model.SearchModel;
import com.project.community.model.UserModel;
import com.project.community.model.UserResponse;
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
     * @param username 用户名
     * @param phone 手机号
     * @param code 验证码
     * @param password 密码
     * @param prepPassowrd 确认密码
     * @param idCard 身份证号
     * @param orgCode 小区
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
     * @param phone 手机号
     * @param code 验证码
     * @param password 密码
     * @param prepPassword 确认密码
     * @param callback
     */

    void doForgetPass(String phone, String code, String password, String prepPassword, JsonCallback<BaseResponse<List>> callback);


    /**
     * 政务首页
     * @param userId 用户id
     * @param pageNo 当前页
     * @param pageSize 条数
     * @param callback
     */
    void getZhengwuIndexData(String userId,int pageNo,int pageSize,JsonCallback<BaseResponse<ZhengwuIndexResponse>> callback);

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
     * @param id 用户id
     * @param photo 用户头像
     * @param loginName 用户昵称
     * @param name 用户姓名
     * @param idCard 身份证号
     * @param roomNo 房屋编号
     * @param isOwner 是否是业主
     * @param occupation 职业
     * @param nation 民族
     * @param religion 宗教信仰
     * @param party 党派
     * @param callback 回调
     */
    void doEditUserInfo(String id, String photo, String loginName, String name,
                        String idCard, String roomNo, String isOwner, String occupation,
                        String nation, String religion, String party, JsonCallback<BaseResponse<List>> callback);


    /**
     * 修改头像
     * @param id
     * @param photo
     * @param callback
     */
    void uploadUserPhoto(String id,String photo,JsonCallback<BaseResponse<List>> callback);

    /**
     * 数据字典
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
     * @param id
     * @param oldPassword 原密码
     * @param password 新密码
     * @param prepPassword 确认新密码
     * @param callback 回调
     */
    void doModifyPwd(String id,String oldPassword,String password,String prepPassword, JsonCallback<BaseResponse<List>> callback);


    /**
     * 上传文件
     * @param file 文件流
     * @param callback
     */
    void uploadFile(File file, JsonCallback<BaseResponse<FileUploadModel>>callback);

    /**
     * 轮播图
     * @param imageType  1轮播图 2社区首页
     * @param imageAddress 1首页 2物业
     * @param callback
     */
    void getBannerData(String imageType, String imageAddress, JsonCallback<BaseResponse<BannerResponse>> callback);

    /**
     * 提交居民意见
     * @param phone 手机号
     * @param title 标题
     * @param context 内容
     * @param callback
     */
    void submitSuggest(String phone,String title,String context,JsonCallback<BaseResponse<List>>callback);

    /**
     * 办事指南列表
     * @param guideTheme 主题
     * @param guidePart 部门
     * @param callback
     */
    void getWorkGuide(String guideTheme, String guidePart, JsonCallback<BaseResponse<List<GuideModel>>> callback);

    /**
     * 热线电话
     * @param type 1政务 2物业
     * @param orgCode 机构代码
     * @param callback
     */
    void getHotLine(String type,String orgCode,JsonCallback<BaseResponse<List>>callback);

    /**
     * 根据类别获取文章
     * @param type 类别id
     * @param callback
     */
    void getTypeTopic(String uesrId,int pageNo,int pageSize,String type, JsonCallback<BaseResponse<List<ArticleModel>>> callback);

    /**
     *
     * @param userId
     * @param artId
     * @param callback
     */
    void getTopicDetail(String userId,String artId,JsonCallback<BaseResponse<ArticleModel>>callback);

    /**
     * 收藏文章
     * @param artId 文章id
     * @param callback
     */
    void doCollectTopic(String userId,String artId,JsonCallback<BaseResponse<List>> callback);

    /**
     * 评论列表
     * @param artId 文章id
     * @param callback
     */
    void getComments(String artId, JsonCallback<BaseResponse<List<CommentModel>>> callback);

    /**
     * 发评论
     * @param userId 用户id
     * @param artId 文章id
     * @param content 评论内容
     * @param targetId 回复人id
     * @param callback
     */
    void doComment(String userId,String artId,String targetId,String content,JsonCallback<BaseResponse<List>> callback);


    /**
     * 小区列表
     * @param callback
     */
    void getDistList(JsonCallback<BaseResponse<List<DistModel>>>callback);

    /**
     * 删除评论
     * @param userId
     * @param commentId
     * @param type
     * @param callback
     */
    void doDeleteComment(String userId,String commentId,int type,JsonCallback<BaseResponse<List>>callback);

    /**
     * 搜索
     * @param type 0-政务 1-物业 2-民生
     * @param keywords 关键字
     * @param callback
     */
    void doSearch(String type, String keywords, JsonCallback<BaseResponse<List<SearchModel>>>callback);

    /**
     * 物业首页数据
     * @param type
     * @param callback
     */
    void getWuyeIndexData(String userId,int pageNo,int pageSize,int type,JsonCallback<BaseResponse<WuyeIndexResponse>>callback);
}
