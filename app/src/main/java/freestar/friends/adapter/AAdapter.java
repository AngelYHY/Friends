package freestar.friends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import freestar.friends.R;
import freestar.friends.bean.ItemU;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class AAdapter extends BaseAdapter {
    public static final int TYPE_IMG = 1;
    public static final int TYPE_CON = 2;
    private final LayoutInflater li;
    private List<ItemU> items;
    private Context context;

    public AAdapter(List<ItemU> items, Context context) {
        this.items = items;
        this.context = context;
        li = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items == null ? 0 : (items.size());
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder1 vh1 = null;
        ViewHolder2 vh2 = null;
        int type = getItemViewType(i);
        if (view == null) {
            switch (type) {
                case TYPE_IMG:
                    //图片；
                    vh1 = new ViewHolder1();
                    view = li.inflate(R.layout.iv_pic, null);
                    vh1.img = (SimpleDraweeView) view.findViewById(R.id.sdv_pic);
                    view.setTag(vh1);
                    break;
                case TYPE_CON:
                    //文字
                    vh2 = new ViewHolder2();
                    view = li.inflate(R.layout.context, null);
                    vh2.tv = (TextView) view.findViewById(R.id.tv_context);
                    view.setTag(vh2);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_IMG:
                    vh1 = (ViewHolder1) view.getTag();
                    break;
                case TYPE_CON:
                    vh2 = (ViewHolder2) view.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_IMG:
                vh1.img.setImageURI(items.get(i).getUrl());
                break;
            case TYPE_CON:
                vh2.tv.setText(items.get(i).getContext());
                break;
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position).isUrl()) {
            return TYPE_IMG;
        } else {
            return TYPE_CON;
        }
    }

    static class ViewHolder1 {
        SimpleDraweeView img;
    }

    static class ViewHolder2 {
        TextView tv;
    }
}
