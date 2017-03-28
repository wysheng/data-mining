package decisiontree;

import java.util.HashMap;

public class Data {
	private int id;
	private HashMap<Integer,String> feature;
	private String type;

	public Data(int id, HashMap<Integer, String> feature, String type) {
		super();
		this.id = id;
		this.feature = feature;
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public HashMap<Integer, String> getFeature() {
		return feature;
	}
	public void setFeature(HashMap<Integer, String> feature) {
		this.feature = feature;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
