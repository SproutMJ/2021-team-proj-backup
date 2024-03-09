package filestructure;

import java.io.Serializable;
import java.util.List;

import datastructure.Tuple;

public class LZFile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6957513501759709994L;
	
	private List<Tuple> tupleList;
	private int originFileSize;
	private String originFileName;
	
	public LZFile(List<Tuple> tupleList, int originFileSize, String originFileName) {
		this.tupleList = tupleList;
		this.originFileSize = originFileSize;
		this.originFileName = originFileName;
	}
	
	public List<Tuple> getTupleList() {
		return tupleList;
	}

	public int getOriginFileSize() {
		return originFileSize;
	}
	
	public String getOriginFileName() {
		return originFileName;
	}
}
