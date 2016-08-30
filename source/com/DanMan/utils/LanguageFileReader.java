package com.DanMan.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LanguageFileReader {
	public HashMap<String, String> LANGUAGE = new HashMap();

	String default_lang = "en";
	String filetype = ".txt";
	String path = "plugins/MPStargate/language_" + this.default_lang + this.filetype;

	LanguageFileReader(String language) {
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
			Object localObject1 = null;
			Object localObject4 = null;
			Object localObject3;
			try {
				BufferedReader br = new BufferedReader(new FileReader(this.path));
				try {
					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) {
						String sCurrentLine;
						if (!sCurrentLine.startsWith("#")) {
							String[] a = sCurrentLine.split("#")[0].split(":");
							String key = a[0].trim();
							String value = a[1].trim();
							value = value.substring(1, value.length() - 1);

							this.LANGUAGE.put(key, value);
						}
					}
					return true;
				} finally {
					if (br != null)
						br.close();
				}
			} finally {
				if (localObject2 == null)
					localObject3 = localThrowable;
				else if (localObject3 != localThrowable) {
					((Throwable) localObject3).addSuppressed(localThrowable);
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String get(String key, ArrayList<String> list) {
		String Str = (String) this.LANGUAGE.get(key);
		String formatted;
		String formatted;
		if (Str != null) {
			formatted = String.format(Str, list.toArray());
		} else {
			formatted = "The LanguagePack is damaged or outdated. Report this to your Admin. (key=" + key + ")";
		}
		return formatted;
	}

	public String get(String key, String oneArgument) {
		ArrayList<String> a = new ArrayList();
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