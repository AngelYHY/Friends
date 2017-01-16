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
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.User;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class AAdapterAtlasDis extends BaseAdapter {
    public static final int HAVE_FATHER = 1;
    public static final int NOT_FATHER = 2;
    private final LayoutInflater li;
    private List<AtlasDis> items;
    private Context context;

    public AAdapterAtlasDis(List<AtlasDis> items, Context context) {
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
                case HAVE_FATHER:
                    //有父评论
                    vh1 = new ViewHolder1();
                    view = li.inflate(R.layout.dis_item_layout, null);
//                    vh1.img = (SimpleDraweeView) view.findViewById(R.id.sdv_pic);
                    vh1.sdvHead = (SimpleDraweeView) view.findViewById(R.id.sdv_head);
                    vh1.userName = (TextView) view.findViewById(R.id.tv_user_name);
                    vh1.date = (TextView) view.findViewById(R.id.tv_data);
                    vh1.disNum = (TextView) view.findViewById(R.id.tv_disNum);
                    vh1.faHead = (SimpleDraweeView) view.findViewById(R.id.sdv_father_head);
                    vh1.faDate = (TextView) view.findViewById(R.id.tv_father_data);
                    vh1.faName = (TextView) view.findViewById(R.id.tv_father_name);
                    vh1.faCom = (TextView) view.findViewById(R.id.tv_father_comment);

                    view.setTag(vh1);
                    break;
                case NOT_FATHER:
                    //文字
                    vh2 = new ViewHolder2();
                    view = li.inflate(R.layout.dis_item_layout_no, null);
//                    vh2.tv = (TextView) view.findViewById(R.id.tv_context);
                    vh2.sdvHead = (SimpleDraweeView) view.findViewById(R.id.sdv_head);
                    vh2.userName = (TextView) view.findViewById(R.id.tv_user_name);
                    vh2.date = (TextView) view.findViewById(R.id.tv_data);
                    vh2.disNum = (TextView) view.findViewById(R.id.tv_disNum);
                    view.setTag(vh2);
                    break;
            }
        } else {
            switch (type) {
                case HAVE_FATHER:
                    vh1 = (ViewHolder1) view.getTag();
                    break;
                case NOT_FATHER:
                    vh2 = (ViewHolder2) view.getTag();
                    break;
            }
        }
        switch (type) {
            case HAVE_FATHER:
//                vh1.img.setImageURI(items.get(i).getUrl());
                User cuser = items.get(i).getCuser();
                User father_user = items.get(i).getFather_user();
                AtlasDis atlasDis = items.get(i).getComment_father_user();
                vh1.sdvHead.setImageURI(cuser.getHeadUrl());
                vh1.userName.setText(cuser.getNiname());
                vh1.date.setText(items.get(i).getCreatedAt());
                vh1.disNum.setText(items.get(i).getComment());

                vh1.faName.setText(father_user.getNiname());
                vh1.faCom.setText(atlasDis.getComment());
                vh1.faDate.setText(atlasDis.getCreatedAt());
                vh1.faHead.setImageURI(father_user.getHeadUrl());





                break;
            case NOT_FATHER:

                User cuser1 = items.get(i).getCuser();
                vh2.sdvHead.setImageURI(cuser1.getHeadUrl());
                vh2.userName.setText(cuser1.getNiname());
                vh2.date.setText(items.get(i).getCreatedAt());
                vh2.disNum.setText(items.get(i).getComment());

//                vh2.tv.setText(items.get(i).getContext());
                break;
        }



        return view;
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position).getComment_father_user() != null) {
            return HAVE_FATHER;
        } else {
            return NOT_FATHER;
        }
    }

    static class ViewHolder1 {
        //        SimpleDraweeView img;
        SimpleDraweeView sdvHead;
        TextView userName;
        TextView date;
        SimpleDraweeView faHead;
        TextView faName;
        TextView faDate;
        TextView faCom;
        TextView disNum;

    }

    static class ViewHolder2 {
        //        TextView tv;
        SimpleDraweeView sdvHead;
        TextView userName;
        TextView date;
        TextView disNum;
    }
}
