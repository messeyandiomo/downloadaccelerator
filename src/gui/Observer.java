package gui;

import java.util.ArrayList;

public interface Observer {
	public void update(boolean complete, boolean suspend, ArrayList<Integer> subdownloadnumbersnotcomplete, long infos);
}
