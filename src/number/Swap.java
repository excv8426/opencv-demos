package number;

import java.io.Serializable;

public class Swap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8847531990453731923L;
	
	private int i;
	private int j;
	
	public Swap(int i,int j){
		this.i=i;
		this.j=j;
	}
	
	public int getI() {
		return i;
	}
	
	public void setI(int i) {
		this.i = i;
	}
	
	public int getJ() {
		return j;
	}
	
	public void setJ(int j) {
		this.j = j;
	}
	
}
