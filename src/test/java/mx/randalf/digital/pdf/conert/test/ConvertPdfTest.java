package mx.randalf.digital.pdf.conert.test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.GregorianCalendar;

import org.im4java.core.IM4JavaException;

import mx.randalf.digital.pdf.convert.ConvertPdf;

public class ConvertPdfTest {

	public ConvertPdfTest() {
	}

	public static void main(String[] args){
		ConvertPdf convert = null;
		File[] filesImg = null;
		File f = null;
		GregorianCalendar gcStart = null;
		GregorianCalendar gcStop = null;
		
		try {
			gcStart = new GregorianCalendar();
			convert = new ConvertPdf("/opt/local/bin");
			f = new File("/Volumes/DatiMac/Users/massi/tmp/bncf/Pal0004992/PAL0004992/Internet");
			filesImg = f.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File f) {
					if (f.isFile()){
						if (f.getName().startsWith(".")){
							return false;
						} else if (f.getName().toLowerCase().endsWith(".jpg")){
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			});
			convert.createPdf(new File("/Volumes/DatiMac/Users/massi/tmp/bncf/Pal0004992/PAL0004992/PAL0004992Inte.pdf"), filesImg);
			gcStop = new GregorianCalendar();
			System.out.println("Tempi milli sec: "+(gcStop.getTimeInMillis()-gcStart.getTimeInMillis()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
	}
}
