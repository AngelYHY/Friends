package freestar.friends.util.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.activities.UserDataActivity;
import freestar.friends.bean.User;
import freestar.friends.fragments.msg_fragment.MyBaseAdapter;
import freestar.friends.util.status_bar.BaseActivity;

public class SearchActivity extends BaseActivity {

    private ExpandableListView listView;//好友list

    ListView lvs; //搜索list
    EditText et;//搜索et
    List<User> friendList;
    MyBaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //好友
        listView = (ExpandableListView) findViewById(R.id.exlist_collect);
        listView.setAdapter(new MyE());
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //点击
                Toast.makeText(SearchActivity.this,childs[i][i1],Toast.LENGTH_SHORT).show();
                return true;
            }
        });


         lvs = (ListView) findViewById(R.id.lv_search);
        //addListener();
    }
    private void qurryUserId(String id ) {
        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereContains("phoneNum", id);   //模糊查询
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){

                    friendList=  list;
                    Log.e("friendList","-----friendList:"+friendList.toString());
                    adapter=new  MyBaseAdapter(friendList,SearchActivity.this);
                    lvs.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void addListener() {
        //  这是单击  显示   用户信息
//        String id=et.getText().toString().trim();
//        Log.e("friendList","得到的id为："+id);
//        qurryUserId(id);
        lvs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(SearchActivity.this,UserDataActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("user",friendList.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
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






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainsearch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new FragmentRevealExample(), "fragment_my")
                    .addToBackStack("fragment:reveal")
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentRevealExample fragment = (FragmentRevealExample) getSupportFragmentManager().findFragmentByTag("fragment_my");
        if(fragment!=null) {
            fragment.onBackPressed();
        }else {
            super.onBackPressed();
        }
    }

}
