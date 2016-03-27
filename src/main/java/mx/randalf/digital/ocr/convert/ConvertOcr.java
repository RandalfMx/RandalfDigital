/**
 * 
 */
package mx.randalf.digital.ocr.convert;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * @author massi
 *
 */
public class ConvertOcr {

	private Tesseract api = null;

	/**
	 * 
	 */
	public ConvertOcr() {
        System.setProperty("jna.encoding", "UTF8");
		api = new Tesseract();
//		.getInstance();
		api.setLanguage("ita");
	}

	public void setHocr(boolean hocr){
		api.setHocr(hocr);
	}

	public void convertOcr(File tiff, File ocr) throws TesseractException, FileNotFoundException, IOException{
		String result = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
        
        try {
        	// TODO: prima di procedere alla conversione in OCR sarebbe consigliabile provare a convertire l'immagine in 1bit 
        	// TIF per vedere se miglioriamo la qualità e i tempi di lavorazione
        	
        	if (!ocr.getParentFile().exists()){
        		if (!ocr.getParentFile().mkdirs()){
        			throw new FileNotFoundException("Problemi nella creazione della cartella ["+ocr.getParentFile().getAbsolutePath()+"]");
        		}
        	}
        	fw = new FileWriter(ocr);
        	bw = new BufferedWriter(fw);
			result = api.doOCR(tiff);
			bw.write(result);
		} catch (TesseractException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}finally {
			if (bw != null){
				bw.flush();
				bw.close();
			}
			if (fw != null){
				fw.close();
			}
		}
	}

	public void setLanguage(String language) {
		api.setLanguage(language);
	}
}
