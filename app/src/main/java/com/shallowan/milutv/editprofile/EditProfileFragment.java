package com.shallowan.milutv.editprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.shallowan.milutv.R;
import com.shallowan.milutv.login.LoginActivity;
import com.shallowan.milutv.main.MainActivity;
import com.shallowan.milutv.utils.ImgUtils;
import com.shallowan.milutv.utils.PicChooserHelper;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendGenderType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.util.Map;


/**
 * Created by ShallowAn.
 */


public class EditProfileFragment extends Fragment {

    private static final int FROM_CAMERA = 2;
    private static final int FROM_ALBUM = 1;
    private static final int CROP = 0;
    private TIMUserProfile mUserProfile;

    private Toolbar mTitlebar;
    private View mAvatarView;
    private ImageView mAvatarImg;
    private ProfileEdit mNickNameEdt;
    private ProfileEdit mGenderEdt;
    private ProfileEdit mSignEdt;

    private ProfileTextView mIdView;
    private ProfileTextView mLevelView;
    private ProfileTextView mGetNumsView;
    private ProfileTextView mSendNumsView;

    private Button mCompleteBtn;

    private PicChooserHelper mPicChooserHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        findAllViews(mainView);
        setListeners();
        setTitleBar();
        setIconKey();//设置字段和icon
        getSelfInfo();

        return mainView;
    }

    private void getSelfInfo() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(), "获取信息失败：" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                //获取自己信息成功
                mUserProfile = timUserProfile;
                updateViews(timUserProfile);
            }
        });
    }

    private void updateViews(TIMUserProfile timUserProfile) {
        //更新界面
        String faceUrl = timUserProfile.getFaceUrl();
        if (TextUtils.isEmpty(faceUrl)) {
            ImgUtils.loadRound(R.drawable.default_avatar, mAvatarImg);
        } else {
            ImgUtils.loadRound(faceUrl, mAvatarImg);
        }
        mNickNameEdt.updateValue(timUserProfile.getNickName());
        long genderValue = timUserProfile.getGender().getValue();
        String genderStr = genderValue == 1 ? "男" : "女";
        mGenderEdt.updateValue(genderStr);
        mSignEdt.updateValue(timUserProfile.getSelfSignature());
        mIdView.updateValue(timUserProfile.getIdentifier());

        Map<String, byte[]> customInfo = timUserProfile.getCustomInfo();
        mLevelView.updateValue(getValue(customInfo, CustomProfile.CUSTOM_LEVEL, "0"));
        mGetNumsView.updateValue(getValue(customInfo, CustomProfile.CUSTOM_GET, "0"));
        mSendNumsView.updateValue(getValue(customInfo, CustomProfile.CUSTOM_SEND, "0"));
    }

    private String getValue(Map<String, byte[]> customInfo, String key, String defaultValue) {
        if (customInfo != null) {
            byte[] valueBytes = customInfo.get(key);
            if (valueBytes != null) {
                return new String(valueBytes);
            }
        }
        return defaultValue;
    }

    private void setIconKey() {
        mNickNameEdt.set(R.drawable.ic_info_nickname, "昵称", "");
        mGenderEdt.set(R.drawable.ic_info_gender, "性别", "");
        mSignEdt.set(R.drawable.ic_info_sign, "签名", "这个人很懒~还没有签名~");
        mIdView.set(R.drawable.ic_info_id, "ID", "");
        mLevelView.set(R.drawable.ic_info_level, "等级", "0");
        mGetNumsView.set(R.drawable.ic_info_get, "获得票数", "0");
        mSendNumsView.set(R.drawable.ic_info_send, "送出票数", "0");
    }

    private void findAllViews(View view) {
        mTitlebar = (Toolbar) view.findViewById(R.id.title_bar);

        mAvatarView = view.findViewById(R.id.avatar);
        mAvatarImg = (ImageView) view.findViewById(R.id.avatar_img);
        mNickNameEdt = (ProfileEdit) view.findViewById(R.id.nick_name);
        mGenderEdt = (ProfileEdit) view.findViewById(R.id.gender);
        mSignEdt = (ProfileEdit) view.findViewById(R.id.sign);


        mIdView = (ProfileTextView) view.findViewById(R.id.id);
        mLevelView = (ProfileTextView) view.findViewById(R.id.level);
        mGetNumsView = (ProfileTextView) view.findViewById(R.id.get_nums);
        mSendNumsView = (ProfileTextView) view.findViewById(R.id.send_nums);

        mCompleteBtn = (Button) view.findViewById(R.id.complete);
    }

    private void setListeners() {
        mAvatarView.setOnClickListener(clickListener);
        mNickNameEdt.setOnClickListener(clickListener);
        mGenderEdt.setOnClickListener(clickListener);
        mSignEdt.setOnClickListener(clickListener);
        mCompleteBtn.setOnClickListener(clickListener);
    }

    private void setTitleBar() {
        mTitlebar.setTitle("编辑个人信息");
        mTitlebar.setTitleTextColor(Color.WHITE);
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(mTitlebar);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.avatar) {
                //修改头像
                choosePic();
            } else if (id == R.id.nick_name) {
                //修改昵称
                showEditNickNameDialog();
            } else if (id == R.id.gender) {
                //修改性别
                showEditGenderDialog();
            } else if (id == R.id.sign) {
                //修改签名
                showEditSignDialog();
            } else if (id == R.id.complete) {
                LemonHello.getInformationHello("您确定要注销吗？", "注销登录后您将无法接收到当前用户的所有推送消息。")
                        .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                helloView.hide();
                            }
                        }))
                        .addAction(new LemonHelloAction("确定", Color.RED, new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                helloView.hide();
                                //完成，点击跳转到主界面
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }
                        }))
                        .show(getActivity());
            }
        }
    };

    private void choosePic() {
        if (mPicChooserHelper == null) {
            mPicChooserHelper = new PicChooserHelper(this, PicChooserHelper.PicType.Avatar);
            mPicChooserHelper.setOnChooseResultListener(new PicChooserHelper.OnChooseResultListener() {
                @Override
                public void onSuccess(String url) {
                    updateAvatar(url);
                }

                @Override
                public void onFail(String msg) {
                    TastyToast.makeText(getActivity().getApplicationContext(), "选择失败：" + msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            });
        }

        mPicChooserHelper.showPicChooserDialog();
    }

    private void showEditSignDialog() {
        EditStrProfileDialog dialog = new EditStrProfileDialog(getActivity());
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, final String content) {
                TIMFriendshipManager.getInstance().setSelfSignature(content, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        TastyToast.makeText(getActivity().getApplicationContext(), "更新签名失败：" + s, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                    @Override
                    public void onSuccess() {
                        //更新成功
                        getSelfInfo();
                    }
                });
            }
        });
        dialog.show("签名", R.drawable.ic_info_sign, mSignEdt.getValue());
    }

    private void showEditGenderDialog() {
        EditGenderDialog dialog = new EditGenderDialog(getActivity());
        dialog.setOnChangeGenderListener(new EditGenderDialog.OnChangeGenderListener() {
            @Override
            public void onChangeGender(boolean isMale) {
                TIMFriendGenderType gender = isMale ? TIMFriendGenderType.Male : TIMFriendGenderType.Female;
                TIMFriendshipManager.getInstance().setGender(gender, new TIMCallBack() {

                    @Override
                    public void onError(int i, String s) {
                        TastyToast.makeText(getActivity().getApplicationContext(), "更新性别失败：" + s, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                    @Override
                    public void onSuccess() {
                        //更新成功
                        getSelfInfo();
                    }
                });
            }
        });
        dialog.show(mGenderEdt.getValue().equals("男"));
    }


    private void showEditNickNameDialog() {
        EditStrProfileDialog dialog = new EditStrProfileDialog(getActivity());
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, final String content) {
                TIMFriendshipManager.getInstance().setNickName(content, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        TastyToast.makeText(getActivity().getApplicationContext(), "更新昵称失败：" + s, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                    @Override
                    public void onSuccess() {
                        //更新成功
                        getSelfInfo();
                    }
                });
            }
        });
        dialog.show("昵称", R.drawable.ic_info_nickname, mNickNameEdt.getValue());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mPicChooserHelper != null) {
            mPicChooserHelper.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void updateAvatar(String url) {
        TIMFriendshipManager.getInstance().setFaceUrl(url, new TIMCallBack() {

            @Override
            public void onError(int i, String s) {
                TastyToast.makeText(getActivity().getApplicationContext(), "更新头像失败：" + s, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onSuccess() {
                //更新头像成功
                getSelfInfo();
            }
        });
    }
}
