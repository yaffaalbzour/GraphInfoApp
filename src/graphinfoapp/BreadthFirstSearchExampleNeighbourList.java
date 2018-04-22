package graphinfoapp;
  
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
 
public class BreadthFirstSearchExampleNeighbourList
{ 
        private static final String FILENAME = "/Users/yaffa/Desktop/Graph.txt";
        public static HashMap<Integer,Node> Graph = new HashMap();//to store the graph 

	private Queue<Node> queue;
	static ArrayList<Node> nodes=new ArrayList<Node>();
	static class Node
	{
		int data;
		boolean visited;
		List<Node> neighbours;
 
		Node(int data)
		{
			this.data=data;
			this.neighbours=new ArrayList<>();
 
		}
		public void addneighbours(Node neighbourNode)
		{
			this.neighbours.add(neighbourNode);
		}
		public List<Node> getNeighbours() {
			return neighbours;
		}
		public void setNeighbours(List<Node> neighbours) {
			this.neighbours = neighbours;
		}
	}
 
	public BreadthFirstSearchExampleNeighbourList()
	{
		queue = new LinkedList<Node>();
	}
 
	public void bfs(Node node)
	{
		queue.add(node);
		node.visited=true;
		while (!queue.isEmpty())
		{
 
			Node element=queue.remove();
			System.out.print(element.data + "\t");
			List<Node> neighbours=element.getNeighbours();
			for (int i = 0; i < neighbours.size(); i++) {
				Node n=neighbours.get(i);
				if(n!=null && !n.visited)
				{
					queue.add(n);
					n.visited=true;
 
				}
			}
		}
	}
 
	public static void main(String arg[])
	{
  try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String sCurrentLine; String[] parts;
            while((sCurrentLine = br.readLine()) != null) {
                //split by , and insert first line elements into HashMap as keys
                if(sCurrentLine.charAt(0)!='('){
                    parts = sCurrentLine.split(",");
                    for(int i=0; i<parts.length; i++){
                        Integer vertexName=Integer.parseInt(parts[i]);
                        Node vertex = new Node(vertexName);
                        //vertex.setDistances(vertex, 0);
                        Graph.putIfAbsent(vertexName,vertex);
                        
                    }
                }
                else{
                    sCurrentLine=sCurrentLine.replaceAll("[^0-9,]","");
                    parts = sCurrentLine.split(",");
                    int vertex1Name=Integer.parseInt(parts[0]);
                    int vertex2Name=Integer.parseInt(parts[1]);
                    Graph.get(vertex1Name).addneighbours(Graph.get(vertex2Name));
                    Graph.get(vertex2Name).addneighbours(Graph.get(vertex1Name));
                   }
            }
        } 
        catch (IOException e) {
        }
		System.out.println("The BFS traversal of the graph is ");
		BreadthFirstSearchExampleNeighbourList bfsExample = new BreadthFirstSearchExampleNeighbourList();
		bfsExample.bfs(Graph.get(2));
 
	}
}