/*
 * SonarQube Cucumber Gherkin Analyzer
 * Copyright (C) 2016-2019 David RACODON
 * david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.gherkin.checks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class CheckTestUtils {

  private static final Logger LOGGER = Loggers.get(CheckTestUtils.class);

  private CheckTestUtils() {
  }

  public static InputFile getTestInputFile(String relativePath) {
    return getTestInputFile(relativePath, StandardCharsets.UTF_8);
  }

  public static InputFile getTestInputFile(String relativePath, Charset charset) {
    InputFile inputfile = null;
    Path basedir = Paths.get("src/test/resources/checks/");
    LOGGER.info("Create InputFile: {}", basedir.resolve(relativePath).toFile().getAbsolutePath());
    try {
      inputfile = TestInputFileBuilder.create("moduleKey", basedir.resolve(relativePath).toFile().getAbsolutePath())
          .setCharset(charset)
          .setLanguage("gherkin")
          .setModuleBaseDir(basedir)
          .setType(InputFile.Type.MAIN)
          .setMetadata(new FileMetadata().readMetadata(new FileReader(basedir.resolve(relativePath).toString())))
          .setContents(new String(Files.readAllBytes(basedir.resolve(relativePath)), charset))
          .build();
    } catch (IOException e) {
      LOGGER.error("Create InputFile failed", e);
    }
    return inputfile;
  }

  public static File getTestFile(String relativePath) {
    return new File("src/test/resources/checks/" + relativePath);
  }
  
  public static  URI getTestFilePath(String relativePath) {
    return CheckTestUtils.getTestFile(relativePath).toURI();
  }
  
}
