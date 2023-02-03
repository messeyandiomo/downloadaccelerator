package decryption;

import java.util.ArrayList;

public class MyType {
	
	private String str = null;
	private Integer nbr = null;
	private ArrayList<MyType> list = null;
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
	
	public MyType(MyType v) {
		
		if(v.getNbr() != null) {
			this.setNbr(v.getNbr());
			this.setStr(null);
			this.setDict(null);
			this.setJson(null);
			this.setList(null);
			this.setArray(null);
			this.setObject(null);
			this.setFunctCaller(null);
		}
		else if(v.getStr() != null) {
			this.setNbr(null);
			this.setStr(v.getStr());
			this.setDict(null);
			this.setJson(null);
			this.setList(null);
			this.setArray(null);
			this.setObject(null);
			this.setFunctCaller(null);
		}
		else if(v.getDict() != null) {
			this.setNbr(null);
			this.setStr(null);
			this.setDict(v.getDict());
			this.setJson(null);
			this.setList(null);
			this.setArray(null);
			this.setObject(null);
			this.setFunctCaller(null);
		}
		else if(v.getJson() != null) {
			this.setNbr(null);
			this.setStr(null);
			this.setDict(null);
			this.setJson(v.getJson());
			this.setList(null);
			this.setArray(null);
			this.setObject(null);
			this.setFunctCaller(null);
		}
		else if(v.getList() != null) {
			this.setNbr(null);
			this.setStr(null);
			this.setDict(null);
			this.setJson(null);
			this.setList(v.getList());
			this.setArray(null);
			this.setObject(null);
			this.setFunctCaller(null);
		}
		else if(v.getArray() != null) {
			this.setNbr(null);
			this.setStr(null);
			this.setDict(null);
			this.setJson(null);
			this.setList(null);
			this.setArray(v.getArray());
			this.setObject(null);
			this.setFunctCaller(null);
		}
		else if(v.getObject() != null) {
			this.setNbr(null);
			this.setStr(null);
			this.setDict(null);
			this.setJson(null);
			this.setList(null);
			this.setArray(null);
			this.setObject(v.getObject());
			this.setFunctCaller(null);
		}
		else if(v.getFunctCaller() != null) {
			this.setNbr(null);
			this.setStr(null);
			this.setDict(null);
			this.setJson(null);
			this.setList(null);
			this.setArray(null);
			this.setObject(null);
			this.setFunctCaller(v.getFunctCaller());
		}
		
	}
	
	public MyType(ArrayList<MyType> v) {
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

	public ArrayList<MyType> getList() {
		return list;
	}

	public void setList(ArrayList<MyType> list) {
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
	
	public MyType get(MyType index) {
		
		MyType toReturn = null;
		
		if(this.getArray() != null) {
			if(index.getNbr() != null)
				toReturn = new MyType(this.getArray()[index.getNbr()]);
			else if(index.getStr() != null)
				toReturn = new MyType(this.getArray()[Integer.parseInt(index.getStr())]);
		}
		else if(this.getList() != null) {
			if(index.getNbr() != null)
				toReturn = new MyType(this.getList().get(index.getNbr()));
			else if(index.getStr() != null)
				toReturn = new MyType(this.getList().get(Integer.parseInt(index.getStr())));
		}
		else if(this.getDict() != null) {
			if(index.getStr() != null)
				toReturn = new MyType(this.getDict().get(index.getStr()));
		}
		else if(this.getObject() != null) {
			if(index.getStr() != null)
				toReturn = new MyType(this.getObject().get(index.getStr()));
		}
		else if(this.getStr() != null) {
			String[] splitThis = this.getStr().split("");
			if(index.getNbr() != null)
				toReturn = new MyType(splitThis[index.getNbr()]);
			else if(Common.isNumeric(index.getStr()))
				toReturn = new MyType(splitThis[Integer.parseInt(index.getStr())]);
		}
		else if(this.getJson() != null) {
			if(index.getStr() != null)
				toReturn = this.getJson().get(index.getStr());
		}
		
		return toReturn;
	}
	
	public MyType set(MyType key, MyType value) {
		
		MyType toReturn = null;
		
		if(this.getArray() != null) {
			if(key.getNbr() != null) {
				if(value.getNbr() != null) {
					this.getArray()[key.getNbr()] = value.getNbr().toString();
					toReturn = value;
				}
				else if(value.getStr() != null) {
					this.getArray()[key.getNbr()] = value.getStr();
					toReturn = value;
				}
			}
		}
		else if(this.getList() != null) {
			if(key.getNbr() != null) {
				if(value.getStr() != null) {
					this.getList().set(key.getNbr(), value);
					toReturn = value;
				}
			}
		}
		else if(this.getStr() != null) {
			if(key.getNbr() != null) {
				String[] splitStr = this.getStr().split("");
				if(value.getNbr() != null) {
					splitStr[key.getNbr()] = value.getNbr().toString();
					toReturn = value;
				}
				else if(value.getStr() != null) {
					splitStr[key.getNbr()] = value.getStr();
					toReturn = value;
				}
				this.setStr(String.join("", splitStr));
			}
		}
		else if(this.getDict() != null) {
			if(value.getObject() != null) {
				this.getDict().put(key.getStr(), value.getObject());
				toReturn = value;
			}
		}
		else if(this.getJson() != null) {
			this.getJson().put(key.getStr(), value);
			toReturn = value;
		}
		else if(this.getObject() != null) {
			if(value.getFunctCaller() != null) {
				this.getObject().put(key.getStr(), value.getFunctCaller());
				toReturn = value;
			}
		}
		
		return toReturn;
	}
	
	public MyType length() {
		
		MyType toReturn = null;
		
		if(this.getObject() != null)
			toReturn = new MyType(this.getObject().size());
		else if(this.getList() != null)
			toReturn = new MyType(this.getList().size());
		else if(this.getArray() != null)
			toReturn = new MyType(this.getArray().length);
		else if(this.getStr() != null)
			toReturn = new MyType(this.getStr().length());
		else if(this.getDict() != null)
			toReturn = new MyType(this.getDict().size());
		else if(this.getJson() != null)
			toReturn = new MyType(this.getJson().size());
		
		return toReturn;
	}
	
	public MyType split() {
		
		MyType toReturn = null;
		
		if(this.getStr() != null)
			toReturn = new MyType(this.getStr().split(""));
		
		return toReturn;
	}
	
	public MyType join(MyType obj) {
		
		MyType toReturn = null;
		
		if(this.getStr() != null) {
			if(obj.getObject() != null)
				toReturn = new MyType(String.join(this.getStr(), obj.getObject().keySet()));
			else if(obj.getList() != null) {
				boolean islistofstring = true;
				ArrayList<String> newlist = new ArrayList<String>();
				for (int i = 0; i < obj.getList().size(); i++) {
					if(obj.getList().get(i).getStr() != null) {
						newlist.add(i, obj.getList().get(i).getStr());
					}
					else {
						islistofstring = false;
						break;
					}
				}
				if(islistofstring)
					toReturn = new MyType(String.join(this.getStr(), newlist));
			}
			else if(obj.getArray() != null)
				toReturn = new MyType(String.join(this.getStr(), obj.getArray()));
		}
		
		return toReturn;
	}
	
	public MyType reverse() {
		
		MyType toReturn = null;
		
		if(this.getObject() != null) {
			MyObject newObject = new MyObject();
			ArrayList<String> keys = new ArrayList<String>(this.getObject().keySet());
			ArrayList<MyDecryptFunctCaller> values = new ArrayList<MyDecryptFunctCaller>(this.getObject().values());
			for (int i = this.getObject().size() - 1; i >= 0 ; i--)
				newObject.put(keys.get(i), values.get(i));
			this.setObject(newObject);
			toReturn = this;
		}
		else if(this.getList() != null) {
			ArrayList<MyType> newList = new ArrayList<>();
			for (int i = this.getList().size() - 1; i >= 0; i--)
				newList.add(this.getList().size() - 1 - i, this.getList().get(i));
			this.setList(newList);
			toReturn = this;
		}
		else if(this.getArray() != null) {
			String[] newArray = new String[this.getArray().length];
			for (int i = newArray.length - 1; i >= 0; i--)
				newArray[newArray.length - 1 - i] = this.getArray()[i];
			this.setArray(newArray);
			toReturn = this;
		}
		else if(this.getStr() != null) {
			String[] splitStr = this.getStr().split("");
			String[] newSplitStr = new String[splitStr.length];
			for (int i = newSplitStr.length - 1; i >= 0; i--)
				newSplitStr[newSplitStr.length - 1 - i] = splitStr[i];
			this.setStr(String.join("", newSplitStr));
			toReturn = this;
		}
		
		return toReturn;
	}
	
	public MyType slice(MyType field) {
		
		MyType toReturn = null;
		
		if((this.getObject() != null) && (field.getStr() != null)){
			ArrayList<String> keys = new ArrayList<String>(this.getObject().keySet());
			ArrayList<MyDecryptFunctCaller> values = new ArrayList<MyDecryptFunctCaller>(this.getObject().values());
			int j = 0;
			for (int i = 0; i < this.getObject().size(); i++)
				if(keys.get(i) == field.getStr()) {
					j = i;
					break;
				}
			MyObject newObject = new MyObject(this.getObject().size() - j);
			for (int i = j; i < this.getDict().size(); i++)
				newObject.put(keys.get(i), values.get(i));
			toReturn = new MyType(newObject);
		}
		else if((this.getList() != null)  && (field.getNbr() != null)){
			int j = field.getNbr();
			ArrayList<MyType> newList = new ArrayList<>(this.getList().size() - j);
			for (int i = j; i < this.getList().size(); i++)
				newList.add(i - j, this.getList().get(i));
			toReturn = new MyType(newList);
		}
		else if((this.getArray() != null)  && (field.getNbr() != null)) {
			int j = field.getNbr();
			String[] newArray = new String[this.getArray().length - j];
			for (int i = j; i < this.getArray().length; i++)
				newArray[i - j] = this.getArray()[i];
			toReturn = new MyType(newArray);
		}
		else if((this.getStr() != null)  && (field.getNbr() != null))
			toReturn = new MyType(this.getStr().substring(field.getNbr()));
		
		return toReturn;
	}
	
	public MyType splice(MyType mytypeidx, MyType destiny) {
		
		MyType toReturn = null;
		
		Integer index = mytypeidx.getNbr();
		Integer howMany = null;
		MyType itemToAdd = null;
		
		if(destiny.getNbr() != null)
			howMany = destiny.getNbr();
		else if(destiny.getStr() != null)
			itemToAdd = destiny;
		
		if(this.getList() != null) {
			if(howMany != null) {
				int sizeSplice = Math.min(index + howMany, this.getList().size());
				ArrayList<MyType> returnList = new ArrayList<>(sizeSplice);
				
				for (int i = index; i < sizeSplice; i++)
					returnList.add(i - index, this.getList().remove(index.intValue()));
				toReturn = new MyType(returnList);
			}
			else if(itemToAdd != null) {
				int sizeSplice = this.getList().size() + 1;
				ArrayList<MyType> newList = new ArrayList<>(sizeSplice);
				ArrayList<MyType> returnList = null;
				
				for (int i = 0; i < sizeSplice; i++) {
					if(i < index)
						newList.add(i, this.getList().get(i));
					else if(i == index)
						newList.add(index, itemToAdd);
					else if(i > index)
						newList.add(i, this.getList().get(i - 1));
				}
				this.setList(newList);
				toReturn = new MyType(returnList);
			}
			System.out.println("splice array entry");
			
			return toReturn;
		}
		if(this.getArray() != null) {
			if(howMany != null) {
				int sizeSplice = Math.min(index + howMany, this.getArray().length);
				int sizeNewArray = this.getArray().length - howMany;
				String[] newArray = new String[sizeNewArray];
				String[] returnArray = new String[sizeSplice];
				
				for (int i = index; i < sizeSplice; i++)
					returnArray[i - index] = this.getArray()[i];
				
				for (int i = 0; i < this.getArray().length; i++) {
					if(i < index)
						newArray[i] = this.getArray()[i];
					if(i >= sizeSplice)
						newArray[i - howMany] = this.getArray()[i];
				}
				
				this.setArray(newArray);
				toReturn = new MyType(returnArray);
			}
			else if(itemToAdd != null) {
				int sizeSplice = this.getArray().length + 1;
				String[] newArray = new String[sizeSplice];
				String[] returnArray = null;
				
				for (int i = 0; i < sizeSplice; i++) {
					if(i < index)
						newArray[i] = this.getArray()[i];
					else if(i == index)
						newArray[index] = itemToAdd.getStr();
					else if(i > index)
						newArray[i] = this.getArray()[i - 1];
				}
				this.setArray(newArray);
				toReturn = new MyType(returnArray);
			}
			
			return toReturn;
		}
		if(this.getStr() != null) {
			if(howMany != null) {
				int sizeSplice = Math.min(index + howMany, this.getStr().length());
				String newStr = new String(this.getStr().substring(0, index.intValue()).concat(this.getStr().substring(sizeSplice)));
				
				this.setStr(newStr);
				toReturn = new MyType(this.getStr().substring(index.intValue(), sizeSplice));
			}
			else if(itemToAdd.getStr() != null) {
				String newStr = new String(this.getStr().substring(0, index.intValue()).concat(itemToAdd.getStr()).concat(this.getStr().substring(index.intValue())));
				String returnString = null;
				
				this.setStr(newStr);
				toReturn = new MyType(returnString);
			}
			
			return toReturn;
		}
		
		return toReturn;
	}
	
	

}

