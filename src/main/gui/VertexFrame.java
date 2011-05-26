package main.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;

import main.gui.model.EdgeTableModel;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class VertexFrame extends JFrame {
	private static final long serialVersionUID = -2370579390468689470L;
	
	private static final String NEW_EDGE = "Nowa krawędź";
	
	private EdgeTableModel edgeTableModel;
	private JPanel mainPanel;
	private JButton newEdgeButton;
	private JTable edgeTable;
	private JScrollPane edgePane;
	
	public VertexFrame(final RemoteGraph graph, final RemoteVertex vertex) {
		super("Graph2 - Vertex");
		
		edgeTableModel = new EdgeTableModel(graph, vertex);
		mainPanel = new JPanel();
		newEdgeButton = new JButton(NEW_EDGE);
		edgeTable = new JTable(edgeTableModel);
		edgePane = new JScrollPane(edgeTable);
		
		GroupLayout layout  = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		newEdgeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new NewEdgeFrame(graph, vertex);
			}
		});
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(newEdgeButton))
				.addGroup(layout.createSequentialGroup()
					.addComponent(edgePane)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(newEdgeButton))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(edgePane)));
		
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(142,220));
		this.setLocationByPlatform(true);
		this.pack();
		this.setVisible(true);
	}
}