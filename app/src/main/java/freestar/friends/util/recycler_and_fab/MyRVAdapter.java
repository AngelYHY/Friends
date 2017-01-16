package freestar.friends.util.recycler_and_fab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import freestar.friends.R;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.MyViewHolder> {
    Context context;
    List<String> list;
    OnItemClickListener listener;

    public MyRVAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    //为item关联布局 inflate  第二个参数是问有没有父布局 如果 match_parent就有效
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent,false);
        return new MyViewHolder(view);
    }

    //设置item的显示内容
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //回调接口的OnItemClick方法
                listener.OnItemClick(view, holder.getLayoutPosition());
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
