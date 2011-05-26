package main.gui.model;

import java.rmi.RemoteException;
import javax.swing.DefaultComboBoxModel;

import main.remote.RemoteGraph;

public class NextVertexModel extends DefaultComboBoxModel {
	private static final long serialVersionUID = 4850064767462942799L;
	
	private RemoteGraph graph;
	
	public NextVertexModel(RemoteGraph graph) {
		this.graph = graph;
	}
	
	@Override
	public int getSize() {
		try {
			return graph.getVertexes().size();
		} catch (RemoteException e) {
			return 0;
		}
	}
	
	@Override
	public Object getElementAt(int index) {
		try {
			return graph.getVertexes().get(index);
		} catch (RemoteException e) {
			return null;
		}
	}
}