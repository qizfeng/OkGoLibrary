package com.project.community.ui.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ValidateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.model.FamilyPersonModel;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/9/18.
 * 人员信息
 */

public class CommunityPersonActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.iv_tag)
    ImageView mIvTag;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_tag1)
    TextView mTvTag1;
    @Bind(R.id.tv_tag2)
    TextView mTvTag2;
    @Bind(R.id.tv_tag3)
    TextView mTvTag3;
    @Bind(R.id.tv_relative)
    TextView mTvRelative;
    @Bind(R.id.tv_position)
    TextView mTvPosition;
    @Bind(R.id.tv_gender)
    TextView mTvGender;
    @Bind(R.id.tv_idcard)
    TextView mTvIDCard;
    @Bind(R.id.tv_age)
    TextView mTvAge;
    @Bind(R.id.tv_birthday)
    TextView mTvBirthday;
    @Bind(R.id.tv_nation)
    TextView mTvNation;
    @Bind(R.id.tv_religion)
    TextView mTvReligion;
    @Bind(R.id.tv_party)
    TextView mTvParty;
    @Bind(R.id.tv_mobile)
    TextView mTvPhone;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    private String memberId;//成员id
    private FamilyPersonModel mData = new FamilyPersonModel();
    public static void startActivity(Context context,Bundle bundle){
        Intent intent = new Intent(context,CommunityPersonActivity.class);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_person);
        initToolBar(mToolBar,mTvTitle,true,getString(R.string.txt_map_person_info),R.mipmap.iv_back);
        loadData();
    }
    private void loadData(){
        memberId=getIntent().getBundleExtra("bundle").getString("memberId");
        serverDao.getCommunityFamilyPersonInfo(memberId, new DialogCallback<BaseResponse<FamilyPersonModel>>(this) {
            @Override
            public void onSuccess(BaseResponse<FamilyPersonModel> baseResponse, Call call, Response response) {
                mData=baseResponse.retData;
                bindData(mData);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    private void bindData(FamilyPersonModel model){
        if(model!=null){
            if(!TextUtils.isEmpty(model.realName))
                mTvName.setText(model.realName);
            else
                mTvName.setText("");
            Glide.with(this)
                    .load(AppConstants.HOST+""+model.photo)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .placeholder(R.mipmap.d54_tx)
                    .into(mIvHeader);
            if("户主".equals(model.headRelation))
                mIvTag.setVisibility(View.VISIBLE);
            else
                mIvTag.setVisibility(View.GONE);
            if(TextUtils.isEmpty(model.headRelation))
                mTvRelative.setText("");
            else
                mTvRelative.setText(model.headRelation);
            if("1".equals(model.sex)){
                mTvGender.setText(getString(R.string.txt_gender_male));
            }else {
                mTvGender.setText(getString(R.string.txt_gender_male));
            }
            mTvPosition.setText(model.occupation);
            if (TextUtils.isEmpty(model.idNumber)){
                mTvIDCard.setText("");
                mTvAge.setText("");
                mTvBirthday.setText("");
            }else {
                mTvIDCard.setText(model.idNumber);
                mTvAge.setText(ValidateUtil.getUserAgeByCardId(model.idNumber));
                mTvBirthday.setText(ValidateUtil.getUserBrithdayByCardId(model.idNumber));
            }

            if (TextUtils.isEmpty(model.nation))
                mTvNation.setText("");
            else
                mTvNation.setText(model.nation);
            if (TextUtils.isEmpty(model.religion))
                mTvReligion.setText("");
            else
                mTvReligion.setText(model.religion);
            if(TextUtils.isEmpty(model.party))
                mTvParty.setText("");
            else
                mTvParty.setText(model.party);
            if (TextUtils.isEmpty(model.phone))
                mTvPhone.setText("");
            else
                mTvPhone.setText(model.phone);
            if(TextUtils.isEmpty(model.roomAddress))
                mTvAddress.setText("");
            else
                mTvAddress.setText(model.roomAddress);

            if (!TextUtils.isEmpty(model.memberTag)) {
                String[] tagArr = model.memberTag.split(",");
                if(tagArr.length==0){
                    mTvTag1.setVisibility(View.GONE);
                    mTvTag2.setVisibility(View.GONE);
                    mTvTag3.setVisibility(View.GONE);
                }else if (tagArr.length==1){
                    mTvTag1.setVisibility(View.VISIBLE);
                    mTvTag1.setText(tagArr[0]);
                    mTvTag2.setVisibility(View.GONE);
                    mTvTag3.setVisibility(View.GONE);
                }else if(tagArr.length==2){
                    mTvTag1.setVisibility(View.VISIBLE);
                    mTvTag1.setText(tagArr[0]);
                    mTvTag2.setVisibility(View.VISIBLE);
                    mTvTag2.setText(tagArr[1]);
                    mTvTag3.setVisibility(View.GONE);
                }else if(tagArr.length==3){
                    mTvTag1.setVisibility(View.VISIBLE);
                    mTvTag1.setText(tagArr[0]);
                    mTvTag2.setVisibility(View.VISIBLE);
                    mTvTag2.setText(tagArr[1]);
                    mTvTag3.setVisibility(View.VISIBLE);
                    mTvTag3.setText(tagArr[2]);
                }
            }else {
                mTvTag1.setVisibility(View.GONE);
                mTvTag2.setVisibility(View.GONE);
                mTvTag3.setVisibility(View.GONE);
            }
        }
    }

}
