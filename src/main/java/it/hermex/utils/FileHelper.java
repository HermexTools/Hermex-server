package it.hermex.utils;

import it.hermex.main.HermexServer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Sergio on 25/09/2016.
 */
public class FileHelper {
	
	public final static HashMap<String, String> acceptedTypes = new HashMap<String, String>() {{
		put("img", ".png");
		put("file", ".zip");
		put("txt", ".txt");
	}};
	
	
	public static long folderSize() {
		File dir = new File(HermexServer.config.getFolder());
		long length = 0;
		for (File file : dir.listFiles()) {
			if (file.isFile())
				length += file.length();
		}
		return length;
	}
	
	public static String generateName() {
		String fileName = new SimpleDateFormat("ddMMyy-HHmmss").format(Calendar.getInstance().getTime());
		return new Random().nextInt(999) + "-" + fileName;
	}
	
	public static String generateName(String filename){
		String st = filename;
		st = st.replaceAll("[-+^,èòàù()%&:\\[\\]{\\}]", "");
		st = st.replaceAll(" ", "_");
		return new Random().nextInt(99999) + "-" + st;
	}
		
}
