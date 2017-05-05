/**
 * 
 */
package mx.randalf.digital;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.im4java.core.IM4JavaException;

/**
 * @author massi
 *
 */
public class ConvertImg {

	private String pathImageMagick = null;

	private String pathInput = null;
	
	private String pathOutput = null;

	private Integer ppi = null;

	private String extOut = "jpg";

	private int width = 0;

	private int height = 0;

	private Double quality = new Double("60");

	/**
	 * 
	 */
	public ConvertImg() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConvertImg convertImg = null;
		
		try {
			convertImg = new ConvertImg();
			for (int x=0; x<args.length; x++){
				if (args[x].equals("-image")){
					convertImg.setPathImageMagick(args[x+1]);
					x++;
				} else if (args[x].equals("-pi")){
					convertImg.setPathInput(args[x+1]);
					x++;
				} else if (args[x].equals("-po")){
					convertImg.setPathOutput(args[x+1]);
					x++;
				} else if (args[x].equals("-ppi")){
					convertImg.setPpi(new Integer(args[x+1]));
					x++;
				} else if (args[x].equals("-extOut")){
					convertImg.setExtOut(args[x+1]);
					x++;
				} else if (args[x].equals("-width")){
					convertImg.setWidth(new Integer(args[x+1]));
					x++;
				} else if (args[x].equals("-height")){
					convertImg.setHeight(new Integer(args[x+1]));
					x++;
				} else if (args[x].equals("-quality")){
					convertImg.setQuality(new Double(args[x+1]));
					x++;
				} 
			}
			if (convertImg.isValid()){
				convertImg.esegui();
			} else {
				System.out.println("E' necessario indicare i seguenti paramenti");
				System.out.println("-image Indica il Path in cui si trova ImageMagick (Obbligatorio)");
				System.out.println("-pi Path da analizzare (Obbligatorio)");
				System.out.println("-po Path di destinazione, se non indicato sarà la stessa della Path da Analozzzare");
				System.out.println("-ppi PPI a cui ricampionare l'immagine (Obblicatorio)");
				System.out.println("-extOut Indica l'estensione del file di destinazione (Obbligatorio)");
				System.out.println("-width Larghezza del file di destinazione (Opzionale)");
				System.out.println("-height Altezza del file di destinazione (Opzionale)");
				System.out.println("-quality Qualità di compressione JPEG default 60 (Opzionale)");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
	}

	public boolean isValid(){
		boolean result = true;
		if (pathInput != null){
			if (pathOutput ==null){
				pathOutput = pathInput;
			}
		} else {
			result = false;
		}
		if (extOut ==null){
			result = false;
		}
		if (pathImageMagick== null){
			result = false;
		}
		if (ppi== null){
			result = false;
		}
		if (extOut== null){
			result = false;
		}
		return result;
	}
	/**
	 * @param pathImageMagick the pathImageMagick to set
	 */
	public void setPathImageMagick(String pathImageMagick) {
		this.pathImageMagick = pathImageMagick;
	}

	/**
	 * @param pathInput the pathInput to set
	 */
	public void setPathInput(String pathInput) {
		this.pathInput = pathInput;
	}

	/**
	 * @param pathOutput the pathOutput to set
	 */
	public void setPathOutput(String pathOutput) {
		this.pathOutput = pathOutput;
	}

	public void esegui() throws NumberFormatException, IOException, InterruptedException, IM4JavaException, FileNotFoundException{
		File f = null;
		try {
			f = new File(pathInput);
			if (f.exists()){
				if (f.isDirectory()){
					scanFolder(f);
				} else {
					convertFile(f);
				}
			} else {
				throw new FileNotFoundException("La cartella "+f.getAbsolutePath()+" non esiste");
			}
		} catch (NumberFormatException e) {
			throw e;
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
	
	private void scanFolder(File f) throws NumberFormatException, FileNotFoundException, IOException, InterruptedException, IM4JavaException{
		File[] fl = null;
		try {
			fl = f.listFiles(new convertImgFileFilter());
			for (int x=0; x<fl.length; x++){
				if (fl[x].isDirectory()){
					scanFolder(fl[x]);
				} else {
					convertFile(fl[x]);
				}
			}
		} catch (NumberFormatException e) {
			throw e;
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

	private void convertFile(File f) throws NumberFormatException, FileNotFoundException, IOException, InterruptedException, IM4JavaException{
		mx.randalf.digital.img.convert.ConvertImg convertImg = null;
		File fOutput = null;
		
		try {
			convertImg = new mx.randalf.digital.img.convert.ConvertImg(pathImageMagick);
			
			fOutput = new File(pathOutput+
					File.separator+
					f.getName().substring(0,f.getName().lastIndexOf("."))+"."+
					extOut);
			convertImg.resize(f, fOutput, ppi,  width,  height,  quality);
		} catch (NumberFormatException e) {
			throw e;
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

	/**
	 * @param ppi the ppi to set
	 */
	public void setPpi(Integer ppi) {
		this.ppi = ppi;
	}

	/**
	 * @param extOut the extOut to set
	 */
	public void setExtOut(String extOut) {
		this.extOut = extOut;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @param quality the quality to set
	 */
	public void setQuality(Double quality) {
		this.quality = quality;
	}
}

class convertImgFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		boolean result = false;
		
		if (!pathname.getName().startsWith(".")){
			if (pathname.isDirectory()){
				result=true;
			} else if (pathname.getName().toLowerCase().endsWith(".tif") ||
					pathname.getName().toLowerCase().endsWith(".jpg")){
				result = true;
			}
		}
	
		return result;
	}
	
}