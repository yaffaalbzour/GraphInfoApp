package graphinfoapp;
import java.util.HashMap;
public class Vertex {
    private int name;
    private HashMap<Integer,Vertex> adjacentsMap=new HashMap();
    private HashMap<Vertex, Integer> distances=new HashMap();
    
    public Vertex(){}
    
    public Vertex (int name){
        setName(name); 
    }
    
    public void setName(int name){
        this.name=name;  
    }
    
    public int getName(){ 
        return this.name;  
    }
    public void addAdjacent(Vertex adjacent){
        adjacentsMap.put(adjacent.getName(),adjacent );
    }
    public HashMap getAdjacentsMap(){return adjacentsMap;}
    
    public String getAdjacentsNames(){
        String out=" ";
            for(Object key:adjacentsMap.keySet()){//for all node in the graph
         out+=key+" ";
       
        }
        return out;
    }
    public Vertex getAdjacent(int key){
        return adjacentsMap.get(key);
    }
    
    public void setDistances(Vertex n, int cost){  
        distances.put(n, cost);
    }

    public HashMap getDistances(){
        return distances;
    }
    
    public int calculateEccentricity(){
        int ecc=0;
        Vertex currentVertex = null;
        for(Object key:distances.keySet()){//find the eccentricity for this Vertex 
           currentVertex=(Vertex) key;
            if(distances.get(key)>ecc){
               ecc=distances.get(key) ;  
            }
        }
        return ecc;
    }
}