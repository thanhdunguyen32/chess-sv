package test.lion;

import lion.math.RandomDispatcher;

public class RandomDispatcherTest {

	public static void main(String[] args) {
		// 创建一个分发器
		RandomDispatcher<Integer> randomDispatcher = new RandomDispatcher<Integer>();

		// 设置每种事件的概率，注意，概率之和不一定是100哦
		randomDispatcher.put(20, 1);
		randomDispatcher.put(1, 2);
		randomDispatcher.put(29, 3);
		randomDispatcher.put(50, 4);

		// 随机
		int retIndex = randomDispatcher.random();

		// 根据随机结果去执行不同的方法
		switch (retIndex) {
		case 1:
			System.out.println("do method A");
			break;
		case 2:
			System.out.println("do method B");
			break;
		case 3:
			System.out.println("do method C");
			break;
		case 4:
			System.out.println("do method D");
			break;

		default:
			break;
		}
		testFloat();
	}

	private static void testFloat() {
		RandomDispatcher<Float> randomDispatcher = new RandomDispatcher<Float>();

		randomDispatcher.put(40, 20.5f);
		randomDispatcher.put(60, 5.3f);

		int rand1Count = 0;
		for (int i = 0; i < 20000; i++) {
			float randVal = randomDispatcher.random();
			if (randVal == 20.5f) {
				rand1Count++;
			}
		}

		System.out.println(rand1Count * 0.005);

	}

}
