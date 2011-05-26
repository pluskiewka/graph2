package main.gui.model;

import java.rmi.RemoteException;

import javax.swing.table.DefaultTableModel;

import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class VertexTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1235329209779281312L;
	private RemoteGraph graph;
	
	public VertexTableModel(RemoteGraph graph) {
		this.graph = graph;
	}
	
	@Override
	public int getColumnCount() {
		return 1;
	}
	
	@Override
	public int getRowCount() {
		if(graph != null)
			try {
				return graph.getVertexes().size();
			} catch (RemoteException e) {
				return 0;
			}
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
			return graph.getVertexes().get(row).toString();
		} catch (RemoteException e) {
			return "error";
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public RemoteVertex get(int selectedRow) {
		try {
			return graph.getVertexes().get(selectedRow);
		} catch (RemoteException e) {
			return null;
		}
	}
}