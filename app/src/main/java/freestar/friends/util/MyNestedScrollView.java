package freestar.friends.util;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import freestar.friends.util.freshload.XListViewHeader;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class MyNestedScrollView  extends NestedScrollView {

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float mLastY = -1; // save event y
    private XListViewHeader mHeaderView;

    public void setHeaderView(XListViewHeader headerView){
        this.mHeaderView = headerView;
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta
                + mHeaderView.getVisiableHeight());

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                Log.e("===============",  mLastY + "--ACTION_DOWN");

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                Log.e("===============",  deltaY + "--ACTION_MOVE");
                mLastY = ev.getRawY();
                if (mHeaderView.getVisiableHeight() > 0 || deltaY > 0) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / 1.8f);
                }
                break;
            default:
                mLastY = -1; // reset

                break;
        }
        return super.onTouchEvent(ev);
    }

}
