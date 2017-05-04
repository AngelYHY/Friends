package freestar.friends.fragment.community;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import freestar.friends.R;
import freestar.friends.util.pager_and_indicator.ModelPagerAdapter;
import freestar.friends.util.pager_and_indicator.PagerManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class ArticleFragment extends Fragment {

    private ScrollerViewPager vp;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("FreeStar", "ArticleListFragment→→→onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("FreeStar", "ArticleListFragment→→→onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("FreeStar", "ArticleListFragment→→→onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("FreeStar", "ArticleListFragment→→→onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FreeStar", "ArticleListFragment→→→onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("FreeStar", "ArticleListFragment→→→onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("FreeStar", "ArticleListFragment→→→onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("FreeStar", "ArticleListFragment→→→onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("FreeStar", "ArticleListFragment→→→onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FreeStar", "ArticleListFragment→→→onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("FreeStar", "ArticleListFragment→→→onCreateView");
        View view = inflater.inflate(R.layout.article_fragment, container, false);
        vp = (ScrollerViewPager) view.findViewById(R.id.vp_article);
        SpringIndicator indicator = (SpringIndicator) view.findViewById(R.id.indicator);
        ArrayList<String> list = new ArrayList<>();
        list.add("推荐");
        list.add("技巧");
        list.add("器材");
        list.add("游记");
        PagerManager manager = new PagerManager();
        //设置指示器的文字
        manager.setTitles(list);
        manager.addFragment(ArticleListFragment.newInstance(list.get(0)));
        manager.addFragment(ArticleListFragment.newInstance(list.get(1)));
        manager.addFragment(ArticleListFragment.newInstance(list.get(2)));
        manager.addFragment(ArticleListFragment.newInstance(list.get(3)));
        ModelPagerAdapter adapter = new ModelPagerAdapter(getActivity().getSupportFragmentManager(), manager);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(3);
//        vp.setOffscreenPageLimit(4);
        //将ViewPager关联到springIndicator
        indicator.setViewPager(vp);
        Log.e("FreeStar", "ArticleListFragment→→→onCreateView" + view);
        return view;
    }
}
