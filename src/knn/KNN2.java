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

	private ArrayList<String> attribute = new ArrayList<String>(); // 存储属性的名称
	private ArrayList<ArrayList<String>> attributevalue = new ArrayList<ArrayList<String>>(); // 存储每个属性的取值
	private static ArrayList<String[]> data = new ArrayList<String[]>();; // 原始数据
	int decatt; // 决策变量在属性集中的索引
	public static final String patternString = "@attribute(.*)[{](.*?)[}]";

	public static void main(String[] args) {
		KNN2 knn = new KNN2();
		knn.readARFF(new File("C:/Users/fzj/Workspaces/MyEclipse Professional 2014/DataMining/src/knn/point.arff"));
		for(String[] raw:data){
			for(int i = 0;i<raw.length;i++){
				System.out.print(raw[i]+",");
			}
			System.out.println();
		}
	}
	
	// 读取arff文件，给attribute、attributevalue、data赋值
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
	
	//设置决策变量
		public void setDec(int n){
			if(n < 0 || n >= attribute.size()){
				System.out.println("决策变量错误");
			}
			decatt = n;
		}
		
		public void setDec(String name){
			int n = attribute.indexOf(name);
			setDec(n);
		}
	
	// 欧式距离计算
	public static double oudistance(double[] point1, double[] point2) {
		double temp = 0;
		if(point1.length==point2.length){
			for(int i = 0;i<point1.length;i++){
				 temp += Math.pow(point1[i]-point2[i], 2);
			}
			return Math.sqrt(temp);
		}else{
			System.out.println("参数个数不相同");
			return 0.0;
		}
	}

	// 找出最大频率
	public static String maxP(String[] type) {
		Map<String, Integer> p = new HashMap<String, Integer>();
		for(int i = 0;i<type.length;i++){
			if(!p.containsKey(type[i]))
				p.put(type[i], 0);
			else
				p.put(type[i],p.get(type[i]+1));
		}
		return type[0];
	}
	

	// 计算频率
	public static Map<String, Double> computeP(Map<String, Integer> map,
			double k) {
		Map<String, Double> p = new HashMap<String, Double>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			p.put(entry.getKey(), entry.getValue() / k);
		}
		return p;
	}

}
