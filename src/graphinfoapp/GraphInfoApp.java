/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphinfoapp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
//Reading file libraries
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author yaffa
 */
public class GraphInfoApp {
    public static HashMap<Integer,Vertex> Graph = new HashMap();//to store the graph the LinkedList its the Vertices neighbors
    public static int size = Graph.size();
    //Graph file path
    private static final String FILENAME = "/Users/yaffa/Desktop/Graph.txt";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String sCurrentLine;
            while((sCurrentLine = br.readLine()) != null) {
                //split by , and insert first line elements into HashMap as keys
                String[] parts = sCurrentLine.split(",");
                if(parts[0].charAt(0)!='(')
                for(int i=0;i<parts.length;i++){
                    Integer vertexName=Integer.parseInt(parts[i]);
                    Vertex vertex = new Vertex(vertexName);
                    Graph.putIfAbsent(vertexName,vertex);
                }
                else{
                parts[0]=parts[0].replaceAll("[^0-9]","");
                parts[1]=parts[1].replaceAll("[^0-9]+","");
                int vertex1Name=Integer.parseInt(parts[0]);
                int vertex2Name=Integer.parseInt(parts[1]);
                Vertex vertex1=new Vertex(vertex1Name);
                Vertex vertex2=new Vertex(vertex2Name);
                Graph.get(vertex1Name).addAdjacent(vertex2);
                Graph.get(vertex2Name).addAdjacent(vertex1);
                
                }
                //split rest lines by , and insert into neighbors HashMap <->
            }
        } catch (IOException e) {
            } 
        System.out.println("****"+Graph.get(4).getAdjacentsNames());

    }
    
   
}
    
    
