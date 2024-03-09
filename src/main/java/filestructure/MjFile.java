package filestructure;

import java.io.Serializable;

public class MjFile implements Serializable{

	//MjFile의 meta정보를 가지고있는 header
	private MjFileHeader header;
	//MjFile의 contents 정보를 가지고 있는 body
	private MjFileBody body;
	
	public MjFile(MjFileHeader header, MjFileBody body) {
		this.header = header;
		this.body = body;
	}
	
	public MjFileHeader getHeader() {
		return header;
	}

	public MjFileBody getBody() {
		return body;
	}
}
