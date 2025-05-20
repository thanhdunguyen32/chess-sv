package cn.hxh;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cn.hxh.tool.ConvertorMain;

public class ExtractorTool {

	// extends JPanel implements ActionListener
	static JFrame frame = new JFrame("导表工具");
	static JPanel p = new JPanel();
	JButton jb = new JButton("选择目录");
	JLabel directoryLabel = new JLabel();
	JButton beginConvert = new JButton("开始导表");
	JFileChooser chooser;
	String choosertitle = "选择策划excel目录";

	private File excelDirectory = null;

	JTextArea logArea = null;

	public static void main(String s[]) {
		new ExtractorTool().Show();
	}

	public void Show() {
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle(choosertitle);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {
					System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
					System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
					excelDirectory = chooser.getSelectedFile();
					directoryLabel.setText(excelDirectory.toString());
				} else {
					System.out.println("No Selection ");
				}
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// 导表
		beginConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logArea.setText("");
				new ConvertorMain().doConvert(excelDirectory.getPath() + "/", logArea);
			}
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
	}

}
