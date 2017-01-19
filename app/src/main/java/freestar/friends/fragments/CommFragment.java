package freestar.friends.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import freestar.friends.R;
import freestar.friends.fragments.community.ArticleFragment;
import freestar.friends.fragments.community.PictureFragment;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class CommFragment extends Fragment {
    @Bind(R.id.bt_picture)
    Button btPicture;
    @Bind(R.id.bt_article)
    Button btArticle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

}

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    int oldOperFlag;//前一次操作的是哪个按钮
    int currentOperFlag;//现在操作的是哪个按钮
    private List<Fragment> fragList = new ArrayList<>();
    private FragmentManager manager;
    private List<Button> buttonList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View commView = inflater.inflate(R.layout.communty_view, container, false);
        ButterKnife.bind(this, commView);
        fragList.add(new PictureFragment());
        fragList.add(new ArticleFragment());
        manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().add(R.id.frag_change, fragList.get(0))
                .commitAllowingStateLoss();
        btPicture.setSelected(true);

        buttonList.add(btPicture);
        buttonList.add(btArticle);
        return commView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @OnClick({R.id.bt_picture, R.id.bt_article})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_picture:
                currentOperFlag = 0;//第一个按钮
                break;
            case R.id.bt_article:
                currentOperFlag = 1;
                break;
        }
        //实现旧的隐藏，新的显示
        switchFragment(fragList.get(oldOperFlag), fragList.get(currentOperFlag));
        buttonList.get(oldOperFlag).setSelected(false);//上一次点击的按钮，取消select
        buttonList.get(currentOperFlag).setSelected(true);//当前点击的按钮，设置select
        oldOperFlag = currentOperFlag;
    }

    private void switchFragment(Fragment oldFragment, Fragment newFragment) {
        //判断是否是同一个fragment
        if (oldFragment != newFragment) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            //隐藏oldfragment
            fragmentTransaction.hide(oldFragment);
            Log.i("modefy_one", "modefy_one");
            if (!newFragment.isAdded()) {
                //没有添加过，添加
                Log.i("two", "two");
                fragmentTransaction.add(R.id.frag_change, newFragment).commit();
            } else {
                fragmentTransaction.show(newFragment).commit();
                Log.i("three", "three");
            }
        }
    }
}
