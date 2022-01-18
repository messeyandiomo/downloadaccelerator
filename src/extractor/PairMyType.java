package extractor;

class PairMyType {
	
	private MyType res = null;
	private Boolean abort = false;
	
	public PairMyType(MyType v, Boolean abort) {
		this.setRes(v);
		this.setAbort(abort);
	}

	public MyType getRes() {
		return res;
	}

	public void setRes(MyType v) {
		this.res = v;
	}

	public Boolean getAbort() {
		return abort;
	}

	public void setAbort(Boolean abort) {
		this.abort = abort;
	}
}
