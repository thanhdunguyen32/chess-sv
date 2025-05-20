package cn.hxh.tool;

import java.util.ArrayList;
import java.util.List;

import lion.math.RandomDispatcher;

public class HeroIdMain {

	public static List<Integer> heroIds = new ArrayList<>();

	static {
		heroIds.add(1001);
		heroIds.add(1002);
		heroIds.add(1003);
		heroIds.add(1004);
		heroIds.add(1005);
		heroIds.add(1006);
		heroIds.add(1007);
		heroIds.add(1008);
		heroIds.add(1009);
		heroIds.add(1010);
		heroIds.add(1011);
		heroIds.add(1012);
		heroIds.add(1013);
		heroIds.add(1014);
		heroIds.add(2001);
		heroIds.add(2002);
		heroIds.add(2003);
		heroIds.add(2004);
		heroIds.add(2005);
		heroIds.add(2006);
		heroIds.add(2007);
		heroIds.add(2008);
		heroIds.add(2009);
		heroIds.add(2010);
		heroIds.add(2011);
		heroIds.add(2012);
		heroIds.add(2012);
		heroIds.add(2013);
		heroIds.add(3001);
		heroIds.add(3002);
		heroIds.add(3003);
		heroIds.add(3004);
		heroIds.add(3005);
		heroIds.add(3006);
		heroIds.add(3007);
		heroIds.add(3008);
		heroIds.add(3009);
		heroIds.add(3010);
		heroIds.add(3011);
	}

	public static void main(String[] args) {
		RandomDispatcher<Integer> dispatcher = new RandomDispatcher<>();
		dispatcher.put(1,1002);
		dispatcher.put(1,1003);
		dispatcher.put(1,1005);
		dispatcher.put(1,1006);
		dispatcher.put(1,1007);
		dispatcher.put(1,1008);
		dispatcher.put(1,1009);
		dispatcher.put(1,1010);
		dispatcher.put(1,1011);
		dispatcher.put(1,1012);
		dispatcher.put(1,1013);
		dispatcher.put(1,1014);
		dispatcher.put(1,2002);
		dispatcher.put(1,2003);
		dispatcher.put(1,2004);
		dispatcher.put(1,2005);
		dispatcher.put(1,2007);
		dispatcher.put(1,2008);
		dispatcher.put(1,2010);
		dispatcher.put(1,2011);
		dispatcher.put(1,2012);
		dispatcher.put(1,2013);
		dispatcher.put(1,3002);
		dispatcher.put(1,3003);
		dispatcher.put(1,3004);
		dispatcher.put(1,3005);
		dispatcher.put(1,3006);
		dispatcher.put(1,3007);
		dispatcher.put(1,3008);
		dispatcher.put(1,3009);
		dispatcher.put(1,3010);
		dispatcher.put(1,3011);
		for (int i = 0; i < 32; i++) {
			System.out.println(dispatcher.randomRemove());
		}
	}

}
