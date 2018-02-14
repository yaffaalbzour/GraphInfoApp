/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphinfoapp;
import java.util.HashMap;
/**
 *
 * @author yaffa
 */
public class Vertex {
    private int name;
    private HashMap<Vertex, Integer> distances=new HashMap();
    
    public Vertex(){}
    
    public Vertex (int name){
        setName(name); 
    }
    
    public void setName(int name){
        this.name=name;  
    }
    
    public void setDistance(Vertex n, int cost){  
        this.distances.put(n, cost);
    }
    public int getName(){ 
        return this.name;  
    }
    
    public HashMap getDistances(){
        return this.distances;
        
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