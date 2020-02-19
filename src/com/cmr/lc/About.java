package com.cmr.lc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class About extends JDialog {
    private static final long serialVersionUID = 4693799019369193520L;
    private JPanel contentPane;
    private Font f1 = new Font("微软雅黑",Font.PLAIN,15);
	private Font f2 = new Font("微软雅黑",Font.PLAIN,20);
	private ImageIcon icon;
	private JLabel label;
    public About() {
        setTitle("关于");//设置窗体标题
        Image img=Toolkit.getDefaultToolkit().getImage("title.png");//窗口图标
        setIconImage(img);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);//设置为模态窗口
        setSize(410,380);
        setResizable(false);
        setLocationRelativeTo(null);
        contentPane = new JPanel();// 创建内容面板
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        ShadePanel shadePanel = new ShadePanel();// 创建渐变背景面板
        contentPane.add(shadePanel, BorderLayout.CENTER);// 添加面板到窗体内容面板
        shadePanel.setLayout(null);
        
        JTextArea J1 = new JTextArea("版本：2020_02_14_007.0.0\n开发者：LGD\n开发语言：Java\n"
    			+ "Email: liang_gdong@163.com");
        J1.setFocusable(false);
    	J1.setFont(f2);
    	J1.setEditable(false);
    	J1.setOpaque(false);//背景透明
    	shadePanel.add(J1);
    	J1.setBounds(10, 10, 400, 180);
    	icon = new ImageIcon("title.png");
    	icon.setImage(icon.getImage().getScaledInstance(120,120,Image.SCALE_SMOOTH));//保持图片的清晰
    	label = new JLabel(icon);
    	shadePanel.add(label);
    	label.setBounds(270, 0, 130, 130);
    	
    	JPanel p = new JPanel();
    	p.setBounds(5, 130, 395, 1);
	    p.setBorder(BorderFactory.createLineBorder(Color.black));
	    shadePanel.add(p);
	    
	    JLabel J2 = new JLabel("欢迎访问我的主页:");
	    J2.setBounds(10, 145, 200, 30);
	    J2.setFont(f2);
	    shadePanel.add(J2);
	    
    	JLabel MyGithub_Label = new JLabel("Github:");
    	MyGithub_Label.setFont(f2);
    	final JLabel MyGithub = new JLabel("https://github.com/");
    	MyGithub.setFont(f2);
    	MyGithub.setBackground(Color.white);
    	MyGithub.addMouseListener(new InternetMonitor());
    	JLabel MyCnBlog_Label = new JLabel("博客园:");
    	MyCnBlog_Label.setFont(f2);
    	final JLabel MyCnBlog = new JLabel("https://blog.csdn.net/aigus");
    	MyCnBlog.setFont(f2);
    	MyCnBlog.addMouseListener(new InternetMonitor());
    	JTextArea Copyright = new JTextArea("     	Copyright @LGD 2020.\n   	  All rights reserved.");
    	Copyright.setFocusable(false);
    	Copyright.setOpaque(false);
    	Copyright.setFont(f1);
    	Copyright.setEditable(false);
    	
    	shadePanel.add(MyGithub_Label);
    	MyGithub_Label.setBounds(10, 180, 400, 20);
    	shadePanel.add(MyGithub);
    	MyGithub.setBounds(10, 200, 400, 30);
    	shadePanel.add(MyCnBlog_Label);
    	MyCnBlog_Label.setBounds(10, 240, 400, 25);
    	shadePanel.add(MyCnBlog);
    	MyCnBlog.setBounds(10, 265, 400, 30);
    	shadePanel.add(Copyright);
    	Copyright.setBounds(10, 300, 400, 50);
       
    	setVisible(true);
    }
    
    public static void main(String[] args) {
		new About();
	}
}

