package extractor;

import java.util.function.Function;

public class Operator {
	

	public static Function<MyType[], MyType> or = new Function<MyType[], MyType>() {
		
		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() | arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() | Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) | arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) | Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> xor = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() ^ arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() ^ Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) ^ arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) ^ Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> and = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() & arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() & Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) & arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) & Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> rshift = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() >> arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() >> Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) >> arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) >> Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> lshift = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() << arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() << Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) << arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) << Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> sub = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() - arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() - Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) - arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) - Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> add = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() + arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() + Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) + arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) + Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> mod = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() % arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() % Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) % arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) % Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> mul = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				toReturn = new MyType(arg0[0].getNbr() * arg0[1].getNbr());
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(arg0[0].getNbr() * Integer.parseInt(arg0[1].getStr()));
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) * arg0[1].getNbr());
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				toReturn = new MyType(Integer.parseInt(arg0[0].getStr()) * Integer.parseInt(arg0[1].getStr()));
			return toReturn;
		}
	};
	
	public static Function<MyType[], MyType> truediv = new Function<MyType[], MyType>() {

		@Override
		public MyType apply(MyType[] arg0) {
			// TODO Auto-generated method stub
			MyType toReturn = null;
			String stringval = null;
			
			if((arg0[0].getNbr() != null) && (arg0[1].getNbr() != null))
				stringval = (arg0[0].getNbr() / arg0[1].getNbr()) + "";
			else if((arg0[0].getNbr() != null) && (Common.isNumeric(arg0[1].getStr())))
				stringval = (arg0[0].getNbr() / Integer.parseInt(arg0[1].getStr())) + "";
			else if((Common.isNumeric(arg0[0].getStr())) && (arg0[1].getNbr() != null))
				stringval = (Integer.parseInt(arg0[0].getStr()) / arg0[1].getNbr()) + "";
			else if((Common.isNumeric(arg0[0].getStr())) && (Common.isNumeric(arg0[1].getStr())))
				stringval = (Integer.parseInt(arg0[0].getStr()) / Integer.parseInt(arg0[1].getStr())) + "";
			
			if(stringval != null) {
				if(Common.isNumeric(stringval))
					toReturn = new MyType(Integer.parseInt(stringval));
				else
					toReturn = new MyType(stringval);
			}
			return toReturn;
		}
	};

	private Operator() {
		// TODO Auto-generated constructor stub
	}

}
