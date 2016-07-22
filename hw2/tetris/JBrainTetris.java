package tetris;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class JBrainTetris extends JTetris {
	private Brain brain;
	JBrainTetris(int pixel) {
		super(pixel);
	}
	
	@Override
	public JComponent createControlPanel() {
		
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		JBrainTetris tetris = new JBrainTetris(16);
		JFrame frame = JTetris.createFrame(tetris);
		frame.setVisible(true);
	}
}
