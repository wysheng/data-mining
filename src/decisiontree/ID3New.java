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

	private ArrayList<String> attribute = new ArrayList<String>(); // �洢���Ե�����

	private ArrayList<ArrayList<String>> attributevalue = new ArrayList<ArrayList<String>>(); // �洢ÿ�����Ե�ȡֵ

	private static ArrayList<String[]> data = new ArrayList<String[]>();; // ԭʼ����
	
	int decatt; // ���߱��������Լ��е�����

	private final String patternString = "@attribute(.*)[{](.*?)[}]";
	
	//���þ��߱���
	public void setDec(int n){
		if(n < 0 || n >= attribute.size()){
			System.out.println("���߱�������");
		}
		decatt = n;
	}
	
	public void setDec(String name){
		int n = attribute.indexOf(name);
		setDec(n);
	}
	//����������
	public void buildDT(String name, String value, ArrayList<Integer> subset,
            LinkedList<Integer> selatt){
		
	}
	// ������
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
	//������Ϣ��
	 public double calNodeEntropy(ArrayList<Integer> subset, int index) {
		 return 2+Double.MIN_VALUE;
	 }
	//�ж�����Ƿ���ͬ
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

	// ��ȡarff�ļ�
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
