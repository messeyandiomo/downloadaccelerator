package decryption;

public class Tuple {
	private MyType myType = null;
	private String str = null;
	private Integer nextIndex = null;
	
	public Tuple(MyType mytype, Integer nextindex) {
		this.setMyType(mytype);
		this.setNextIndex(nextIndex);
	}
	
	public Tuple(String str, Integer nextindex) {
		this.setStr(str);
		this.setNextIndex(nextindex);
	}
	
	
	
	public MyType getMyType() {
		return myType;
	}
	public void setMyType(MyType myType) {
		this.myType = myType;
	}
	public Integer getNextIndex() {
		return nextIndex;
	}
	public void setNextIndex(Integer nextIndex) {
		this.nextIndex = nextIndex;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	} 

}
