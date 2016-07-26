package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	private JTextArea puzzle; 
	private JTextArea solution;
	
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
		JButton check = new JButton("Check");
		JCheckBox autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);
		control.add(check);
		control.add(autoCheck);
		
		check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solvePuzzle();
			}
		});
		
		puzzle.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) solvePuzzle();
			}

			public void insertUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) solvePuzzle();
			}

			public void removeUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) solvePuzzle();
			}
		});
		
		add(puzzle, BorderLayout.CENTER);
		add(solution, BorderLayout.EAST);
		add(control, BorderLayout.SOUTH);

		setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void solvePuzzle() {
		String solText = "";
		try {
			Sudoku s = new Sudoku(puzzle.getText());
			int solCount = s.solve();
			if (solCount != 0) {
				solText = s.getSolutionText();
				solText += "solutions:" + solCount + '\n' + "elapsed:" + s.getElapsed() + "ms\n";
			}
		} catch (Exception ex) {
			solText = "Parsing problem";
		}
		solution.setText(solText);
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
