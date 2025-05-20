package lion.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机事件分配器
 * 
 * @author 赵占涛
 * @email 369880281@qq.com
 * @date 2014年3月21日
 */
public class RandomDispatcher<E> {

	/** 概率之和 */
	private int sum = 0;

	/** 概率们 */
	private List<Integer> powers = new ArrayList<Integer>();
	/** 索引之和 */
	private List<E> indexs = new ArrayList<E>();

	/**
	 * 随机分配
	 * 
	 * @return 索引
	 */
	public E random() {
		int rnd = new Random().nextInt(sum);
		int tmpSum = 0;// 临时“和”
		for (int i = 0; i < powers.size(); i++) {
			tmpSum += powers.get(i);
			if (tmpSum > rnd) {
				return indexs.get(i);
			}
		}
		throw new RuntimeException("随机事件分配器发生了问题");
	}
	
	public E randomRemove() {
		int rnd = new Random().nextInt(sum);
		int tmpSum = 0;// 临时“和”
		for (int i = 0; i < powers.size(); i++) {
			tmpSum += powers.get(i);
			if (tmpSum > rnd) {
				E randElemet = indexs.remove(i);
				sum -=powers.remove(i);
				return randElemet;
			}
		}
		throw new RuntimeException("随机事件分配器发生了问题");
	}

	/**
	 * 设置概率
	 * 
	 * @param power
	 *            概率，概率之和不要求是100
	 * @param randValue
	 *            候选值
	 */
	public void put(int power, E index) {
		if (power <= 0) {
			return;
		}
		sum += power;

		// 插入排序，这样可以减少生成随机数时的判断次数，让大概率的在前，小概率的在后
		for (int i = 0; i < powers.size(); i++) {
			if (power > powers.indexOf(i)) {
				powers.add(i, power);
				indexs.add(i, index);
				return;
			}
		}
		powers.add(power);
		indexs.add(index);
	}
	
	public int size(){
		return indexs.size();
	}
	
}