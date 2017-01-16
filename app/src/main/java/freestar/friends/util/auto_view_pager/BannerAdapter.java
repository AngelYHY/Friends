/*
 * AUTHOR：Yolanda
 * 
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package freestar.friends.util.auto_view_pager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Yolanda on 2016/5/5.
 *
 * @author Yolanda; QQ: 757699476
 */
public class BannerAdapter extends PagerAdapter {

    private Context mContext;

    private List<Integer> resIds;

    public BannerAdapter(Context context) {
        this.mContext = context;
    }

    public void update(List<Integer> resIds) {
//        if (resIds != null) {
//            if (this.resIds != null) {
//                this.resIds.clear();
//            }
//            this.resIds = resIds;
//        }
        if (this.resIds != null)
            this.resIds.clear();
        if (resIds != null)
            this.resIds = resIds;
    }

    @Override
    public int getCount() {
        return resIds == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
//        如果是http:
//www.xx.com/xx.png这种连接，这里可以用ImageLoader之类的框架加载
//        imageView.setImageResource(resIds.get(position % resIds.size()));
        Bitmap bitmap = readBitMap(mContext, resIds.get(position % resIds.size()));
        imageView.setImageBitmap(bitmap);
        container.addView(imageView);
//        View view = resIds.get(position % resIds.size());
//        container.addView(view);
        return imageView;
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
// 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
