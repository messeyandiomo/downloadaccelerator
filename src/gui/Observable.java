package gui;

public interface Observable {
	public void addObserver(Observer obs);
	public void updateObserver();
	public void delObserver();
	public void initObserver();
}
