package main.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;

import org.apache.log4j.Logger;

import main.gui.model.EdgeTableModel;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class VertexFrame extends JFrame {
	private static final long serialVersionUID = -2370579390468689470L;
	private static final Logger logger = Logger.getLogger(VertexFrame.class);
	private static final String NEW_EDGE = "Nowa krawędź";
	private static final String REFRESH = "Odśwież";
	
	private EdgeTableModel edgeTableModel;
	private JPanel mainPanel;
	private JButton newEdgeButton, refreshButton;
	private JTable edgeTable;
	private JScrollPane edgePane;
	
	public VertexFrame(final RemoteGraph graph, final RemoteVertex vertex) throws RemoteException {
		super("Graph2 - Vertex");
		
		edgeTableModel = new EdgeTableModel(vertex);
		mainPanel = new JPanel();
		newEdgeButton = new JButton(NEW_EDGE);
		refreshButton = new JButton(REFRESH);
		edgeTable = new JTable(edgeTableModel);
		edgePane = new JScrollPane(edgeTable);
		
		edgeTable.setAutoCreateRowSorter(true);
		
		GroupLayout layout  = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		newEdgeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new NewEdgeFrame(graph, vertex, edgeTableModel);
				} catch (RemoteException e1) {
					logger.error(e1.toString());
					JOptionPane.showMessageDialog(VertexFrame.this, "Błąd podczas otwierania okna");
				}
			}
		});
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					edgeTableModel.refresh();
				} catch (RemoteException e1) {
					logger.error(e1.toString());
					JOptionPane.showMessageDialog(VertexFrame.this, "Błąd w trakcie odświeżania widoku");
				}
			}
		});
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(newEdgeButton)
						.addComponent(refreshButton))
				.addGroup(layout.createSequentialGroup()
					.addComponent(edgePane)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(newEdgeButton)
						.addComponent(refreshButton))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(edgePane)));
		
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(500,300));
		this.setLocationByPlatform(true);
		this.pack();
		this.setVisible(true);
	}
}