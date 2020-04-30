package sommelier;

import tool.compet.javacore.constant.DkConstant;
import tool.compet.javacore.log.DkConsoleLogs;
import tool.compet.javacore.util.DkFiles;
import tool.compet.javacore.util.DkStrings;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// This generates db schema for each table from php class inside Model/Entity folder.
public class GenDbSchema {
   public GenDbSchema() {
   }

   private void start() throws Exception {
      File testdata = new File(DkFiles.makePath(DkConstant.ABS_PATH, "testdata", "in"));
      DkFiles.createNewDirRecursively(testdata);

      for (File f : DkFiles.collectFilesRecursively(testdata)) {
         gen(f);
      }
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
         new GenDbSchema().start();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}
