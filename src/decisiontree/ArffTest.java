package decisiontree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//ѧϰBufferedReader�Ȼ�����IO�࣬��������ʽ���Լ���ζ�ȡARFF��ʽ���ļ�
public class ArffTest {
	
	private ArrayList<String> attribute = new ArrayList<String>(); // �洢���Ե�����
	private ArrayList<ArrayList<String>> attributevalue = new ArrayList<ArrayList<String>>(); // �洢ÿ�����Ե�ȡֵ
	private static final String patternString = "@attribute(.*)[{](.*?)[}]";

	public static void main(String[] args) {
		ArffTest at = new ArffTest();
		at.readARFF(new File(
				"C:/Users/fzj/Workspaces/MyEclipse Professional 2014/DataMining/src/decisiontree/animal.arff"));
	}

	public void readARFF(File file) {
		try {
			FileReader fr = new FileReader(file);
			// �����ı�����
			BufferedReader br = new BufferedReader(fr);
			String line;
			Pattern pattern = Pattern.compile(patternString);
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()){
					attribute.add(matcher.group(1).trim());
					String[] values = matcher.group(2).split(",");
					ArrayList<String> al = new ArrayList<String>(values.length);
					for(String value:values){
						al.add(value);
					}
				attributevalue.add(al);
				}
					
			}
			for(String str:attribute){
				System.out.println(str);
			}
			for(ArrayList<String> str1:attributevalue){
				for(String str2:str1){
					System.out.println(str2);
				}
				System.out.println();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
