package freestar.friends.fragments.msg_fragment.util.interfaces;



import java.util.List;

import freestar.friends.fragments.msg_fragment.util.SwipeLayout;
import freestar.friends.fragments.msg_fragment.util.util.Attributes;

public interface SwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(SwipeLayout layout);
    
    void closeAllItems();

    List<Integer> getOpenItems();

    List<SwipeLayout> getOpenLayouts();

    void removeShownLayouts(SwipeLayout layout);

    boolean isOpen(int position);

    Attributes.Mode getMode();

    void setMode(Attributes.Mode mode);
}
