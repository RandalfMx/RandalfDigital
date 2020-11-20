/**
 * 
 */
package mx.randalf.digital.img.convert;

import java.util.Vector;

/**
 * @author massi
 *
 */
public class Utility {

	/**
	 * 
	 */
	public Utility() {
	}

	// da arabi (base 10) a romani
	public static int converti(String str)
	{
		int value=0;
		byte x=0;

		for (int i=str.length()-1; i>=0; i--)
		{
			switch (str.charAt(i))
			{
				case 'I':
					if (x<=0)	value+=1;
					else		value-=1;
					x=0;
					break;
				case 'V':
					if (x<=1)	value+=5;
					else		value-=5;
					x=1;
					break;
				case 'X':
					if (x<=2)	value+=10;
					else		value-=10;
					x=2;
					break;
				case 'L':
					if (x<=3)	value+=50;
					else		value-=50;
					x=3;
					break;
				case 'C':
					if (x<=4)	value+=100;
					else		value-=100;
					x=4;
					break;
				case 'D':
					if (x<=5)	value+=500;
					else		value-=500;
					x=5;
					break;
				case 'M':
					if (x<=6)	value+=1000;
					else		value-=1000;
					x=6;
					break;
			}
		}
		return value;
	}

	// da romani ad arabi (base 10)
	public static String converti(int value)
	{
		if (value==1000) return "M";

		String str="";

		int unita;
		int decine;
		int centinaia;

		centinaia=value/100;
		decine=(value-(centinaia*100))/10;
		unita=(value-(centinaia*100)-(decine*10));
		switch (centinaia)
		{
			case 1:
				str+="C";
				break;
			case 2:
				str+="CC";
				break;
			case 3:
				str+="CCC";
				break;
			case 4:
				str+="CD";
				break;
			case 5:
				str+="D";
				break;
			case 6:
				str+="DC";
				break;
			case 7:
				str+="DCC";
				break;
			case 8:
				str+="DCCC";
				break;
			case 9:
				str+="CM";
				break;
		}

		switch (decine)
		{
			case 1:
				str+="X";
				break;
			case 2:
				str+="XX";
				break;
			case 3:
				str+="XXX";
				break;
			case 4:
				str+="XL";
				break;
			case 5:
				str+="L";
				break;
			case 6:
				str+="LX";
				break;
			case 7:
				str+="LXX";
				break;
			case 8:
				str+="LXXX";
				break;
			case 9:
				str+="XC";
				break;
		}

		switch (unita)
		{
			case 1:
				str+="I";
				break;
			case 2:
				str+="II";
				break;
			case 3:
				str+="III";
				break;
			case 4:
				str+="IV";
				break;
			case 5:
				str+="V";
				break;
			case 6:
				str+="VI";
				break;
			case 7:
				str+="VII";
				break;
			case 8:
				str+="VIII";
				break;
			case 9:
				str+="IX";
				break;
		}
			return str;
	}

	public static String convertMese(int mese){
		Vector<String> mesi=null;
		String sMese = "nd";
		
		mesi = new Vector<String>();
		mesi.add("Gennaio");
		mesi.add("Febbraio");
		mesi.add("Marzo");
		mesi.add("Aprile");
		mesi.add("Maggio");
		mesi.add("Giugno");
		mesi.add("Luglio");
		mesi.add("Agosto");
		mesi.add("Settembre");
		mesi.add("Ottobre");
		mesi.add("Novembre");
		mesi.add("Dicembre");
		if (mese>0 &&
				mesi.size()>=mese &&
				mesi.get(mese-1) != null){
			sMese = mesi.get(mese-1);
		}
		return sMese;
	}

	public static String convertMeseSmol(int mese){
		Vector<String> mesi=null;
		String sMese = "nd";
		
		mesi = new Vector<String>();
		mesi.add("gen.");
		mesi.add("feb.");
		mesi.add("mar.");
		mesi.add("apr.");
		mesi.add("mag.");
		mesi.add("giu.");
		mesi.add("lug.");
		mesi.add("ago.");
		mesi.add("set.");
		mesi.add("ott.");
		mesi.add("nov.");
		mesi.add("dic.");
		if (mese>0 &&
				mesi.size()>=mese &&
				mesi.get(mese-1) != null){
			sMese = mesi.get(mese-1);
		}
		return sMese;
	}
}
