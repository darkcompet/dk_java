import sommelier.GenDbSchema;
import tool.compet.javacore.constant.DkConstant;
import tool.compet.javacore.log.DkConsoleLogs;
import tool.compet.javacore.util.DkFiles;
import tool.compet.javacore.util.DkStrings;

import java.io.BufferedWriter;
import java.io.File;
import java.util.*;

public class Tmp {
   private void start() throws Exception {
      File tmpDir = new File(DkFiles.makePath(DkConstant.ABS_PATH, "testdata", "tmp"));
      DkFiles.createNewDirRecursively(tmpDir);

      HashSet<String> tableNames = new HashSet<>();

      for (File f : DkFiles.collectFilesRecursively(tmpDir)) {
         String fname = f.getName();
         int endIndex = fname.lastIndexOf("Table.php");

         if (endIndex > 0) {
            String tableName = fname.substring(0, endIndex).trim();

            if (tableName.length() > 0) {
               tableNames.add(tableName);
            }
         }
      }

      File inDir = new File(DkFiles.makePath(DkConstant.ABS_PATH, "testdata", "in"));
      DkFiles.createNewDirRecursively(inDir);

      for (File inFile : Objects.requireNonNull(inDir.listFiles())) {
         List<String> lines = DkFiles.readFileAsUtf8Lines(inFile);

         for (int i = 0, N = lines.size(); i < N; ++i) {
            String line = lines.get(i).trim();

            if (line.startsWith("class")) {
               String className = line.split(" ")[1].trim();
               String tableName = calcTableName(tableNames, className);
               lines.set(i - 1, DkStrings.format("/** This is schema definition of table `%s` */", tableName));

               if (!line.contains("extends __CommonSchema")) {
                  lines.set(i, DkStrings.format("class %s extends __CommonSchema {", className));
               }

               File outFile = new File(DkFiles.makePath(DkConstant.ABS_PATH, "testdata", "out", inFile.getName()));
               BufferedWriter bw = DkFiles.newUtf8Writer(outFile);
               for (String l : lines) {
                  bw.write(l);
                  bw.write(DkConstant.LS);
               }
               bw.close();

               break;
            }
         }
      }
   }

   private String calcTableName(HashSet<String> tableNames, String className) {
      String result = null;
      float max = -1f;

      for (String tabName : tableNames) {
         float percent = matchPercent(tabName, className);
         if (percent > max) {
            max = percent;
            result = tabName;
         }
         else if (percent == max) {
            DkConsoleLogs.warn(this, "What???");
         }
      }

      DkConsoleLogs.info(this, "Matching className [%s] => tableName [%s]", className, result);

      return result;
   }

   private float matchPercent(String tabName, String className) {
      int N = Math.min(tabName.length(), className.length());
      int M = Math.max(tabName.length(), className.length());
      int cnt = 0;

      for (int i = 0; i < N; ++i) {
         if (tabName.charAt(i) == className.charAt(i)) {
            ++cnt;
         }
      }

      return cnt * 1f / M;
   }

   private void gen(File inFile) throws Exception {
      List<String> lines = DkFiles.readFileAsUtf8Lines(inFile);
      List<String> fieldNames = new ArrayList<>();
      String propertyPrefix = "* @property ";
      for (String line : lines) {
         line = line.trim();

         // * @property int $seqNo
         if (line.startsWith(propertyPrefix)) {
            int startIndex = line.indexOf(propertyPrefix) + propertyPrefix.length();

            String[] arr = line.substring(startIndex).trim().split(" ");

            if (arr.length > 1) {
               String fieldName = arr[1].trim().substring(1);
               fieldNames.add(fieldName);
            }
         }
      }
      Collections.sort(fieldNames);

      String className = inFile.getName();
      className = className.substring(0, className.indexOf('.'));
      List<String> definitions = new ArrayList<>();
      definitions.add("<?php");
      definitions.add(DkConstant.LS);
      definitions.add(DkConstant.LS);
      definitions.add("namespace App\\Common\\DbSchema;");
      definitions.add(DkConstant.LS);
      definitions.add(DkConstant.LS);
      definitions.add(DkStrings.format("/** This is schema definition of table `%s` */", className));
      definitions.add(DkConstant.LS);
      definitions.add(DkStrings.format("class %s {", className));
      definitions.add(DkConstant.LS);
      for (String fieldName : fieldNames) {
         definitions.add(DkStrings.format("   const %s = \"%s\";", fieldName, fieldName));
         definitions.add(DkConstant.LS);
      }
      definitions.add("}");
      definitions.add(DkConstant.LS);

      File outFile = new File(DkFiles.makePath(DkConstant.ABS_PATH, "testdata", "out", className + ".php"));
      DkFiles.createNewFileRecursively(outFile);
      BufferedWriter bw = DkFiles.newUtf8Writer(outFile);
      for (String line : definitions) {
         bw.write(line);
      }
      bw.close();
      DkConsoleLogs.info(this, "Added %d definition to file [%s]", definitions.size(), outFile.getName());
   }

   public static void main(String... args) {
      try {
         new Tmp().start();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}
