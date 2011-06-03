package main.gui.model;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class VertexTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1235329209779281312L;
	private static final Logger logger = Logger.getLogger(VertexTableModel.class);
	private RemoteGraph graph;
	private List<RemoteVertex> vertexes;
	
	public VertexTableModel(RemoteGraph graph) throws RemoteException {
		this.graph = graph;
		vertexes = new LinkedList<RemoteVertex>(graph.getVertexes());
	}
	
	@Override
	public int getColumnCount() {
		return 1;
	}
	
	@Override
	public int getRowCount() {
		if(vertexes != null)
			return vertexes.size();
		return 0;
	}
	
	@Override
	public String getColumnName(int column) {
		switch(column) {
		case 0: return "Vertex";
		default: return "null";
		}
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		try {
			return vertexes.get(row).getName();
		} catch (RemoteException e) {
			logger.error(e.toString());
			return "null";
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public RemoteVertex get(int selectedRow) {
		return vertexes.get(selectedRow);
	}

	public void refresh() throws RemoteException {
		vertexes.removeAll(vertexes);
		vertexes.addAll(graph.getVertexes());
		fireTableDataChanged();
	}
}