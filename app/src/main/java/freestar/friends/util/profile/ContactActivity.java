package freestar.friends.util.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import freestar.friends.R;
import freestar.friends.util.status_bar.BaseActivity;

public class ContactActivity extends BaseActivity {

    private ExpandableListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        listView = (ExpandableListView) findViewById(R.id.exlist_collect);
        listView.setAdapter(new MyE());
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //点击
                Toast.makeText(ContactActivity.this,childs[i][i1],Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private  String[] groups = {"我的好友","我的网友","陌生人"};
    private  String[][] childs = {{"aa","bb","cc"},{"aaa","bbb","ccc"},{"AA","AA","AA"}};
    class  MyE extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return childs[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return groups[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return childs[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if(view==null){
                view = getLayoutInflater().inflate(R.layout.group1,null);
            }

            TextView title = (TextView) view.findViewById(R.id.group1_name);
            title.setText(groups[i]);
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            if(view==null){
                view = getLayoutInflater().inflate(R.layout.group2,null);
            }
            ImageView icon = (ImageView) view.findViewById(R.id.group2_icon);
            TextView title = (TextView) view.findViewById(R.id.group2_title);
            title.setText(childs[i][i1]);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }


    public void search_Click(View view){
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);
    }
}
