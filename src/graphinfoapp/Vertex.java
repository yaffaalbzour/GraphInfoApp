package graphinfoapp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Vertex
	{
		int data;
		boolean visited;
		List<Vertex> neighbours;
 
		Vertex(int data)
		{
			this.data=data;
			this.neighbours=new ArrayList<>();
 
		}
		public void addneighbours(Vertex neighbourNode)
		{
			this.neighbours.add(neighbourNode);
		}
		public List<Vertex> getNeighbours() {
			return neighbours;
		}
		public void setNeighbours(List<Vertex> neighbours) {
			this.neighbours = neighbours;
		}
	}
 