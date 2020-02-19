/**
 * 
 */
package com.cmr.lc;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;

/**
 * @author liang_gdong
 *
 */
public class InternetMonitor extends MouseAdapter {
	public void mouseClicked(MouseEvent e) {
		JLabel JLabel_temp = (JLabel) e.getSource();
		String J_temp = JLabel_temp.getText();
		System.out.println(J_temp);
		URI uri;
		try {
			uri = new URI(J_temp);
			Desktop desk = Desktop.getDesktop();
			if (Desktop.isDesktopSupported() && desk.isSupported(Desktop.Action.BROWSE)) {
				try {
					desk.browse(uri);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent e) {
		JLabel JLabel_temp = (JLabel) e.getSource();
		JLabel_temp.setForeground(Color.red);
	}

	public void mouseExited(MouseEvent e) {
		JLabel JLabel_temp = (JLabel) e.getSource();
		JLabel_temp.setForeground(Color.blue);
	}
}
