package ru.ifmomd.translator;

import java.io.*;
import java.net.*;

public class Translator {
	private final static String apiURL = "http://translate.yandex.ru/tr.json/translate?lang=en-ru&text=";

	public static String getTranslation(String source) throws IOException {

		URL yandexTranslate = new URL(apiURL + source);
		URLConnection uc = yandexTranslate.openConnection();
		uc.connect();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				uc.getInputStream(), "UTF-8"));
		String translation;
		translation = in.readLine();
		in.close();

		return translation;
	}

}
