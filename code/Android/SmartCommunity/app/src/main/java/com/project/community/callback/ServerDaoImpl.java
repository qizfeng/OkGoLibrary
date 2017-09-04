package com.project.community.callback;

import android.content.Context;
import android.text.TextUtils;

import com.library.okgo.OkGo;
import com.library.okgo.cache.CacheMode;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.model.HttpParams;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.App;
import com.project.community.R;
import com.project.community.constants.AppConstants;
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
import com.project.community.util.NetworkUtils;

import java.io.File;
import java.util.List;

/**
 * Created by qizfeng on 17/7/3.
 */

public class ServerDaoImpl implements ServerDao {
    private Context mContext;

    public ServerDaoImpl(Context context) {
        mContext = context;
    }

    /**
     * 檢查網絡
     */
    private void checkNet() {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            ToastUtils.showShortToast(mContext, mContext.getString(R.string.network_error));
            return;
        }
    }

    @Override
    public void getNewsList(JsonCallback<BaseResponse<List<NewsModel>>> callback) {
        checkNet();
        OkGo.get("http://api.tianapi.com/mobile/?")
                .tag(mContext)
                .params("key", "480633c4534f495d5ca4723e83f4b16f")
                .params("num", "15")
                .cacheKey("TabFragment_" + mContext.getApplicationContext().getPackageName())       //由于该fragment会被复用,必须保证key唯一,否则数据会发生覆盖
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .execute(callback);
    }

    @Override
    public void getNewsList(String type, JsonCallback<BaseResponse<List<NewsModel>>> callback) {
        checkNet();
        OkGo.get("http://api.tianapi.com/" + type + "/?")
                .tag(mContext)
                .params("key", "480633c4534f495d5ca4723e83f4b16f")
                .params("num", "15")
                .execute(callback);
    }


    @Override
    public void getCode(String telphone, JsonCallback<BaseResponse<List>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_CODE)
                .tag(mContext)
                .params("phone", telphone)
                .execute(callback);

    }

    @Override
    public void doRegister(String username, String phone, String code, String password, String prepPassword, String idcard, String sysOrg, JsonCallback<BaseResponse<UserModel>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_REGISTER)
                .tag(mContext)
                .params("username", phone)
                .params("name", username)
                .params("phone", phone)
                .params("code", code)
                .params("password", password)
                .params("prepPassword", prepPassword)
                .params("idCard", idcard)
                .params("orgCode", "650422100007000")
                .execute(callback);
    }

    @Override
    public void doLogin(String username, String password, JsonCallback<BaseResponse<UserModel>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_LOGIN)
                .tag(mContext)
                .params("username", username)
                .params("password", password)
                .params("mobileLogin", "true")
                .execute(callback);
    }

    @Override
    public void doForgetPass(String phone, String code, String password, String prepPassword, JsonCallback<BaseResponse<List>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_FORGET_PASS)
                .tag(mContext)
                .params("phone", phone)
                .params("code", code)
                .params("password", password)
                .params("prepPassword", prepPassword)
                .execute(callback);
    }

    @Override
    public void getZhengwuIndexData(String userId, int pageNo, int pageSize, JsonCallback<BaseResponse<ZhengwuIndexResponse>> callback) {
        checkNet();
        HttpParams params = new HttpParams();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        if (!TextUtils.isEmpty(userId)) {
            params.put("userId", userId);
        }
        OkGo.post(AppConstants.URL_ZHENGWU_INDEX)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }


    @Override
    public void getAgreement(JsonCallback<BaseResponse<AgreementResponse>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_AGREEMENT)
                .tag(mContext)
                .execute(callback);
    }

    @Override
    public void getUserInfo(String id, JsonCallback<BaseResponse<UserModel>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_USER_INFO)
                .tag(mContext)
                .params("id", id)
                .execute(callback);
    }

    @Override
    public void doEditUserInfo(String id, String photo, String loginName, String name, String idCard,
                               String roomNo, String isOwner, String occupation, String nation,
                               String religion, String party, JsonCallback<BaseResponse<List>> callback) {
        checkNet();
        HttpParams params = new HttpParams();
        params.put("id", id);
        if (!TextUtils.isEmpty(photo))
            params.put("photo", photo);
        if (!TextUtils.isEmpty(loginName))
            params.put("loginName", loginName);
        if (!TextUtils.isEmpty(name))
            params.put("name", name);
        if (!TextUtils.isEmpty(idCard))
            params.put("idCard", idCard);
        if (!TextUtils.isEmpty(roomNo))
            params.put("roomNo", roomNo);
        if (!TextUtils.isEmpty(isOwner))
            params.put("isOwner", isOwner);
        if (!TextUtils.isEmpty(occupation))
            params.put("occupation", occupation);
        if (!TextUtils.isEmpty(nation))
            params.put("nation", nation);
        if (!TextUtils.isEmpty(religion))
            params.put("religion", religion);
        if (!TextUtils.isEmpty(party))
            params.put("party", party);
        OkGo.post(AppConstants.URL_EDIT_USER_INFO)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void getDictionaryData(String dictType, JsonCallback<BaseResponse<DictionaryResponse>> callback) {
        checkNet();
        OkGo.get(AppConstants.URL_DICTIONARY)
                .tag(mContext)
                .params("dictType", dictType)
                .execute(callback);
    }

    @Override
    public void doModifyPwd(String id, String oldPassword, String password, String prepPassword, JsonCallback<BaseResponse<List>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_MODIFY_PWD)
                .tag(mContext)
                .params("id", id)
                .params("oldPassword", oldPassword)
                .params("password", password)
                .params("prepPassword", prepPassword)
                .execute(callback);
    }

    @Override
    public void uploadFile(File file, JsonCallback<BaseResponse<FileUploadModel>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_UPLOAD_FILE)
                .tag(mContext)
                .params("file", file)
                .execute(callback);
    }

    @Override
    public void uploadUserPhoto(String id, String photo, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_EDIT_USER_INFO)
                .tag(mContext)
                .params("id", id)
                .params("photo", photo)
                .execute(callback);
    }

    @Override
    public void getBannerData(String imageType, String imageAddress, JsonCallback<BaseResponse<BannerResponse>> callback) {
        OkGo.post(AppConstants.URL_BANNER)
                .tag(mContext)
                .params("imageType", imageType)
                .params("imageAddress", imageAddress)
                .execute(callback);
    }

    @Override
    public void submitSuggest(String phone, String title, String context, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_SUGGEST)
                .tag(mContext)
                .params("phone", phone)
                .params("title", title)
                .params("content", context)
                .execute(callback);
    }


    @Override
    public void getWorkGuide(String guideTheme, String guidePart, JsonCallback<BaseResponse<List<GuideModel>>> callback) {
        OkGo.post(AppConstants.URL_GUIDE_LIST)
                .tag(mContext)
                .params("guideTheme", guideTheme)
                .params("guidePart", guidePart)
                .execute(callback);
    }

    @Override
    public void getHotLine(String type, String orgCode, JsonCallback<BaseResponse<List>> callback) {
        String url = AppConstants.URL_ZHENGWU_HOTLINE;
        if ("1".equals(type))
            url = AppConstants.URL_ZHENGWU_HOTLINE;
        else if ("2".equals(type))
            url = AppConstants.URL_WUYE_HOTLINE;
        OkGo.post(url)
                .tag(mContext)
                .params("orgCode", orgCode)
                .execute(callback);
    }

    @Override
    public void getTypeTopic(String userId,int pageNo,int pageSize,String type, JsonCallback<BaseResponse<List<ArticleModel>>> callback) {
        HttpParams params = new HttpParams();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        if (!TextUtils.isEmpty(userId)) {
            params.put("userId", userId);
        }
        params.put("type",type);
        OkGo.post(AppConstants.URL_TYPE_TOPIC)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void getTopicDetail(String userId, String artId, JsonCallback<BaseResponse<ArticleModel>> callback) {
        OkGo.post(AppConstants.URL_TOPIC_DETAIL)
                .tag(mContext)
                .params("userId", userId)
                .params("artId", artId)
                .execute(callback);
    }

    @Override
    public void doCollectTopic(String userId, String artId, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_COLLECT_TOPIC)
                .tag(mContext)
                .params("userId", userId)
                .params("artId", artId)
                .execute(callback);
    }

    @Override
    public void getComments(String artId, JsonCallback<BaseResponse<List<CommentModel>>> callback) {
        OkGo.post(AppConstants.URL_COMMENT_LIST)
                .tag(mContext)
                .params("artId", artId)
                .execute(callback);
    }

    @Override
    public void doComment(String userId, String artId, String content, String targetId, JsonCallback<BaseResponse<List>> callback) {
        HttpParams params = new HttpParams();
        params.put("userId", userId);
        params.put("artId", artId);
        params.put("content", content);
        if (!TextUtils.isEmpty(targetId))
            params.put("targetId", targetId);
        OkGo.post(AppConstants.URL_COMMENT)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void getDistList(JsonCallback<BaseResponse<List<DistModel>>> callback) {
        OkGo.post(AppConstants.URL_DIST_LIST)
                .tag(mContext)
                .execute(callback);
    }

    @Override
    public void doDeleteComment(String userId, String commentId, int type, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_COMMENT_DELETE)
                .tag(mContext)
                .params("userId", userId)
                .params("commentId", commentId)
                .params("type", type)
                .execute(callback);
    }

    @Override
    public void doSearch(String type, String keywords, JsonCallback<BaseResponse<List<SearchModel>>> callback) {
        String url = AppConstants.URL_SEARCH_ZHENGWU;
        if ("0".equals(type))
            url = AppConstants.URL_SEARCH_ZHENGWU;
        else if ("1".equals(type))
            url = AppConstants.URL_SEARCH_WUYE;
        HttpParams params = new HttpParams();
        params.put("type", type);
        params.put("keywords", keywords);
        OkGo.post(url)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void getWuyeIndexData(String userId,int pageNo,int pageSize,int type, JsonCallback<BaseResponse<WuyeIndexResponse>> callback) {
        HttpParams params = new HttpParams();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        if (!TextUtils.isEmpty(userId)) {
            params.put("userId", userId);
        }
        params.put("type",type);
        OkGo.post(AppConstants.URL_WUYE_INDEX)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }
}
