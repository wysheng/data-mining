package decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ���������������
 * 
 * @author fzj
 * 
 */
public class DecisionTree {
	public static void main(String[] args) {
		// �������ݼ�
		List<Data> dataList = createData();
		//��ũ��
		double shannonEnt = computeShannonEnt(dataList);
		System.out.println("��ũ��Ϊ��"+shannonEnt);
	}
	
	//�������ݼ�����ũ��
	public static double computeShannonEnt(List<Data> dataList){
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
		for (Map.Entry<String, Integer> entry : labelCounts.entrySet()) {
			prob = (double) entry.getValue() / num;
			shannonEnt -= prob * computeLog(prob, 2);
		}
		return shannonEnt;
	}

	// �������ݼ�
	public static ArrayList<Data> createData() {
		ArrayList<Data> dataList = new ArrayList<Data>();
		Data data1 = new Data(1, 1, 1, "maybe");
		Data data2 = new Data(2, 1, 1, "yes");
		Data data3 = new Data(3, 1, 0, "no");
		Data data4 = new Data(4, 0, 1, "no");
		Data data5 = new Data(5, 0, 1, "no");
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
