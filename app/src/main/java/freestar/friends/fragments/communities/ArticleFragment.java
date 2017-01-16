package freestar.friends.fragments.communities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import freestar.friends.R;
import freestar.friends.fragments.pages_fragment.PageP5;
import freestar.friends.fragments.pages_fragment.PageP6;
import freestar.friends.fragments.pages_fragment.PageP7;
import freestar.friends.fragments.pages_fragment.PageP8;
import freestar.friends.util.pager_and_indicator.ModelPagerAdapter;
import freestar.friends.util.pager_and_indicator.PagerManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class ArticleFragment extends Fragment {

    private ScrollerViewPager vp;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("FreeStar", "ArticleFragment→→→onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("FreeStar", "ArticleFragment→→→onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("FreeStar", "ArticleFragment→→→onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("FreeStar", "ArticleFragment→→→onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FreeStar", "ArticleFragment→→→onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("FreeStar", "ArticleFragment→→→onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("FreeStar", "ArticleFragment→→→onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("FreeStar", "ArticleFragment→→→onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("FreeStar", "ArticleFragment→→→onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FreeStar", "ArticleFragment→→→onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("FreeStar", "ArticleFragment→→→onCreateView");
        View view = inflater.inflate(R.layout.article_fragment, container, false);
        vp = (ScrollerViewPager) view.findViewById(R.id.vp_article);
        SpringIndicator indicator = (SpringIndicator) view.findViewById(R.id.indicator);
        PagerManager manager = new PagerManager();
        //设置指示器的文字
        manager.setTitles(getTitles());
        manager.addFragment(new PageP5());
        manager.addFragment(new PageP6());
        manager.addFragment(new PageP7());
        manager.addFragment(new PageP8());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getActivity().getSupportFragmentManager(), manager);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(3);
//        vp.setOffscreenPageLimit(4);
        //将ViewPager关联到springIndicator
        indicator.setViewPager(vp);
        Log.e("FreeStar", "ArticleFragment→→→onCreateView" + view);
        return view;
    }

    public List<String> getTitles() {
        ArrayList<String> list = new ArrayList<>();
        list.add("推荐");
        list.add("技巧");
        list.add("器材");
        list.add("游记");
        return list;
    }
}
