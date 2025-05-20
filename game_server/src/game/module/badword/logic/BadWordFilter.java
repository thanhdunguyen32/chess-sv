package game.module.badword.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.badword.dao.BadWordTemplateCache;
import game.module.template.BadWordTemplate;

public class BadWordFilter {

	private static Logger logger = LoggerFactory.getLogger(BadWordFilter.class);

	static class SingletonHolder {
		static BadWordFilter instance = new BadWordFilter();
	}

	public static BadWordFilter getInstance() {
		return SingletonHolder.instance;
	}

	private volatile List<String> arrt = new ArrayList<String>();// 文字
	private volatile Node rootNode = new Node('R');

	/**
	 * 过滤敏感词
	 * 
	 * @param content
	 * @return
	 */
	public String filterBadWords(String content) {
		char[] chars = content.toCharArray();
		Node node = rootNode;
		StringBuilder buffer = new StringBuilder();
		List<String> badList = new ArrayList<String>();
		int a = 0;
		while (a < chars.length) {
			node = findNode(node, chars[a]);
			if (node == null) {
				node = rootNode;
				a = a - badList.size();
				if (badList.size() > 0) {
					badList.clear();
				}
				buffer.append(chars[a]);
			} else if (node.flag == 1) {
				badList.add(String.valueOf(chars[a]));
				for (int i = 0; i < badList.size(); i++) {
					buffer.append("*");
				}
				node = rootNode;
				badList.clear();
			} else {
				badList.add(String.valueOf(chars[a]));
				if (a == chars.length - 1) {
					for (int i = 0; i < badList.size(); i++) {
						buffer.append(badList.get(i));
					}
				}
			}
			a++;
		}
		return buffer.toString();
	}

	private void initWords(List<BadWordTemplate> badWordTemplates) {
		for (BadWordTemplate aTemplate : badWordTemplates) {
			String astr = aTemplate.getName();
			arrt.add(astr.replace("\"", "").trim());
		}
	}

	private void createTree() {
		for (String str : arrt) {
			char[] chars = str.toCharArray();
			if (chars.length > 0)
				insertNode(rootNode, chars, 0);
		}
	}

	private void insertNode(Node node, char[] cs, int index) {
		Node n = findNode(node, cs[index]);
		if (n == null) {
			n = new Node(cs[index]);
			node.nodes.put(String.valueOf(cs[index]), n);
		}

		if (index == (cs.length - 1)) {
			n.flag = 1;
		}

		index++;
		if (index < cs.length)
			insertNode(n, cs, index);
	}

	/**
	 * 是否有坏字
	 * 
	 * @param content
	 * @return true有坏字 false 没有
	 */
	public boolean hasBadWords(String content) {
		char[] chars = content.toCharArray();
		Node node = rootNode;
		List<String> word = new ArrayList<String>();
		int a = 0;
		while (a < chars.length) {
			node = findNode(node, chars[a]);
			if (node == null) {
				node = rootNode;
				a = a - word.size();
				word.clear();
			} else if (node.flag == 1) {
				node = null;
				return true;
			} else {
				word.add(String.valueOf(chars[a]));
			}
			a++;
		}
		chars = null;
		word.clear();
		word = null;
		return false;
	}

	private Node findNode(Node node, char c) {
		// for (Iterator iterator = node.nodes.keySet().iterator();
		// iterator.hasNext();) {
		// String str = (String) iterator.next();
		// }
		return node.nodes.get(String.valueOf(c));
	}

	private static class Node {
		// public char c;
		public int flag;
		public HashMap<String, Node> nodes = new HashMap<String, Node>();

		public Node(char c) {
			this(c, 0);
		}

		public Node(char c, int flag) {
			// this.c = c;
			this.flag = flag;
		}
	}

	public void reload(List<BadWordTemplate> badWordTemplates) {
		rootNode = new Node('R');
		arrt.clear();
		initWords(badWordTemplates);
		createTree();
	}

	public String getAppname() {
		return "gameServer";
	}

	public String getCachename() {
		return "badword";
	}

	public static void main(String[] args) {
		List<BadWordTemplate> badWordTemplates = BadWordTemplateCache.getInstance().loadFromDb();
		BadWordFilter.getInstance().reload(badWordTemplates);
		long startTime = System.currentTimeMillis();
		boolean ret1 = BadWordFilter.getInstance().hasBadWords("性高潮1");
		logger.info("ret1={},cost={}ms", ret1, System.currentTimeMillis() - startTime);
	}

}
