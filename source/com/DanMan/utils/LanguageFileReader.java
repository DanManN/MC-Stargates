/*    */ package hauptklassen;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class LanguageFileReader
/*    */ {
/* 12 */   public HashMap<String, String> LANGUAGE = new HashMap();
/*    */   
/* 14 */   String default_lang = "en";
/* 15 */   String filetype = ".txt";
/* 16 */   String path = "plugins/MPStargate/language_" + this.default_lang + this.filetype;
/*    */   
/*    */   LanguageFileReader(String language)
/*    */   {
/* 20 */     if (checkLanguageFile(language)) {
/* 21 */       this.path = ("plugins/MPStargate/language_" + language + this.filetype);
/*    */     }
/* 23 */     getLanguage();
/*    */   }
/*    */   
/*    */   public boolean checkLanguageFile(String language) {
/* 27 */     File languageData = new File("plugins/MPStargate/language_" + language + this.filetype);
/* 28 */     if (!languageData.exists()) {
/* 29 */       return false;
/*    */     }
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public boolean getLanguage() {
/*    */     try {
/* 36 */       Object localObject1 = null;Object localObject4 = null; Object localObject3; try { BufferedReader br = new BufferedReader(new FileReader(this.path));
/*    */         try { String sCurrentLine;
/* 38 */           while ((sCurrentLine = br.readLine()) != null) { String sCurrentLine;
/* 39 */             if (!sCurrentLine.startsWith("#"))
/*    */             {
/* 41 */               String[] a = sCurrentLine.split("#")[0].split(":");
/* 42 */               String key = a[0].trim();
/* 43 */               String value = a[1].trim();
/* 44 */               value = value.substring(1, value.length() - 1);
/*    */               
/* 46 */               this.LANGUAGE.put(key, value);
/*    */             }
/*    */           }
/* 49 */           return true;
/*    */         } finally {
/* 51 */           if (br != null) br.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { ((Throwable)localObject3).addSuppressed(localThrowable);
/*    */         }
/*    */       }
/* 54 */       return false;
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 52 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public String get(String key, ArrayList<String> list)
/*    */   {
/* 59 */     String Str = (String)this.LANGUAGE.get(key);
/*    */     String formatted;
/* 61 */     String formatted; if (Str != null) {
/* 62 */       formatted = String.format(Str, list.toArray());
/*    */     } else {
/* 64 */       formatted = "The LanguagePack is damaged or outdated. Report this to your Admin. (key=" + key + ")";
/*    */     }
/* 66 */     return formatted;
/*    */   }
/*    */   
/*    */   public String get(String key, String oneArgument) {
/* 70 */     ArrayList<String> a = new ArrayList();
/* 71 */     a.add(oneArgument);
/* 72 */     String formatted = "";
/* 73 */     String Str = (String)this.LANGUAGE.get(key);
/*    */     
/* 75 */     if (Str != null) {
/* 76 */       formatted = String.format(Str, a.toArray());
/*    */     } else {
/* 78 */       formatted = "The LanguagePack is damaged or outdated. Report this to your Admin. (key=" + key + ")";
/*    */     }
/*    */     
/* 81 */     return formatted;
/*    */   }
/*    */ }


/* Location:              /home/danman/Minecraft/mpgates v0.73.jar!/hauptklassen/LanguageFileReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */