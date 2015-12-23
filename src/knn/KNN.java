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
 1������������֪�� 2������δ֪�� 3������������֪�㵽δ֪���ŷʽ���� 4�����ݾ����������֪������ 5��ѡ������δ֪�������k����
 * 6������k�������ڷ�����ֵ�Ƶ�� 7��ѡ��Ƶ���������Ϊδ֪������
 * 
 * @author fzj
 * 
 */
public class KNN {

	public static void main(String[] args) {
		// һ������������֪��
		Point point1 = new Point(1, 1.0, 1.1, "A");
		Point point2 = new Point(2, 1.0, 1.0, "A");
		Point point3 = new Point(3, 1.0, 1.2, "A");
		Point point4 = new Point(4, 0, 0, "B");
		Point point5 = new Point(5, 0, 0.1, "B");
		Point point6 = new Point(6, 0, 0.2, "B");
		// ��������δ֪��
		Point x = new Point(5, 1.2, 1.2);
		// ��������������֪�㵽δ֪���ŷʽ���룬�����ݾ����������֪������
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

		// �ġ�ѡȡ�����k����
		double k = 5;
		// �塢����k�������ڷ�����ֵ�Ƶ��

		// 1������ÿ�������������ĵ�ĸ���

		Map<String, Integer> map = new HashMap<String, Integer>();
		Iterator<Distance> iter2 = list3.iterator();
		List<Distance> list4 = new ArrayList<Distance>(list3);
		int i = 0;
		for (Distance distance : list4) {
			System.out.println("idΪ" + distance.getId() + ",����Ϊ��"
					+ distance.getDisatance());
			long id = distance.getId();
			// ͨ��id�ҵ���������,���洢��HashMap��
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
		// 2������Ƶ��
		Map<String, Double> p = new HashMap<String, Double>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			p.put(entry.getKey(), entry.getValue() / k);
		}
		x.setType(maxP(p));
		System.out.println(x.getType());
	}

	// ŷʽ�������
	public static double oudistance(Point point1, Point point2) {
		double temp = Math.pow(point1.getX() - point2.getX(), 2)
				+ Math.pow(point1.getY() - point2.getY(), 2);
		return Math.sqrt(temp);
	}
	//�ҳ����Ƶ��
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
