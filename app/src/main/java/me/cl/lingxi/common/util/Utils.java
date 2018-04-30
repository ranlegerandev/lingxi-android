package me.cl.lingxi.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.cl.library.utils.ColorPhrase;
import me.cl.lingxi.R;
import me.cl.lingxi.common.config.Constants;
import me.cl.lingxi.entity.Like;

/**
 * 工具类
 * Created by bafsj on 17/3/1.
 */
public class Utils {

    private static Toast toast;

    @SuppressLint("ShowToast")
    public static void toastShow(Context context, String info) {
        if (toast == null) {
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }

    public static void toastShow(Context context, int infoId) {
        toastShow(context, context.getResources().getString(infoId));
    }

    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    private static final long loadTime = 1500;
    private static long time;

    public static void setTime() {
        time = System.currentTimeMillis();
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public interface OnLoading {
        void onLoading();
    }

    /**
     * 加载延时
     */
    public static void loadingTime(final Handler handler, final OnLoading loading) {
        time = System.currentTimeMillis() - time;
        if (time < loadTime) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep((loadTime - time));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loading.onLoading();
                        }
                    });
                }
            }).start();
        } else {
            loading.onLoading();
        }
    }

    /**
     * 手机验证
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("1[34578]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 隐藏虚拟键盘
     */
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    /**
     * 获取点赞人字符串
     */
    public static String getLikeStr(List<Like> likes) {
        String likeStr = "";
        for (int i = 0, size = likes.size(); i < size; i++) {
            if (i == 3) break;
            likeStr = likeStr + "{" + likes.get(i).getUsername() + "、}" + "";
        }
        if (likes.size() > 0) likeStr = likeStr.substring(0, likeStr.length() - 2) + "}";
        return likeStr;
    }

    /**
     * 获取所有点赞人字符串
     */
    public static String getLongLikeStr(List<Like> likes) {
        String likeStr = "";
        for (int i = 0; i < likes.size(); i++) {
            if (i == likes.size() - 1) {
                likeStr = likeStr + "{" + likes.get(i).getUsername() + "}";
            } else {
                likeStr = likeStr + "{" + likes.get(i).getUsername() + "、}";
            }
        }
        return likeStr;
    }

    /**
     * 突出颜色
     */
    public static CharSequence colorFormat(String str) {
        return ColorPhrase.from(str).withSeparator("{}").innerColor(0xFF4FC1E9).outerColor(0xFF666666).format();
    }

    /**
     * 设置头像
     */
    public static void setPersonImg(String uName, ImageView imgView) {
        String pathName = Environment.getExternalStorageDirectory() + "/lingci/image/avatar/headportraits.png";
        if (!new File(pathName).exists()) {
            pathName = Constants.IMG_URL + "/image/avatar/at_" + MD5Util.MD5(uName) + ".jpg";
        }
        Glide.with(imgView.getContext())
                .load(pathName)
                .placeholder(R.drawable.img_user)
                .error(R.drawable.img_user)
                .bitmapTransform(new CropCircleTransformation(imgView.getContext()))
                .into(imgView);
    }

    /**
     * 获取Manifest中meta值
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String metaValue = null;
        if (context == null || TextUtils.isEmpty(metaKey)) {
            return null;
        }
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != app) {
                metaData = app.metaData;
            }
            if (null != metaData) {
                metaValue = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return metaValue;
    }

    // 获取版本名称
    public static String getAppVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取版本号
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
