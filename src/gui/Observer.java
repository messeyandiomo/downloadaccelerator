package gui;

public interface Observer {
	public void update(boolean complete, boolean suspend, long infos, boolean shutdown);
}
