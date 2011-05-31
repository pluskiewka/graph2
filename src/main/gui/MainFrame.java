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

import org.apache.log4j.Logger;

import main.gui.model.AllEdgeTableModel;
import main.gui.model.VertexTableModel;
import main.remote.RemoteGraph;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 6059401950657455790L;
	private static final Logger logger = Logger.getLogger(MainFrame.class);
	private static final String NEW_VERTEX = "Nowy wierzchołek";
	private static final String REFRESH = "Odśwież";
	
	private VertexTableModel vertexTableModel;
	private AllEdgeTableModel edgeTableModel;
	
	private JPanel mainPanel;
	private JScrollPane vertexPane, edgePane;
	private JTable vertexTable, edgeTable;
	private JButton newVertexButton, refreshButton;
	
	public MainFrame(final RemoteGraph graph) throws RemoteException {
		super("Graph2");
	
		vertexTableModel = new VertexTableModel(graph);
		edgeTableModel = new AllEdgeTableModel(graph);
		mainPanel = new JPanel();
		newVertexButton = new JButton(NEW_VERTEX);
		refreshButton = new JButton(REFRESH);
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
					JOptionPane.showMessageDialog(MainFrame.this, "Błąd w trakcie tworzenia wierzchołka");
				}
			}
		});
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					vertexTableModel.refresh();
					edgeTableModel.refresh();
				} catch (RemoteException e1) {
					logger.error(e1.toString());
					JOptionPane.showMessageDialog(MainFrame.this, "Błąd w trakcie odświeżania tabeli");
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
					try {
						new VertexFrame(graph, vertexTableModel.get(vertexTable.getSelectedRow()));
					} catch (RemoteException e1) {
						logger.error(e1.toString());
						JOptionPane.showMessageDialog(MainFrame.this, "Błąd podczas otwierania okna");
					}
			}
		});
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(newVertexButton)
						.addComponent(refreshButton))
				.addGroup(layout.createSequentialGroup()
					.addComponent(vertexPane)
					.addComponent(edgePane)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(newVertexButton)
						.addComponent(refreshButton))
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