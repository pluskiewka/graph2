package main.gui.model;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import main.remote.RemoteVertex;

public class VertexTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1235329209779281312L;
	private List<RemoteVertex> vertexes;
	
	public VertexTableModel(List<RemoteVertex> vertexes) {
		this.vertexes = vertexes;
	}
	
	@Override
	public int getColumnCount() {
		return 1;
	}
	
	@Override
	public int getRowCount() {
		return vertexes.size();
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
		return vertexes.get(row).toString();
	}
}