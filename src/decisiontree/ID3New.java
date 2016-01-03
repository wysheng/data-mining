package decisiontree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ID3New {

	private ArrayList<String> attribute = new ArrayList<String>(); // 存储属性的名称

	private ArrayList<ArrayList<String>> attributevalue = new ArrayList<ArrayList<String>>(); // 存储每个属性的取值

	private static ArrayList<String[]> data = new ArrayList<String[]>();; // 原始数据
	
	int decatt; // 决策变量在属性集中的索引

	private final String patternString = "@attribute(.*)[{](.*?)[}]";
	
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
	//构建决策树
	public void buildDT(String name, String value, ArrayList<Integer> subset,
            LinkedList<Integer> selatt){
		
	}
	// 计算熵
	public double getEntropy(int[] arr) {
		double entropy = 0.0;
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			entropy -= arr[i] * Math.log(arr[i] + Double.MIN_VALUE)
					/ Math.log(2);
			sum += arr[i];
		}
		entropy += sum * Math.log(sum + Double.MIN_VALUE) / Math.log(2);
		entropy /= sum;
		return entropy;
	}
	//计算信息熵
	 public double calNodeEntropy(ArrayList<Integer> subset, int index) {
		 return 2+Double.MIN_VALUE;
	 }
	//判断类别是否相同
	public boolean infoPure(ArrayList<Integer> subSet){

		if(subSet==null||subSet.size()==0){
			System.out.println("Error");
			return true;
		}
		if(subSet.size()==1)
			return true;
		if(subSet.size()>1){
			String [] values = data.get(subSet.get(0));
			String value = values[decatt];
			String next;
			for(int i = 1;i<subSet.size();i++){
				values = data.get(subSet.get(i));
				next = values[decatt];
				if(next!=value)
					return false;
			}
		}
		return true;
	}

	// 读取arff文件
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
						al.add(value);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
