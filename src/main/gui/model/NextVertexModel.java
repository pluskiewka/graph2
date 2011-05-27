package main.gui.model;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class NextVertexModel extends DefaultComboBoxModel {
	private static final long serialVersionUID = 4850064767462942799L;
	
	private List<RemoteVertex> vertexes;
	
	public NextVertexModel(RemoteGraph graph) throws RemoteException {
		vertexes = new LinkedList<RemoteVertex>(graph.getVertexes());
	}
	
	@Override
	public int getSize() {
		if(vertexes != null)
			return vertexes.size();
		return 0;
	}
	
	@Override
	public Object getElementAt(int index) {
		return new RemoteVertexWrapper(vertexes.get(index));
	}
}