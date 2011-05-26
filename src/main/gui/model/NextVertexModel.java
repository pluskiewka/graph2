package main.gui.model;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import main.remote.RemoteVertex;

public class NextVertexModel extends DefaultComboBoxModel {
	private static final long serialVersionUID = 4850064767462942799L;
	
	private List<RemoteVertex> vertexes;
	
	public NextVertexModel(List<RemoteVertex> vertexes) {
		this.vertexes = vertexes;
	}
	
	@Override
	public Object getElementAt(int index) {
		return vertexes.get(index);
	}
}