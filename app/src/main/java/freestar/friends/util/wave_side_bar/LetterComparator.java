package freestar.friends.util.wave_side_bar;


import java.util.Comparator;

import freestar.friends.bean.City;

public class LetterComparator implements Comparator<City> {

    @Override
    public int compare(City l, City r) {
        if (l == null || r == null) {
            return 0;
        }
        String lhsSortLetters = l.pys.toUpperCase();
        String rhsSortLetters = r.pys.toUpperCase();
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}