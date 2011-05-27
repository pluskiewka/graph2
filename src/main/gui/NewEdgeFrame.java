package main.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import main.gui.model.EdgeTableModel;
import main.gui.model.NextVertexModel;
import main.gui.model.RemoteVertexWrapper;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class NewEdgeFrame extends JFrame {
	private static final long serialVersionUID = -5831381802616614633L;
	
	private static final String CANCEL = "Anuluj";
	private static final String SAVE = "Zapisz";
	private static final String NEXT_VERTEX = "Wierzchołek";
	private static final String LEVEL = "Waga";
	
	private JPanel mainPanel;
	private JLabel nextVertexLabel, levelLabel;
	private JComboBox nextVertexComboBox;
	private JTextField levelTextField;
	private JButton saveButton, cancelButton;
	
	public NewEdgeFrame(final RemoteGraph graph, final RemoteVertex vertex, final EdgeTableModel tableModel) throws RemoteException {
		super("Graph2 - New Edge");
		
		mainPanel = new JPanel();
		nextVertexLabel = new JLabel(NEXT_VERTEX);
		levelLabel = new JLabel(LEVEL);
		saveButton = new JButton(SAVE);
		cancelButton = new JButton(CANCEL);
		nextVertexComboBox = new JComboBox(new NextVertexModel(graph));
		levelTextField = new JTextField();
		
		nextVertexComboBox.setEditable(true);
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(nextVertexLabel)
								.addComponent(levelLabel))
						.addGroup(layout.createParallelGroup(Alignment.CENTER)
								.addComponent(nextVertexComboBox)
								.addComponent(levelTextField)))
				.addGroup(layout.createSequentialGroup()
						.addComponent(cancelButton)
						.addComponent(saveButton)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(nextVertexLabel)
						.addComponent(nextVertexComboBox))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(levelLabel)
						.addComponent(levelTextField))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(cancelButton)
						.addComponent(saveButton)));
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NewEdgeFrame.this.dispose();
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					graph.newEdge(vertex, ((RemoteVertexWrapper)nextVertexComboBox.getSelectedItem()).vertex, Integer.parseInt(levelTextField.getText()));
					tableModel.fireTableDataChanged();
					NewEdgeFrame.this.dispose();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(NewEdgeFrame.this, "Wartość wagi krawędzi całkowitoliczowa.");
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(NewEdgeFrame.this, "Ups.. remote error");
				}
			}
		});
		
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(300,135));
		this.setLocationByPlatform(true);
		this.pack();
		this.setVisible(true);
	}
}