package knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 1、输入所有已知点 2、输入未知点 3、计算所有已知点到未知点的欧式距离 4、根据距离对所有已知点排序 5、选出距离未知点最近的k个点
 * 6、计算k个点所在分类出现的频率 7、选择频率最大的类别即为未知点的类别
 * 
 * @author fzj
 * 
 */
public class KNN {

	public static void main(String[] args) {
		// 一、输入所有已知点
		Point point1 = new Point(1, 1.0, 1.1, "A");
		Point point2 = new Point(2, 1.0, 1.0, "A");
		Point point3 = new Point(3, 1.0, 1.2, "A");
		Point point4 = new Point(4, 0, 0, "B");
		Point point5 = new Point(5, 0, 0.1, "B");
		Point point6 = new Point(6, 0, 0.2, "B");
		// 二、输入未知点
		Point x = new Point(5, 1.2, 1.2);
		// 三、计算所有已知点到未知点的欧式距离，并根据距离对所有已知点排序
		ArrayList<Point> list1 = new ArrayList<Point>();
		list1.add(point1);
		list1.add(point2);
		list1.add(point3);
		list1.add(point4);
		list1.add(point5);
		list1.add(point6);
		CompareClass compare = new CompareClass();
		Set<Distance> list3 = new TreeSet<Distance>(compare);
		for (Point point : list1) {
			list3.add(new Distance(point.getId(), x.getId(), oudistance(point,
					x)));
		}

		// 四、选取最近的k个点
		double k = 5;
		// 五、计算k个点所在分类出现的频率

		// 1、计算每个分类所包含的点的个数

		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<Distance> iter2 = list3.iterator();
		List<Distance> list4 = new ArrayList<Distance>(list3);
		int i = 0;
		for (Distance distance : list4) {
			System.out.println("id为" + distance.getId() + ",距离为："
					+ distance.getDisatance());
			long id = distance.getId();
			// 通过id找到所属类型,并存储到HashMap中
			for (Point point : list1) {
				if (point.getId() == id) {
					if (map.get(point.getType()) != null)
						map.put(point.getType(), map.get(point.getType()) + 1);
					else {
						map.put(point.getType(), 1);
					}
				}
			}
			i++;
			if (i >= k)
				break;
		}
		// 2、计算频率
		Map<String, Double> p = new HashMap<String, Double>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			p.put(entry.getKey(), entry.getValue() / k);
		}
		x.setType(maxP(p));
		System.out.println(x.getType());
	}

	// 欧式距离计算
	public static double oudistance(Point point1, Point point2) {
		double temp = Math.pow(point1.getX() - point2.getX(), 2)
				+ Math.pow(point1.getY() - point2.getY(), 2);
		return Math.sqrt(temp);
	}
	//找出最大频率
	public static String maxP(Map<String, Double> map) {
		String key = null;
		double value = 0.0;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			if(entry.getValue()>value){
				key = entry.getKey();
				value = entry.getValue();
			}
		}
		return key;
	}

}
