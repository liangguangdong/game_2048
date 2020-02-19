/**
 * 
 */
package com.cmr.lc;

/**
 * @author liang_gdong
 *
 */
public class Boxinfo {
	
	int expn;
	boolean flag;

	public Boxinfo(int expn, boolean flag) {
		super();
		this.expn = expn;
		this.flag = flag;
	}

	/**
	 * @return the expn
	 */
	public int getExpn() {
		return expn;
	}

	/**
	 * @param expn the expn to set
	 */
	public void setExpn(int expn) {
		this.expn = expn;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
