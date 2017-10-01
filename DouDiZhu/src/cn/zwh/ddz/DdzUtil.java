package cn.zwh.ddz;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import cn.zwh.ddz.client.Card;
import cn.zwh.ddz.client.CardType;
import cn.zwh.ddz.client.CardUtil;

/**
 * 斗地主工具类
 * 
 * @author zwh
 * 
 */

public final class DdzUtil {

	private DdzUtil() {

	}

	/**
	 * 根据名字获取图标对象
	 * 
	 * @param name
	 *            图片的名字
	 * @return ImageIcon对象或null
	 */
	public static ImageIcon getImageIcon(String name) {
		String path = "img/" + name;
		URL imgURL = DdzUtil.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			return null;
		}
	}

	/**
	 * 获取Image对象
	 * 
	 * @param name
	 *            图片的名字
	 * @return Image 对象或null
	 */
	public static Image getImage(String name) {
		String path = "img/" + name;
		URL imgURL = DdzUtil.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL).getImage();
		} else {
			return null;
		}
	}

	/**
	 * 返回应用程序图标所用到的图像
	 * 
	 * @return
	 */
	public static Image getAppIcon() {
		String path = "img/" + "dizhu.png";
		URL imgURL = DdzUtil.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL).getImage();
		} else {
			return null;
		}
	}

	/**
	 * 根据牌的类型播放对应的声音
	 * 
	 * @param myCards
	 */
	public static void playSound(List<Card> myCards, CardType cardType) {
		String name = "";

		if (cardType == CardType.WANG_ZHA) {
			name = "wangzha";
		} else if (cardType == CardType.DAN) {
			name = "" + CardUtil.typeToZi(myCards.get(0).smallType);
		} else if (cardType == CardType.DUI_ZI) {
			name = "dui" + CardUtil.typeToZi(myCards.get(0).smallType);
		} else if (cardType == CardType.SAN_BU_DAI) {
			name = "sange";
			playSound(name + ".wav");
			try {
				Thread.sleep(4000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			playSound(CardUtil.typeToZi(myCards.get(0).smallType) + ".wav");
			return;
		} else if (cardType == CardType.SAN_DAI_YI) {
			name = "sandaiyi";
		} else if (cardType == CardType.ZHA_DAN) {
			name = "zhadan";
		} else if (cardType == CardType.SHUN_ZI) {
			name = "shunzi";
		} else if (cardType == CardType.LIAN_DUI) {
			name = "liandui";
		} else if (cardType == CardType.FEI_JI) {
			name = "feiji";
		} else if (cardType == CardType.SI_DAI_ER) {
			name = "sidaier";
		}

		if (!name.equals("")) {
			playSound(name + ".wav");
		}
	}

	/**
	 * 根据名字，播放声音目录下的声音文件
	 * 
	 * @param name
	 *            声音文件的名字(带后缀)
	 */
	@Deprecated
	public static void playSound2(String name) {
		String path = "";
		try {
			URL url = DdzUtil.class.getResource("");
			if (url != null) {
				path = DdzUtil.class.getClassLoader().getResource("")
						+ "/sound/" + name;
				InputStream is = new FileInputStream(path);
				AudioStream as = new AudioStream(is);
				AudioPlayer.player.start(as);
			} else {
				System.out.println("声音文件路径获取失败");
			}

		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * 根据名字，播放声音目录下的声音文件
	 * 
	 * 声音文件放在外边，这样打包之后才能访问；如果声音文件和class一起打包成jar文件，可能访问路径有误。
	 * 
	 * @param name
	 *            声音文件的名字(带后缀)
	 */
	public static void playSound(String name) {
		String path = "";
		path = "sounds/" + name;

		File file = new File(path);
		InputStream is;
		try {
			is = new FileInputStream(file.getAbsolutePath());
			AudioStream as = new AudioStream(is);
			AudioPlayer.player.start(as);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		DdzUtil.playSound("发牌.wav");
		// DdzUtil.playSound("发牌.mp3");//不支持
	}
}
