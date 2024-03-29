package ru.ifmomd.translator;

import java.io.*;
import java.net.*;

public class FindPicturesURL {
	static String adress;
	final static String ajax = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&imgsz=small|medium&rsz=8&q=";
	final static String parse = "\"url\":\"";
	static StringBuilder PC;
	static URL PU;
	static URLConnection connection;
	static BufferedReader bufR;
	static int count = 0;
			
	public static String nextURL(String word) throws Exception {
		adress = ajax + word + "&start=";
		if (count % 8 == 0) {
			PC = new StringBuilder();
			try {
			PU = new URL(adress + count);
			connection = PU.openConnection();
			connection.setConnectTimeout(5000);
			connection.connect();
			bufR = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			}
			 catch (Exception e) {
					count += 8;
					return null;
			 }
			String nextLine;
			while ((nextLine = bufR.readLine()) != null) {
				PC.append(nextLine);
			}
		}
		int index = PC.indexOf(parse);
		count++;
		if (index != -1) {
			PC = PC.delete(0, index + parse.length());
			String Time = PC.substring(0, PC.indexOf("\""));
			return Time;
		} else {
			return null;
		}

	}

}