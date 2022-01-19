package extractor;

import java.util.ArrayList;

public class MyType {
	
	private String str = null;
	private Integer nbr = null;
	private ArrayList<String> list = null;
	private String[] array = null;
	private MyObject object = null;
	private MyJSONObject json = null;
	private MyDict dict = null;
	private MyDecryptFunctCaller functCaller = null;

	public MyType(String v) {
		this.setStr(v);
		this.setDict(null);
		this.setJson(null);
		this.setList(null);
		this.setNbr(null);
		this.setArray(null);
		this.setObject(null);
		this.setFunctCaller(null);
	}
	
	public MyType(ArrayList<String> v) {
		this.setList(v);
		this.setStr(null);
		this.setDict(null);
		this.setJson(null);
		this.setNbr(null);
		this.setArray(null);
		this.setObject(null);
		this.setFunctCaller(null);
	}
	
	public MyType(MyDict v) {
		this.setList(null);
		this.setStr(null);
		this.setDict(v);
		this.setJson(null);
		this.setNbr(null);
		this.setArray(null);
		this.setObject(null);
		this.setFunctCaller(null);
	}
	
	public MyType(MyJSONObject v) {
		this.setList(null);
		this.setStr(null);
		this.setDict(null);
		this.setJson(v);
		this.setNbr(null);
		this.setArray(null);
		this.setObject(null);
		this.setFunctCaller(null);
	}
	
	public MyType(Integer v) {
		this.setNbr(v);
		this.setList(null);
		this.setStr(null);
		this.setDict(null);
		this.setJson(null);
		this.setArray(null);
		this.setObject(null);
		this.setFunctCaller(null);
	}
	
	public MyType(String[] array) {
		this.setArray(array);
		this.setNbr(null);
		this.setList(null);
		this.setStr(null);
		this.setDict(null);
		this.setJson(null);
		this.setObject(null);
		this.setFunctCaller(null);
	}
	
	public MyType(MyObject v) {
		this.setArray(null);
		this.setNbr(null);
		this.setList(null);
		this.setStr(null);
		this.setDict(null);
		this.setJson(null);
		this.setObject(v);
		this.setFunctCaller(null);
	}
	
	public MyType(MyDecryptFunctCaller v) {
		this.setFunctCaller(v);
		this.setArray(null);
		this.setNbr(null);
		this.setList(null);
		this.setStr(null);
		this.setDict(null);
		this.setJson(null);
		this.setObject(null);
	}
	
	public String type() {
		
		String result = "";
		if(str != null)
			result = str.getClass().getName();
		else if(list != null)
			result = list.getClass().getName();
		else if(dict != null)
			result = dict.getClass().getName();
		else if(json != null)
			result = json.getClass().getName();
		else if(nbr != null)
			result = nbr.getClass().getName();
		else if(array != null)
			result = array.getClass().getName();
		else if(object != null)
			result = object.getClass().getName();
		else if(functCaller != null)
			result = functCaller.getClass().getName();
		
		return result;
	}
	
	public Object val() {
		
		Object result = null;
		if(str != null)
			result = str;
		else if(list != null)
			result = list;
		else if(dict != null)
			result = dict;
		else if(json != null)
			result = json;
		else if(nbr != null)
			result = nbr;
		else if(array != null)
			result = array;
		else if(object !=null)
			result = object;
		else if(functCaller != null)
			result = functCaller;
		
		return result;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	public MyDict getDict() {
		return dict;
	}

	public void setDict(MyDict dict) {
		this.dict = dict;
	}

	public MyJSONObject getJson() {
		return json;
	}

	public void setJson(MyJSONObject json) {
		this.json = json;
	}

	public Integer getNbr() {
		return nbr;
	}

	public void setNbr(Integer nbr) {
		this.nbr = nbr;
	}
	
	public String[] getArray() {
		return array;
	}
	
	public void setArray(String[] array) {
		this.array = array;
	}
	
	public MyObject getObject() {
		return object;
	}

	public void setObject(MyObject object) {
		this.object = object;
	}
	
	public MyDecryptFunctCaller getFunctCaller() {
		return functCaller;
	}
	
	public void setFunctCaller(MyDecryptFunctCaller functcaller) {
		this.functCaller = functcaller;
	}

}

