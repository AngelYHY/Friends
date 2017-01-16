package freestar.friends.fragments.msg_fragment.util.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import freestar.friends.fragments.msg_fragment.util.SwipeLayout;
import freestar.friends.fragments.msg_fragment.util.implments.SwipeItemMangerImpl;
import freestar.friends.fragments.msg_fragment.util.interfaces.SwipeAdapterInterface;
import freestar.friends.fragments.msg_fragment.util.interfaces.SwipeItemMangerInterface;
import freestar.friends.fragments.msg_fragment.util.util.Attributes;

public abstract class BaseSwipeAdapter extends BaseAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {

    protected SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);


    public abstract int getSwipeLayoutResourceId(int position);

    public abstract View generateView(int position, ViewGroup parent);

    /**
     * fill values or bind listeners to the view.
     * @param position
     * @param convertView
     */
    public abstract void fillValues(int position, View convertView);

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = generateView(position, parent);
        }
        mItemManger.bind(v, position);
        fillValues(position, v);
        return v;
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
