package main.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.LinkedList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;

import main.gui.model.VertexTableModel;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 6059401950657455790L;

	private static final String NEW_VERTEX = "Nowy wierzchołek";
	
	private VertexTableModel vertexTableModel;
	
	private JPanel mainPanel;
	private JScrollPane vertexPane;
	private JTable vertexTable;
	private JButton newVertexButton;
	
	public MainFrame(final RemoteGraph graph) {
		super("Graph2");
	
		try {
			vertexTableModel = new VertexTableModel(graph.getVertexes());
		} catch (RemoteException e2) {
			vertexTableModel = new VertexTableModel(new LinkedList<RemoteVertex>());
		}
		mainPanel = new JPanel();
		newVertexButton = new JButton(NEW_VERTEX);
		vertexTable = new JTable(vertexTableModel);
		vertexPane = new JScrollPane(vertexTable);
		
		GroupLayout layout  = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		newVertexButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					graph.newVertex();
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e.toString());
				}
			}
		});
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(newVertexButton))
				.addGroup(layout.createSequentialGroup()
					.addComponent(vertexPane)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(newVertexButton))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(vertexPane)));
		
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(142,220));
		this.setLocationByPlatform(true);
		this.pack();
		this.setVisible(true);
	}
}