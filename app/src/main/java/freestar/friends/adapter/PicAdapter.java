package freestar.friends.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public class PicAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> picStr;

    public PicAdapter(Context mContext, List<String> picStr) {
        this.mContext = mContext;
        this.picStr = picStr;
    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return picStr == null ? 0 : picStr.size();
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
        SimpleDraweeView view = new SimpleDraweeView(mContext);
        view.setImageURI(picStr.get(position));
//        ImageView imageView = new ImageView(mContext);
////        如果是http:
////www.xx.com/xx.png这种连接，这里可以用ImageLoader之类的框架加载
////        imageView.setImageResource(resIds.get(position % resIds.size()));
//        Bitmap bitmap = readBitMap(mContext, resIds.get(position % resIds.size()));
//        imageView.setImageBitmap(bitmap);
//        container.addView(imageView);
////        View view = resIds.get(position % resIds.size());
        container.addView(view);
        return view;
    }
}
