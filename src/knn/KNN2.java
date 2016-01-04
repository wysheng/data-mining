package knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KNN2 {

	private ArrayList<String> attribute = new ArrayList<String>(); // �洢���Ե�����
	private ArrayList<ArrayList<String>> attributevalue = new ArrayList<ArrayList<String>>(); // �洢ÿ�����Ե�ȡֵ
	private ArrayList<String[]> data = new ArrayList<String[]>();; // ԭʼ����
	int decatt; // ���߱��������Լ��е�����
	public static final String patternString = "@attribute(.*)[{](.*?)[}]";

	// ��ȡarff�ļ�����attribute��attributevalue��data��ֵ
	public void readARFF(File file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			Pattern pattern = Pattern.compile(patternString);
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					attribute.add(matcher.group(1).trim());
					String[] values = matcher.group(2).split(",");
					ArrayList<String> al = new ArrayList<String>(values.length);
					for (String value : values) {
						al.add(value.trim());
					}
					attributevalue.add(al);
				} else if (line.startsWith("\t@data")) {
					while ((line = br.readLine()) != null) {
						if (line == "")
							continue;
						String[] row = line.split(",");
						data.add(row);
					}
				} else {
					continue;
				}
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// ŷʽ�������
	public static double oudistance(Point point1, Point point2) {
		double temp = Math.pow(point1.getX() - point2.getX(), 2)
				+ Math.pow(point1.getY() - point2.getY(), 2);
		return Math.sqrt(temp);
	}

	// �ҳ����Ƶ��
	public static String maxP(Map<String, Double> map) {
		String key = null;
		double value = 0.0;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			if (entry.getValue() > value) {
				key = entry.getKey();
				value = entry.getValue();
			}
		}
		return key;
	}

	// ����Ƶ��
	public static Map<String, Double> computeP(Map<String, Integer> map,
			double k) {
		Map<String, Double> p = new HashMap<String, Double>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			p.put(entry.getKey(), entry.getValue() / k);
		}
		return p;
	}

}
