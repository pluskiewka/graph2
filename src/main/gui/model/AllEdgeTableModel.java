package main.gui.model;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class AllEdgeTableModel extends DefaultTableModel {
	private static final long serialVersionUID = -212057958050954513L;
	private List<RemoteEdge> edges;
	
	public AllEdgeTableModel(RemoteGraph graph) throws RemoteException {
		this.edges = new LinkedList<RemoteEdge>();
		for(RemoteVertex vertex : graph.getVertexes()) {
			edges.addAll(vertex.getEdges());
		}
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
}
