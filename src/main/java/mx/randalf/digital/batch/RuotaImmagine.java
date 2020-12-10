package mx.randalf.digital.batch;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.im4java.core.IM4JavaException;

import mx.randalf.digital.img.convert.ConvertImg;

public class RuotaImmagine {

	public RuotaImmagine() {
	}

	public static void main(String[] args) {
		RuotaImmagine ruotaImmagine = null;
		try {
			if (args.length==4) {
				ruotaImmagine = new RuotaImmagine();
				ruotaImmagine.esegui(args[0], new File(args[1]),new File(args[2]),Double.valueOf(args[3]));
			} else {
				System.out.println("Indicare:");
				System.out.println("1) Path ImageMagick");
				System.out.println("2) Path di orgine");
				System.out.println("3) Path di destinazione");
				System.out.println("4) Gradi 90, 180, -90");
			}
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

	public void esegui(String pathImageMagick, File pathInput, File pathOutput, Double ruota) throws IOException, InterruptedException, IM4JavaException {
		ConvertImg convertImg = null;
		File[] fl = null;
		File fOutput = null;
		
		try {
			if (pathInput.exists()) {
				if (!pathOutput.exists()) {
					if (!pathOutput.mkdirs()) {
						throw new IOException("Problemi nella creazione della cartella ["+pathOutput.getAbsolutePath()+"]");
					}
				}
				convertImg = new ConvertImg(pathImageMagick);

				fl = pathInput.listFiles(new FileFilter() {
					
					@Override
					public boolean accept(File pathname) {
						boolean result = false;
						if (!pathname.getName().startsWith(".")) {
							if (pathname.isFile()) {
								if (pathname.getName().toLowerCase().endsWith(".tif") ||
										pathname.getName().toLowerCase().endsWith(".jpg")) {
									result = true;
								}
							}
						}
						return result;
					}
				});
				Arrays.sort(fl);
				
				for (File fInput: fl) {
					fOutput = new File(pathOutput.getAbsolutePath()+File.separator+fInput.getName());
					convertImg.ruota(fInput, fOutput, ruota);
				}
			} else {
				System.out.println("La cartella ["+pathInput+"] non esiste");
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (IM4JavaException e) {
			throw e;
		}
	}

}
