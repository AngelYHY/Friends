package freestar.friends.fragments.msg_fragment.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import freestar.friends.R;
import freestar.friends.activities.UserDataActivity;
import freestar.friends.bean.User;
import freestar.friends.fragments.msg_fragment.util.adapters.BaseSwipeAdapter;


public class ListViewAdapter extends BaseSwipeAdapter {
    SimpleDraweeView draweeView;
    TextView name;
    private Context mContext;

    List<User> mList;

    public ListViewAdapter(List<User> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item1, parent, false);

        draweeView = (SimpleDraweeView) v.findViewById(R.id.img_chat_friend);
        name = (TextView) v.findViewById(R.id.text_data);

        final User u = mList.get(position);
        name.setText(u.getNiname());

        if (u.getHeadUrl() == null) {
            //默认的图片
            Uri uri = Uri.parse("http://www.ld12.com/upimg358/20160130/000604738158232.jpg");

            draweeView.setImageURI(uri);
        } else {
            Uri uri = Uri.parse(u.getHeadUrl());

            draweeView.setImageURI(uri);
        }

        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

//        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
//            @Override
//            public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
//            }
//        });


        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "删除！！！", Toast.LENGTH_SHORT).show();
            }
        });

        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = mList.get(position);
                Intent intent = new Intent(mContext, UserDataActivity.class);
                intent.putExtra("user", user);
                mContext.startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
//        TextView t = (TextView)convertView.findViewById(R.id.position);
//        t.setText((position + 1) + ".");
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
