package assign3;

/*
Demonstrate the censor table model -- creates a model with a table,
and a censor model.
*/

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import javax.swing.event.*;
import java.io.*;

class MetFrame extends JFrame {
	private static final int TEXT_FIELDS_SIZE = 12;
	private static final String POPULATION_LEQ = "Population Smaller Than or Equal To";
    private static final String POPULATION_LAR  = "Population Larger Than";
    private static final String EXACT_MATCH = "Exact Match";
    private static final String PARTIAL_MATCH = "Partial Match";
	private MetTableModel model;
	
	JTextField metText;
	JTextField conText;
	JTextField popText;
		
	public MetFrame(String title) {
		super(title);
		model = new MetTableModel();
		JTable table = new JTable(model);
		
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(300,200));
		add(scrollpane, BorderLayout.CENTER);
		
		JPanel search = new JPanel();
		search.setLayout(new BoxLayout(search, BoxLayout.X_AXIS));
		add(search, BorderLayout.NORTH);
		
		search.add(new JLabel("Metropolis: "));
		metText = new JTextField(TEXT_FIELDS_SIZE);
		metText.setMaximumSize(new Dimension(140, 24));
		search.add(metText);
		search.add(new JLabel("Continent: "));
		conText = new JTextField(TEXT_FIELDS_SIZE);
		conText.setMaximumSize(new Dimension(140, 24));
		search.add(conText);		
		search.add(new JLabel("Population: "));
		popText = new JTextField(TEXT_FIELDS_SIZE);
		popText.setMaximumSize(new Dimension(140, 24));
		search.add(popText);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		add(buttons, BorderLayout.EAST);

		JButton addButton = new JButton("Add");
		buttons.add(addButton);
		addButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String city = metText.getText();
					String region = conText.getText();
					String popNum = popText.getText();
					
					model.addData(city, region, popNum);
				}
			}
		);
			
		JButton searButton = new JButton("Search");
		buttons.add(searButton);
		
		buttons.add(new JLabel("Search options"));
		JComboBox<String> popPulldown = new JComboBox(new String[]{POPULATION_LAR, POPULATION_LEQ});
		JComboBox<String> mtPulldown = new JComboBox(new String[]{EXACT_MATCH, PARTIAL_MATCH});
		buttons.add(popPulldown);
		buttons.add(mtPulldown);	
		
		searButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean larger = popPulldown.getSelectedItem().equals(POPULATION_LAR);
                boolean exact = mtPulldown.getSelectedItem().equals(EXACT_MATCH);
                model.searchData(metText.getText(), conText.getText(), popText.getText(), larger, exact);
            }
        });
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	static public void main(String[] args) {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
		new MetFrame("Metropolis Viewer");
	}
}