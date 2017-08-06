package com.huadi.android.ainiyo.util;

import com.huadi.android.ainiyo.entity.Friends;


import java.util.Comparator;

public class PinyinComparator implements Comparator<Friends> {

	public int compare(Friends o1, Friends o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
