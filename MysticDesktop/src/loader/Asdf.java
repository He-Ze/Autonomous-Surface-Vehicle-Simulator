package loader;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class Asdf {
	
	private ArrayList<Vector3f> rawVerts = new ArrayList<Vector3f>();
	private ArrayList<Vector2f> rawTextureST = new ArrayList<Vector2f>();
	private ArrayList<Vector3f> rawNormals = new ArrayList<Vector3f>();
	
	
	private ArrayList<LinkedList<vertexSTdata>> calculatedVerts;
	
	private LinkedList<vertexSTdata> finalVertData = new LinkedList<vertexSTdata>();
	private LinkedList<Integer> indiciesList = new LinkedList<Integer>();
	
	private void loadVertex(Vector3f vertex){
		rawVerts.add(vertex);
	}
	
	private void loadTextureST(Vector2f st){
		rawTextureST.add(st);
	}
	
	private void loadNormal(Vector3f normal){
		rawNormals.add(normal);
	}
	
	private void loadAllFaces(){
		this.calculatedVerts = new ArrayList<LinkedList<vertexSTdata>>();
		for(int i = 0; i < rawVerts.size(); i++){
			LinkedList<vertexSTdata> newVertList = new LinkedList<vertexSTdata>();
			this.calculatedVerts.add(newVertList);
		}
		/*
		for(int i = 0; i < rawVerts.size(); i++){
			vertexSTdata newLoadedVertex = new vertexSTdata(rawVerts.get(i), rawTextureST.get(i), rawNormals.get(i));
			this.calculatedVerts.get(i).add(newLoadedVertex);
		}
		*/
	}
	
	
	private void loadFace(int v1, int t1, int n1, int v2, int t2, int n2, int v3, int t3, int n3){
		loadVectorData(v1, t1, n1);
		loadVectorData(v2, t2, n2);
		loadVectorData(v3, t3, n3);
	}
	
	private void loadVectorData(int v1, int t1, int n1){
		vertexSTdata vertData1 = new vertexSTdata(rawVerts.get(v1), rawTextureST.get(t1), rawNormals.get(n1));
		LinkedList<vertexSTdata> vertsUsingV1 = calculatedVerts.get(v1);
		
		int indexPosition = -1;
		for(vertexSTdata vertxData : vertsUsingV1){
			if(vertData1.equals(vertxData)){
				indexPosition = vertData1.finalVertexListPosition;
				break;
			}
		}
		if(indexPosition != -1){
			
		} else {
			indexPosition = finalVertData.size();
			vertData1.setFinalVertexListPosition(indexPosition);
			calculatedVerts.get(v1).add(vertData1);
			finalVertData.add(vertData1);
		}
		indiciesList.add(indexPosition);
	}
	
	
	
	private class vertexSTdata {
		public Vector3f vertexPosition;
		public Vector2f vertexST;
		public Vector3f normalVector;
		
		public int finalVertexListPosition = -1;
		
		public vertexSTdata(Vector3f position, Vector2f st, Vector3f normal){
			this.vertexPosition = position;
			this.vertexST = st;
			this.normalVector = normal;
		}
		
		public void setFinalVertexListPosition(int newPosition){
			this.finalVertexListPosition = newPosition;
		}
		
		public boolean equals(vertexSTdata vertexData){
			boolean equal = false;
			equal &= vertexData.vertexPosition.equals(this.vertexPosition);
			equal &= vertexData.vertexST.equals(this.vertexST);
			equal &= vertexData.normalVector.equals(this.normalVector);
			return equal;
		}
	}
}
