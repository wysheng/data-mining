package decisiontree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.modelmbean.XMLParseException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.omg.CORBA_2_3.portable.OutputStream;

/**
 * 决策树ID3
 * 
 * 1、创建数据集 2、计算香农熵或信息增益 3、通过计算选择最优数据集划分特征 4、通过最优数据划分特征来划分数据集，创建决策树
 * 
 * 
 * @author fzj
 * 
 */
public class DecisionTree {

	public static TreeNode root = null;

	private  ArrayList<String> attribute = new ArrayList<String>(); // 存储属性的名称
	
	private  ArrayList<ArrayList<String>> attributevalue = new ArrayList<ArrayList<String>>(); // 存储每个属性的取值
	
	private  static ArrayList<String[]> data = new ArrayList<String[]>();; // 原始数据

	private  final String patternString = "@attribute(.*)[{](.*?)[}]";
	
	Document xmldoc;
	Element root1;
	
	public DecisionTree(){
		xmldoc = DocumentHelper.createDocument();
		root1 = xmldoc.addElement("root");
		root1.addElement("DecisionTree").addAttribute("value", "null");
	}
	
	public static void main(String[] args) throws FileNotFoundException {

//		List<Data> dataList = createData();
//		HashMap<Integer, String> labels = getLabels();
//		String tree = createTree1(dataList, labels);
//		System.out.println(tree);
		DecisionTree at = new DecisionTree();
//		DecisionTree dt = new DecisionTree();
//		dt.read();
		at.readARFF(new File(
				"C:/Users/fzj/Workspaces/MyEclipse Professional 2014/DataMining/src/decisiontree/animal.arff"));
//		at.writeXML("dt.xml");
		LinkedList<Integer> ll = new LinkedList<Integer>();
		for(int i = 0;i<at.attribute.size();i++){
			ll.add(i);
		}
		ArrayList<Integer> al = new ArrayList<Integer>();
		for(int i = 0;i<at.data.size();i++){
			al.add(i);
		}
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
				}else if(line.startsWith("\t@data")){
					while ((line = br.readLine()) != null) {
                        if(line=="")
                            continue;
                        String[] row = line.split(",");
                        data.add(row);
                    }
				}else{
					 continue;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 创建决策树，参数:数据集，特征名集合
	public static String createTree1(List<Data> dataList,
			HashMap<Integer, String> labels) {
		// 类别因子"yes","no"
		ArrayList<String> classList = new ArrayList<String>();
		for (Data data : dataList) {
			classList.add(data.getType());
		}
		// 类别完全相同则停止划分
		Set<String> classSet = new HashSet(classList);
		if (classSet.size() == 1) {
			System.out.println(classList.get(0));
			return classList.get(0);
		}
		// 遍历完所有特征，返回出现次数最多的
		if (dataList.size() == 1) {
			System.out.println("majorityCnt" + majorityCnt(classList));
			return majorityCnt(classList);
		}
		// 最优划分特征的索引位置
		int bestFeat = findBestFeatureToSplit(dataList);
		// 最优划分特征名
		String bestFeatLabel = labels.get(bestFeat);

		System.out.println(bestFeatLabel);

		String mytree = "";
		// 删除最优划分特征名
		labels.remove(bestFeatLabel);
		ArrayList<String> featValues = new ArrayList<String>();
		// 将数据集中所有数据的最优特征名对应的特征值保存到featValues中
		for (Data data : dataList) {
			featValues.add(data.getFeature().get(bestFeat));
		}
		ArrayList<Data> retDataSet = null;
		// 不重复的特征值
		Set<String> uniqueVals = new HashSet(featValues);
		HashMap<Integer, String> subLabels = null;
		if (uniqueVals != null) {
			for (String value : uniqueVals) {
				subLabels = labels;
				System.out.println(value);
				mytree = createTree1(splitDataList(dataList, bestFeat, value),
						subLabels);
			}
		}
		return mytree;
	}
	//

	// 返回每一项特征中出现次数最多的类别因子
	public static String majorityCnt(ArrayList<String> classList) {
		HashMap<String, Integer> classCount = new HashMap<String, Integer>();
		for (String vote : classList) {
			if (!classCount.containsKey(vote)) {
				classCount.put(vote, 0);
			}
			classCount.put(vote, classCount.get(vote) + 1);
		}
		return maxP(classCount);
	}

	// 选择最优数据集划分特征
	public static int findBestFeatureToSplit(List<Data> dataList) {
		// 特征值数量
		int numFeatures = dataList.get(0).getFeature().size() - 1;
		// 香农熵
		double baseEntroy = computeShannonEnt(dataList);
		// 信息增益
		double bestInfoGain = 0.0;
		// 最优特征值
		int bestFeature = -1;

		// 特征值
		ArrayList<String> featList = new ArrayList<String>();
		ArrayList<Data> subDataSet = null;
		for (int i = 0; i <= numFeatures; i++) {
			for (Data data : dataList) {
				featList.add(data.getFeature().get(i));
			}
			Set<String> set = new HashSet(featList);
			double newEntropy = 0.0;
			for (String str : set) {
				subDataSet = splitDataList(dataList, i, str);
				double size1 = dataList.size();
				double size2 = subDataSet.size();
				double prob = size2 / size1;
				newEntropy += prob * computeShannonEnt(dataList);
			}
			double infoGain = baseEntroy - newEntropy;
			if (infoGain >= bestInfoGain) {
				bestInfoGain = infoGain;
				bestFeature = i;
			}
			featList.clear();
		}
		return bestFeature;
	}

	// 数据集划分,dataList待划分数据集,axis划分数据集的特征，特征返回值
	public static ArrayList<Data> splitDataList(List<Data> dataList, int axis,
			String value) {
		ArrayList<Data> retDataSet = new ArrayList<Data>();
		for (Data featVec : dataList) {
			if (featVec.getFeature().get(axis).equals(value)) {
				retDataSet.add(featVec);
			}
		}

		return retDataSet;
	}

	// 计算数据集的香农熵
	public static double computeShannonEnt(List<Data> dataList) {
		long num = dataList.size();
		Map<String, Integer> labelCounts = new HashMap<String, Integer>();
		String currentLabel = "";
		for (Data data : dataList) {
			currentLabel = data.getType();
			if (!labelCounts.containsKey(currentLabel))
				labelCounts.put(currentLabel, 0);
			labelCounts.put(currentLabel, labelCounts.get(currentLabel) + 1);
		}
		double shannonEnt = 0.0;
		double prob;
		// 香农熵公式
		for (Map.Entry<String, Integer> entry : labelCounts.entrySet()) {
			prob = (double) entry.getValue() / num;
			shannonEnt -= prob * computeLog(prob, 2);
		}
		return shannonEnt;
	}

	// 1、创建数据集
	public static ArrayList<Data> createData() {
		ArrayList<Data> dataList = new ArrayList<Data>();
		HashMap<Integer, String> map1 = new HashMap<Integer, String>();
		HashMap<Integer, String> map2 = new HashMap<Integer, String>();
		HashMap<Integer, String> map3 = new HashMap<Integer, String>();
		HashMap<Integer, String> map4 = new HashMap<Integer, String>();
		HashMap<Integer, String> map5 = new HashMap<Integer, String>();

		map1.put(0, "1");
		map1.put(1, "1");
		map2.put(0, "1");
		map2.put(1, "1");
		map3.put(0, "1");
		map3.put(1, "0");
		map4.put(0, "0");
		map4.put(1, "1");
		map5.put(0, "0");
		map5.put(1, "1");

		Data data1 = new Data(1, map1, "yes");
		Data data2 = new Data(2, map2, "yes");
		Data data3 = new Data(3, map3, "no");
		Data data4 = new Data(4, map4, "no");
		Data data5 = new Data(5, map5, "no");

		dataList.add(data1);
		dataList.add(data2);
		dataList.add(data3);
		dataList.add(data4);
		dataList.add(data5);

		return dataList;
	}

	// 获取所有特征名
	public static String[][] getLabelsValue(List<Data> dataList) {
		int featureNumber = dataList.get(0).getFeature().size();
		int dataSize = dataList.size();
		String[][] labels = new String[featureNumber][dataSize];

		for (int i = 0; i < featureNumber; i++) {
			for (int j = 0; j < dataSize; j++)
				labels[i][j] = dataList.get(j).getFeature().get(i);
		}

		return labels;
	}
	//计算熵
	public double getEntropy(int[] arr) {
		double entropy = 0.0;
		int sum = 0;
		for(int i = 0;i<arr.length;i++){
			entropy -= arr[i] * Math.log(arr[i]+Double.MIN_VALUE)/Math.log(2);
			sum += arr[i];
		}
		entropy += sum * Math.log(sum+Double.MIN_VALUE)/Math.log(2);
		entropy /= sum;
		return entropy;
	}
	
	// 获取所有特征名
	public static HashMap<Integer, String> getLabels() {
		HashMap<Integer, String> labels = new HashMap<Integer, String>();
		labels.put(0, "no surfacing");
		labels.put(1, "flippers");
		return labels;
	}
	
	// 计算以base的value的对数
	public static double computeLog(double value, double base) {
		return Math.log(value) / Math.log(base);
	}

	// 找出出现频率最大的特征
	public static String maxP(Map<String, Integer> map) {
		String key = null;
		double value = 0.0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() > value) {
				key = entry.getKey();
				value = entry.getValue();
			}
		}
		return key;
	}
	
	//xml写入文件
	public void writeXML(String fileName){
		try {
			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			OutputFormat of = OutputFormat.createPrettyPrint();
			XMLWriter output = new XMLWriter(fw,of);
			output.write(xmldoc);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
