package com.cmr.lc;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 
 * @author liang_gdong
 *
 */
@SuppressWarnings("serial")
public class Help extends JDialog {
	private Image img;
	private int tag = 0;
	private JLabel b = new JLabel();
	private String[] image = new String[3];

	public Help() {
		setTitle("How to play");// ���ô������
		img = Toolkit.getDefaultToolkit().getImage("title.png");// ����ͼ��
		setModal(true);// ����Ϊģ̬����
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(img);
		setSize(400, 400);
		setResizable(false);
		setLocationRelativeTo(null);

		image[0] = "bug0.png";
		image[1] = "bug1.png";
		image[2] = "bug2.png";
		Icon bug = new ImageIcon(image[0]);
		b = new JLabel("���   >  ������", bug, SwingConstants.CENTER);
		b.setFont(new Font("΢���ź�", Font.BOLD, 15));
		b.setHorizontalTextPosition(SwingConstants.CENTER);
		b.setVerticalTextPosition(SwingConstants.BOTTOM);
		add(b, BorderLayout.CENTER);

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_RIGHT) {
					tag++;
					if (tag > 2)
						tag = 0;
					b.setIcon(new ImageIcon(image[tag]));
				}
			}
		});
		setVisible(true);
	}
}
