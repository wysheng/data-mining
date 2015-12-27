package decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * 决策树
 * 
 * 1、创建数据集
 * 2、计算香农熵或信息增益 
 * 3、通过计算选择最优数据集划分特征 
 * 4、通过最优数据划分特征来划分数据集，创建决策树
 * 
 * 
 * @author fzj
 * 
 */
public class DecisionTree {
	

	public static void main(String[] args) {
		// 创建数据集
		List<Data> dataList = createData();
		// 香农熵
		double shannonEnt = computeShannonEnt(dataList);
		System.out.println("香农熵为：" + shannonEnt);
		List<Data> retDataSet = splitDataList(dataList, 1, "0");
		int t = findBestFeatureToSplit(dataList);
		System.out.println("第一数据集划分特征:"+t);
		ArrayList<String> labels = getLabels();
		createTree(dataList, labels);
	}
	//创建决策树
	public static String createTree(List<Data> dataList,ArrayList<String> labels){
		ArrayList<String> classList = new ArrayList<String>();
		for(Data data:dataList){
			classList.add(data.getType());
		}
		//若类别中只存在一个类别
		Set<String> classSet = new HashSet(classList);		
		if(classSet.size()==1){
			return classList.get(0);
		}
		if(dataList.size()==1)
			return majorityCnt(classList);
		int bestFeat = findBestFeatureToSplit(dataList);
		String bestFeatLabel = labels.get(bestFeat);
	//	TreeNode root = new TreeNode(bestFeatLabel);
   //		Object[][] tree = {bestFeatLabel[],{}};
		String str1=null;
		labels.remove(bestFeatLabel);
		ArrayList<String> featValues = new ArrayList<String>();
		for(Data data:dataList){
			featValues.add(data.getFeature().get(bestFeat));
		}
		Set<String> uniqueVals = new HashSet(featValues);
		for(String str:uniqueVals){
			ArrayList<String> subLabels = labels;
		//	root.setLeftNode(leftNode);
			 str1 = createTree(splitDataList(dataList, bestFeat, str), subLabels);
		}
		return str1;
		
		
	}
	//返回每一项特征中出现次数最多的类别值
	public static String majorityCnt(ArrayList<String> classList){
		HashMap<String,Integer> classCount = new HashMap<String,Integer>();
		for(String vote:classList){
			if(!classCount.containsKey(vote)){
				classCount.put(vote, 0);
			}
			classCount.put(vote, classCount.get(vote)+1);
		}
		return maxP(classCount);
	}
	// 选择最优数据集划分特征
	public static int findBestFeatureToSplit(List<Data> dataList) {
		int numFeatures = dataList.get(0).getFeature().size()-1;
		double baseEntroy = computeShannonEnt(dataList);
		double bestInfoGain = 0.0;
		int bestFeature = -1;
		ArrayList<String> featList = new ArrayList<String>();
		ArrayList<Data> subDataSet = null;
		for (int i = 0; i < numFeatures; i++) {
			for (Data data : dataList) {
				featList.add(data.getFeature().get(i));
			}
			Set<String> set = new HashSet(featList);
			double newEntropy = 0.0;
			for (String str : set) {
				subDataSet = splitDataList(dataList, i, str);
				double size1 = dataList.size();
				double size2 = subDataSet.size();
				double prob = size2/ size1;
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

	// 数据集划分
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

	// 创建数据集
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

	// 获取所有特征标签值
	public static String[][] getLabelsValue(
			List<Data> dataList) {
		int featureNumber = dataList.get(0).getFeature().size();
		int dataSize = dataList.size();
		String[][] labels= new String[featureNumber][dataSize];
		
		for(int i = 0;i<featureNumber;i++){
			for(int j = 0;j<dataSize;j++)
			labels[i][j] = dataList.get(j).getFeature().get(i);
		}
		
//		HashMap<Integer, ArrayList<String>> labels = new HashMap<Integer, ArrayList<String>>();
//		for (int i = 0; i < 2; i++) {
//			ArrayList<String> oneLabels = new ArrayList<String>();
//			for (Data data : dataList) {
//				oneLabels.add(data.getFeature().get(i));
//				labels.put(i, oneLabels);
//			}
//			oneLabels.clear();
//		}

		return labels;
	}
	// 获取所有特征标签
	public static ArrayList<String> getLabels(){
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("no surfacing");
		labels.add("flippers");
		return labels;
	}
	// 计算以base的value的对数
	public static double computeLog(double value, double base) {
		return Math.log(value) / Math.log(base);
	}
	// 找出出现频率最大的特征
	public static String maxP(Map<String,Integer> map) {
		String key = null;
		double value = 0.0;
		for (Map.Entry<String,Integer> entry : map.entrySet()) {
			if (entry.getValue() > value) {
				key = entry.getKey();
				value = entry.getValue();
			}
		}
		return key;
	}
}
