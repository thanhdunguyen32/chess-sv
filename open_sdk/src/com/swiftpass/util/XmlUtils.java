/**
 * Project Name:pay-protocol
 * File Name:Xml.java
 * Package Name:cn.swiftpass.pay.protocol
 * Date:2014-8-10下午10:48:21
 *
*/

package com.swiftpass.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * ClassName:Xml Function: XML的工具方法 Date: 2014-8-10 下午10:48:21
 * 
 * @author
 */
public class XmlUtils {

	/**
	 * <一句话功能简述> <功能详细描述>request转字符串
	 * 
	 * @param request
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String parseRequst(FullHttpRequest request) {
		String body = "";
		try {
			ByteBuf httpContent = request.content();
			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteBufInputStream(httpContent)));
			while (true) {
				String info = br.readLine();
				if (info == null) {
					break;
				}
				if (body == null || "".equals(body)) {
					body = info;
				} else {
					body += info;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	public static String parseRequst(String httpContent) {
		String body = "";
		try {
			BufferedReader br = new BufferedReader(new StringReader(httpContent));
			while (true) {
				String info = br.readLine();
				if (info == null) {
					break;
				}
				if (body == null || "".equals(body)) {
					body = info;
				} else {
					body += info;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	public static String parseXML(SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"appkey".equals(k)) {
				sb.append("<" + k + ">" + parameters.get(k) + "</" + k + ">\n");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 从request中获得参数Map，并返回可读的Map
	 * 
	 * @param request
	 * @return
	 */
	public static SortedMap getParameterMap(FullHttpRequest request) {
		// 参数Map
		HttpHeaders properties = request.headers();
		// 返回值Map
		SortedMap returnMap = new TreeMap();
		Iterator entries = properties.iteratorAsString();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value.trim());
		}
		return returnMap;
	}

	/**
	 * 转XMLmap
	 * 
	 * @author
	 * @param xmlBytes
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> toMap(byte[] xmlBytes, String charset) throws Exception {
		InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		source.setEncoding(charset);
		Document doc = builder.parse(source);
		Map<String, String> params = XmlUtils.toMap(doc.getDocumentElement());
		return params;
	}

	public static Map<String, String> toMap(String xmlContent, String charset) throws Exception {
		InputSource source = new InputSource(new StringReader(xmlContent));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		source.setEncoding(charset);
		Document doc = builder.parse(source);
		Map<String, String> params = XmlUtils.toMap(doc.getDocumentElement());
		return params;
	}

	/**
	 * 转MAP
	 * 
	 * @author
	 * @param element
	 * @return
	 */
	public static Map<String, String> toMap(Element element) {
		Map<String, String> rest = new HashMap<String, String>();
		NodeList els = element.getChildNodes();
		for (int i = 0; i < els.getLength(); i++) {
			Node aNode = els.item(i);
			if (aNode.getNodeType() != 1) { // 为element 元素，3为文本元素
				continue;
			}
			String aa = aNode.getTextContent();
			rest.put(aNode.getNodeName().toLowerCase(), aa);
		}
		return rest;
	}

	public static String toXml(Map<String, String> params) {
		StringBuilder buf = new StringBuilder();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		buf.append("<xml>");
		for (String key : keys) {
			buf.append("<").append(key).append(">");
			buf.append("<![CDATA[").append(params.get(key)).append("]]>");
			buf.append("</").append(key).append(">\n");
		}
		buf.append("</xml>");
		return buf.toString();
	}
}
