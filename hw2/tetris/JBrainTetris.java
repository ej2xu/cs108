package tetris;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

public class JBrainTetris extends JTetris {
	private Brain brain;
	private Brain.Move move;
	
	private JCheckBox brainMode;
	private JCheckBox animateMode;
	private JSlider adversary;
	private JButton loadBrain;
	private JTextField brainName;  
	
	protected int countRep;
	
	JBrainTetris(int pixel) {
		super(pixel);
		countRep = 0;
		brain = new DefaultBrain();
	}
	
	@Override
	public JComponent createControlPanel() {
		JComponent panel = super.createControlPanel();
		
		JPanel brainOp = new JPanel();
		panel.add(new JLabel("Brain:"));
		brainMode = new JCheckBox("Brain active");
		animateMode = new JCheckBox("Animate fall");
		panel.add(brainMode);
		panel.add(animateMode);
		animateMode.setSelected(true);
		
		JPanel adOp = new JPanel();
		panel.add(adOp);		
		adOp.add(new JLabel("Adversary:"));
		adversary = new JSlider(0, 100, 0);
		adversary.setPreferredSize(new Dimension(100, 15));
		adOp.add(adversary);
		
		panel.add(Box.createVerticalStrut(15));
		
		JPanel lbOp = new JPanel();
		panel.add(lbOp);
		loadBrain = new JButton("Load brain");
		lbOp.add(loadBrain);
		brainName = new JTextField(8);
		lbOp.add(brainName);		
		
		adversary.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
			}
		});
		
		return panel;
	}	

    @Override
    public void tick(int verb) {
        if (verb == DOWN && brainMode.isSelected()) {
            if (countRep != super.count) {
                countRep = super.count;
                board.undo();
                move = brain.bestMove(board, super.currentPiece, board.getHeight()-TOP_SPACE, move);
            }
            if(move != null){
                if (!move.piece.equals(currentPiece))
                	super.tick(ROTATE);
                if (move.x < currentX)
                	super.tick(LEFT);
                else if (move.x > currentX)
                	super.tick(RIGHT);
                else if (!animateMode.isSelected() && move.x == currentX && currentY > move.y)
                	super.tick(DROP);
            }
        }
        super.tick(verb);
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
