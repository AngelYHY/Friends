package freestar.friends.fragments.msg_fragment.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import freestar.friends.R;
import freestar.friends.bean.User;

public abstract class MyAdaper extends BaseAdapter {

    TextView name;
    private Context mContext;

    private List<User> mList;
    private LayoutInflater inflater;

    public MyAdaper(Context mContext, List<User> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.listview_item1, parent, false);

//        View v = convertView;
//        if (v == null) {
//            v = generateView(position, parent);
//        }
//        mItemManger.bind(v, position);
//        fillValues(position, v);
        return v;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
