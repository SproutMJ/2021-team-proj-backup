package datastructure;

import java.io.Serializable;

public class Tuple implements Serializable{

	private static final long serialVersionUID = -8864117869986718902L;
	/**
	 * 
	 */
	public byte offset;
	public byte length;
	public byte binary;
	
	public Tuple(byte offset, byte length, byte binary) {
		this.offset = offset;
		this.length = length;
		this.binary = binary;
	}
}
