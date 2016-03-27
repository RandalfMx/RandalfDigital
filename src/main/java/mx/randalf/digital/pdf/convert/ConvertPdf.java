package mx.randalf.digital.pdf.convert;

import java.io.File;
import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.MogrifyCmd;
import org.im4java.process.ProcessStarter;

public class ConvertPdf {

	private String pathImageMagick = null;

	public ConvertPdf(String pathImageMagick) {
		this.pathImageMagick = pathImageMagick;
		ProcessStarter.setGlobalSearchPath(pathImageMagick);
	}

//	public void createPdfAndOcr(File filePdf, File[] filesImg){
//		Path dPath = null;
//		ConvertOcr ocr = null;
//		HocrToPdf hocr = null;
//		File fOcr = null;
//		File fPdf = null;
//
//		hocr = new HocrToPdf();
//		dPath = Files.createTempDirectory("CenvertPdf-");
//		ocr = new ConvertPdf(pathImageMagick);
//		ocr.setHocr(true);
//		for (int x=0; x<filesImg.length; x++){
//			fOcr = new File(dPath.toString()+
//					File.separator+
//					filesImg[x].getName().toLowerCase().
//						replace(".jpg", ".html").replace(".tif", ".html"));
//			fPdf = new File(dPath.toString()+
//					File.separator+
//					filesImg[x].getName().toLowerCase().
//						replace(".jpg", ".pdf").replace(".tif", ".pdf"));
//			ocr.convertOcr(filesImg[x], fOcr);
//			hocr.hocrToPdf(filesImg[x], fOcr, fPdf);
//		}
//		ocr.
//		dPath.toFile();
//	}
//
	public void createPdf(File filePdf, File[] filesImg)
			throws NumberFormatException, IOException, InterruptedException,
			IM4JavaException {
		ConvertCmd cc = null;
		IMOperation imo = null;

		try {
			cc = new ConvertCmd();
			cc.setSearchPath(pathImageMagick);

			imo = new IMOperation();
			for (int x = 0; x < filesImg.length; x++) {
				imo.addImage(filesImg[x].getAbsolutePath());
			}
			imo.quality(new Double("25"));
//			imo.resize(50);
			imo.addImage(filePdf.getAbsolutePath());
			// convert -quality 25 -resize 50% *.jpg -adjoin output.pdf

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
	}

	public void createPdfA(File filePdf, File[] filesImg)
			throws NumberFormatException, IOException, InterruptedException,
			IM4JavaException {
		MogrifyCmd mc = null;
		IMOperation imo = null;
		File filePdfTmp = null;
		File filePdfA = null;
	
		try {
			filePdfTmp = new File(filePdf.getAbsolutePath().replace(".pdf", "_tmp.pdf"));
			createPdf(filePdfTmp, filesImg);
			
			mc = new MogrifyCmd();
			mc.setSearchPath(pathImageMagick);
			
			imo = new IMOperation();
			imo.format("pdfa");
			imo.addImage(filePdfTmp.getAbsolutePath());
			mc.run(imo);
			
			filePdfA = new File(filePdfTmp.getAbsolutePath()+"a");
			filePdfA.renameTo(filePdf);
			filePdfTmp.delete();
			//mogrify -format pdfa pippo.pdf
		} catch (NumberFormatException e) {
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
