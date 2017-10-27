package com.project.community.callback;

import android.content.Context;
import android.text.TextUtils;

import com.library.okgo.OkGo;
import com.library.okgo.cache.CacheMode;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.model.HttpParams;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.bean.AddressListBean;
import com.project.community.bean.CommunityBean;
import com.project.community.bean.ForumListBean;
import com.project.community.bean.GovernmentBean;
import com.project.community.bean.MerchantBean;
import com.project.community.constants.AppConstants;
import com.project.community.model.AgreementResponse;
import com.project.community.model.ArticleModel;
import com.project.community.model.AuditStatusModel;
import com.project.community.model.BannerResponse;
import com.project.community.model.CommentResponse;
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
import com.project.community.model.GoodsManagerModel;
import com.project.community.model.GuideModel;
import com.project.community.model.HotlineModel;
import com.project.community.model.HouseModel;
import com.project.community.model.NewsModel;
import com.project.community.model.PaymentHouseHistroyModel;
import com.project.community.model.PaymentInfoModel;
import com.project.community.model.PaymentWayModel;
import com.project.community.model.SearchModel;
import com.project.community.model.ShopIndexModel;
import com.project.community.model.ShopModel;
import com.project.community.model.ShoppingCartModel;
import com.project.community.model.ShopResponse;
import com.project.community.model.SubdomainAccountModel;
import com.project.community.model.UserModel;
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
                .params("username", phone)//15210213731
                .params("name", username)//齐志峰
                .params("phone", phone)//15210213731
                .params("code", code)
                .params("password", password)
                .params("prepPassword", prepPassword)
                .params("idCard", idcard)
                .params("orgCode", sysOrg)//650422100007000
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
        checkNet();
        OkGo.post(AppConstants.URL_EDIT_USER_INFO)
                .tag(mContext)
                .params("id", id)
                .params("photo", photo)
                .execute(callback);
    }

    @Override
    public void getBannerData(String imageType, String imageAddress, JsonCallback<BaseResponse<BannerResponse>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_BANNER)
                .tag(mContext)
                .params("imageType", imageType)
                .params("imageAddress", imageAddress)
                .execute(callback);
    }

    @Override
    public void submitSuggest(String userId, String orgCode, String phone, String title, String context, JsonCallback<BaseResponse<List>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_SUGGEST)
                .tag(mContext)
                .params("userId", userId)
                .params("orgCode", orgCode)
                .params("phone", phone)
                .params("title", title)
                .params("content", context)
                .execute(callback);
    }


    @Override
    public void getWorkGuide(String guideTheme, String guidePart, JsonCallback<BaseResponse<List<GuideModel>>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_GUIDE_LIST)
                .tag(mContext)
                .params("guideTheme", guideTheme)
                .params("guidePart", guidePart)
                .execute(callback);
    }

    @Override
    public void searchGuide(String keywords, JsonCallback<BaseResponse<List<GuideModel>>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_GUIDE_SEARCH)
                .tag(mContext)
                .params("keywords", keywords)
                .execute(callback);
    }

    @Override
    public void getHotLine(String type, String orgCode, JsonCallback<BaseResponse<List<HotlineModel>>> callback) {
        checkNet();
        String url = AppConstants.URL_ZHENGWU_HOTLINE;
        if ("1".equals(type))
            url = AppConstants.URL_ZHENGWU_HOTLINE;
        else if ("2".equals(type))
            url = AppConstants.URL_WUYE_HOTLINE;
//        if (TextUtils.isEmpty(orgCode))
//            orgCode = "650422100007000";
        OkGo.post(url)
                .tag(mContext)
                .params("orgCode", orgCode)
                .params("type", type)
                .execute(callback);
    }

    @Override
    public void getTypeTopic(String userId, int pageNo, int pageSize, String type, JsonCallback<BaseResponse<List<ArticleModel>>> callback) {
        checkNet();
        HttpParams params = new HttpParams();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        if (!TextUtils.isEmpty(userId)) {
            params.put("userId", userId);
        }
        params.put("type", type);
        OkGo.post(AppConstants.URL_TYPE_TOPIC)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void getTopicDetail(String userId, String artId, JsonCallback<BaseResponse<ArticleModel>> callback) {
        checkNet();
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
    public void getComments(String artId, int page, int pageSize, JsonCallback<BaseResponse<CommentResponse>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_COMMENT_LIST)
                .tag(mContext)
                .params("artId", artId)
                .params("pageNo", page)
                .params("pageSize", pageSize)
                .execute(callback);
    }

    @Override
    public void doComment(String userId, String artId, String content, String targetId, JsonCallback<BaseResponse<List>> callback) {
        checkNet();
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
        checkNet();
        OkGo.post(AppConstants.URL_DIST_LIST)
                .tag(mContext)
                .execute(callback);
    }

    @Override
    public void doDeleteComment(String userId, String commentId, int type, JsonCallback<BaseResponse<List>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_COMMENT_DELETE)
                .tag(mContext)
                .params("userId", userId)
                .params("commentId", commentId)
                .params("type", type)
                .execute(callback);
    }

    @Override
    public void doSearch(String type, String keywords, JsonCallback<BaseResponse<List<SearchModel>>> callback) {
        checkNet();
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
    public void getWuyeIndexData(String userId, int pageNo, int pageSize, int type, JsonCallback<BaseResponse<WuyeIndexResponse>> callback) {
        checkNet();
        HttpParams params = new HttpParams();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        if (!TextUtils.isEmpty(userId)) {
            params.put("userId", userId);
        }
        params.put("type", type);
        OkGo.post(AppConstants.URL_WUYE_INDEX)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void getPaymentWay(JsonCallback<BaseResponse<List<PaymentWayModel>>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_JIAOFEI_TYPE)
                .tag(mContext)
                .execute(callback);
    }

    @Override
    public void getPaymentNoData(String userId, JsonCallback<BaseResponse<List<PaymentHouseHistroyModel>>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_JIAOFEI_NO_LIST)
                .tag(mContext)
                .params("userId", userId)
                .execute(callback);
    }

    @Override
    public void selectHouseInfo(String roomNo, JsonCallback<BaseResponse<HouseModel>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_SELECT_HOUSE)
                .tag(mContext)
                .params("roomNo", roomNo)
                .execute(callback);
    }

    @Override
    public void addHouse(String userId, String roomNo, JsonCallback<BaseResponse<HouseModel>> callback) {
        OkGo.post(AppConstants.URL_ADD_HOUSE)
                .tag(mContext)
                .params("userId", userId)
                .params("roomNo", roomNo)
                .execute(callback);
    }

    @Override
    public void deleteHouse(String userId, String payRoomId, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_DELETE_HOUSE)
                .tag(mContext)
                .params("userId", userId)
                .params("payRoomId", payRoomId)
                .execute(callback);
    }

    @Override
    public void getPaymentDetailInfo(String paymentType, String roomNo, JsonCallback<BaseResponse<PaymentInfoModel>> callback) {
        OkGo.post(AppConstants.URL_SELECT_PAY_INFO)
                .tag(mContext)
                .params("paymentType", paymentType)
                .params("roomNo", roomNo)
                .execute(callback);
    }

    @Override
    public void getFamilyListInfo(String userId, String phone, String roomNo, JsonCallback<BaseResponse<List<FamilyModel>>> callback) {
        HttpParams params = new HttpParams();
        params.put("userId", userId);
        params.put("phone", phone);
        params.put("roomNo", roomNo);
        OkGo.post(AppConstants.URL_FAMILY_LIST)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void getFamilyInfo(String userId, String phone, String roomNo, String familyId, JsonCallback<BaseResponse<List<FamilyModel>>> callback) {
        HttpParams params = new HttpParams();
        params.put("userId", userId);
        params.put("phone", phone);
        params.put("roomNo", roomNo);
        params.put("familyId", familyId);
        OkGo.post(AppConstants.URL_FAMILY_LIST)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void checkOwner(String roomNo, String userId, String phone, JsonCallback<BaseResponse<AuditStatusModel>> callback) {
        OkGo.post(AppConstants.URL_CHECK_OWNER)
                .tag(mContext)
                .params("userId", userId)
                .params("roomNo", roomNo)
                .params("phone", phone)
                .execute(callback);
    }

    @Override
    public void addFamily(String userId, String roomNo, String familyName, JsonCallback<BaseResponse<HouseModel>> callback) {
        OkGo.post(AppConstants.URL_ADD_FAMILY)
                .tag(mContext)
                .params("userId", userId)
                .params("roomNo", roomNo)
                .params("familyName", familyName)
                .execute(callback);
    }

    @Override
    public void addPerson(HttpParams params, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_ADD_PERSON)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }

    @Override
    public void deletePerson(String userId, String memberId, String roomNo, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_DELETE_PERSON)
                .tag(mContext)
                .params("userId", userId)
                .params("memberId", memberId)
                .params("roomNo", roomNo)
                .execute(callback);
    }

    @Override
    public void getPerson(String memberId, JsonCallback<BaseResponse<FamilyPersonModel>> callback) {
        OkGo.post(AppConstants.URL_GET_PERSON)
                .tag(mContext)
                .params("memberId", memberId)
                .execute(callback);
    }

    @Override
    public void deleteFamily(String userId, String roomNo, String familyId, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_DELETE_FAMILY)
                .tag(mContext)
                .params("familyId", familyId)
                .params("userId", userId)
                .params("roomNo", roomNo)
                .execute(callback);
    }


    @Override
    public void auditFamily(String userId, String familyName, String familyNo, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_FAMILY_AUDIT)
                .tag(mContext)
                .params("userId", userId)
                .params("familyName", familyName)
                .params("roomNo", familyNo)
                .execute(callback);
    }

    @Override
    public void getPayOrderInfo(String userId, String paymentId, JsonCallback<BaseResponse<String>> callback) {
        OkGo.post(AppConstants.URL_GET_PAY_ORDER)
                .tag(mContext)
                .params("userId", userId)
                .params("paymentId", paymentId)
                .execute(callback);
    }

    @Override
    public void getCommunityList(String userId, String orgCode, JsonCallback<BaseResponse<List<CommunityModel>>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_LIST)
                .tag(mContext)
                .params("userId", userId)
                .params("orgCode", orgCode)
                .execute(callback);
    }

    @Override
    public void getCommunityCensusInfo(String userId, String orgCode, JsonCallback<BaseResponse<List<CommunityCensusModel>>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_CENSUS)
                .tag(mContext)
                .params("userId", userId)
                .params("orgCode", orgCode)
                .execute(callback);
    }

    @Override
    public void getCommunityFamilyPersonList(String userId, String unit, String floor, JsonCallback<BaseResponse<List<CommunityFamilyModel>>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_MEMBER_LIST)
                .tag(mContext)
                .params("userId", userId)
                .params("unit", unit)
                .params("floor", floor)
                .execute(callback);
    }

    @Override
    public void getCommunityFamilyPersonInfo(String memberId, JsonCallback<BaseResponse<FamilyPersonModel>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_MEMBER_INFO)
                .tag(mContext)
                .params("memberId", memberId)
                .execute(callback);
    }

    @Override
    public void getCommunityShopList(String userId, String coordinate, String distance, String auditStatus, JsonCallback<BaseResponse<List<ShopModel>>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_SHOP_LIST)
                .tag(mContext)
                .params("userId", userId)
                .params("coordinate", coordinate)
                .params("distance", distance)
                .params("auditStatus", auditStatus)
                .execute(callback);
    }

    @Override
    public void getCommunityShopFilter(JsonCallback<BaseResponse<CommunityShopFilterModel>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_SHOP_FILTER)
                .tag(mContext)
                .execute(callback);
    }

    @Override
    public void getCommunityPersonFilter(JsonCallback<BaseResponse<CommunityPersonFilterModel>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_PERSON_FILTER)
                .tag(mContext)
                .execute(callback);
    }

    @Override
    public void getCommunityPersonList(String userId, String coordinate, String distance, String memberTag, JsonCallback<BaseResponse<List<FamilyPersonModel>>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_PERSON_LIST)
                .tag(mContext)
                .params("userId", userId)
                .params("coordinate", coordinate)
                .params("distance", distance)
                .params("memberTag", memberTag)
                .execute(callback);
    }

    @Override
    public void getCommunityDeviceFilter(JsonCallback<BaseResponse<CommunityDeviceFilterModel>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_DEVICE_FILTER)
                .tag(mContext)
                .execute(callback);
    }

    @Override
    public void getCommunityDeviceList(String userId, String coordinate, String userCode, String distance, String facilitiesType, JsonCallback<BaseResponse<List<DeviceModel>>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_DEVICE_LIST)
                .tag(mContext)
                .params("userId", userId)
                .params("coordinate", coordinate)
                .params("orgCode", userCode)
                .params("distance", distance)
                .params("facilitiesType", facilitiesType)
                .execute(callback);
    }

    @Override
    public void getCommunityShopDetail(String userId, String shopId, JsonCallback<BaseResponse<ShopModel>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_SHOP_DETAIL)
                .tag(mContext)
                .params("userId", userId)
                .params("shopId", shopId)
                .execute(callback);
    }

    @Override
    public void getCommunityPersonDetail(String userId, String id, JsonCallback<BaseResponse<FamilyPersonModel>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_PERSON_DETAIL)
                .tag(mContext)
                .params("userId", userId)
                .params("id", id)
                .execute(callback);
    }

    @Override
    public void getCommunityDeviceDetail(String userId, String faiId, JsonCallback<BaseResponse<DeviceModel>> callback) {
        OkGo.post(AppConstants.URL_COMMUNITY_DEVICE_DETAIL)
                .tag(mContext)
                .params("userId", userId)
                .params("faiId", faiId)
                .execute(callback);
    }

    @Override
    public void doUploadLocation(String userId, String coordinate, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_UPLOAD_LATLNG)
                .tag(mContext)
                .params("userId", userId)
                .params("coordinate", coordinate)
                .execute(callback);
    }

    @Override
    public void getMinshengIndexData(String userId,String coordinate, int pageNo, int pageSize, JsonCallback<BaseResponse<ShopResponse>> callback) {
        OkGo.post(AppConstants.URL_MINSHENG_INDEX)
                .tag(mContext)
                .params("userId",userId)
                .params("coordinate", coordinate)
                .params("pageNo", pageNo)
                .params("pageSize", pageSize)
                .execute(callback);
    }
    @Override
    public void propShops(
            String id,
            String userId,
            double longitude,
            double latitude,
            String shopsName,
            String shopPhoto,
            String contactName,
            String businessAddress, String shopsCategory,
            String mainBusiness, String entName, String licenseNo, String licensePositive, String licenseReverse, String legalPerson, String legalCardPositive,
            String legalCardReverse, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_APPLY_STORE)
                .tag(mContext)
                .params("userId", userId)
                .params("longitude", longitude)
                .params("latitude", latitude)
                .params("shopsName", shopsName)
                .params("shopPhoto", shopPhoto)
                .params("contactName", contactName)
                .params("businessAddress", businessAddress)
                .params("shopsCategory", shopsCategory)
                .params("mainBusiness", mainBusiness)
                .params("entName", entName)
                .params("licenseNo", licenseNo)
                .params("licensePositive", licensePositive)
                .params("licenseReverse", licenseReverse)
                .params("legalPerson", legalPerson)
                .params("legalCardPositive", legalCardPositive)
                .params("legalCardReverse", legalCardReverse)
                .execute(callback);
    }

    @Override
    public void addAddress(String userId, String consignee,
                           String contactPhone, String userArea,
                           String userStreet, String address,
                           String addressId, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_ADD_ADDRESS)
                .tag(mContext)
                .params("userId", userId)
                .params("consignee", consignee)
                .params("contactPhone", contactPhone)
                .params("userArea", userArea)
                .params("userStreet", userStreet)
                .params("address", address)
                .params("addressId", addressId)
                .execute(callback);
    }
    @Override
    public void updateIsOpen(String userId, JsonCallback<BaseResponse<List<List>>> callback) {
        OkGo.post(AppConstants.URL_UPDATEISOPEN)
                .tag(mContext)
                .params("userId", userId)
                .execute(callback);
    }

    @Override
    public void shopIndex(String userId, JsonCallback<BaseResponse<ShopIndexModel>> callback) {
        OkGo.get(AppConstants.URL_SHOPINDEX)
                .tag(mContext)
                .params("userId", userId)
                .execute(callback);
    }


    @Override
    public void deleteAddress(String userId, String addressId, JsonCallback<BaseResponse<List<String>>> callback) {
        OkGo.post(AppConstants.URL_DEL_ADDRESS)
                .tag(mContext)
                .params("userId", userId)
                .params("addressId", addressId)
                .execute(callback);
    }

    @Override
    public void setDefaultAddress(String userId, String addressId, JsonCallback<BaseResponse<List<String>>> callback) {
        OkGo.post(AppConstants.URL_SET_DEFAULT)
                .tag(mContext)
                .params("userId", userId)
                .params("addressId", addressId)
                .execute(callback);
    }

    @Override
    public void getGovernmentList(String userId, String type, String pageNo, String pageSize,
                                  JsonCallback<BaseResponse<List<GovernmentBean>>> callback) {
        OkGo.get(AppConstants.URL_GET_COLLECTION)
                .tag(mContext)
                .params("userId", userId)
                .params("type", type)
                .params("pageNo", pageNo)
                .params("pageSize", pageSize)
                .execute(callback);
    }

    @Override
    public void getCommunityList(String userId, String type, String pageNo, String pageSize, JsonCallback<BaseResponse<List<CommunityBean>>> callback) {
        OkGo.get(AppConstants.URL_GET_COLLECTION)
                .tag(mContext)
                .params("userId", userId)
                .params("type", type)
                .params("pageNo", pageNo)
                .params("pageSize", pageSize)
                .execute(callback);
    }

    @Override
    public void getForumList(String userId, String type, String pageNo, String pageSize, JsonCallback<BaseResponse<List<ForumListBean>>> callback) {
        OkGo.get(AppConstants.URL_GET_COLLECTION)
                .tag(mContext)
                .params("userId", userId)
                .params("type", type)
                .params("pageNo", pageNo)
                .params("pageSize", pageSize)
                .execute(callback);
    }

    @Override
    public void getMerchantList(String userId, String type, String pageNo, String pageSize, JsonCallback<BaseResponse<List<MerchantBean>>> callback) {
        OkGo.get(AppConstants.URL_GET_COLLECTION)
                .tag(mContext)
                .params("userId", userId)
                .params("type", type)
                .params("pageNo", pageNo)
                .params("pageSize", pageSize)
                .execute(callback);
    }

    @Override
    public void getAddressList(String userId, JsonCallback<BaseResponse<List<AddressListBean>>> callback) {
        OkGo.get(AppConstants.URL_GET_ADDRESS_LIST)
                .tag(mContext)
                .params("userId", userId)
                .execute(callback);
    }


    @Override
    public void getPropShops(String userId, JsonCallback<BaseResponse<ShopModel>> callback) {
        OkGo.get(AppConstants.URL_PROPSHOPS)
                .tag(mContext)
                .params("userId", userId)
                .execute(callback);
    }

    @Override
    public void getSubdomainsAccount(String userId, JsonCallback<BaseResponse<List<SubdomainAccountModel>>> callback) {
        OkGo.get(AppConstants.URL_SUBDOMAINACCOUNTLIST)
                .tag(mContext)
                .params("userId", userId)
                .execute(callback);
    }

    @Override
    public void delSubdomainsAccount(String userId, String childId, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.DELETE_SUBDOMAINACCOUNT)
                .tag(mContext)
                .params("userId", userId)
                .params("childId", childId)
                .execute(callback);
    }

    @Override
    public void addGoods(String userId,String goodId, String shopId, String name, String images, String description, String price, String originalPrice, String unit, String stock, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_ADDGOODS)
                .tag(mContext)
                .params("userId", userId)
                .params("goodId", goodId)
                .params("shopId", shopId)
                .params("name", name)
                .params("images", images)
                .params("description", description)
                .params("price", price)
                .params("originalPrice", originalPrice)
                .params("unit", unit)
                .params("stock", stock)
                .execute(callback);
    }

    @Override
    public void addSubdomainsAccount(String userId,String id, String shopId, String name, String phone,int sumMoney, int addGoods, int goodsMge, int orderMge, int shopInfo, int sumOrder, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_ADDSUBDOMAINACCOUNT)
                .tag(mContext)
                .params("userId", userId)
                .params("id", id)
                .params("shopId", shopId)
                .params("name", name)
                .params("phone", phone)
                .params("sumMoney", sumMoney)
                .params("addGoods", addGoods)
                .params("goodsMge", goodsMge)
                .params("orderMge", orderMge)
                .params("shopInfo", shopInfo)
                .params("sumOrder", sumOrder)
                .execute(callback);
    }

    @Override
    public void getSubdomainsAccountDetail(String childId, JsonCallback<BaseResponse<SubdomainAccountModel>> callback) {
        OkGo.get(AppConstants.URL_GETSUBDOMAINACCOUNTDETAIL)
                .tag(mContext)
                .params("childId", childId)
                .execute(callback);
    }

    @Override
    public void getGoodsManagerList(String userId, String status, JsonCallback<BaseResponse<List<GoodsManagerModel>>> callback) {
        OkGo.get(AppConstants.URL_GETGOODSMANAGER)
                .tag(mContext)
                .params("userId", userId)
                .params("status", status)
                .execute(callback);
    }


    @Override
    public void upDownGoods(String userId, String goodId, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_UPDOWNGOODS)
                .tag(mContext)
                .params("userId", userId)
                .params("goodId", goodId)
                .execute(callback);
    }

    @Override
    public void getCartList(String userId, JsonCallback<BaseResponse<List<ShoppingCartModel>>> callback) {
        OkGo.get(AppConstants.URL_GETCARTLIST)
                .tag(mContext)
                .params("userId", userId)
                .execute(callback);
    }

    @Override
    public void delCart(String cartId, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_DELCART)
                .tag(mContext)
                .params("cartId", cartId)
                .execute(callback);
    }

    @Override
    public void delGoods(String userId, String goodId, JsonCallback<BaseResponse<List>> callback) {
        OkGo.post(AppConstants.URL_DELGOODS)
                .tag(mContext)
                .params("userId", userId)
                .params("goodId", goodId)
                .execute(callback);
    }

    @Override
    public void getEmploymentData(String userId, int pageNo, int pageSize, String type, String categoryType, JsonCallback<BaseResponse<List<ArticleModel>>> callback) {
        checkNet();
        HttpParams params = new HttpParams();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        if (!TextUtils.isEmpty(userId)) {
            params.put("userId", userId);
        }
        params.put("type", type);
        params.put("categoryType","2");
        OkGo.post(AppConstants.URL_TYPE_TOPIC)
                .tag(mContext)
                .params(params)
                .execute(callback);
    }
    @Override
    public void uploadUDID(String userId, String machineCode, JsonCallback<BaseResponse<List>> callback) {
        checkNet();
        OkGo.post(AppConstants.URL_UPLOAD_UDID)
                .tag(mContext)
                .params("userId", userId)
                .params("machineCode", machineCode)
                .execute(callback);
    }
//    @Override
//    public void addAddress(String userId, String consignee,
//                           String contactPhone, String userArea,
//                           String userStreet, String address,
//                           String addressId,
//                           JsonCallback<BaseResponse> callback) {
//        OkGo.post(AppConstants.URL_ADD_ADDRESS)
//                .tag(mContext)
//                .params("userId", userId)
//                .params("consignee", consignee)
//                .params("contactPhone", contactPhone)
//                .params("userArea", userArea)
//                .params("userStreet", userStreet)
//                .params("address", address)
//                .params("addressId", addressId)
//                .execute(callback);
//    }

}
