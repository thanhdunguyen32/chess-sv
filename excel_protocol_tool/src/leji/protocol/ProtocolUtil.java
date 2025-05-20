package leji.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class ProtocolUtil {

	static JFrame frame = new JFrame("protocol消息协议工具");
	static JPanel p = new JPanel();
	JButton jb = new JButton("选择目录");
	JLabel directoryLabel = new JLabel();
	JButton beginConvert = new JButton("开始生成协议接口文件");
	JFileChooser chooser;
	String choosertitle = "选择消息协议配置json目录";
	private File excelDirectory = null;
	JTextArea logArea = null;

	private static Logger logger = LoggerFactory.getLogger(ProtocolUtil.class);

	public static void main(String[] args) throws IOException {
		new ProtocolUtil().Show();
	}

	public void Show() {
		jb.addActionListener(e -> {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("../../"));
			chooser.setDialogTitle(choosertitle);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {
				System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
				excelDirectory = chooser.getSelectedFile();
				directoryLabel.setText(excelDirectory.toString());
				logArea.setText("消息协议目录："+excelDirectory);
			} else {
				System.out.println("No Selection ");
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// 导表
		beginConvert.addActionListener(e -> {
			if(excelDirectory == null){
				logArea.setText("请选择消息协议json目录！");
				return;
			}
			logArea.setText("");
			logger.info("------------protocol start--------------");
			String protocolPath = excelDirectory.getPath() + "/";
			try {
				GeneratorJava.generate(protocolPath);
				GeneratorTsCreator.generate(protocolPath);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
			logger.info("------------protocol end----------------");
		});

		logArea = new JTextArea(20, 20);
		logArea.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 5));
		logArea.setColumns(0);
		logArea.setRows(0);// 相当于设置文本区组件的初始大小,并不是说一行只能写0个字符;
		logArea.setLineWrap(false);// 设置为禁止自动换行,初始值为false.
		logArea.setTabSize(4);// 设置制表符的大小为8个字符,初始值为4个字符
		logArea.setWrapStyleWord(true);
		logArea.setBackground(Color.white);// 文本区背景
		logArea.setForeground(Color.blue);// 字体颜色
		// logArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

		// JTextArea.append("you text");
		// JTextArea.paintImmediately(JTextArea.getBounds());

		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setPreferredSize(new Dimension(500, 300));
		scrollPane.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 10, Color.white));

		p.add(jb);
		p.add(directoryLabel);
		p.add(beginConvert);
		p.add(scrollPane);
		frame.add(p, "Center");
		frame.setSize(600, 400);
		frame.setVisible(true);

		//init
		excelDirectory = new File("/hack/git_hack_sango/protocol");
		directoryLabel.setText(excelDirectory.toString());
		logArea.setText("消息协议目录："+excelDirectory);
	}

}
