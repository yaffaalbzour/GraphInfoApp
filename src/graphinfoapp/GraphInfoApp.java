
package graphinfoapp;
import graphinfoapp.Vertex;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.*;
import javafx.scene.*;
import java.util.*;
import javax.swing.JFrame;
public class GraphInfoApp  {
    public static HashMap<Integer,Vertex> Graph = new HashMap();//to store the graph 
    public static ArrayList< Set< Integer > > GraphEdges = new ArrayList< Set< Integer > >();// for the edges in the Graph
    public static ArrayList<Integer> Pathes;//The path from the Stack in DFS algorithm
    public static HashMap<Integer,Vertex> FakeGraph = new HashMap();// to make the graph as tree
    public static ArrayList< Set< Integer > > FakeGraphEdges = new ArrayList< Set< Integer > >();// for the edges in the FakeGraph
    public static ArrayList<ArrayList< Set< Integer > >> Cycles=new ArrayList<ArrayList< Set< Integer > >>();/////////
    public static ArrayList< Set< Integer > > CyclesEdges = new ArrayList< Set< Integer > >();
    private static final String FILENAME = ".\\.\\Graph.txt";//Graph file path
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {//put file in buff.
            String sCurrentLine;
            while((sCurrentLine = br.readLine()) != null) {
                //split by , and insert first line elements into HashMap as keys
                String[] parts = sCurrentLine.split(",");
                if(parts[0].charAt(0)!='(')
                for(int i=0;i<parts.length;i++){//the first line
                    Integer vertexName=Integer.parseInt(parts[i]);
                    Vertex vertex = new Vertex(vertexName);
                    Graph.putIfAbsent(vertexName,vertex);
                }
                else{
                    parts[0]=parts[0].replaceAll("[^0-9]","");
                    parts[1]=parts[1].replaceAll("[^0-9]+","");
                    int vertex1Name=Integer.parseInt(parts[0]);
                    int vertex2Name=Integer.parseInt(parts[1]);
                    Graph.get(vertex1Name).addAdjacent(Graph.get(vertex2Name));
                    Graph.get(vertex2Name).addAdjacent(Graph.get(vertex1Name));
                    Set< Integer > entry =  new HashSet< Integer >();//put the two end point for the edge
                    entry.add(vertex1Name);//the first part
                    entry.add(vertex2Name);// the last part
                    GraphEdges.add(entry);// add this edge to the set of edges for the graph
                }
                //split rest lines by , and insert into neighbors HashMap <->
            }
        } 
        catch (IOException e) {
        } 
        int [][] adjMatrix=new int[Graph.size()+1][Graph.size()+1];// +1 for the name of the vertex
        long startTime = System.nanoTime();
        adjMatrix=adjMatrix();//get the 2D array from the method
        for(int i=0;i<=Graph.size();i++){
            for(int j=0;j<=Graph.size();j++){
                System.out.print(adjMatrix[i][j]+"\t");//to print the element 
            }
        System.out.println();    
        }
        calculateDiameterAndRadius(); 
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("\n --------------- \n \n The Total Time : "+totalTime+"\n");
        
        
        
        GraphDraw frame = new GraphDraw("Test Window");
        frame.setSize(2000,1000);
        frame.setVisible(true);
        ArrayList <Integer> visited=new ArrayList<Integer>();
        int position=100;
        for(Integer ekey:Graph.keySet()){//key is Integer
            if(!visited.contains(ekey)){
                frame.addNode(ekey.toString(),position,(int)(100 + Math.random() * 301));
                visited.add(ekey);
                //System.out.println(frame.getindex(ekey));
                position+=120;
            }
            for(Object ikey : Graph.get(ekey).getAdjacentsMap().keySet()){//Integer
               if(!visited.contains((int)ikey)){
                   Integer ikeyint=(int)ikey;
                    frame.addNode(ikeyint.toString(),position,(int)(100 + Math.random() * 301));
                    visited.add(ikeyint);
                    position+=100;
                }
                frame.addEdge(frame.getindex(ekey),frame.getindex((int)ikey));
            }
           
        }
        
        
       
    }
    public static int[][] adjMatrix(){
        int [][] adjMatrix = new int[Graph.size()+1][Graph.size()+1];// +1 for the name of the vertex
       int i=1;
       for(Integer key:Graph.keySet()){// add the name vertex in the first row & first coll
           if(i<=Graph.size()){
               int IntKey=key.intValue();
                adjMatrix[0][i]=IntKey;
                adjMatrix[i][0]=IntKey;
           }
           i++;
        }
        for(i=1;i<=Graph.size();i++){ // set all value in the matrix 
           for(int j=1;j<i;j++){
               if( Graph.get(adjMatrix[0][i]).getAdjacentsMap().containsKey(adjMatrix[j][0])){//if the two vertex are neighbors
                   adjMatrix[i][j]=adjMatrix[j][i]=1;   // because its indirect graph
                }
               else{
                    adjMatrix[i][j]=adjMatrix[j][i]=0;//if the two vertex are not neighbors
               }
            }
        }
        return adjMatrix;//returen the array to the main to print it
    }
    public static void calculateDiameterAndRadius(){
        calculateDistances();
        int Diameter=0;
        int Radius=9999999;
        for(Object key:Graph.keySet()){
            if(Radius==1&&Diameter==9999999){//if any eccentricity =1 one so it's the radius & any eccentricity =9999999 (infinity) its the Diameter
            break;
            }
            if(Graph.get(key).calculateEccentricity()>Diameter){
               Diameter= Graph.get(key).calculateEccentricity();//get the largest value for the Diameter
            }
            if(Graph.get(key).calculateEccentricity()<Radius){
               Radius= Graph.get(key).calculateEccentricity();//get the lowest value for the Radius
            }
        }
        CyclesEdges.addAll(GraphEdges);
        System.out.println(" The Diameter : "+Diameter);
        System.out.println(" The Radius : "+Radius);
        if(Diameter==9999999){//we have vertex its distance=infinity
            System.out.println(" The Graph is Disconnected !! ");
            FindeCycles();
            GirtAndCircom();
            System.out.println(" The Graph is Disconnected So There's No bridges :");
        }
        else{
            System.out.println(" The Graph is Connected ");
            FindeCycles();
            GirtAndCircom();
            FindBridges();
        }
    }
    public static void calculateDistances(){
        for(Object key:Graph.keySet()){// get vertex from the graph
           for(Object key1:Graph.keySet()){// set all vertex in the graph in its hashMap (distances)
               if(((int)key)==((int)key1)){// the distance between vertex and itself equals zero
                   Graph.get(key).getDistances().put(Graph.get(key1),0);
               }
               else{//at the first time every distance equals infinty
                   Graph.get(key).getDistances().put(Graph.get(key1), 9999999);
               }
           }  
       }
       for(Object key:Graph.keySet()){// get vertex from the graph
           calculateDistance(Graph.get(key));//calculate the distances between the vertex and the other vertices in the graph 
       }
    }
    public static void calculateDistance(Vertex current){
        ArrayList<Integer> Visited=new ArrayList<Integer>();//to Calculate the distance only once
        Visited.add(current.getName());
        Vertex Vkey=new Vertex();
        /*
        Closest node to me its my neighbor then the neighbors of my neighbors 
        because it's unweighted graph so we dont have link's cost 
        Based on that we should not visit all links we know the least cost is the less number of hops
        */
        for(int i=0;i<Graph.size();i++){//the worst case if the graph as a line or its diconnected so the size of the graph the largest possible value
                for(Object key:current.getDistances().keySet()){//vertex
                    Vkey=(Vertex)key;
                if((int)(current.getDistances().get(key))==i){// when the distance = i
                    for(Object key1:Vkey.getAdjacentsMap().keySet()){//Integer
                         Vertex Vkey1 = Graph.get(key1);
                        if(!(Visited.contains(Vkey1.getName()))){
                           current.setDistances(Vkey1,i+1);
                           Visited.add(Vkey1.getName());
                        }
                    }
                }
                if(Visited.size()==Graph.size()){//we visited all vertices and we have a distance to all
                    break;
                }
            }
        }
    }
    public static ArrayList<Integer> MakeTree(){//in this method we use the DFS algorithm for traversing graph and make it as tree
        ArrayList<Integer> Visited=new ArrayList<Integer>();//to travel to all vertices
        Stack st = new Stack();
        ArrayList<Integer> Pathes=new ArrayList<Integer>();
        boolean Done=true;
        for (Integer key:Graph.keySet() ){//add all vertices in the graph to the fake graph
            Vertex vertex = new Vertex(key);
            FakeGraph.put(key, vertex);
        }
        Object myKey = Graph.keySet().toArray()[0];//get the first element from the graph
        st.push((Integer)myKey);
        while (!st.empty()){//we have vertex dosent travel
            if(!Visited.contains(st.peek())){
               Visited.add((Integer) st.peek());
            }   
               Pathes.add((Integer) st.peek());
            for (Object key:((Graph.get(st.peek())).getAdjacentsMap()).keySet() ){//Integer
                if(!Visited.contains((Integer)key)){
                    Done=false;
                    FakeGraph.get((Integer)key).addAdjacent(FakeGraph.get((Integer)st.peek()));//key and st.peek() neighbors
                    FakeGraph.get((Integer)st.peek()).addAdjacent(FakeGraph.get((Integer)key));//st.peek() and key neighbors
                    Set< Integer > entry =  new HashSet< Integer >();//put the two end point for the edge
                    entry.add((Integer)key);//the first part
                    entry.add((Integer)st.peek());// the last part
                    FakeGraphEdges.add(entry);// add this edge to the set of edges for the graph
                    st.add(key);
                    break;
                }
            } 
            if(Done){
               st.pop();//all its neighbors travel
            }
            Done=true; 
        }
        return Pathes;
    }
    public static void FindeCycles(){
        Pathes=MakeTree();//make the graph as tree
         //System.out.println("The set of edges in the Tree : "+FakeGraphEdges);
        CyclesEdges.removeAll(FakeGraphEdges);//get the edges that make the cycles (edges in the Graph but not in the FakeGraph)
        for (Set s : CyclesEdges) {//get the element(set of vertices the edge)
            Iterator iter = s.iterator();
            int V1=(int) iter.next();//the first part of the edge
            int V2=0;
            while(iter.hasNext()) {
                V2=(int) iter.next();// the last part of the edge
            }
             FindeCycle(V1,V2);
        }
        for(int i=0;i<Cycles.size();i++) {
            ArrayList< Set< Integer > > prev = Cycles.get(i);//
            for(int j=i+1;(j<Cycles.size());j++){
                ArrayList< Set< Integer > > current=Cycles.get(j);//
                ArrayList< Set< Integer > > NewCycle=null;
                if((current!=null)&&(prev!=null)){
                 NewCycle=XOR(current,prev);//
                }
                boolean flag = false;
                if(NewCycle!=null){
                    flag = false;//for the same cycles
                for(int k=0;k<Cycles.size();k++){//if two cycles have the same edges
                    int count=0;
                    ArrayList< Set< Integer > > Test=Cycles.get(k);
                    if(Test!=null){
                        for(int c=0;c<Test.size();c++){
                            if(NewCycle.contains(Test.get(c))){
                                count++;
                                //System.out.println(count);
                            }
                        }
                    }
                    if(count==Test.size()){
                        
                        flag=true;
                        break;
                    }
                }
                    if((!flag)){
                        Cycles.add(NewCycle);
                    }
                }
            }
            
            
            
            
        }
    }
    public static void FindeCycle(int V1,int V2){
        /* get the edge from the complement set of edges 
        and finde the path between the two vertices in the edge from the tree*/
        int IndexV1=0;
        int IndexV2=0;
        if(Pathes.indexOf(V1)<Pathes.indexOf(V2)){
            IndexV1=Pathes.indexOf(V1);
            IndexV2=Pathes.indexOf(V2);  
        }
        else{
            IndexV1=Pathes.indexOf(V2);
            IndexV2=Pathes.indexOf(V1);   
        }
        ArrayList<Integer> Path=new ArrayList();
        for(int i=IndexV1;i<IndexV2+1;i++){
            if(!Path.contains(Pathes.get(i))){
                Path.add(Pathes.get(i));
            }
            else{
                for(int j=Path.indexOf(Pathes.get(i))+1;j<Path.size();j++){
                    Path.remove(j);
                }
            }
        }
        
        Path.add(Pathes.get(IndexV1));//add the first part of the edge to the path to make a cycle
        ArrayList< Set< Integer > > Cycle=new ArrayList< Set< Integer > >();
        for(int i = 0,j = 1;j<Path.size();i++,j++){
            Set< Integer > entry =  new HashSet< Integer >();
            entry.add(Path.get(i));
            entry.add(Path.get(j));
            Cycle.add(entry);    
        }
        Cycles.add(Cycle);
        
    }
    public static ArrayList<Set<Integer>> XOR(ArrayList< Set< Integer > > current,ArrayList< Set< Integer > > prev ){
        ArrayList< Set< Integer > > NewCycle=new ArrayList< Set< Integer > >();
        boolean flag=false;//contains one edge smaoe at lest
        boolean flag1=false;//all element=zero
        boolean flag2=false;//Is it real cycle
        //boolean flag3=false;//if the same cycle
        for(int i=0;i<current.size();i++){
            if(prev.contains(current.get(i))){ 
                flag=true; 
            }
            
            if(!((prev.contains(current.get(i)))||(NewCycle.contains(current.get(i))))){
               NewCycle.add(current.get(i));
               flag1=true;
            }   
        }
        for(int i=0;i<prev.size();i++){
            if(!((current.contains(prev.get(i)))||(NewCycle.contains(prev.get(i))))){
               NewCycle.add(prev.get(i));
               flag1=true;
            }
        }
        if(!(flag&&flag1)){
            NewCycle=null;
        }
        if(flag&&flag1){
           int count1=0;
           int count2=0;
            for (Set e : NewCycle){
                count1=0;
                count2=0;
                //Set  < Integer > edge=new HashSet();
                Iterator iterator = e.iterator();
                int e1=(int) iterator.next();//the first part of the edge
                int e2=0;
                while(iterator.hasNext()) {
                    e2=(int) iterator.next();// the last part of the edge
                }
                for (Set s : NewCycle) {
                    Iterator iter = s.iterator();
                    int V1=(int) iter.next();//the first part of the edge
                    int V2=0;
                    while(iter.hasNext()) {
                        V2=(int) iter.next();// the last part of the edge
                    }
                    if((e1==V1)||(e1==V2)){
                        count1++;
                    }
                    if((e2==V1)||(e2==V2)){
                        count2++;
                    }
                } 
                if((count1>2)||(count2>2)){
                    flag2=true;
                }
            }
            if(flag2){
                NewCycle=null;
            }
        }
        
       return NewCycle;
    }
    public static void GirtAndCircom(){
        int cir=3;
        int gir=999999;
        //int count=0;
        boolean flag=false;//if we dont have a cycle
        for(int j=0;j<Cycles.size();j++) { 
            ArrayList< Set< Integer > > viseted=new ArrayList< Set< Integer > >();
                if((Cycles.get(j).size()>cir)&&(Cycles.get(j).size()>2)){
                    flag=true;
                    cir=Cycles.get(j).size();
                }
                if((Cycles.get(j).size()<gir)&&(Cycles.get(j).size()>2)){
                    flag=true;
                    gir=Cycles.get(j).size();
            }
        }
        if(flag){
            System.out.println(" Circumference : "+cir);
            System.out.println(" Girth : "+gir);
        }
        else{
            System.out.println(" There is No Cycles So the Circumference : infinity And The Grith : Zero");
        }
        
       
    }
    public static void FindBridges(){
        /* if the edge dosent follow to any Cycle it is a Bridge
        */
        
        ArrayList< Set< Integer > > Visited = new ArrayList< Set< Integer > >();
         ArrayList< Set< Integer > > Bridges = new ArrayList< Set< Integer > >();
        Bridges.addAll(GraphEdges);
        for(int j=0;j<CyclesEdges.size();j++) { 
            for(int i=0;i<Cycles.get(j).size();i++){
                
                if(!Visited.contains(Cycles.get(j).get(i))){
                    Visited.add(Cycles.get(j).get(i));
                }
                
            }
            if(Visited.size()==GraphEdges.size()){
                System.out.println(" There is no bridges.");
                return;
            }
            
        }
        
        Bridges.removeAll(Visited);
                System.out.println(" The bridge/s :"+Bridges);
    }           
    
}

 class GraphDraw extends JFrame {
    int width;
    int height;

    public ArrayList<Node> nodes;
    public ArrayList<edge> edges;
    
    public int getindex(Integer index){
        String in=index.toString();
        for(int i=0;i<nodes.size();i++){
            if((nodes.get(i).name).equals(in)){
                return i;
            }
        }
        return 0;
    }
    
    public GraphDraw() { //Constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	nodes = new ArrayList<Node>();
	edges = new ArrayList<edge>();
	width = 30;
	height = 30;
    }

    public GraphDraw(String name) { //Construct with label
	this.setTitle(name);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	nodes = new ArrayList<Node>();
	edges = new ArrayList<edge>();
	width = 30;
	height = 30;
    }

    class Node {
	int x, y;
	String name;
	
	public Node(String myName, int myX, int myY) {
	    x = myX;
	    y = myY;
	    name = myName;
	}
    }
    
    class edge {
	int i,j;
	
	public edge(int ii, int jj) {
	    i = ii;
	    j = jj;	    
	}
    }
    
    public void addNode(String name, int x, int y) { 
	//add a node at pixel (x,y)
	nodes.add(new Node(name,x,y));
	this.repaint();
    }
    public void addEdge(int i, int j) {
	//add an edge between nodes i and j
	edges.add(new edge(i,j));
	this.repaint();
    }
    
    public void paint(Graphics g) { // draw the nodes and edges
	FontMetrics f = g.getFontMetrics();
	int nodeHeight = Math.max(height, f.getHeight());

	g.setColor(Color.black);
	for (edge e : edges) {
	    g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
		     nodes.get(e.j).x, nodes.get(e.j).y);
	}

	for (Node n : nodes) {
	    int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
	    g.setColor(Color.white);
	    g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
		       nodeWidth, nodeHeight);
	    g.setColor(Color.black);
	    g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
		       nodeWidth, nodeHeight);
	    
	    g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
			 n.y+f.getHeight()/2);
	}
    }
}


