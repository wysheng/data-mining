package decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ������
 * 
 * 1���������ݼ�
 * 2��������ũ�ػ���Ϣ���� 
 * 3��ͨ������ѡ���������ݼ��������� 
 * 4��ͨ���������ݻ����������������ݼ�������������
 * 
 * 
 * @author fzj
 * 
 */
public class DecisionTree {

	public static void main(String[] args) {
		// �������ݼ�
		List<Data> dataList = createData();
		// ��ũ��
		double shannonEnt = computeShannonEnt(dataList);
		System.out.println("��ũ��Ϊ��" + shannonEnt);
		List<Data> retDataSet = splitDataList(dataList, 1, "0");
		int t = findBestFeatureToSplit(dataList);
		System.out.println(t);
	}
	//����
	public static String majorityCnt(ArrayList<String> classList){
		HashMap<String,Integer> classCount = new HashMap<String,Integer>();
		for(String vote:classList){
			if(!classCount.containsKey(vote)){
				classCount.put(vote, 0);
			}
			classCount.put(vote, classCount.get(vote)+1);
		}
		return null;
	}
	// ѡ���������ݼ���������
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

	// ���ݼ�����
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

	// �������ݼ�����ũ��
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
		// ��ũ�ع�ʽ
		for (Map.Entry<String, Integer> entry : labelCounts.entrySet()) {
			prob = (double) entry.getValue() / num;
			shannonEnt -= prob * computeLog(prob, 2);
		}
		return shannonEnt;
	}

	// �������ݼ�
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

	// ������base��value�Ķ���
	public static double computeLog(double value, double base) {
		return Math.log(value) / Math.log(base);
	}
}
