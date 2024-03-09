package service;

import java.io.File;

import javax.swing.JProgressBar;

public interface CompressionService {
	public void deCompressFile(File compressedFile, String decompressedFilePath, JProgressBar progressBar);
	public void compressFile(File saveFile, File originFile, JProgressBar progressBar);
	public void lz77Compress(File saveFile, File originFile, JProgressBar progressBar);
	public void deCompressLZFile(File compressedFile, String decompressedfilePath, JProgressBar progressBar);
}
