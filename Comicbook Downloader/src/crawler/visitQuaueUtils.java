package crawler;

import java.util.HashSet;
import java.util.LinkedList;

public interface visitQuaueUtils {
	 HashSet vistiedQuaue =  new HashSet<String>();
	 LinkedList<String> unvisitedQuaue = new LinkedList<String>();
	 
	 public void setVistiedQuaue();
	 public void getVistiedQuaue();
	 public void setUnvisitedQuaue();
	 public void getUnvisitedQuaue();
	 
}
