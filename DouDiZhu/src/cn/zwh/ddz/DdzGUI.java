package cn.zwh.ddz;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument.BranchElement;

import cn.zwh.ddz.client.Card;
import cn.zwh.ddz.client.CardType;
import cn.zwh.ddz.client.CardUtil;
import cn.zwh.ddz.client.DealingDevice;
import cn.zwh.ddz.client.GameRule;


/**
 * 斗地主客户端
 * 
 * @author zwh
 */
public class DdzGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	// 我的牌
	public int num=0;
	public List<Card> myCards = new ArrayList<Card>();

	private List<Card> allCards = new ArrayList<Card>(54);

	public List<Card> leftCards = new ArrayList<Card>(17);// 左边玩家的牌

	public List<Card> rightCards = new ArrayList<Card>(17);// 右边玩家的牌

	public List<Card> topCards = new ArrayList<Card>(3);// 底牌

	public List<Card> copyOfTopCards = new ArrayList<Card>(3);// 底牌

	// 我选中的牌，想出的牌
	public List<Card> selectedCards = new ArrayList<Card>();

	// 上家出的牌
	public List<Card> prevCards = new ArrayList<Card>();

	// 按钮
	private JButton jiaoDiZhu, buJiao, chuPai, buChu;

	// 默认的背景色
	public static Color DEFAULT_BGCOLOR = new Color(232, 220, 184);

	public DdzGUI() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		init();
	}

	private void initGUI() {

		BorderLayout borderLayout = new BorderLayout();
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(borderLayout);
		this.setSize(new Dimension(800, 600));

		this.setLayout(null);
		contentPane.setBackground(Color.blue);

		Icon back_icon = DdzUtil.getImageIcon("back.gif");
		JLabel back_label = new JLabel("", back_icon, JLabel.CENTER);
		back_label.setBounds(0, 0, 800, 600);
		contentPane.add(back_label);
		contentPane.setBackground(DEFAULT_BGCOLOR);
		initButtons();
		try {
			initPanel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}// 初始化 自己的界面

	}

	private void init() {
		DealingDevice one = new DealingDevice();
		allCards = one.getAllCards();

		// 1号玩家的牌
		for (int j = 0; j < 17; j++) {
			Card card = allCards.get(j * 3);
			myCards.add(card);
		}

		// 2号玩家的牌
		for (int j = 0; j < 17; j++) {
			Card card = allCards.get(j * 3 + 1);
			rightCards.add(card);
		}

		// 3号玩家的牌
		for (int j = 0; j < 17; j++) {
			Card card = allCards.get(j * 3 + 2);
			leftCards.add(card);
		}

		// 底牌
		for (int i = 51; i < 54; i++) {
			Card card = allCards.get(i);
			topCards.add(card);

			Card card2 = new Card(card.id);
			copyOfTopCards.add(card2);
		}

		CardUtil.sortCards(myCards);
		CardUtil.sortCards(rightCards);
		CardUtil.sortCards(leftCards);
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	private void initPanel() throws InterruptedException {
		// 初始化 自己的界面
		for (int i = 0; i < 17; i++) {
			myCards.get(i).setBounds(400 - i * 15, 470, 71, 96);
			contentPane.add(myCards.get(i));
			myCards.get(i).addMouseListener(
					new CardsMouseAdapter(myCards.get(i), this));

			rightCards.get(i).setBounds(710, 420 - i * 25, 71, 96);
			contentPane.add(rightCards.get(i));

			leftCards.get(i).setBounds(20, 420 - i * 25, 71, 96);
			contentPane.add(leftCards.get(i));
			validate();
			repaint();

		}

		for (int i = 0; i < 3; i++) {
			topCards.get(i).setBounds(430 - i * 80, 20, 71, 96);
			contentPane.add(topCards.get(i));
			topCards.get(i).addMouseListener(
					new CardsMouseAdapter(topCards.get(i), this));
		}

		contentPane.add(jiaoDiZhu);
		contentPane.add(buJiao);
		contentPane.add(chuPai);
		contentPane.add(buChu);
	}

	/**
	 * 初始化按钮
	 * 
	 */
	private void initButtons() {
		chuPai = new JButton("出牌");
		chuPai.setBounds(520, 430, 70, 30);
		chuPai.addActionListener(this);

		buChu = new JButton("不出");
		buChu.setBounds(600, 430, 70, 30);
		buChu.addActionListener(this);

		jiaoDiZhu = new JButton("叫地主");
		jiaoDiZhu.setBounds(370, 430, 90, 30);
		jiaoDiZhu.addActionListener(this);

		buJiao = new JButton("不叫");
		buJiao.setBounds(450, 430, 70, 30);
		buJiao.addActionListener(this);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			DdzGUI mainFrame = new DdzGUI();
			mainFrame.initGUI();
			showGUI(mainFrame, 800, 600);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示用户界面
	 * 
	 * @param frame
	 * @param width
	 * @param height
	 */
	public static void showGUI(JFrame frame, int width, int height) {
		frame.setTitle("斗地主zwh");
		frame.setIconImage(DdzUtil.getAppIcon());
		frame.setResizable(false);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public class CardsMouseAdapter extends MouseAdapter {

		Card card = null;

		DdzGUI owner;

		public void mouseClicked(MouseEvent e) {
			if (card.getBounds().y == 470) {
				card.setBounds(card.getBounds().x, 470 - 20, 71, 96);
				System.out
						.println("选中的牌增加了一张：" + card.bigType + card.smallType);
				owner.selectedCards.add(card);

			} else if (card.getBounds().y == 450) {
				card.setBounds(card.getBounds().x, 470, 71, 96);
				System.out
						.println("选中的牌减少了一张：" + card.bigType + card.smallType);
				owner.selectedCards.remove(card);
			}
			System.out.println("牌的X坐标:" + card.getBounds().getCenterX());
		}

		public CardsMouseAdapter(Card labels, DdzGUI owner) {
			super();
			this.card = labels;
			this.owner = owner;
		}

	}

	/**
	 * 响应事件
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		if (source == chuPai) {
			CardType cardType = GameRule.getCardType(selectedCards);
			boolean isOvercome = GameRule.isOvercomePrev(selectedCards,
					cardType, prevCards, cardType);// 暂时假定上一首牌和这一首牌的类型相同
			if (isOvercome||num==2) {
				GameRule.print("我出的牌大过上家。");
			} else {
				JOptionPane.showMessageDialog(this, "我出的牌不能大过上家", "我出的牌不能大过上家",
						JOptionPane.YES_OPTION);
				GameRule.print("我出的牌不能大过上家。");
				return;
			}

			if (cardType != null) {
				DdzUtil.playSound(selectedCards, cardType);
				int size = selectedCards.size();

				if (prevCards != null) {
					for (int j = 0; j < prevCards.size(); j++) {
						remove(prevCards.get(j));
						System.out.println("删除" + prevCards.get(j));
					}
					
					prevCards.clear();
					prevCards.addAll(selectedCards);

					CardUtil.sortCards(prevCards);
					for (int i = 0; i < size; i++) {
						prevCards.get(i).setBounds(400 - i * 15, 350, 71, 96);
					}
				}
				
				myCards.removeAll(selectedCards);

				validate();
				repaint();
				selectedCards.clear();

				sortAndUpdateMyCards();
				num=0;
				try {
					rightrun();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					leftrun();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int  my=myCards.size();
				int  le=leftCards.size();
				int  ri=rightCards.size();
				if(my==0){
					JOptionPane.showMessageDialog(this, "玩家赢", "玩家赢",
							JOptionPane.YES_OPTION);
					return;
				}
				if(le==0){
					JOptionPane.showMessageDialog(this, "电脑赢", "电脑赢",
							JOptionPane.YES_OPTION);
					return;
				}
				if(ri==0){
					JOptionPane.showMessageDialog(this, "电脑赢", "电脑赢",
							JOptionPane.YES_OPTION);
					return;
				}
				// 测试用的，根据上一首牌，是否灰化出牌按钮
//				boolean flag = GameRule.isOvercomePrev(myCards, prevCards,
//						cardType);
//				if (flag) {
//					chuPai.setEnabled(true);
//				} else {
//					chuPai.setEnabled(false);
//				}

			} else {
				JOptionPane.showMessageDialog(this, "不符合规则", "不符合规则",
						JOptionPane.YES_OPTION);
			}
		} else if (source == jiaoDiZhu) {
			jiaoDiZhu.setEnabled(false);// 灰化叫地主按钮
			buJiao.setEnabled(false);
			myCards.addAll(topCards);// 将底牌加入到我的牌中

			selectedCards.addAll(topCards);// 选中的牌增加3张

			sortAndUpdateMyCards();// 更新我的牌
			topCards.clear();//
			DdzUtil.playSound("jiaodizhu.wav");

			// 底牌的位置
			for (int i = 0; i < 3; i++) {
				copyOfTopCards.get(i).setBounds(430 - i * 80, 20, 71, 96);
				contentPane.add(copyOfTopCards.get(i));
			}
		}
		else if(source == buChu){
			num++;
			try {
				rightrun();
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			try {
				leftrun();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			int  my=myCards.size();
			int  le=leftCards.size();
			int  ri=rightCards.size();
			if(my==0){
				JOptionPane.showMessageDialog(this, "玩家赢", "玩家赢",
						JOptionPane.YES_OPTION);
				return;
			}
			if(le==0){
				JOptionPane.showMessageDialog(this, "电脑赢", "电脑赢",
						JOptionPane.YES_OPTION);
				return;
			}
			if(ri==0){
				JOptionPane.showMessageDialog(this, "电脑赢", "电脑赢",
						JOptionPane.YES_OPTION);
				return;
			}
		}
	}

	private void leftrun() throws Exception {
		
		
		CardType prevCardType = GameRule.getCardType(prevCards);
		if (leftCards == null || prevCards == null) {
              System.out.println("没有牌");
            }

		if (prevCardType == null) {
			System.out.println("上家出的牌不合法，所以不能出。");
		}

		// 默认情况：上家和自己想出的牌都符合规则
		CardUtil.sortCards(leftCards);// 对牌排序
		CardUtil.sortCards(prevCards);// 对牌排序
		if(num==2)
		{
			num=0;
			List<Card> temp=findsele(leftCards);
			if(temp==null){
				selectedCards.add(leftCards.get(0));
			}
			else {
				selectedCards.addAll(temp);				
			}
			chupai(1);
			return;
		}
			
		
		// 上一首牌的个数
		int prevSize = prevCards.size();
		int mySize = leftCards.size();

		// 我先出牌，上家没有牌
		if (prevSize == 0 && mySize != 0) {
			System.out.println("我先出牌，上家没有牌");
		}

		// 集中判断是否王炸，免得多次判断王炸
		if (prevCardType == CardType.WANG_ZHA) {
			System.out.println("上家王炸，肯定不能出。");
		}

		if (mySize >= 2) {
			List<Card> cards = new ArrayList<Card>();
			cards.add(new Card(leftCards.get(mySize - 1).id));
			cards.add(new Card(leftCards.get(mySize - 2).id));
			if (GameRule.isDuiWang(cards)) {
				selectedCards.addAll(cards);
				chupai(1);
				return;
			}
		}

		// 集中判断对方不是炸弹，我出炸弹的情况
		if (prevCardType != CardType.ZHA_DAN) {
			if (mySize < 4) {
				System.out.println("要不起");
			} else {
				for (int i = 0; i < mySize - 3; i++) {
					int grade0 = leftCards.get(i).grade;
					int grade1 = leftCards.get(i + 1).grade;
					int grade2 = leftCards.get(i + 2).grade;
					int grade3 = leftCards.get(i + 3).grade;

					if (grade1 == grade0 && grade2 == grade0
							&& grade3 == grade0) {
						selectedCards.add(leftCards.get(i));
						selectedCards.add(leftCards.get(i+1));
						selectedCards.add(leftCards.get(i+2));
						selectedCards.add(leftCards.get(i+3));
						chupai(1);
						return;
					}
				}
			}

		}

		int prevGrade = prevCards.get(0).grade;

		// 比较2家的牌，主要有2种情况，1.我出和上家一种类型的牌，即对子管对子；
		// 2.我出炸弹，此时，和上家的牌的类型可能不同
		// 王炸的情况已经排除

		// 上家出单
		if (prevCardType == CardType.DAN) {
			// 一张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 0; i--) {
				int grade = leftCards.get(i).grade;
				if (grade > prevGrade) {
					// 只要有1张牌可以大过上家，则返回true
					selectedCards.add(leftCards.get(i));
					chupai(1);
					return;
				}
			}

		}
		// 上家出对子
		else if (prevCardType == CardType.DUI_ZI) {
			// 2张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 1; i--) {
				int grade0 = leftCards.get(i).grade;
				int grade1 = leftCards.get(i - 1).grade;

				if (grade0 == grade1) {
					if (grade0 > prevGrade) {
						// 只要有1对牌可以大过上家，则返回true
						selectedCards.add(leftCards.get(i));
						selectedCards.add(leftCards.get(i-1));
						chupai(1);
						return;
					}
				}
			}

		}
		// 上家出3不带
		else if (prevCardType == CardType.SAN_BU_DAI) {
			// 3张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 2; i--) {
				int grade0 = leftCards.get(i).grade;
				int grade1 = leftCards.get(i - 1).grade;
				int grade2 = leftCards.get(i - 2).grade;

				if (grade0 == grade1 && grade0 == grade2) {
					if (grade0 > prevGrade) {
						// 只要3张牌可以大过上家，则返回true
						selectedCards.add(leftCards.get(i));
						selectedCards.add(leftCards.get(i-1));
						selectedCards.add(leftCards.get(i-2));
						chupai(1);
						return;
					}
				}
			}

		}
		// 上家出3带1
		else if (prevCardType == CardType.SAN_DAI_YI) {
			// 3带1 3不带 比较只多了一个判断条件
			if (mySize < 4) {
				System.out.println("要不起");
			}

			// 3张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 2; i--) {
				int grade0 = leftCards.get(i).grade;
				int grade1 = leftCards.get(i - 1).grade;
				int grade2 = leftCards.get(i - 2).grade;

				if (grade0 == grade1 && grade0 == grade2) {
					if (grade0 > prevGrade) {
						// 只要3张牌可以大过上家，则返回true
						selectedCards.add(leftCards.get(i));
						selectedCards.add(leftCards.get(i-1));
						selectedCards.add(leftCards.get(i-2));
						chupai(1);
						return;
					}
				}
			}

		}
		// 上家出炸弹
		else if (prevCardType == CardType.ZHA_DAN) {
			// 4张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 3; i--) {
				int grade0 = leftCards.get(i).grade;
				int grade1 = leftCards.get(i - 1).grade;
				int grade2 = leftCards.get(i - 2).grade;
				int grade3 = leftCards.get(i - 3).grade;

				if (grade0 == grade1 && grade0 == grade2 && grade0 == grade3) {
					if (grade0 > prevGrade) {
						// 只要有4张牌可以大过上家，则返回true
						selectedCards.add(leftCards.get(i));
						selectedCards.add(leftCards.get(i-1));
						selectedCards.add(leftCards.get(i-2));
						selectedCards.add(leftCards.get(i-3));
						chupai(1);
						return;
					}
				}
			}

		}
		// 上家出4带2
		else if (prevCardType == CardType.SI_DAI_ER) {
			// 4张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 3; i--) {
				int grade0 = leftCards.get(i).grade;
				int grade1 = leftCards.get(i - 1).grade;
				int grade2 = leftCards.get(i - 2).grade;
				int grade3 = leftCards.get(i - 3).grade;

				if (grade0 == grade1 && grade0 == grade2 && grade0 == grade3) {
					// 只要有炸弹，则返回true
					selectedCards.add(leftCards.get(i));
					selectedCards.add(leftCards.get(i-1));
					selectedCards.add(leftCards.get(i-2));
					selectedCards.add(leftCards.get(i-3));
					chupai(1);
					return;
				}
			}
		}
		// 上家出顺子
		else if (prevCardType == CardType.SHUN_ZI) {
			if (mySize < prevSize) {
				System.out.println("要不起");
			} else {
				for (int i = mySize - 1; i >= prevSize - 1; i--) {
					List<Card> cards = new ArrayList<Card>();
					for (int j = 0; j < prevSize; j++) {
						cards.add(new Card(leftCards.get(i - j).grade));
					}

					CardType myCardType = GameRule.getCardType(cards);
					if (myCardType == CardType.SHUN_ZI) {
						int myGrade2 = cards.get(cards.size() - 1).grade;// 最大的牌在最后
						int prevGrade2 = prevCards.get(prevSize - 1).grade;// 最大的牌在最后

						if (myGrade2 > prevGrade2) {

							selectedCards.addAll(cards);
							chupai(1);
							return;
						}
					}
				}
			}

		}
		// 上家出连对
		else if (prevCardType == CardType.LIAN_DUI) {
			if (mySize < prevSize) {
				System.out.println("要不起");
			} else {
				for (int i = mySize - 1; i >= prevSize - 1; i--) {
					List<Card> cards = new ArrayList<Card>();
					for (int j = 0; j < prevSize; j++) {
						cards.add(new Card(leftCards.get(i - j).grade));
					}

					CardType myCardType = GameRule.getCardType(cards);
					if (myCardType == CardType.LIAN_DUI) {
						int myGrade2 = cards.get(cards.size() - 1).grade;// 最大的牌在最后,getCardType会对列表排序
						int prevGrade2 = prevCards.get(prevSize - 1).grade;// 最大的牌在最后

						if (myGrade2 > prevGrade2) {
							selectedCards.addAll(cards);
							chupai(1);
							return;
						}
					}
				}
			}

		}
		// 上家出飞机
		else if (prevCardType == CardType.FEI_JI) {
			if (mySize < prevSize) {
				System.out.println("要不起");
			} else {
				for (int i = mySize - 1; i >= prevSize - 1; i--) {
					List<Card> cards = new ArrayList<Card>();
					for (int j = 0; j < prevSize; j++) {
						cards.add(new Card(leftCards.get(i - j).grade));
					}

					CardType myCardType = GameRule.getCardType(cards);
					if (myCardType == CardType.FEI_JI) {
						int myGrade4 = cards.get(4).grade;// 
						int prevGrade4 = prevCards.get(4).grade;//

						if (myGrade4 > prevGrade4) {
							selectedCards.addAll(cards);
							chupai(1);
							return;
						}
					}
				}
			}
		}

		// 默认不能出牌
		num++;
	}

	@SuppressWarnings("null")
	private List<Card> findsele(List<Card> leftCards2) {
		// TODO Auto-generated method stub
		List<Card> selecard = new ArrayList<Card>();
		if(leftCards2.get(0).grade==leftCards2.get(1).grade){
			if(leftCards2.get(2).grade==leftCards2.get(1).grade){
				if(leftCards2.get(2).grade!=leftCards2.get(3).grade){
					for(int l=0;l<3;l++)
						selecard.add(leftCards2.get(l));
					return selecard;
				}
				else{
					List<Card> temp=leftCards2;
					for(int k=0;k<4;k++)
						temp.remove(k);
					return findsele(temp);
				}
			}
			else {
				selecard.add(leftCards2.get(0));
				selecard.add(leftCards2.get(1));
				return selecard;
			}
				
			
		}
		else if(leftCards2.get(0).grade==(leftCards2.get(1).grade-1)){
			int mm=0,nn=2;
			int ca=leftCards2.get(1).grade;
			for(int k=2;k<leftCards2.size();k++){
				if(k>=5)
					mm=k;
				if(leftCards2.get(k).grade==ca+2)
						break;
				if(leftCards2.get(k).grade==ca+1)
				{
					nn++;
					ca++;
				}
					
						
			}
			if(nn>4){
				int cc=leftCards2.get(0).grade;
				for(int k=0;k<leftCards2.size();k++){
					if(leftCards2.get(0).grade==cc){
						selecard.add(leftCards2.get(k));
						cc++;
					}
					
				}
				return selecard;
			}
				
		}
		else {
			return null;
		}
		return null;
		
	}

	private void chupai(int k) throws AWTException {
		// TODO Auto-generated method stub
		CardType cardType = GameRule.getCardType(selectedCards);
		DdzUtil.playSound(selectedCards, cardType);
		int size = selectedCards.size();

		if (prevCards != null) {
			for (int j = 0; j < prevCards.size(); j++) {
				remove(prevCards.get(j));
				System.out.println("删除" + prevCards.get(j));
			}
			
			prevCards.clear();
			prevCards.addAll(selectedCards);

			CardUtil.sortCards(prevCards);
			if(k==1){
				for (int i = 0; i < size; i++) {
					prevCards.get(i).setBounds(120, 20+i*25, 71, 96);
				}
				CardUtil.sortCards(leftCards);
				leftCards.removeAll(selectedCards);
				//CardUtil.sortCards(leftCards);
				validate();
				repaint();
				selectedCards.clear();
				int lesize = leftCards.size();
				for (int i = 0; i < lesize; i++) {
					//Card card = leftCards.get(i);
					
						leftCards.get(i).setBounds(20,420-i*25, 71, 96);
					
					contentPane.add(leftCards.get(i));
				}
				
			}
			else if(k==2){
				for (int i = 0; i < size; i++) {
					prevCards.get(i).setBounds(620, 20+i*25, 71, 96);
				}
				
				rightCards.removeAll(selectedCards);
				CardUtil.sortCards(rightCards);
				validate();
				repaint();
				selectedCards.clear();
				int lesize = rightCards.size();
				for (int i = 0; i < lesize; i++) {
					//Card card = leftCards.get(i);
				
						rightCards.get(i).setBounds(720,420-i*25, 71, 96);
					
					contentPane.add(rightCards.get(i));
				}
			}
			
			javax.swing.Timer timer = new javax.swing.Timer(5000, new ActionListener() { 
				public void actionPerformed(ActionEvent e) {   
					repaint(); 
					}
				});
					  
			timer.start();
			num=0;
			//num=(num+1)%2;
			
			
			
		}
	}

	private void rightrun() throws Exception {
		// TODO Auto-generated method stub
		CardType prevCardType = GameRule.getCardType(prevCards);
		if (rightCards == null || prevCards == null) {
              System.out.println("没有牌");
            }
		
		if (prevCardType == null) {
			System.out.println("上家出的牌不合法，所以不能出。");
		}
		// 默认情况：上家和自己想出的牌都符合规则
		CardUtil.sortCards(rightCards);// 对牌排序
		CardUtil.sortCards(prevCards);// 对牌排序
		if(num==2)
		{
			num=0;
			List<Card> temp=findsele(rightCards);
			if(temp==null){
				selectedCards.add(rightCards.get(0));
			}
			else {
				selectedCards.addAll(temp);				
			}
			
			chupai(2);
			return;
		}
		// 上一首牌的个数
		int prevSize = prevCards.size();
		int mySize = rightCards.size();

		// 我先出牌，上家没有牌
		if (prevSize == 0 && mySize != 0) {
			System.out.println("我先出牌，上家没有牌");
		}

		// 集中判断是否王炸，免得多次判断王炸
		if (prevCardType == CardType.WANG_ZHA) {
			System.out.println("上家王炸，肯定不能出。");
		}

		if (mySize >= 2) {
			List<Card> cards = new ArrayList<Card>();
			cards.add(new Card(rightCards.get(mySize - 1).id));
			cards.add(new Card(rightCards.get(mySize - 2).id));
			if (GameRule.isDuiWang(cards)) {
				selectedCards.addAll(cards);
				chupai(2);
				return;
			}
		}

		// 集中判断对方不是炸弹，我出炸弹的情况
		if (prevCardType != CardType.ZHA_DAN) {
			if (mySize < 4) {
				System.out.println("要不起");
			} else {
				for (int i = 0; i < mySize - 3; i++) {
					int grade0 = rightCards.get(i).grade;
					int grade1 = rightCards.get(i + 1).grade;
					int grade2 = rightCards.get(i + 2).grade;
					int grade3 = rightCards.get(i + 3).grade;

					if (grade1 == grade0 && grade2 == grade0
							&& grade3 == grade0) {
						selectedCards.add(rightCards.get(i));
						selectedCards.add(rightCards.get(i+1));
						selectedCards.add(rightCards.get(i+2));
						selectedCards.add(rightCards.get(i+3));
						chupai(2);
						return;
					}
				}
			}

		}

		int prevGrade = prevCards.get(0).grade;

		// 比较2家的牌，主要有2种情况，1.我出和上家一种类型的牌，即对子管对子；
		// 2.我出炸弹，此时，和上家的牌的类型可能不同
		// 王炸的情况已经排除

		// 上家出单
		if (prevCardType == CardType.DAN) {
			// 一张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 0; i--) {
				int grade = rightCards.get(i).grade;
				if (grade > prevGrade) {
					// 只要有1张牌可以大过上家，则返回true
					selectedCards.add(rightCards.get(i));
					chupai(2);
					return;
				}
			}

		}
		// 上家出对子
		else if (prevCardType == CardType.DUI_ZI) {
			// 2张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 1; i--) {
				int grade0 = rightCards.get(i).grade;
				int grade1 = rightCards.get(i - 1).grade;

				if (grade0 == grade1) {
					if (grade0 > prevGrade) {
						// 只要有1对牌可以大过上家，则返回true
						selectedCards.add(rightCards.get(i));
						selectedCards.add(rightCards.get(i-1));
						chupai(2);
						return;
					}
				}
			}

		}
		// 上家出3不带
		else if (prevCardType == CardType.SAN_BU_DAI) {
			// 3张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 2; i--) {
				int grade0 = rightCards.get(i).grade;
				int grade1 = rightCards.get(i - 1).grade;
				int grade2 = rightCards.get(i - 2).grade;

				if (grade0 == grade1 && grade0 == grade2) {
					if (grade0 > prevGrade) {
						// 只要3张牌可以大过上家，则返回true
						selectedCards.add(rightCards.get(i));
						selectedCards.add(rightCards.get(i-1));
						selectedCards.add(rightCards.get(i-2));
						chupai(2);
						return;
					}
				}
			}

		}
		// 上家出3带1
		else if (prevCardType == CardType.SAN_DAI_YI) {
			// 3带1 3不带 比较只多了一个判断条件
			if (mySize < 4) {
				System.out.println("要不起");
			}

			// 3张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 2; i--) {
				int grade0 = rightCards.get(i).grade;
				int grade1 = rightCards.get(i - 1).grade;
				int grade2 = rightCards.get(i - 2).grade;

				if (grade0 == grade1 && grade0 == grade2) {
					if (grade0 > prevGrade) {
						// 只要3张牌可以大过上家，则返回true
						selectedCards.add(rightCards.get(i));
						selectedCards.add(rightCards.get(i-1));
						selectedCards.add(rightCards.get(i-2));
						chupai(2);
						return;
					}
				}
			}

		}
		// 上家出炸弹
		else if (prevCardType == CardType.ZHA_DAN) {
			// 4张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 3; i--) {
				int grade0 = rightCards.get(i).grade;
				int grade1 = rightCards.get(i - 1).grade;
				int grade2 = rightCards.get(i - 2).grade;
				int grade3 = rightCards.get(i - 3).grade;

				if (grade0 == grade1 && grade0 == grade2 && grade0 == grade3) {
					if (grade0 > prevGrade) {
						// 只要有4张牌可以大过上家，则返回true
						selectedCards.add(rightCards.get(i));
						selectedCards.add(rightCards.get(i-1));
						selectedCards.add(rightCards.get(i-2));
						selectedCards.add(rightCards.get(i-3));
						chupai(2);
						return;
					}
				}
			}

		}
		// 上家出4带2
		else if (prevCardType == CardType.SI_DAI_ER) {
			// 4张牌可以大过上家的牌
			for (int i = mySize - 1; i >= 3; i--) {
				int grade0 = rightCards.get(i).grade;
				int grade1 = rightCards.get(i - 1).grade;
				int grade2 = rightCards.get(i - 2).grade;
				int grade3 = rightCards.get(i - 3).grade;

				if (grade0 == grade1 && grade0 == grade2 && grade0 == grade3) {
					// 只要有炸弹，则返回true
					selectedCards.add(rightCards.get(i));
					selectedCards.add(rightCards.get(i-1));
					selectedCards.add(rightCards.get(i-2));
					selectedCards.add(rightCards.get(i-3));
					chupai(2);
					return;
				}
			}
		}
		// 上家出顺子
		else if (prevCardType == CardType.SHUN_ZI) {
			if (mySize < prevSize) {
				System.out.println("要不起");
			} else {
				for (int i = mySize - 1; i >= prevSize - 1; i--) {
					List<Card> cards = new ArrayList<Card>();
					for (int j = 0; j < prevSize; j++) {
						cards.add(new Card(rightCards.get(i - j).grade));
					}

					CardType myCardType = GameRule.getCardType(cards);
					if (myCardType == CardType.SHUN_ZI) {
						int myGrade2 = cards.get(cards.size() - 1).grade;// 最大的牌在最后
						int prevGrade2 = prevCards.get(prevSize - 1).grade;// 最大的牌在最后

						if (myGrade2 > prevGrade2) {

							selectedCards.addAll(cards);
							chupai(2);
							return;
						}
					}
				}
			}

		}
		// 上家出连对
		else if (prevCardType == CardType.LIAN_DUI) {
			if (mySize < prevSize) {
				System.out.println("要不起");
			} else {
				for (int i = mySize - 1; i >= prevSize - 1; i--) {
					List<Card> cards = new ArrayList<Card>();
					for (int j = 0; j < prevSize; j++) {
						cards.add(new Card(rightCards.get(i - j).grade));
					}

					CardType myCardType = GameRule.getCardType(cards);
					if (myCardType == CardType.LIAN_DUI) {
						int myGrade2 = cards.get(cards.size() - 1).grade;// 最大的牌在最后,getCardType会对列表排序
						int prevGrade2 = prevCards.get(prevSize - 1).grade;// 最大的牌在最后

						if (myGrade2 > prevGrade2) {
							selectedCards.addAll(cards);
							chupai(2);
							return;
						}
					}
				}
			}

		}
		// 上家出飞机
		else if (prevCardType == CardType.FEI_JI) {
			if (mySize < prevSize) {
				System.out.println("要不起");
			} else {
				for (int i = mySize - 1; i >= prevSize - 1; i--) {
					List<Card> cards = new ArrayList<Card>();
					for (int j = 0; j < prevSize; j++) {
						cards.add(new Card(rightCards.get(i - j).grade));
					}

					CardType myCardType = GameRule.getCardType(cards);
					if (myCardType == CardType.FEI_JI) {
						int myGrade4 = cards.get(4).grade;// 
						int prevGrade4 = prevCards.get(4).grade;//

						if (myGrade4 > prevGrade4) {
							selectedCards.addAll(cards);
							chupai(2);
							return;
						}
					}
				}
			}
		}

		// 默认不能出牌
		num++;
		
	}

	/**
	 * 排序并更新我的牌
	 * 
	 */
	public void sortAndUpdateMyCards() {
		CardUtil.sortCards(myCards);

		int size = myCards.size();
		for (int i = 0; i < size; i++) {
			Card card = myCards.get(i);
			if (topCards.contains(card)) {
				myCards.get(i).setBounds(400 - i * 15, 470 - 20, 71, 96);
			} else {
				myCards.get(i).setBounds(400 - i * 15, 470, 71, 96);
			}
			contentPane.add(myCards.get(i));
		}

	}
}