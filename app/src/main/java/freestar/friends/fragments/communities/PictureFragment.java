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
import freestar.friends.fragments.pages_fragment.PageP1;
import freestar.friends.fragments.pages_fragment.PageP2;
import freestar.friends.fragments.pages_fragment.PageP3;
import freestar.friends.fragments.pages_fragment.PageP4;
import freestar.friends.util.pager_and_indicator.ModelPagerAdapter;
import freestar.friends.util.pager_and_indicator.PagerManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends Fragment {
    private ScrollerViewPager vp;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("FreeStar", "PictureFragment→→→onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("FreeStar", "PictureFragment→→→onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("FreeStar", "PictureFragment→→→onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("FreeStar", "PictureFragment→→→onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FreeStar", "PictureFragment→→→onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("FreeStar", "PictureFragment→→→onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("FreeStar", "PictureFragment→→→onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("FreeStar", "PictureFragment→→→onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("FreeStar", "PictureFragment→→→onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FreeStar", "PictureFragment→→→onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("FreeStar", "PictureFragment→→→onCreateView");
        View view = inflater.inflate(R.layout.picture_fragment, container, false);
        vp = (ScrollerViewPager) view.findViewById(R.id.vp_picture);
        SpringIndicator indicator = (SpringIndicator) view.findViewById(R.id.indicator);
        PagerManager manager = new PagerManager();
        //设置指示器的文字
        manager.setTitles(getTitles());
        manager.addFragment(new PageP1());
        manager.addFragment(new PageP2());
        manager.addFragment(new PageP3());
        manager.addFragment(new PageP4());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getActivity().getSupportFragmentManager(), manager);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(3);
        //将ViewPager关联到springIndicator
        indicator.setViewPager(vp);
        Log.e("FreeStar", "PictureFragment→→→onCreateView" + view);
        return view;
    }

    public List<String> getTitles() {
        ArrayList<String> list = new ArrayList<>();
        list.add("推荐");
        list.add("风景");
        list.add("动物");
        list.add("其他");
        return list;
    }
}
