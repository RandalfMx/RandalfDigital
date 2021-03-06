package mx.randalf.digital.img.reader;

import java.io.File;
import java.math.BigInteger;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
//import org.niso.pdfs.datadict.CompressiontypeNiso;
//import org.niso.pdfs.datadict.Dimensions;
//import org.niso.pdfs.datadict.Format;
//import org.niso.pdfs.datadict.ImgMimetype;
//import org.niso.pdfs.datadict.Photometricinterpretationtype;
//import org.niso.pdfs.datadict.Spatialmetrics;

public class CalcImg {

	private static Logger log = Logger.getLogger(CalcImg.class);
	Info info = null;
	File fImg = null;

	/**
	 * Questa variabime viene utilizzata per indicare la larghezza originale
	 * dell'oggetto scannerizzato
	 */
	private Double larOggScan = null;

	/**
	 * Questa variabile viene utilizzata per indicare l'altezza originale
	 * dell'oggetto scannerizzato
	 */
	private Double altOggScan = null;

	/**
	 * Questa variabile viene utilizzata per indicare l'unità di misura del
	 * campionamento.<BR>
	 * Valori accettati:<BR>
	 * 1 (nessuna unità di misura definita)<BR>
	 * 2 (inch, pollice)<BR>
	 * 3 (centimetro)
	 */
	private BigInteger freqUnit = null;

	/**
	 * Questa variabile viene utilizzata per indicare il piano di campionamento.<BR>
	 * Valori accettati:<BR>
	 * 1 (camera/scanner focal plane): quando non sono definite le dimensioni
	 * dell'oggetto che si sta digitalizzando (per es. quando si riproduce con una
	 * fotocamera)<BR>
	 * 2 (object plane): quando l'oggetto e la riproduzione hanno la stessa
	 * dimensione (per es. quando si riproduce con uno scanner)<BR>
	 * 3 (source object plane): quando la dimensione della riproduzione è maggiore
	 * dell'oggetto origianale (per ese. quando si riproduce da un microfilm)
	 */
	private BigInteger freqPlan = null;

	public CalcImg(File fImg) throws InfoException {
		this(fImg, false);
	}

	public CalcImg(File fImg, boolean basic) throws InfoException {

		try {
			info = new Info(fImg.getAbsolutePath(), basic);
			this.fImg = fImg;
			this.freqUnit = new BigInteger("2");
			this.freqPlan = new BigInteger("2");
		} catch (InfoException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new InfoException(e.getMessage(), e);
		}
	}

	public CalcImg(File fImg, Double larOggScan, Double altOggScan) throws InfoException {
		this(fImg);
		this.larOggScan = larOggScan;
		this.altOggScan = altOggScan;
	}

	public CalcImg(File fImg, Double larOggScan, Double altOggScan, BigInteger freqUnit, BigInteger freqPlan)
			throws InfoException {
		this(fImg, larOggScan, altOggScan);
		this.freqUnit = freqUnit;
		this.freqPlan = freqPlan;
	}

	/**
	 * Questo metodo viene utilizzato per leggere il tipo di immagine da gestire
	 * 
	 * @return Nome del tipo di immagine
	 */
	public String getMimeName() {
		String ris = "";
		ris = getMagick().toLowerCase();
		return ris;
	}

	/**
	 * Questo metodo viene utilizzato per leggere la lunghezza della immagine
	 * 
	 * @return Lunghezza della immagine
	 * @throws InfoException
	 */
	public BigInteger getImageLength() throws InfoException {
		BigInteger ris = new BigInteger("0");
		String geometry = null;
		String[] st = null;
		try {
			ris = BigInteger.valueOf(info.getImageHeight());
		} catch (InfoException e) {
			try {
				ris = BigInteger.valueOf(info.getImageHeight(0));
			} catch (InfoException e1) {
				if (info.getProperty("Image:Properties:exif:PixelYDimension") != null) {
					ris = BigInteger.valueOf(Long.valueOf(info.getProperty("Image:Properties:exif:PixelYDimension")));
				} else if (info.getProperty("Image:Properties:exif:PixelYDimension", 0) != null) {
					ris = BigInteger
							.valueOf(Long.valueOf(info.getProperty("Image:Properties:exif:PixelYDimension", 0)));
				} else if (info.getProperty("Image:Geometry") != null) {
					geometry = info.getProperty("Image:Geometry");
					st = geometry.split("x");
					st = st[1].split("\\+");
					ris = BigInteger.valueOf(Long.valueOf(st[0]));
				} else if (info.getProperty("Image:Geometry", 0) != null) {
					geometry = info.getProperty("Image:Geometry", 0);
					st = geometry.split("x");
					st = st[1].split("\\+");
					ris = BigInteger.valueOf(Long.valueOf(st[0]));
				}
			}
		}
		return ris;
	}

	/**
	 * Questo metodo viene utilizzato per leggere la larghezza della immagine
	 * 
	 * @return Larghezza della immagine
	 * @throws InfoException
	 */
	public BigInteger getImageWidth() throws InfoException {
		BigInteger ris = new BigInteger("0");
		String geometry = null;
		String[] st = null;
		try {
			ris = BigInteger.valueOf(info.getImageWidth());
		} catch (InfoException e) {
			try {
				ris = BigInteger.valueOf(info.getImageWidth(0));
			} catch (InfoException e1) {
				if (info.getProperty("Image:Properties:exif:PixelXDimension") != null) {
					ris = BigInteger.valueOf(Long.valueOf(info.getProperty("Image:Properties:exif:PixelXDimension")));
				} else if (info.getProperty("Image:Properties:exif:PixelXDimension", 0) != null) {
					ris = BigInteger
							.valueOf(Long.valueOf(info.getProperty("Image:Properties:exif:PixelXDimension", 0)));
				} else if (info.getProperty("Image:Geometry") != null) {
					geometry = info.getProperty("Image:Geometry");
					st = geometry.split("x");
					ris = BigInteger.valueOf(Long.valueOf(st[0]));
				} else if (info.getProperty("Image:Geometry", 0) != null) {
					geometry = info.getProperty("Image:Geometry", 0);
					st = geometry.split("x");
					ris = BigInteger.valueOf(Long.valueOf(st[0]));
				}
			}
		}
		return ris;
	}

	public XMLGregorianCalendar getDateTimeCreate() throws DatatypeConfigurationException {
		XMLGregorianCalendar ris = null;
		GregorianCalendar gc = null;
		String[] st = null;
		String[] st2 = null;
		String[] st3 = null;
		if (info.getProperty("Properties:date:create") != null) {
			st = info.getProperty("Properties:date:create").split("T");
			st2 = st[0].split("-");
			st3 = st[1].split(":");
			gc = new GregorianCalendar(Integer.parseInt(st2[0]), Integer.parseInt(st2[1]) - 1, Integer.parseInt(st2[2]),
					Integer.parseInt(st3[0]), Integer.parseInt(st3[1]), Integer.parseInt(st3[2].substring(0, 2)));
			ris = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} else if (info.getProperty("Image:Properties:date:create") != null) {
			st = info.getProperty("Image:Properties:date:create").split("T");
			st2 = st[0].split("-");
			st3 = st[1].split(":");
			gc = new GregorianCalendar(Integer.parseInt(st2[0]), Integer.parseInt(st2[1]) - 1, Integer.parseInt(st2[2]),
					Integer.parseInt(st3[0]), Integer.parseInt(st3[1]), Integer.parseInt(st3[2].substring(0, 2)));
			ris = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

		}
		return ris;
	}

	public BigInteger getDpi() {
		BigInteger ris = null;
		String dpi = null;

		if (info.getProperty("Resolution") != null) {
			dpi = info.getProperty("Resolution").split("x")[0];
			if (dpi.indexOf(".") > -1) {
				dpi = dpi.substring(0, dpi.indexOf("."));
			}
			ris = new BigInteger(dpi);
		} else if (info.getProperty("Image:Resolution") != null) {
			dpi = info.getProperty("Image:Resolution").split("x")[0];
			if (dpi.indexOf(".") > -1) {
				dpi = dpi.substring(0, dpi.indexOf("."));
			}
			ris = new BigInteger(dpi);
		} else {
			ris = new BigInteger("72");
		}
		return ris;
	}

	// private String getImageType(){
	// String ris = null;
	// if (info.getProperty("Type") != null){
	// ris = info.getProperty("Type");
	// }
	// return ris;
	// }

	/**
	 * Questo metodo viene utilizzato per leggere i Bit per Sample dell'immagine
	 * 
	 * @throws InfoException
	 */
	public String getBitPerSampleValue() throws InfoException {
		String ris = "";

		ris = getBitperSample(getBitPerSample());
		if (getMagick().equals("JPEG")) {
			if (getPhotoInter().equals("TrueColor")) {
				ris = "8,8,8";
			}
		}
		return ris;
	}

	/**
	 * Questo metodo viene utilizzato leggere l'interpretazione fotometrica dei bit
	 * del campione
	 * 
	 * @return id dell'interpretazione fotometrica dei bit del campione
	 */
	public String getPhotoInter() {
		String ris = "";
		ris = info.getProperty("Type");
		if (ris == null) {
			ris = info.getProperty("Image:Type");
		}
		// if (ris.equals("TrueColor"))
		// {
		// if (!getMagick().equals("JPEG"))
		// {
		// if (info.getImageDepth() == 8)
		// ris = ImageJ.getPhotoInter(file);
		// else if (image.getDepth() == 1)
		// ris = Photometricinterpretationtype..BilevelType;
		// }
		// }
		return ris;
	}

	public String getMagick() {
		String ris = null;
		try {
			ris = info.getImageFormat().split(" ")[0].trim();
		} catch (Exception e) {
			try {
				ris = info.getImageFormat(0).split(" ")[0].trim();
			} catch (Exception e1) {
				ris = info.getProperty("Image:Format").split(" ")[0].trim();
			}
		}
		return ris;
	}

	/**
	 * Questo metodo viene utilizzato per indicare il Bit per Sample
	 * 
	 * @param code Codice di riferimento del Bit per Sample da decodificare
	 * @return Valore Decodificato per Bit per Sample
	 */
	public static String getBitperSample(int code) {
		String ris = "";
		ris = Integer.toString(code);
		return ris;
	}

	/**
	 * Questo metodo viene utilizzato per leggere i Bit per Sample dell'immagine
	 * 
	 * @throws InfoException
	 */
	public int getBitPerSample() throws InfoException {
		int ris = 0;
		try {
			ris = info.getImageDepth();
		} catch (Exception e) {
			try {
				ris = info.getImageDepth(0);
			} catch (Exception e1) {
				ris = info.getProperty("Image:Depth").equals("8-bit") ? 8 : 16;
			}
		}
		return ris;
	}

	/**
	 * @return the larOggScan
	 */
	public Double getLarOggScan() {
		return larOggScan;
	}

	/**
	 * @return the altOggScan
	 */
	public Double getAltOggScan() {
		return altOggScan;
	}

	/**
	 * @return the freqUnit
	 */
	public BigInteger getFreqUnit() {
		return freqUnit;
	}

	/**
	 * @return the freqPlan
	 */
	public BigInteger getFreqPlan() {
		return freqPlan;
	}

	/**
	 * @return the info
	 */
	public Info getInfo() {
		return info;
	}
}
