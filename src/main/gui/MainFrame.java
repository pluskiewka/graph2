package main.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;

import main.gui.model.AllEdgeTableModel;
import main.gui.model.VertexTableModel;
import main.remote.RemoteGraph;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 6059401950657455790L;

	private static final String NEW_VERTEX = "Nowy wierzchołek";
	
	private VertexTableModel vertexTableModel;
	private AllEdgeTableModel edgeTableModel;
	
	private JPanel mainPanel;
	private JScrollPane vertexPane, edgePane;
	private JTable vertexTable, edgeTable;
	private JButton newVertexButton;
	
	public MainFrame(final RemoteGraph graph) throws RemoteException {
		super("Graph2");
	
		vertexTableModel = new VertexTableModel(graph);
		edgeTableModel = new AllEdgeTableModel(graph);
		mainPanel = new JPanel();
		newVertexButton = new JButton(NEW_VERTEX);
		vertexTable = new JTable(vertexTableModel);
		vertexPane = new JScrollPane(vertexTable);
		edgeTable = new JTable(edgeTableModel);
		edgePane = new JScrollPane(edgeTable);
		
		GroupLayout layout  = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		newVertexButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					graph.newVertex(0);
					vertexTableModel.fireTableDataChanged();
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(MainFrame.this, e.toString());
				}
			}
		});
		
		vertexTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)
					new VertexFrame(graph, vertexTableModel.get(vertexTable.getSelectedRow()));
			}
		});
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(newVertexButton))
				.addGroup(layout.createSequentialGroup()
					.addComponent(vertexPane)
					.addComponent(edgePane)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(newVertexButton))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(vertexPane)
						.addComponent(edgePane)));
		
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(500,300));
		this.setLocationByPlatform(true);
		this.pack();
		this.setVisible(true);
	}
}