package webLoaderAssign;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;


public class WebFrame extends JFrame {
	private static final String FILENAME = System.getProperty("user.dir") + "/bin/links.txt";
	private DefaultTableModel model;
	private JTable table;
	// in top to button order on the GUI
	private JButton single;
	private JButton concurrent;
	private JTextField numConcurrent;
	private JLabel running;
	private JLabel completed;
	private JLabel elapsed;
	private JProgressBar progress;
	private JButton stop;
	
	private List<WebWorker> workers;
	private List<String> urls;
	public Launcher launcher;
	private int numWorkers;
	public AtomicInteger runCount;
    private AtomicInteger completedCount;
	private Semaphore workerSemaphore;
    private long startTime;
	
	public WebFrame() {
		super("WebLoader");
		launcher = null;
		urls = new ArrayList<String>();
		JPanel panel = (JPanel) getContentPane();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		model = new DefaultTableModel(new String[] {"url", "status"}, 0);
		loadFile();
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(600, 300));
		panel.add(scrollpane);
		
		single = new JButton("Single Thread Fetch");
		add(single);
		single.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher = new Launcher(1);
				launch();
			}
		});		
		concurrent = new JButton("Concurrent Fetch");
		add(concurrent);
		concurrent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher = new Launcher(Integer.parseInt(numConcurrent.getText()));
				launch();	
			}
		});
		numConcurrent = new JTextField("4", 4);
		numConcurrent.setMaximumSize(numConcurrent.getPreferredSize());
		add(numConcurrent);
		running = new JLabel("Running:");
		add(running);
		completed = new JLabel("Completed:");
		add(completed);
		elapsed = new JLabel("Elapsed:");
		add(elapsed);
		progress = new JProgressBar(0, urls.size());
		progress.setValue(0);
		add(progress);
		stop = new JButton("Stop");
		stop.setEnabled(false);
		add(stop);
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.interrupt();
				for (WebWorker w : workers)
					w.interrupt();
				stop();
			}
		});		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void launch() {
		single.setEnabled(false);
		concurrent.setEnabled(false);
		running.setText("Running:");
		completed.setText("Completed:");
		elapsed.setText("Elapsed:");
		startTime = System.currentTimeMillis();
		launcher.start();
		stop.setEnabled(true);
		for (int i = 0; i < urls.size(); i++)
			model.setValueAt("", i, 1);
	}
	
	private void stop() {
		single.setEnabled(true);
		concurrent.setEnabled(true);
		stop.setEnabled(false);
		progress.setValue(0);
	}
	
	public void releaseWorker(String status, int rowToUpdate) {
		runCount.decrementAndGet();
		completedCount.incrementAndGet();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				running.setText("Running:" + runCount);
				completed.setText("Completed:" + completedCount);
				progress.setValue(completedCount.get());
				model.setValueAt(status, rowToUpdate, 1);
			}
		});
		workerSemaphore.release();
		checkDone();
	}
	
	private void checkDone() {
		long timeElapsed = System.currentTimeMillis() - startTime;
		if (runCount.get() == 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					stop();
					elapsed.setText("Elapsed:" + timeElapsed);
				}
			});
		}
	}
	
	public class Launcher extends Thread {
		private WebFrame frame;
		
		public Launcher(int numConcurrent) {
			workerSemaphore = new Semaphore(numConcurrent);
			workers = new ArrayList<WebWorker>();
			runCount = new AtomicInteger(0);
			completedCount = new AtomicInteger(0);
			frame = WebFrame.this;
		}
		
		public void run() {
			runCount.incrementAndGet();
			showRun();
			for (int i = 0; i < numWorkers; i++) {
				try {
					workerSemaphore.acquire();
					WebWorker worker = new WebWorker(urls.get(i), i, frame);
					workers.add(worker);
					worker.start();
				}
				catch (InterruptedException e) {
					break;
				}
				
				if (isInterrupted()) break;	
			}
			runCount.decrementAndGet();
			showRun();
			checkDone();
		}
		
		public void showRun() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					running.setText("Running:" + runCount);
				}
			});
		}

	}
	
	public void loadFile() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(FILENAME));

			String line;
			while ((line = in.readLine()) != null) {
				model.addRow(new String[]{line, ""});
				urls.add(line);
				numWorkers++;
			} 
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}
		new WebFrame();
	}
}
