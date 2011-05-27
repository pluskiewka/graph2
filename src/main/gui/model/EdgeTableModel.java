package main.gui.model;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import main.remote.RemoteEdge;
import main.remote.RemoteVertex;

public class EdgeTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 8693903517704997240L;
	private RemoteVertex vertex;
	private List<RemoteEdge> edges;
	
	public EdgeTableModel(RemoteVertex vertex) throws RemoteException {
		this.vertex = vertex;
		this.edges = new LinkedList<RemoteEdge>(vertex.getEdges());
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public int getRowCount() {
		if(edges != null)
			return edges.size();
		return 0;
	}
	
	@Override
	public String getColumnName(int column) {
		switch(column) {
		case 0: return "Vertex - source";
		case 1: return "Vertex - dest";
		case 2: return "Level";
		case 3: return "Color";
		default: return "null";
		}
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		try {
			switch(column) {
			case 0: return edges.get(row).getSource().getName();
			case 1: return edges.get(row).getDest().getName();
			case 2: return edges.get(row).getLevel();
			case 3: return edges.get(row).getColor();
			default: return "null";
			}
		} catch (RemoteException e) {
			System.err.println(e.toString());
			return "error";
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if(column == 2)
			return true;
		return false;
	}
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		if(column == 2) {
			try {
				edges.get(row).getGraph().setLevel(edges.get(row), Integer.parseInt(value.toString()));
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	public void refresh() throws RemoteException {
		edges.removeAll(edges);
		edges.addAll(vertex.getEdges());
		fireTableDataChanged();
	}
}