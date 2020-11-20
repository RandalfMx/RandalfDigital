/**
 * 
 */
package mx.randalf.digital.img.convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.im4java.core.CommandException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.InfoException;
import org.im4java.process.ProcessStarter;

import mx.randalf.digital.img.reader.CalcImg;

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
	public ConvertImg(String pathImageMagick) throws FileNotFoundException // throws
																			// IOException
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
		resize(input, output, ppi, width, height, Double.valueOf("60")); // new Double("60"));
	}

	public void resize(File input, File output, int ppi, int width, int height, Integer resize)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, width, height, Double.valueOf("60"), resize);
		// new Double("60")
	}

	public void resize(File input, File output, int ppi, int width, int height, Double quality)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		resize(input, output, ppi, width, height, quality, null);
	}

	public void resize(File input, File output, int ppi, int width, int height, Double quality, Integer resize)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		ConvertCmd cc = null;
		IMOperation imo = null;
		CalcImg calcImg = null;
//    Info info = null;
		double resolution = 0;
		double perc = 0;
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

				try {
					calcImg = new CalcImg(input);
//          info = new Info(input.getAbsolutePath());
				} catch (InfoException e) {
					System.out.println(e.getMessage());
				}
				if (calcImg.getDpi() != null) {
					resolution = calcImg.getDpi().doubleValue();
//            		  new Integer(
//        		  new Integer(
//              info.getProperty("Resolution").split("x")[0]);
				} else {
					resolution = 72;
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
					try {
						log.debug("getImageWidth: " + calcImg.getImageWidth());
					} catch (Exception e) {
					}
					try {
						log.debug("getImageHeight:" + calcImg.getImageLength());
					} catch (Exception e) {
					}
//          try {
//            log.debug("getPageHeight:" + calcImg.getPageHeight());
//          } catch (Exception e) {
//          }
//          try {
//            log.debug("getPageWidth:" + info.getPageWidth());
//          } catch (Exception e) {
//          }
					if (resolution > ppi) {
						perc = (resolution / ppi);
						imo.resize((int)(calcImg.getImageWidth().intValue() / perc),
								(int)(calcImg.getImageLength().intValue() / perc));
					} else {
						perc = (ppi / resolution);
						imo.resize((int)(calcImg.getImageWidth().intValue() * perc),
								(int)(calcImg.getImageLength().intValue() * perc));

					}
				}
				if (width > 0) {
					iWidth = Integer.valueOf(width); // new Integer(width);
				}
				if (height > 0) {
					iHeigth = Integer.valueOf(height); // new Integer(height);
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
					try {
						cc.run(imo);
					} catch (IM4JavaException e) {
						System.out.println(e.getMessage());
					}
					convertSrgb(fTmp, output, quality);
				} else {
					imo.addImage(output.getAbsolutePath());
					cc.run(imo);
				}
			} else {
				throw new FileNotFoundException("Il file [" + input.getAbsolutePath() + "] non esiste");
			}
		} catch (NumberFormatException e) {
			log.error("Errore: [" + input.getAbsolutePath() + "] " + e.getMessage());
			throw e;
		} catch (IOException e) {
			log.error("Errore: [" + input.getAbsolutePath() + "] " + e.getMessage());
			throw e;
		} catch (InterruptedException e) {
			log.error("Errore: [" + input.getAbsolutePath() + "] " + e.getMessage());
			throw e;
		} catch (IM4JavaException e) {
			log.error("Errore: [" + input.getAbsolutePath() + "] " + e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Errore: [" + input.getAbsolutePath() + "] " + e.getMessage());
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

	private void convertSrgb(File input, File output, Double quality) throws IOException, InterruptedException, IM4JavaException {
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
			if (quality==null || quality.intValue()==0) {
				imo.quality(Double.valueOf("60"));// new Double("60"));
			} else {
				imo.quality(quality);// new Double("60"));
			}
			imo.addImage(output.getAbsolutePath());
			cc.run(imo);
		} catch (IOException e) {
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (CommandException e) {
		} catch (IM4JavaException e) {
			throw e;
		}
	}

	public static String setStpiecePer(int anno, int mese, int giorno, String annata, String fascicolo,
			String edizione, Boolean ciancio) {
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
				iAnnata = Integer.valueOf(annata); // new Integer(annata);
				stpiecePer += df2.format(iAnnata);
			} catch (NumberFormatException e) {
				stpiecePer += df2.format(Utility.converti(annata));
			}
		} else {
			stpiecePer += df2.format(0);
		}
		if (fascicolo != null && !fascicolo.trim().equals("")) {
			if (!stpiecePer.trim().equals("") && !stpiecePer.trim().endsWith(")")) {
				stpiecePer += ":";
			}
			if (ciancio) {
				stpiecePer += df4.format(Integer.valueOf(fascicolo));
			} else {
				fasc = df8.format(Integer.valueOf(fascicolo)); // new Integer(fascicolo));
				stpiecePer += fasc.substring(0, 4) + ":";
				stpiecePer += fasc.substring(4);
			}
		}
		
		if (edizione != null && !edizione.trim().equals("")) {
			if (edizione.startsWith(":")) {
				stpiecePer += edizione;
			} else {
				stpiecePer += ":"+edizione;
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
					stpiecePer += df2.format(Integer.valueOf(st[x])); // new Integer(st[x]));
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
					stpiecePer += df2.format(Integer.valueOf(st[x])); // new Integer(st[x]));
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

	public void ruota(File input, File output, Double ruota)
			throws IOException, InterruptedException, IM4JavaException {
		ConvertCmd cc = null;
		IMOperation imo = null;

		try {
			cc = new ConvertCmd();
			cc.setSearchPath(pathImageMagick);
			imo = new IMOperation();
			imo.rotate(ruota);
			imo.addImage(input.getAbsolutePath());
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

	public void crop(File input, File output, int larghezza, int altezza, int x, int y)
			throws IOException, InterruptedException, IM4JavaException {
		ConvertCmd cc = null;
		IMOperation imo = null;

		try {
			cc = new ConvertCmd();
			cc.setSearchPath(pathImageMagick);
			imo = new IMOperation();
			imo.crop(larghezza, altezza, x, y);
			imo.addImage(input.getAbsolutePath());
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

	public void convertJp2(File input, File output)
			throws NumberFormatException, IOException, InterruptedException, IM4JavaException {
		ConvertCmd cc = null;
		IMOperation imo = null;

		try {
			if (!output.getParentFile().exists()) {
				if (!output.getParentFile().mkdirs()) {
					throw new IOException(
							"Problemi nella creazione della cartella " + output.getParentFile().getAbsolutePath());
				}
			}
			cc = new ConvertCmd();
			cc.setSearchPath(pathImageMagick);
			imo = new IMOperation();
			imo.addImage(input.getAbsolutePath());
			if (input.getName().toLowerCase().endsWith(".jpg")) {
				imo.quality(Double.valueOf("0")); // new Double("0"));
			} else {
				imo.define("jp2:rate=5");
			}
			imo.addImage(output.getAbsolutePath());
			cc.run(imo);
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (IM4JavaException e) {
			throw e;
		}

		// convert inputImg.jpg -define tiff:tile-geometry=128x128 -compress jpeg
		// ptif:outputImg.tif
	}
}