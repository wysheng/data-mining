package decisiontree;

public class Data {
	private int id;
	private int feature1;
	private int feature2;
	private String type;
	public Data(int id, int feature1, int feature2, String type) {
		this.id = id;
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFeature1() {
		return feature1;
	}
	public void setFeature1(int feature1) {
		this.feature1 = feature1;
	}
	public int getFeature2() {
		return feature2;
	}
	public void setFeature2(int feature2) {
		this.feature2 = feature2;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
