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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//import com.google.common.io.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.gherkin.checks.verifier.GherkinCheckVerifier;

import java.io.FileReader;

import static org.fest.assertions.Assertions.assertThat;

public class EndLineCharactersCheckTest {

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  private EndLineCharactersCheck check = new EndLineCharactersCheck();

  @Test
  public void should_find_only_crlf_and_not_raise_any_issues() throws Exception {
    check.setEndLineCharacters("CRLF");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\r\n")).noMore();
  }

  @Test
  public void should_find_only_cr_and_not_raise_any_issues() throws Exception {
    check.setEndLineCharacters("CR");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\r")).noMore();
  }

  @Test
  public void should_find_only_lf_and_not_raise_any_issues() throws Exception {
    check.setEndLineCharacters("LF");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\n")).noMore();
  }

  @Test
  public void crlf_should_find_lf_and_raise_issues() throws Exception {
    check.setEndLineCharacters("CRLF");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\n")).next()
        .withMessage("Set all end-line characters to 'CRLF' in this file.").noMore();
  }

  @Test
  public void crlf_should_find_cr_and_raise_issues() throws Exception {
    check.setEndLineCharacters("CRLF");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\r")).next()
        .withMessage("Set all end-line characters to 'CRLF' in this file.").noMore();
  }

  @Test
  public void cr_should_find_crlf_and_raise_issues() throws Exception {
    check.setEndLineCharacters("CR");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\r\n")).next()
        .withMessage("Set all end-line characters to 'CR' in this file.").noMore();
  }

  @Test
  public void cr_should_find_lf_and_raise_issues() throws Exception {
    check.setEndLineCharacters("CR");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\n")).next()
        .withMessage("Set all end-line characters to 'CR' in this file.").noMore();
  }

  @Test
  public void lf_should_find_crlf_and_raise_issues() throws Exception {
    check.setEndLineCharacters("LF");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\r\n")).next()
        .withMessage("Set all end-line characters to 'LF' in this file.").noMore();
  }

  @Test
  public void lf_should_find_cr_and_raise_issues() throws Exception {
    check.setEndLineCharacters("LF");
    GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\r")).next()
        .withMessage("Set all end-line characters to 'LF' in this file.").noMore();
  }

  @Test
  public void should_throw_an_illegal_state_exception_as_the_endLineCharacters_parameter_is_not_valid()
      throws Exception {
    try {
      check.setEndLineCharacters("abc");
      GherkinCheckVerifier.issues(check, getTestFileWithProperEndLineCharacters("\r")).noMore();
    } catch (IllegalStateException e) {
      assertThat(e.getMessage())
          .isEqualTo("Check gherkin:end-line-characters (End-line characters should be consistent): "
              + "endLineCharacters parameter is not valid.\nActual: 'abc'\nExpected: 'CR' or 'CRLF' or 'LF'");
    }
  }

  private InputFile getTestFileWithProperEndLineCharacters(String endLineCharacter) throws Exception {
    String relativePath = "end-line-characters.feature";
    Path basedir = Paths.get("src/test/resources/checks/");
    String contents = new String(Files.readAllBytes(basedir.resolve(relativePath)), StandardCharsets.UTF_8)
        .replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n").replaceAll("\\n", endLineCharacter);

    InputFile testInputFile = TestInputFileBuilder
        .create("moduleKey", basedir.resolve(relativePath).toFile().getAbsolutePath())
        .setCharset(StandardCharsets.UTF_8)
        .setLanguage("gherkin")
        .setModuleBaseDir(basedir)
        .setType(InputFile.Type.MAIN)
        .setMetadata(new FileMetadata().readMetadata(new FileReader(basedir.resolve(relativePath).toString())))
        .setContents(contents)
        .build();
    return testInputFile;
  }

}
