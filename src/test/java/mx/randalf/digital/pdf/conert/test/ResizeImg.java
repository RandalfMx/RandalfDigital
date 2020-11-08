/**
 * 
 */
package mx.randalf.digital.pdf.conert.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.im4java.core.IM4JavaException;

import mx.randalf.digital.img.convert.ConvertImg;

/**
 * @author massi
 *
 */
public class ResizeImg {

	/**
	 * 
	 */
	public ResizeImg() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConvertImg convertImg = null;
		File input = null;
		File output = null;
		
		try {
			convertImg = new ConvertImg("/opt/bin");
			input = new File("/Users/massi/temp/StronzateCiancio/PErCiancio/CFI0375187_19460510_123/TIFF/PR0126_Per_3_loc_1946_05_10_0001.tif");
			output = new File("/Users/massi/temp/StronzateCiancio/PErCiancio/CFI0375187_19460510_123/JPEG150/PR0126_Per_3_loc_1946_05_10_0001.jpg");
			convertImg.resize(input, output, 150,Double.parseDouble("85"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
