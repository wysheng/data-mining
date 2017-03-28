package knn;

import java.util.Comparator;

public class CompareClass implements Comparator<Distance>{

	public int compare(Distance d1, Distance d2) {
		return d1.getDisatance()>d2.getDisatance()?20 : -1;
	}

}
