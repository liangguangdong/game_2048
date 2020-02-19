/**
 * 
 */
package com.cmr.lc;

import javax.swing.JButton;

/**
 * @author liang_gdong
 *
 */
public class Box {

	public Box(JButton button, int expn, boolean flag) {
		super();
		this.button = button;
		this.expn = expn;
		this.flag = flag;
	}

	JButton button;
	int expn;
	boolean flag;

	public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}

	public int getExpn() {
		return expn;
	}

	public void setExpn(int expn) {
		this.expn = expn;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
