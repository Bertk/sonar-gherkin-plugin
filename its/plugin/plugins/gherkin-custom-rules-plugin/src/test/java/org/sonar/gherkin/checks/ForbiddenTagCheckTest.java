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

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.gherkin.checks.verifier.GherkinCheckVerifier;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ForbiddenTagCheckTest {

  private static InputFile inputFile;
  
  @Before
  public void PrepareInputFile() throws FileNotFoundException, IOException {
    String relativePath = "forbidden-tag.feature";
    Path basedir = Paths.get("src/test/resources/checks/");
    inputFile =    new TestInputFileBuilder("moduleKey", relativePath)
    .setCharset(StandardCharsets.UTF_8)
    .setLanguage("gherkin")
    .setModuleBaseDir(basedir)
    .setType(InputFile.Type.MAIN)
    .setMetadata(new FileMetadata().readMetadata(new FileReader(basedir.resolve(relativePath).toString())))
    .setContents(new String(Files.readAllBytes(basedir.resolve(relativePath)), StandardCharsets.UTF_8))
    .build();
  }

  @Test
  public void test() {
    GherkinCheckVerifier.issues(new ForbiddenTagCheck(), inputFile)
      .next().atLine(1).withMessage("Remove this usage of the forbidden \"bar\" tag.")
      .next().atLine(4).withMessage("Remove this usage of the forbidden \"foo\" tag.")
      .noMore();
  }

}
