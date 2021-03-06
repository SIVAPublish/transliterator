package org.dattapeetham.transliteration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;


public class FileTransliterator {

	// Charset and encoder, decoder for UTF-16
	private static Charset charset = Charset.forName("UTF-16");
	private static CharsetDecoder decoder = charset.newDecoder();
	private static CharsetEncoder encoder = charset.newEncoder();
    /**
     * 
     * @param inputfile
     * @param targetLanguage
     */
	public static void transliterateFile(String inputfile, String targetLanguage) {
		
		transliterateFile(inputfile, inputfile + ".samskrutam.txt" , targetLanguage);
	}


	/**
	 * 	
	 * @param inputFileName
	 * @param outputFileName
	 * @param targetLanguage
	 */
	public static void transliterateFile(String inputFileName, String outputFileName, String targetLanguage) {
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		transliterateFile(targetLanguage, inputFile, outputFile);		
	}


	/**
	 * 
	 * @param targetLanguage
	 * @param inputFile
	 * @param outputFile
	 */
	public static void transliterateFile(String targetLanguage,
			File inputFile, File outputFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(inputFile);
			FileChannel fc = fis.getChannel();

			// Get the file's size and then map it into memory
			int sz = (int)fc.size();

			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

			CharBuffer cb = decoder.decode(bb);
			String inputText = cb.toString(); //TODO change to read bunch of chars at a time instead of whole file as a string
			CharBuffer cbOut = CharBuffer.wrap(ICUHelper.transliterate(inputText, targetLanguage));

			ByteBuffer bbOut = encoder.encode(cbOut);

			fos = new FileOutputStream(outputFile);
			FileChannel fcOut = fos.getChannel();
			fcOut.write(bbOut);

			fos.close();
			fis.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}finally {
			
				closeFile(fis);
			
		}
	}


	private static void closeFile(FileInputStream fis) {
		if (fis == null) return;
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		transliterateFile("G:\\Documents\\Projects\\Transliteration\\sandhyavandanamtel.txt", "sandhyasamskrutam.txt", ICUHelper.DEVANAGARI);
	}
}









