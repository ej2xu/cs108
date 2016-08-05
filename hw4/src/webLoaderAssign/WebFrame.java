package webLoaderAssign;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;


public class WebFrame extends JFrame {
	private DefaultTableModel model;
	private JTable table;
	// in top to button order on the GUI
	private JButton single;
	private JButton concurrent;
	private JTextField numOfWorker;
	private JLabel running;
	private JLabel completed;
	private JLabel elapsed;
	private JProgressBar progress;
	private JButton stop;
	
	public WebFrame() {
		super("WebLoader");
		JPanel panel = (JPanel) getContentPane();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		model = new DefaultTableModel(new String[] {"url", "status"}, 0);
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(600, 300));
		panel.add(scrollpane);
		
		single = new JButton("Single Thread Fetch");
		add(single);
		single.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});		
		concurrent = new JButton("Concurrent Fetch");
		add(concurrent);
		single.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		numOfWorker = new JTextField("4", 4);
		numOfWorker.setMaximumSize(numOfWorker.getPreferredSize());
		add(numOfWorker);
		running = new JLabel("Running:");
		add(running);
		completed = new JLabel("Completed:");
		add(completed);
		elapsed = new JLabel("Elapsed:");
		add(elapsed);
		progress = new JProgressBar(0, model.getRowCount());
		add(progress);
		stop = new JButton("Stop");
		stop.setEnabled(false);
		add(stop);
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}
		new WebFrame();
	}
}
