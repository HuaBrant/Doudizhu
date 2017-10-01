package cn.zwh.ddz.client;

import java.util.Collections;
import java.util.List;

import cn.zwh.ddz.client.Card.CardBigType;
import cn.zwh.ddz.client.Card.CardSmallType;


/**
 * 斗地主牌的工具类
 * 
 * @author zwh
 */
public final class CardUtil {
	/**
	 * 不允许实例化
	 */
	private CardUtil() {

	}

	/**
	 * 根据牌的id获得一张牌的大类型：方块，梅花,红桃,黑桃,小王,大王
	 * 
	 * @param id
	 *            牌的id
	 * 
	 * @return 牌的大类型：方块，梅花,红桃,黑桃,小王,大王
	 */
	public static CardBigType getBigType(int id) {
		CardBigType bigType = null;
		if (id >= 1 && id <= 13) {
			bigType = CardBigType.FANG_KUAI;
		} else if (id >= 14 && id <= 26) {
			bigType = CardBigType.MEI_HUA;
		} else if (id >= 27 && id <= 39) {
			bigType = CardBigType.HONG_TAO;
		} else if (id >= 40 && id <= 52) {
			bigType = CardBigType.HEI_TAO;
		} else if (id == 53) {
			bigType = CardBigType.XIAO_WANG;
		} else if (id == 54) {
			bigType = CardBigType.DA_WANG;
		}
		return bigType;
	}

	/**
	 * 根据牌的id，获取牌的小类型：2_10,A,J,Q,K
	 * 
	 * @param id
	 *            牌的id
	 * 
	 * @return 牌的小类型：2_10,A,J,Q,K
	 */
	public static CardSmallType getSmallType(int id) {
		if (id < 1 || id > 54) {
			throw new RuntimeException("牌的数字不合法");
		}

		CardSmallType smallType = null;

		if (id >= 1 && id <= 52) {
			smallType = numToType(id % 13);
		} else if (id == 53) {
			smallType = CardSmallType.XIAO_WANG;
		} else if (id == 54) {
			smallType = CardSmallType.DA_WANG;
		} else {
			smallType = null;
		}
		return smallType;
	}

	/**
	 * 将阿拉伯数字0到12转换成对应的小牌型,被getSmallType方法调用
	 * 
	 * @param num
	 *            数字（0到12）
	 * @return 牌的小类型
	 */
	private static CardSmallType numToType(int num) {
		CardSmallType type = null;
		switch (num) {
		case 0:
			type = CardSmallType.K;
			break;
		case 1:
			type = CardSmallType.A;
			break;
		case 2:
			type = CardSmallType.ER;
			break;
		case 3:
			type = CardSmallType.SAN;
			break;
		case 4:
			type = CardSmallType.SI;
			break;
		case 5:
			type = CardSmallType.WU;
			break;
		case 6:
			type = CardSmallType.LIU;
			break;
		case 7:
			type = CardSmallType.QI;
			break;
		case 8:
			type = CardSmallType.BA;
			break;
		case 9:
			type = CardSmallType.JIU;
			break;
		case 10:
			type = CardSmallType.SHI;
			break;
		case 11:
			type = CardSmallType.J;
			break;
		case 12:
			type = CardSmallType.Q;
			break;

		}
		return type;
	}

	/**
	 * 根据牌的id，获得一张牌的等级
	 * 
	 * @param id
	 *            牌的id
	 * @return 与牌数字对应的等级
	 */
	public static int getGrade(int id) {

		if (id < 1 || id > 54) {
			throw new RuntimeException("牌的数字不合法");
		}

		int grade = 0;

		// 2个王必须放在前边判断
		if (id == 53) {
			grade = 16;
		} else if (id == 54) {
			grade = 17;
		}

		else {
			int modResult = id % 13;

			if (modResult == 1) {
				grade = 14;
			} else if (modResult == 2) {
				grade = 15;
			} else if (modResult == 3) {
				grade = 3;
			} else if (modResult == 4) {
				grade = 4;
			} else if (modResult == 5) {
				grade = 5;
			} else if (modResult == 6) {
				grade = 6;
			} else if (modResult == 7) {
				grade = 7;
			} else if (modResult == 8) {
				grade = 8;
			} else if (modResult == 9) {
				grade = 9;
			} else if (modResult == 10) {
				grade = 10;
			} else if (modResult == 11) {
				grade = 11;
			} else if (modResult == 12) {
				grade = 12;
			} else if (modResult == 0) {
				grade = 13;
			}

		}

		return grade;
	}

	/**
	 * 根据牌的id获得牌图片的名字
	 * 
	 * @param id
	 *            牌的id
	 * @return 图片的名字
	 */
	public static String getImageName(int id) {
		// 得到图片的前一个字符，表示是第几个牌
		String imageName = "";

		if (id == 53) {
			imageName += "smallJoker";
		} else if (id == 54) {
			imageName += "bigJoker";
		} else {
			int mod = id % 13;
			String firstLetter = "";
			switch (mod) {
			case 0:
				firstLetter = "K";
				break;
			case 1:
				firstLetter = "A";
				break;
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				firstLetter = "" + mod;
				break;
			case 11:
				firstLetter = "J";
				break;
			case 12:
				firstLetter = "Q";
				break;
			default:
				break;
			}

			String secondLetter = "";
			// 得到图片的后一个字符，表示什么颜色的牌
			if (id >= 1 && id <= 13) {
				secondLetter = "0";
			} else if (id >= 14 && id <= 26) {
				secondLetter = "1";
			} else if (id >= 27 && id <= 39) {
				secondLetter = "2";
			} else if (id >= 40 && id <= 52) {
				secondLetter = "3";
			}

			imageName = firstLetter + secondLetter;
		}
		String extension = ".gif";

		return imageName + extension;
	}

	/**
	 * 获得一张牌的大类型（枚举类型）的String表示
	 * 
	 * @param bigType
	 *            牌的大类型
	 * @return String表示的大类型
	 */
	public static String typeToZi(CardBigType bigType) {
		String resultType = "";
		switch (bigType) {
		case HEI_TAO:
			resultType = "黑桃";
			break;
		case HONG_TAO:
			resultType = "红桃";
			break;
		case MEI_HUA:
			resultType = "梅花";
			break;
		case FANG_KUAI:
			resultType = "方块";
			break;
		case DA_WANG:
			resultType = "大王";
			break;
		case XIAO_WANG:
			resultType = "小王";
			break;
		default:
			break;
		}
		return resultType;

	}

	/**
	 * 获得一张牌的小类型（枚举类型）的String表示
	 * 
	 * @param smallType
	 *            牌的小类型
	 * @return String表示的小类型
	 */
	public static String typeToZi(CardSmallType smallType) {
		String resultType = "";
		switch (smallType) {
		case A:
			resultType = "A";
			break;
		case ER:
			resultType = "2";
			break;
		case SAN:
			resultType = "3";
			break;
		case SI:
			resultType = "4";
			break;
		case WU:
			resultType = "5";
			break;
		case LIU:
			resultType = "6";
			break;
		case QI:
			resultType = "7";
			break;
		case BA:
			resultType = "8";
			break;
		case JIU:
			resultType = "9";
			break;
		case SHI:
			resultType = "10";
			break;
		case J:
			resultType = "J";
			break;
		case Q:
			resultType = "Q";
			break;
		case K:
			resultType = "K";
			break;
		case XIAO_WANG:
			resultType = "XiaoWang";
			break;
		case DA_WANG:
			resultType = "DaWang";
			break;

		default:
			break;
		}
		return resultType;
	}

	/**
	 * 打印牌的集合
	 * 
	 * @param cards
	 *            牌的列表集合
	 */
	public static void printCards(List<Card> cards) {
		if (cards != null) {
			int size = cards.size();
			for (int j = 0; j < size; j++) {
				System.out.print(cards.get(j) + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * 对牌进行排序，从小到大，比较器为CardComparator
	 * 
	 * @param cards
	 *            牌的集合
	 */
	public static void sortCards(List<Card> cards) {
		// 策略模式；复用已有类；
		Collections.sort(cards, new CardComparator());
	}

	/**
	 * 对牌进行排序，从小到大，使用冒泡排序，此种方法不是很好
	 * 
	 * @param cards
	 *            牌
	 */
	public static boolean bubbleSortCards(List<Card> cards) {
		if (cards == null) {
			return false;
		}

		int size = cards.size();
		// 冒泡排序,从左到右，从小到大
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size - i - 1; j++) {
				int gradeOne = cards.get(j).grade;
				int gradeTwo = cards.get(j + 1).grade;

				boolean isExchange = false;
				if (gradeOne > gradeTwo) {
					isExchange = true;
				} else if (gradeOne == gradeTwo) {
					// 2张牌的grade相同
					CardBigType type1 = cards.get(j).bigType;
					CardBigType type2 = cards.get(j + 1).bigType;

					// 从做到右，方块、梅花、红桃、黑桃
					if (type1.equals(CardBigType.HEI_TAO)) {
						isExchange = true;
					} else if (type1.equals(CardBigType.HONG_TAO)) {
						if (type2.equals(CardBigType.MEI_HUA)
								|| type2.equals(CardBigType.FANG_KUAI)) {
							isExchange = true;
						}
					} else if (type1.equals(CardBigType.MEI_HUA)) {
						if (type2.equals(CardBigType.FANG_KUAI)) {
							isExchange = true;
						}
					}
				}

				if (isExchange) {
					Card cardOne = cards.get(j);
					Card cardTwo = cards.get(j + 1);
					// 交换
					cards.set(j + 1, cardOne);
					cards.set(j, cardTwo);
				}
			}
		}
		return true;
	}
}
