package countAssign;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private final static int COUNT_NUM = 4;
	
	private JTextField input;
	private JLabel current;
	private JButton start;
	private JButton stop;
	private Worker worker;
	
	public JCount() {
		super();
		worker = null;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		input = new JTextField("100000000", 8);
		add(input);
		current = new JLabel("0");
		add(current);
		start = new JButton("Start");
		add(start);		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (worker != null) worker.interrupt();
				
				worker = new Worker(input.getText());
				worker.start();
			}
		});
		
		stop = new JButton("Stop");
		add(stop);
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (worker != null) {
					worker.interrupt();
					worker = null;
				}
			}
		});
		
		add(Box.createRigidArea(new Dimension(0, 40)));
	}
	
	private class Worker extends Thread {
		private int num;
		private final static int UPDATE_INTERVAL = 10000;
		public Worker(String numStr) {
			num = Integer.parseInt(numStr);
		}
		
		public void run() {
			for (int i = 1; i <= num; i++) {
				if (i % UPDATE_INTERVAL == 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						break;
					}
					final String finalText = "" + i;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							current.setText(finalText);
						}
					});
				}
				if (isInterrupted()) break;				
			}				
		}
	}
	
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("The Count");
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		for (int i = 0; i < COUNT_NUM; i++)
			frame.add(new JCount());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
