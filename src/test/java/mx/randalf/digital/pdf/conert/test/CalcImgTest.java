/**
 * 
 */
package mx.randalf.digital.pdf.conert.test;

import java.io.File;

import org.im4java.core.InfoException;
import org.im4java.process.ProcessStarter;

import mx.randalf.digital.img.reader.CalcImg;

/**
 * @author massi
 *
 */
public class CalcImgTest {

	/**
	 * 
	 */
	public CalcImgTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		CalcImg calcImg = null;
		File f = null;
		
		try {
			if (args.length==2){
				f = new File(args[1]);
				if (f.exists()){
					ProcessStarter.setGlobalSearchPath(args[0]);
					calcImg = new CalcImg(f);
				} else {
					System.out.println("Il file ["+f.getAbsolutePath()+"] non esiste");
				}
			} else {
				System.out.println("E' necessario indicare i seguenti parametri:");
				System.out.println("1) path di installazione dell'ImageMagick");
				System.out.println("2) file da analizzare");
			}
		} catch (InfoException e) {
			e.printStackTrace();
		}
	}

}
