package main.gui.model;

import java.rmi.RemoteException;

import javax.swing.table.DefaultTableModel;

import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class EdgeTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 8693903517704997240L;
	private RemoteGraph graph;
	private RemoteVertex vertex;
	
	public EdgeTableModel(RemoteGraph graph, RemoteVertex vertex) {
		this.graph = graph;
		this.vertex = vertex;
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public int getRowCount() {
		try {
			return vertex.getEdges().size();
		} catch (RemoteException e) {
			System.err.println(e.toString());
			return 0;
		}
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
			case 0: return vertex.getEdges().get(row).getSource().toString();
			case 1: return vertex.getEdges().get(row).getDest().toString();
			case 2: return vertex.getEdges().get(row).getLevel();
			case 3: return vertex.getEdges().get(row).getColor();
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
				graph.setLevel(vertex.getEdges().get(row), Integer.parseInt(value.toString()));
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
}