/**
 * 
 */
package com.cmr.lc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author liang_gdong
 *
 */
public class MainWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3733117110572934516L;
	private static int row = 4,col = 4;//����Ĭ�϶���4
	private static Box[][] box = new Box[row][col];
	private static Boxinfo[][] history = new Boxinfo[row][col];//������¼�������ʷ����
	private static Boxinfo[][] present = new Boxinfo[row][col];//������¼����ĵ�ǰ����
	private static boolean swap = false;//�ж� present �Ƿ� ���� history
	private static boolean show = true;//�жϿ�ʼ�Ƿ�����ϴμ�¼
	private Color[] color = new Color[12];//2048 ÿ����ֵ�ķ���һ����ɫ
	private JPanel J2;
	private JTextField J12,J14;
	private JLabel showtime;
	private JRadioButtonMenuItem modeItems[];
	private ButtonGroup modeGroup;
	private int count = 0;//��¼��ǰ�÷�,��ʼ�÷�Ϊ0
	private int best_score = 0;//��¼��ѵ÷֣���ʼ��Ϊ0��ÿ�ο�ʼʱ��txt�ж�ȡ����ˢ�¸�ֵ
	private String temp = "";
	//��������洢ʱ����
	private int hour =0;
	private int min =0;
	private int sec =0 ;
	private boolean isRun = true;
    
	public MainWindow(){
		super("2048 By LGD");
		Image img = Toolkit.getDefaultToolkit().getImage("title.png");//����ͼ��
		setIconImage(img);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(500,600);
		setFocusable(true);
		
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		setLayout(new BorderLayout());
		setResizable(false);
		JPanel J1 = new JPanel();
		J1.setBackground(Color.gray);
		J2 = new JPanel();
		J1.setLayout(new GridLayout(1,4,2,2));
		J2.setLayout(new GridLayout(row,col,10,10));
		J2.setFocusable(true);
		JPanel J3 = new JPanel();
		J3.setLayout(new GridLayout(1,2,2,2));
		JButton J31 = new JButton("RESTART");
		JButton J32 = new JButton("UNDO");
		
		//����ģ�������
		J31.addActionListener( 
				new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int result=JOptionPane.showConfirmDialog(null, "Are you sure to restart the game?", "Information", JOptionPane.YES_NO_OPTION);
					if(result==JOptionPane.YES_NO_OPTION)
					{
						for(int i = 0;i < row;i++)
							for(int j = 0;j < col;j++)
							{
								box[i][j].flag = false;
								box[i][j].expn = 0;
								box[i][j].button.setVisible(false);
							}
						best_score = (best_score > count )? best_score:count;//�жϵ�ǰ�÷��Ƿ������ʷ��ѵ÷֣�ˢ����ѵ÷�
						temp += best_score;
						J14.setText(temp);
						temp = "";
						count = 0;
						J12.setText("0");
						//���¿�ʼ�����������������
						isRun = false;
						hour =0;
					    min =0;
					    sec =0 ;
						isRun = true;
						produceRandom();
						produceRandom();
						history = record_box();//��¼��ʼ�������̼�¼��history������
						present = record_box();//��¼��ʼ�������̼�¼��present������
						swap = false;//�ǵ�����
						J31.setFocusable(false);
						J32.setFocusable(false);
						setFocusable(true);
					}
				}
			}
		);
		
		//���ص���һ��ģ��
		J32.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						for(int i = 0;i < row;i++)
							for(int j = 0;j < col;j++)
								{
									box[i][j].expn = history[i][j].expn;
									box[i][j].flag = history[i][j].flag;
									box[i][j].button.setVisible(box[i][j].flag);
									temp += binary(box[i][j].expn);
									box[i][j].button.setText(temp);
									box[i][j].button.setBackground(color[box[i][j].expn]);
									temp = "";
								}
						swap = false;
						history = record_box();
						present = record_box();
						J32.setFocusable(false);
						J31.setFocusable(false);
						setFocusable(true);
					}
				}
		);
		
		J3.add(J31);
		J3.add(J32);
		JPanel J4 = new JPanel();
		J4.setLayout(new GridLayout(2,1,2,2));
		J4.add(J1);
		JPanel timePanel = new JPanel();
		timePanel.setBackground(Color.white);
		timePanel.setLayout(new GridLayout(1,2,2,2));
		JLabel timeLabel = new JLabel("                     Time used:");
		showtime = new JLabel("   00:00:00");
		showtime.setBackground(Color.gray);
		timePanel.add(timeLabel);
		timePanel.add(showtime);
		J4.add(timePanel);
		JLabel J11 = new JLabel("    SCORE:");
		J12 = new JTextField("0");
		J12.setBackground(Color.cyan);
		J12.setEditable(false);
		JLabel J13 = new JLabel("     BEST:");
		J14 = new JTextField("0");
		J14.setBackground(Color.cyan);
		J14.setEditable(false);
		J11.setFont(new Font("΢���ź�",Font.BOLD,20));
		J12.setFont(new Font("΢���ź�",Font.BOLD,20));
		J13.setFont(new Font("΢���ź�",Font.BOLD,20));
		J14.setFont(new Font("΢���ź�",Font.BOLD,20));
		J31.setFont(new Font("΢���ź�",Font.BOLD,20));
		J32.setFont(new Font("΢���ź�",Font.BOLD,20));
		timeLabel.setFont(new Font("΢���ź�",Font.BOLD,20));
		showtime.setFont(new Font("΢���ź�",Font.BOLD,20));
		J1.add(J11);
		J1.add(J12);
		J1.add(J13);
		J1.add(J14);
		add(J4,BorderLayout.NORTH);
		add(J2,BorderLayout.CENTER);
		add(J3,BorderLayout.SOUTH);
		for(int i = 0;i < row;i++)
			for(int j = 0; j < col;j++)
			{
				box[i][j] = new Box(new JButton(), 0, false);
				box[i][j].button.setFont(new Font("΢���ź�",Font.BOLD,40));
				box[i][j].button.setVisible(false);
				J2.add(box[i][j].button);
			}
		setLocationRelativeTo(null);//��ʾ����Ļ����
		setVisible(true);
		addKeyListener(new KeyMonitor());
		color[1] = Color.yellow;
		color[2] = Color.blue;
		color[3] = Color.green;
		color[4] = Color.gray;
		color[5] = Color.red;
		color[6] = Color.pink;
		color[7] = Color.magenta;
		color[8] = Color.orange;
		color[9] = new Color(128,0,128);
		color[10] = Color.white;
		color[11] = new Color(0,0,0);
		
		//�˵�������
		JMenuBar bar = new JMenuBar();
		bar.setBackground(Color.white);
		setJMenuBar(bar);
		JMenu fileMenu = new JMenu("File");
		JMenu setMenu = new JMenu("Settings");
		JMenu modeMenu = new JMenu("Modes");
		bar.add(fileMenu);
		bar.add(setMenu);
		setMenu.add(modeMenu);
		
		//ѡ��ģʽ
		String modes[] = {"4 * 4","5 * 5","6 * 6"};
		modeItems = new JRadioButtonMenuItem[modes.length];
		modeGroup = new ButtonGroup();
		for(int i = 0;i < modes.length;i++)
		{
			modeItems[i] = new JRadioButtonMenuItem(modes[i]);
			modeMenu.add(modeItems[i]);
			modeGroup.add(modeItems[i]);
			modeItems[i].addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e){
							//ֹͣ��ʱ
							isRun = false;
							String temp = "";
							for(int i = 0;i < modeItems.length;i++)
							{
								if(modeItems[i].isSelected()){
									temp = modes[i];
									row = temp.charAt(0) - 48;
									col = temp.charAt(temp.length()-1) - 48;
									System.out.println("row = " + row);
									System.out.println("col = " + col);
								}
							}
							//���¿�ʼ�����������������
							J2.setVisible(false);
							J2 = new JPanel();
							J2.setFocusable(true);
							add(J2,BorderLayout.CENTER);
							box = new Box[row][col];
							for(int i = 0;i < row;i++)
								for(int j = 0; j < col;j++)
								{
									box[i][j] = new Box(new JButton(), 0, false);
									box[i][j].button.setFont(new Font( "΢���ź�",Font.BOLD, 120/row) );
									box[i][j].button.setVisible(false);
									J2.add(box[i][j].button);
								}
							history = new Boxinfo[row][col];//������¼�������ʷ����
							present = new Boxinfo[row][col];//������¼����ĵ�ǰ����
							J2.setLayout(new GridLayout(row,col,10,10));
							//���¿�ʼ��ʱ
							hour =0;
						    min =0;
						    sec =0 ;
							isRun = true;
							produceRandom();
							produceRandom();
							history = record_box();//��¼��ʼ�������̼�¼��history������
							present = record_box();//��¼��ʼ�������̼�¼��present������
							swap = false;//�ǵ�����
						}
					}
			);
		}
		
		
		//File�˵���ģ��
		JMenuItem aboutItem = new JMenuItem("About");
		JMenuItem helpItem = new JMenuItem("Help");
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(aboutItem);
		fileMenu.add(helpItem);
		fileMenu.add(exitItem);

		aboutItem.addActionListener(
				new ActionListener(){
				public void actionPerformed(ActionEvent e){
					new About();
				}
			}
		);
		helpItem.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						new Help();
					}
				}
		);
		//��ע�⣬�˰�ť��ֱ���˳����˳�ʱ�����¼��ĵ÷ֺ���ʷ��¼
		exitItem.addActionListener(
				new ActionListener(){
				public void actionPerformed(ActionEvent e){
					setVisible(false);
					System.exit(0);
				}
			}
		);
		//ȷ���Ƿ��˳�����
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				 int result=JOptionPane.showConfirmDialog(null, "Are you sure to exit the game?", "Information", JOptionPane.YES_NO_OPTION);
				 if(result==JOptionPane.YES_NO_OPTION)
				 {
					try {
						//ÿ���˳�����֮ǰ������ѵ÷֣��Ա���һ����������ʱʹ��
						if(J14.getText().equals(""))//����˳���ʱ��BEST��ʾΪ�գ�˵����ǰbestΪ0
						{
							temp += count;
							write(temp,"best_score.txt");
							temp = "";
						}
						else
						{
							temp = J14.getText();
							best_score = StringToInt(temp);
							best_score = (best_score > count)? best_score:count;
							temp = "";
							temp += best_score;
							write(temp,"best_score.txt");
							temp = "";
						}
						//ÿ���˳�����֮ǰ�����˳�֮ǰ�ļ�¼���Ա���һ����������ʱʹ��
						temp += "$ " + row + " " + col + " ";
						for(int i = 0;i < row;i++)
							for(int j = 0;j < col;j++)
							{
								temp += box[i][j].expn + " ";
							}
						
						write(temp,"history.dat");
						temp = "";
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					setVisible(false);
					System.exit(0);
				 }
				 else
				 {
					 setVisible(true);
				 }
			}
		});
		
		/*---------------------------��Ϸ����------------------------------*/
		try {
			read_history();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		if(show)
		{
			int result=JOptionPane.showConfirmDialog(null, "�Ƿ�����ϴ���Ϸ?", "Information", JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_NO_OPTION)
			{
				for(int i = 0; i < row;i++)
					for(int j = 0;j < col;j++)
					{
						box[i][j].button.setVisible(box[i][j].flag);
					}
				modeItems[row-4].setSelected(true);
			}
			else
			{
				for(int i = 0;i < row;i++)
					for(int j = 0;j < col;j++)
					{
						box[i][j].flag = false;
						box[i][j].expn = 0;
						box[i][j].button.setVisible(false);
					}
				//����ʼ����ʱ�������2������
				modeItems[0].setSelected(true);//Ĭ����4*4ģʽ
				produceRandom();
				produceRandom();
			}
		}
		else
		{
			for(int i = 0;i < row;i++)
				for(int j = 0;j < col;j++)
				{
					box[i][j].flag = false;
					box[i][j].expn = 0;
					box[i][j].button.setVisible(false);
				}
			//����ʼ����ʱ�������2������
			produceRandom();
			produceRandom();
		}
		history = record_box();//��¼��ʼ�������̼�¼��history������
		present = record_box();//��¼��ʼ�������̼�¼��present������
		
		//��ʱ����ʼ��ʱ
		new Timer();
		
		//��ʼʱ��ȡtxt�е����ݣ���Ϊbest�ĳ�ʼֵ
		try {
			temp = read_best();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		J14.setText(temp);
		best_score = StringToInt(temp);
		temp = "";
	}
	
	void print(Boxinfo[][] b){
		for(int i = 0;i < row;i++)
		{
			for(int j = 0;j < col;j++)
				System.out.print(b[i][j].flag + " ");
			System.out.println();
		}
	}
	
	Boxinfo[][] record_history(Boxinfo[][] b){
		Boxinfo[][] temp = new Boxinfo[row][col];
		for(int i = 0;i < row;i++)
			for(int j = 0;j < col;j++)
			{
				temp[i][j] = new Boxinfo(b[i][j].expn, b[i][j].flag);
			}
		return temp;
	}
	
	Boxinfo[][] record_box(){
		Boxinfo[][] temp = new Boxinfo[row][col];
		for(int i = 0;i < row;i++)
			for(int j = 0;j < col;j++)
			{
				temp[i][j] = new Boxinfo(box[i][j].expn, box[i][j].flag);
			}
		return temp;
	}
	
	String read_best() throws IOException {
		String best = "0";
		FileInputStream fis = new FileInputStream("best_score.txt");
		BufferedReader dis = new BufferedReader(new InputStreamReader(fis));
		try
		{
			best = dis.readLine();
		}
		finally
		{
			dis.close();
		}
		return best;
	}
	
	void read_history() throws IOException {
		String s = "";
		int ptr = 0;
		String temp = "";
		FileInputStream fis = new FileInputStream("history.dat");
		BufferedReader dis = new BufferedReader(new InputStreamReader(fis));
		try
		{
			s = dis.readLine();
			if(s.equals(""))//history����Ϊ��
			{
				show = false;
			}
			else
			{
				if(s.charAt(ptr) != '$') show = false;
				else//��һ������Ϊ$��ʾ�����ϴμ�¼
				{
					//�ж��ϴμ�¼���Ƿ���ڷǷ��ַ�
					for(int i = 2;i < s.length();i++)
					{
						if((s.charAt(i) >= 48 && s.charAt(i) <= 57) || s.charAt(i) == 32) {}
						else
						{
							show = false;
							return;
						}
					}
				}
			}
			
			if(show)
			{
				ptr += 2;
				row = s.charAt(ptr) - 48;
				ptr += 2;
				col = s.charAt(ptr) - 48;
				ptr += 2;
				if(row != 4 || col != 4)//��ʷ��¼�е�row����col�����ڳ����ʼֵ
				{
					J2.setVisible(false);
					J2 = new JPanel();
					J2.setFocusable(true);
					add(J2,BorderLayout.CENTER);
					box = new Box[row][col];
					for(int i = 0;i < row;i++)
						for(int j = 0; j < col;j++)
						{
							box[i][j] = new Box(new JButton(), 0, false);
							box[i][j].button.setFont(new Font( "΢���ź�",Font.BOLD, 120/row) );
							box[i][j].button.setVisible(false);
							J2.add(box[i][j].button);
						}
					history = new Boxinfo[row][col];//������¼�������ʷ����
					present = new Boxinfo[row][col];//������¼����ĵ�ǰ����
					J2.setLayout(new GridLayout(row,col,10,10));
				}
				for(int i = 0;i < row;i++)
					for(int j = 0;j < col;j++)
					{
						if(s.charAt(ptr+1) != ' ')
						{
							temp += s.charAt(ptr);
							temp += s.charAt(ptr+1);
							box[i][j].expn = StringToInt(temp);
							temp = "";
							box[i][j].flag = true;
							temp += binary(box[i][j].expn);
							box[i][j].button.setText(temp);
							temp = "";
							box[i][j].button.setBackground(color[box[i][j].expn]);
							ptr += 3;
						}
						else
						{
							box[i][j].expn = s.charAt(ptr) - 48;
							if(box[i][j].expn > 0)
							{
								box[i][j].flag = true;
								temp += binary(box[i][j].expn);
								box[i][j].button.setText(temp);
								temp = "";
								box[i][j].button.setBackground(color[box[i][j].expn]);
							}
							else
							{
								box[i][j].flag = false;
							}
							ptr += 2;
						}
					}
			}
		}
		finally
		{
			dis.close();
		}
	}
	
	void write(String s,String filepath) throws IOException {
		FileOutputStream fos = new FileOutputStream(filepath);
		DataOutputStream dos = new DataOutputStream(fos);
		try
		{
			dos.writeBytes(s);
		}
		finally
		{
			dos.close();
		}
	}

	int StringToInt(String s){
		int a = 0;
		for(int i = 0;i < s.length();i++)
		{
			a += ( (int)s.charAt(i) - 48 ) * decimal(s.length()-1-i);
		}
		return a;
	}

    int decimal(int n){ //����10��n�η�
		int s = 1;
		for(int i = 0;i < n;i++)
		{
			s *= 10;
		}
		return s;
	}
	
	int binary(int n){ //����2��n�η�
		int s = 1;
		for(int i = 0;i < n;i++)
		{
			s *= 2;
		}
		return s;
	}
	//�ж��Ƿ�Ӯ�˵ķ���
	boolean success(){
		boolean flag = false;
		for(int i = 0;i < row;i++)
			for(int j = 0;j < col;j++)
				if(box[i][j].flag)
				{
					if(box[i][j].expn == 11)//�Ƿ����ĳ�������ֵΪ2^11 = 2048
					{
						flag = true;
						return flag;
					}
				}
		return flag;
	}
	//���������������ķ���
	public void produceRandom(){
		Random rand = new Random();
		String s[] = new String[2];
		s[0] = "2";
		s[1] = "4";
		int x = rand.nextInt(row);
		int y = rand.nextInt(col);
		int z = rand.nextInt(2)+1;//zΪ1����2
		while(box[x][y].flag){
			x = rand.nextInt(row);
			y = rand.nextInt(col);
			if(isfull())//���� 
			{
				System.out.println("FULL!");
				return;
			}
		}
		box[x][y].flag = true;
		box[x][y].expn = z;//������ɵĶ���2����4
		box[x][y].button.setText(s[z-1]);
		box[x][y].button.setBackground(color[z]);
		box[x][y].button.setVisible(true);
		System.out.print(x + " , ");
		System.out.println(y);
	}
	//�жϲ����Ƿ����ķ���
	public boolean isfull()
	{
		boolean tag = true;
		for(int i = 0;i < row;i++)
			for(int j = 0;j < col;j++)
				if(!box[i][j].flag)
				{
					tag = false;
					break;
				}
		return tag;
	}
	//�ж��Ƿ�����ƶ��ķ���
	public boolean movable(String s){
		boolean tag = false;//Ĭ�ϲ����ƶ�
		if(s == "L")//�����ƶ�
		{
			for(int i = 0;i < row;i++)
			{
				for(int j = col-1;j >= 1;j--)//ÿһ�д���������
				{
					if(box[i][j].flag)//����ҵ�һ�����ڵķ���
					{
						if(!box[i][j-1].flag) //�ж�������Ƿ��пո�
						{
							tag = true;//�����ƶ�
							break;
						}
						else //��߲�Ϊ�ո��ʱ���ж��Ƿ���Ժϲ�
						{
							if(box[i][j-1].expn == box[i][j].expn) tag = true;
						}
					}
				}
				if(tag) break;
			}
		}
		else if(s == "R")
		{
			for(int i = 0;i < row;i++)
			{
				for(int j = 0;j < col-1;j++)//ÿһ�д���������
				{
					if(box[i][j].flag)//����ҵ�һ�����ڵķ���
					{
						if(!box[i][j+1].flag) //�ж����ұ��Ƿ��пո�
						{
							tag = true;//�����ƶ�
							break;
						}
						else //�ұ߲�Ϊ�ո��ʱ���ж��Ƿ���Ժϲ�
						{
							if(box[i][j+1].expn == box[i][j].expn) tag = true;
						}
					}
				}
				if(tag) break;
			}
		}
		else if(s == "U")
		{
			for(int i = 0;i < row;i++)
			{
				for(int j = col-1;j >= 1;j--)//ÿһ�д���������
				{
					if(box[j][i].flag)//����ҵ�һ�����ڵķ���
					{
						if(!box[j-1][i].flag) //��ô�ж����ϱ��Ƿ��пո�
						{
							tag = true;//�����ƶ�
							break;
						}
						else //�ϱ߲�Ϊ�ո��ʱ���ж��Ƿ���Ժϲ�
						{
							if(box[j-1][i].expn == box[j][i].expn) tag = true;
						}
					}
				}
				if(tag) break;
			}
		}
		else if(s == "D")
		{
			for(int i = 0;i < row;i++)
			{
				for(int j = 0;j < col-1;j++)//ÿһ�д���������
				{
					if(box[j][i].flag)//����ҵ�һ�����ڵķ���
					{
						if(!box[j+1][i].flag) //�ж����±��Ƿ��пո�
						{
							tag = true;//�����ƶ�
							break;
						}
						else //�±߲�Ϊ�ո��ʱ���ж��Ƿ���Ժϲ�
						{
							if(box[j+1][i].expn == box[j][i].expn) tag = true;
						}
					}
				}
				if(tag) break;
			}
		}
		return tag;
	}
	
	//���¿�ʼ
	public void Restart(){
		if(success())//ÿ���ƶ��궼�ж��Ƿ�Ӯ��
		{
			isRun = false;
			int result=JOptionPane.showConfirmDialog(null, "��ϲ��Ӯ�ˣ��Ƿ������", "Information", JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_NO_OPTION)
			{
				for(int i = 0;i < row;i++)
					for(int j = 0;j < col;j++)
					{
						box[i][j].flag = false;
						box[i][j].expn = 0;
						box[i][j].button.setVisible(false);
					}
				best_score = (best_score > count )? best_score:count;//�жϵ�ǰ�÷��Ƿ������ʷ��ѵ÷֣�ˢ����ѵ÷�
				temp += best_score;
				J14.setText(temp);
				temp = "";
				count = 0;
				J12.setText("0");
				//���¿�ʼ�����������������
				hour =0;
			    min =0;
			    sec =0 ;
				isRun = true;
				produceRandom();
				produceRandom();
				history = record_box();//��¼��ʼ�������̼�¼��history������
				present = record_box();//��¼��ʼ�������̼�¼��present������
				swap = false;//�ǵ�����
			}
		}
		else
		{
			produceRandom();
			if(swap)
			{
				history = record_history(present);
			}
			present = record_box();//ÿ���ƶ���ѵ�ǰ���̼�¼��present������
			swap = true;//swap��Ϊtrue��ʾ���ǿ�ʼ��ĵ�һ���ƶ�
			
		}
	}
	
	//����
	public void Die(){
		
		if(isfull() && !movable("R") && !movable("U") && !movable("D"))//��ǰ�Ĳ����������Ҹ������򶼲����ƶ�
		{
			isRun = false;
			int result=JOptionPane.showConfirmDialog(null, "�Բ��������ˣ��Ƿ�����һ�֣�", "Information", JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_NO_OPTION)
			{
				for(int i = 0;i < row;i++)
					for(int j = 0;j < col;j++)
					{
						box[i][j].flag = false;
						box[i][j].expn = 0;
						box[i][j].button.setVisible(false);
					}
				best_score = (best_score > count )? best_score:count;//�жϵ�ǰ�÷��Ƿ������ʷ��ѵ÷֣�ˢ����ѵ÷�
				temp += best_score;
				J14.setText(temp);
				temp = "";
				count = 0;
				J12.setText("0");
				//���¿�ʼ�����������������
				hour =0;
			    min =0;
			    sec =0 ;
				isRun = true;
				produceRandom();
				produceRandom();
				history = record_box();//��¼��ʼ�������̼�¼��history������
				present = record_box();//��¼��ʼ�������̼�¼��present������
				swap = false;//�ǵ�����
			}
		}
	}
	
	//ʵ���ƶ����ܵ���	
	class KeyMonitor extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int key = e.getKeyCode();
			int x = 0;
			String s = "";
			boolean tag = true;
			if(key == KeyEvent.VK_LEFT)
			{
				x = 0;
				if( movable("L") )//���������ƶ�
				{
					for(int i = 0;i < row;i++)
						for(int j = 0;j < col;j++)
						{
							if( box[i][j].flag )
							{
								for(int k = j+1;k < col;k++)
								{
									if( box[i][k].flag )
									{
										if(box[i][j].expn == box[i][k].expn)//���Ժϲ�
										{
											box[i][j].expn++;
											count += binary(box[i][j].expn);
											box[i][k].flag = false;
											box[i][k].expn = 0;
											j = k;
											new AePlayWave("merge.wav").start();
											tag = false;
											break;
										}
										else
										{
											break;
										}
									}
								}
							}
						}
					
					for(int i = 0;i < row;i++)
					{
						for(int j = 0;j < col;j++)
						{
							if(box[i][j].flag)
							{
								box[i][x].expn = box[i][j].expn;
								box[i][x].flag = true;
								box[i][x].button.setVisible(true);
								s += binary(box[i][x].expn);
								box[i][x].button.setText(s);
								box[i][x].button.setBackground(color[box[i][x].expn]);
								x++;
								s = "";//���s
							}
						}
						for(int k = x; k < col;k++)
						{
							box[i][k].flag = false;
							box[i][k].expn = 0;
							box[i][k].button.setVisible(false);
						}
						x = 0;
					}
					
					//ˢ�µ�ǰ�÷�
					s += count;
					J12.setText(s);
					s = "";
					//ˢ����ѵ÷�
					best_score = (best_score > count)? best_score:count;
					s += best_score;
					J14.setText(s);
					s = "";
					if(tag)
					{
						new AePlayWave("move.wav").start();
					}
					Restart();
				}
				else
				{
					Die();
				}
			}
			else if(key == KeyEvent.VK_RIGHT)
			{
				x = row-1;
				if( movable("R") )//���������ƶ�
				{
					for(int i = 0;i < row;i++)
						for(int j = col-1;j >=0 ;j--)
						{
							if( box[i][j].flag )
							{
								for(int k = j-1;k >= 0;k--)
								{
									if( box[i][k].flag )
									{
										if(box[i][j].expn == box[i][k].expn)
										{
											box[i][j].expn++;
											count += binary(box[i][j].expn);
											box[i][k].flag = false;
											box[i][k].expn = 0;
											j = k;
											new AePlayWave("merge.wav").start();
											tag = false;
											break;
										}
										else
										{
											break;
										}
									}
								}
							}
						}
					for(int i = 0;i < row;i++)
					{
						for(int j = col-1;j >= 0;j--)
						{
							if(box[i][j].flag)
							{
								box[i][x].expn = box[i][j].expn;
								box[i][x].flag = true;
								box[i][x].button.setVisible(true);
								s += binary(box[i][x].expn);
								box[i][x].button.setText(s);
								box[i][x].button.setBackground(color[box[i][x].expn]);
								x--;
								s = "";
							}
						}
						for(int k = x;k >= 0;k--)
						{
							box[i][k].flag = false;
							box[i][k].expn = 0;
							box[i][k].button.setVisible(false);
						}
						x = row-1;
					}
					s += count;
					J12.setText(s);
					s = "";
					//ˢ����ѵ÷�
					best_score = (best_score > count)? best_score:count;
					s += best_score;
					J14.setText(s);
					s = "";
					
					if(tag)
					{
						new AePlayWave("move.wav").start();
					}
					Restart();
				}
				else
				{
					Die();
				}
			}
			else if(key == KeyEvent.VK_UP)
			{
				x = 0;
				if( movable("U") )//���������ƶ�
				{
					for(int i = 0;i < row;i++)
						for(int j = 0;j < col;j++)
						{
							if( box[j][i].flag )
							{
								for(int k = j+1;k < row;k++)
								{
									if( box[k][i].flag )
									{
										if(box[j][i].expn == box[k][i].expn)
										{
											box[j][i].expn++;
											count += binary(box[j][i].expn);
											box[k][i].flag = false;
											box[k][i].expn = 0;
											j = k;
											new AePlayWave("merge.wav").start();
											tag = false;
											break;
										}
										else
										{
											break;
										}
									}
								}
							}
						}
					for(int i = 0;i < row;i++)
					{
						for(int j = 0;j < col;j++)
						{
							if(box[j][i].flag)
							{
								box[x][i].expn = box[j][i].expn;
								box[x][i].flag = true;
								box[x][i].button.setVisible(true);
								s += binary(box[x][i].expn);
								box[x][i].button.setText(s);
								box[x][i].button.setBackground(color[box[x][i].expn]);
								x++;
								s = "";
							}
							else
							{
								box[j][i].button.setVisible(false);
							}
						}
						for(int k = x;k < row;k++)
						{
							box[k][i].flag = false;
							box[k][i].expn = 0;
							box[k][i].button.setVisible(false);
						}
						x = 0;
					}
					s += count;
					J12.setText(s);
					s = "";
					//ˢ����ѵ÷�
					best_score = (best_score > count)? best_score:count;
					s += best_score;
					J14.setText(s);
					s = "";
					
					if(tag)
					{
						new AePlayWave("move.wav").start();
					}
					Restart();
				}
				else
				{
					Die();
				}
			}
			else if(key == KeyEvent.VK_DOWN)
			{
				x = col-1;
				if( movable("D") )//���������ƶ�
				{
					for(int i = 0;i < row;i++)
						for(int j = col-1;j >=0 ;j--)
						{
							if( box[j][i].flag )
							{
								for(int k = j-1;k >= 0;k--)
								{
									if( box[k][i].flag )
									{
										if(box[j][i].expn == box[k][i].expn)
										{
											box[j][i].expn++;
											count += binary(box[j][i].expn);
											box[k][i].flag = false;
											box[k][i].expn = 0;
											j = k;
											new AePlayWave("merge.wav").start();
											tag = false;
											break;
										}
										else
										{
											break;
										}
									}
								}
							}
						}
					for(int i = 0;i < row;i++)
					{
						for(int j = col-1;j >= 0;j--)
						{
							if(box[j][i].flag)
							{
								box[x][i].expn = box[j][i].expn;
								box[x][i].flag = true;
								box[x][i].button.setVisible(true);
								s += binary(box[x][i].expn);
								box[x][i].button.setText(s);
								box[x][i].button.setBackground(color[box[x][i].expn]);
								x--;
								s = "";
							}
							else
							{
								box[j][i].button.setVisible(false);
							}
						}
						for(int k = x;k >= 0;k--)
						{
							box[k][i].flag = false;
							box[k][i].expn = 0;
							box[k][i].button.setVisible(false);
						}
						x = col-1;
					}
					s += count;
					J12.setText(s);
					s = "";
					//ˢ����ѵ÷�
					best_score = (best_score > count)? best_score:count;
					s += best_score;
					J14.setText(s);
					s = "";
					
					if(tag)
					{
						new AePlayWave("move.wav").start();
					}
					Restart();
				}
				else
				{
					Die();
				}
			}
			else{}
		}
	}
	
	//������
	class InternetMonitor extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			JLabel JLabel_temp = (JLabel)e.getSource();
			String J_temp = JLabel_temp.getText();
			System.out.println(J_temp);
			URI uri ;
				try {
					uri = new URI(J_temp);
					Desktop desk=Desktop.getDesktop();
					if(Desktop.isDesktopSupported() && desk.isSupported(Desktop.Action.BROWSE)){
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
		public void mouseEntered(MouseEvent e){
			JLabel JLabel_temp = (JLabel)e.getSource();
			JLabel_temp.setForeground(Color.red);
		}
		public void mouseExited(MouseEvent e){
			JLabel JLabel_temp = (JLabel)e.getSource();
			JLabel_temp.setForeground(Color.blue);
		}
	}
	
	//��ʱ����
	class Timer extends Thread{  
	    public Timer(){
	        this.start();
	    }
	    @Override
	    public void run() {
	        while(true){
	            if(isRun){
	                sec +=1 ;
	                if(sec >= 60){
	                    sec = 0;
	                    min +=1 ;
	                }
	                if(min>=60){
	                    min=0;
	                    hour+=1;
	                }
	                showTime();
	            }
	 
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	             
	        }
	    }

	    private void showTime(){
	        String strTime ="" ;
	        if(hour < 10)
	            strTime = "0"+hour+":";
	        else
	            strTime = ""+hour+":";
	         
	        if(min < 10)
	            strTime = strTime+"0"+min+":";
	        else
	            strTime =strTime+ ""+min+":";
	         
	        if(sec < 10)
	            strTime = strTime+"0"+sec;
	        else
	            strTime = strTime+""+sec;
	         
	        //�ڴ�����������ʾʱ��
	        showtime.setText("   " + strTime);
	    }
	}


}
