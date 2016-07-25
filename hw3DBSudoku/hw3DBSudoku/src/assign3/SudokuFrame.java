package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	private JTextArea puzzle; 
	private JTextArea solution; 
	private JButton check;
	private JCheckBox autoCheck;
	
	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4,4));
		
		puzzle = new JTextArea(15, 20);
		puzzle.setBorder(new TitledBorder("Puzzle"));
		solution = new JTextArea(15, 20);
		solution.setBorder(new TitledBorder("Solution"));
		solution.setEditable(false);
		
		JPanel control = new JPanel();
		control.setLayout(new BoxLayout(control, BoxLayout.X_AXIS));
		check = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check");
		control.add(check);
		control.add(autoCheck);
		
		add(puzzle, BorderLayout.CENTER);
		add(solution, BorderLayout.EAST);
		add(control, BorderLayout.SOUTH);
		
		// Could do this:
		// setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
