package com.DanMan.MCStargates.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LanguageFileReader {
	private HashMap<String, String> LANGUAGE = new HashMap<String, String>();

	private String default_lang = "en";
	private String filetype = ".txt";
	private String path = "plugins/MPStargate/language_" + this.default_lang + this.filetype;

	public LanguageFileReader(String language) {
		if (checkLanguageFile(language)) {
			this.path = ("plugins/MPStargate/language_" + language + this.filetype);
		}
		getLanguage();
	}

	public boolean checkLanguageFile(String language) {
		File languageData = new File("plugins/MPStargate/language_" + language + this.filetype);
		if (!languageData.exists()) {
			return false;
		}
		return true;
	}

	public boolean getLanguage() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.path));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.startsWith("#")) {
					String[] a = sCurrentLine.split("#")[0].split(":");
					String key = a[0].trim();
					String value = a[1].trim();
					value = value.substring(1, value.length() - 1);

					this.LANGUAGE.put(key, value);
				}
			}
			br.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String get(String key, ArrayList<String> list) {
		String Str = (String) this.LANGUAGE.get(key);
		String formatted;
		if (Str != null) {
			formatted = String.format(Str, list.toArray());
		} else {
			formatted = "The LanguagePack is damaged or outdated. Report this to your Admin. (key=" + key + ")";
		}
		return formatted;
	}

	public String get(String key, String oneArgument) {
		ArrayList<String> a = new ArrayList<String>();
		a.add(oneArgument);
		String formatted = "";
		String Str = (String) this.LANGUAGE.get(key);

		if (Str != null) {
			formatted = String.format(Str, a.toArray());
		} else {
			formatted = "The LanguagePack is damaged or outdated. Report this to your Admin. (key=" + key + ")";
		}

		return formatted;
	}
}