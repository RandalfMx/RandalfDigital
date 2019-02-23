/**
 * 
 */
package mx.randalf.digital.img.convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;
import org.im4java.process.ProcessStarter;

/**
 * @author massi
 *
 */
public class ConvertImg {

	private Logger log = Logger.getLogger(ConvertImg.class);

	private String pathImageMagick = null;

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public ConvertImg(String pathImageMagick) throws FileNotFoundException // throws IOException
	{
		File fConvert = null;

		try {
			fConvert = new File(pathImageMagick + File.separator + "convert");
			if (!fConvert.exists()) {
				throw new FileNotFoundException("Impossibile trovare il file " + fConvert.getAbsolutePath());
			}
			this.pathImageMagick = pathImageMagick;
			ProcessStarter.setGlobalSearchPath(pathImageMagick);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

	}

	public void resize(File input, File output, int ppi)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, 0, 0);
	}

	public void resize(File input, File output, int ppi, Integer resize)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, 0, 0, resize);
	}

	public void resize(File input, File output, int ppi, Double quality)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, 0, 0, quality);
	}

	public void resize(File input, File output, int ppi, Double quality, Integer resize)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, 0, 0, quality, resize);
	}

	public void resize(File input, File output, int ppi, int width, int height)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, width, height, new Double("60"));
	}

	public void resize(File input, File output, int ppi, int width, int height, Integer resize)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, width, height, new Double("60"), resize);
	}

	public void resize(File input, File output, int ppi, int width, int height, Double quality)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, width, height, quality, null);
	}

	public void resize(File input, File output, int ppi, int width, int height, Double quality, Integer resize)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		ConvertCmd cc = null;
		IMOperation imo = null;
		Info info = null;
		int resolution = 0;
		File fTmp = null;
		Integer iWidth = null;
		Integer iHeigth = null;

		try {
			if (input.exists()) {
				if (!output.getParentFile().exists()) {
					if (!output.getParentFile().mkdirs()) {
						throw new IOException("Riscontrato un problema nella creazione della cartella ["
								+ output.getParentFile().getAbsolutePath() + "]");
					}
				}

				info = new Info(input.getAbsolutePath());
				if (info.getProperty("Resolution") != null) {
					resolution = new Integer(info.getProperty("Resolution").split("x")[0]);
				}

				cc = new ConvertCmd();
				cc.setSearchPath(pathImageMagick);

				imo = new IMOperation();
				imo.addImage(input.getAbsolutePath());
				imo.units("PixelsPerInch");
				if (!output.getName().toLowerCase().endsWith(".tif")) {
					imo.quality(quality);
				}
				if (ppi != resolution) {
					imo.density(ppi);
					imo.resample(ppi, ppi);
					if (resolution > ppi) {
						imo.resize(info.getImageWidth() / (resolution / ppi),
								info.getImageHeight() / (resolution / ppi));
					} else {
						imo.resize(info.getImageWidth() * (ppi / resolution),
								info.getImageHeight() * (ppi / resolution));

					}
				}
				if (width > 0) {
					iWidth = new Integer(width);
				}
				if (height > 0) {
					iHeigth = new Integer(height);
				}
				if (iWidth != null || iHeigth != null) {
					imo.resize(iWidth, iHeigth);
				} else if (resize != null) {
					imo.resize(resize);
				}
				if (!output.getName().toLowerCase().endsWith(".tif")) {
					fTmp = new File(output.getParentFile().getAbsolutePath() + File.separator + "TMP.jpg");
					imo.addImage(fTmp.getAbsolutePath());
					if (cc.isAsyncMode()) {
						log.debug("\n" + "Async");
					}
					cc.run(imo);
					convertSrgb(fTmp, output);
				} else {
					imo.addImage(output.getAbsolutePath());
					cc.run(imo);
				}
			} else {
				throw new FileNotFoundException("Il file [" + input.getAbsolutePath() + "] non esiste");
			}
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (IM4JavaException e) {
			throw e;
		} finally {
			if (fTmp != null && fTmp.exists()) {
				if (!fTmp.delete()) {
					throw new IOException(
							"Riscontrato un problema nella cancellazione del file [" + fTmp.getAbsolutePath() + "]");
				}
			}
		}
	}

	private void convertSrgb(File input, File output) throws IOException, InterruptedException, IM4JavaException {
		ConvertCmd cc = null;
		IMOperation imo = null;

		try {
			cc = new ConvertCmd();
			cc.setSearchPath(pathImageMagick);
			imo = new IMOperation();
			imo.addImage(input.getAbsolutePath());
			imo.profile("sRGB.icc");
			imo.colorspace("sRGB");
			imo.type("TrueColor");
			imo.units("PixelsPerInch");
			imo.quality(new Double("60"));
			imo.addImage(output.getAbsolutePath());
			cc.run(imo);
		} catch (IOException e) {
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (IM4JavaException e) {
			throw e;
		}
	}

	@SuppressWarnings("unused")
	public static String setStpiecePer(int anno, int mese, int giorno, String annata, String fascicolo,
			String edizione) {
		String stpiecePer = "";
		String fasc = "";
		int iAnnata = 0;
		DecimalFormat df8 = new DecimalFormat("00000000");
		DecimalFormat df4 = new DecimalFormat("0000");
		DecimalFormat df2 = new DecimalFormat("00");

		if (anno > 0 || mese > 0 || giorno > 0) {
			stpiecePer = "(";
			if (anno > 0) {
				stpiecePer += df4.format(anno);
			}
			if (mese > 0) {
				stpiecePer += df2.format(mese);
			}
			if (giorno > 0) {
				stpiecePer += df2.format(giorno);
			}
			stpiecePer += ")";
		}
		if (annata != null && !annata.trim().equals("")) {
			try {
				iAnnata = new Integer(annata);
				stpiecePer += df2.format(iAnnata);
			} catch (NumberFormatException e) {
				stpiecePer += df2.format(Utility.converti(annata));
			}
		}else {
			stpiecePer += df2.format(0);
		}
		if (fascicolo != null && 
				!fascicolo.trim().equals("")) {
			  if (!stpiecePer.trim().equals("") && !stpiecePer.trim().endsWith(")")) {
				stpiecePer += ":";
			  }
			  fasc = df8.format(new Integer(fascicolo));
			  stpiecePer += fasc.substring(0, 4)+":";
			  stpiecePer += fasc.substring(4);
			  
		}
		if (edizione != null && !edizione.trim().equals("")) {
			if (!stpiecePer.trim().equals("") && !stpiecePer.trim().endsWith(")")) {
				stpiecePer += ":";
			}
			if (edizione.equals("mattino")) {
				stpiecePer += "1";
			} else if (edizione.equals("pomeriggio")) {
				stpiecePer += "2";
			} else if (edizione.equals("sera")) {
				stpiecePer += "3";
			} else if (edizione.equals("fascicolo")) {
				stpiecePer += "4";
			} else if (edizione.equals("straordinarie")) {
				stpiecePer += "1*";
			} else if (edizione.equals("supplementi")) {
				stpiecePer += "1+";
			} else {
			}
		}

		return stpiecePer;
	}

	public static String setStpiecePer(int anno, int mese, int giorno, String volume, String numero) {
		String stpiecePer = "";
		DecimalFormat df4 = new DecimalFormat("0000");
		DecimalFormat df2 = new DecimalFormat("00");

		if (anno > 0 || mese > 0 || giorno > 0) {
			stpiecePer = "(";
			if (anno > 0) {
				stpiecePer += df4.format(anno);
			}
			if (mese > 0) {
				stpiecePer += df2.format(mese);
			}
			if (giorno > 0) {
				stpiecePer += df2.format(giorno);
			}
			stpiecePer += ")";
		}
		if (volume != null && !volume.trim().equals("")) {
			stpiecePer += df2.format(Utility.converti(volume));
		}
		if (numero != null && !numero.trim().equals("")) {
			if (!stpiecePer.trim().equals("") && !stpiecePer.trim().endsWith(")")) {
				stpiecePer += ":";
			}
			String[] st = null;
			st = numero.split(" ");
			for (int x = 0; x < st.length; x++) {
				try {
					stpiecePer += df2.format(new Integer(st[x]));
				} catch (NumberFormatException e) {
					if (st[x].equals("[ed.") || st[x].equals("[I")) {
						stpiecePer += "*";
					} else if (st[x].equals("[2") || st[x].equals("[II")) {
						stpiecePer += ":02*";
					} else if (st[x].equals("[3")) {
						stpiecePer += ":03*";
					} else if (st[x].equals("str.]") || st[x].equals("ed.straordinaria]") || st[x].equals("stra.]")) {
					} else {
						stpiecePer += st[x];
					}
				}
			}
		}
		return stpiecePer;
	}

	public static String setStpiecePer(int anno, int mese, String giorno, String volume, String numero) {
		String stpiecePer = "";
		DecimalFormat df4 = new DecimalFormat("0000");
		DecimalFormat df2 = new DecimalFormat("00");
		boolean sup = false;
		boolean bis = false;
		String[] st = null;

		if (anno > 0 || mese > 0 || (giorno != null && !giorno.equals(""))) {
			stpiecePer = "(";
			if (anno > 0) {
				stpiecePer += df4.format(anno);
			}
			if (mese > 0) {
				stpiecePer += df2.format(mese);
			}
			if (giorno != null && !giorno.equals("")) {
				if (giorno.endsWith(" Sup")) {
					sup = true;
					giorno = giorno.replace(" Sup", "").trim();
				}
				if (giorno.endsWith(" Bis")) {
					bis = true;
					giorno = giorno.replace(" Bis", "").trim();
				}
				if (giorno.endsWith("bis")) {
					bis = true;
					giorno = giorno.replace("bis", "").trim();
				}
				giorno = giorno.replace("_", "-");
				st = giorno.split("-");
				for (int x = 0; x < st.length; x++) {
					stpiecePer += (x > 0 ? "/" : "");
					stpiecePer += df2.format(Integer.parseInt(st[x].trim()));
				}
			}
			stpiecePer += ")";
		}
		if (volume != null && !volume.trim().equals("")) {
			stpiecePer += df2.format(Utility.converti(volume));
		}
		if (numero != null && !numero.trim().equals("")) {
			if (!stpiecePer.trim().equals("") && !stpiecePer.trim().endsWith(")")) {
				stpiecePer += ":";
			}
			st = null;
			st = numero.split(" ");
			for (int x = 0; x < st.length; x++) {
				try {
					stpiecePer += df2.format(new Integer(st[x]));
				} catch (NumberFormatException e) {
					if (st[x].equals("[ed.") || st[x].equals("[I")) {
						stpiecePer += "*";
					} else if (st[x].equals("[2") || st[x].equals("[II")) {
						stpiecePer += ":02*";
					} else if (st[x].equals("[3")) {
						stpiecePer += ":03*";
					} else if (st[x].equals("str.]") || st[x].equals("ed.straordinaria]") || st[x].equals("stra.]")) {
					} else {
						stpiecePer += st[x].replace("-", "/");
					}
				}
			}
		}
		if (sup) {
			stpiecePer += "*";
		}
		if (bis) {
			stpiecePer += "+";
		}
		return stpiecePer;
	}
}