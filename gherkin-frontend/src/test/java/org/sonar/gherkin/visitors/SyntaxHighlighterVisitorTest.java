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
package org.sonar.gherkin.visitors;

import java.nio.charset.StandardCharsets;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.gherkin.parser.GherkinParserBuilder;
import org.sonar.plugins.gherkin.api.tree.GherkinDocumentTree;
import org.sonar.plugins.gherkin.api.tree.Tree;
import org.sonar.plugins.gherkin.api.visitors.TreeVisitorContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.sonar.api.batch.sensor.highlighting.TypeOfText.*;

public class SyntaxHighlighterVisitorTest {

  private SyntaxHighlighterVisitor highlighterVisitor;
  private SensorContextTester sensorContext;
  private File file;
  private DefaultInputFile inputFile;
  private TreeVisitorContext visitorContext;
  private DefaultFileSystem fileSystem;

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  @Before
  public void setUp() throws IOException {

    file = tempFolder.newFile();  
    sensorContext = SensorContextTester.create(tempFolder.getRoot());
    
    visitorContext = mock(TreeVisitorContext.class);
    when(visitorContext.getFile()).thenReturn(file);
  }

  @Test
  public void tag() throws Exception {
    highlight("@my-tag\nFeature: my feature...");
    assertHighlighting(1, 0, 7, ANNOTATION);
  }

  @Test
  public void name() throws Exception {
    highlight("@my-tag\nFeature: my feature...");
    assertHighlighting(2, 9, 13, STRING);
  }

  @Test
  public void feature_prefix() throws Exception {
    highlight("Feature: my feature...");
    assertHighlighting(1, 0, 7, KEYWORD);
  }

  @Test
  public void background_prefix() throws Exception {
    highlight("Feature: my feature...\nBackground:");
    assertHighlighting(2, 0, 10, KEYWORD);
  }

  @Test
  public void scenario_prefix() throws Exception {
    highlight("Feature: my feature...\nScenario: my scenario...");
    assertHighlighting(2, 0, 8, KEYWORD);
  }

  @Test
  public void scenario_outline_prefix() throws Exception {
    highlight("Feature: my feature...\nScenario Outline: my scenario...\nExamples:");
    assertHighlighting(2, 0, 16, KEYWORD);
  }

  @Test
  public void examples_prefix() throws Exception {
    highlight("Feature: my feature...\nScenario Outline: my scenario...\nExamples:");
    assertHighlighting(3, 0, 8, KEYWORD);
  }

  @Test
  public void step_prefix() throws Exception {
    highlight("Feature: my feature...\nScenario: my scenario...\nGiven blabla...\nWhen blabla...\nThen blabla...\n* blabla...");
    assertHighlighting(3, 0, 5, KEYWORD);
    assertHighlighting(4, 0, 4, KEYWORD);
    assertHighlighting(5, 0, 4, KEYWORD);
    assertHighlighting(6, 0, 1, KEYWORD);
  }

  @Test
  public void comment1() throws Exception {
    highlight("# blabla\nFeature: my feature...");
    assertHighlighting(1, 0, 8, COMMENT);
  }

  @Test
  public void comment2() throws Exception {
    highlight("#blabla\nFeature: my feature...");
    assertHighlighting(1, 0, 7, COMMENT);
  }

  @Test
  public void language_declaration1() throws Exception {
    highlight("# language: ru");
    assertHighlighting(1, 0, 14, ANNOTATION);
  }

  @Test
  public void language_declaration2() throws Exception {
    highlight("#language: ru");
    assertHighlighting(1, 0, 13, ANNOTATION);
  }

  @Test
  public void byte_order_mark() throws Exception {
    highlight("\ufeffFeature: my feature...");
    assertHighlighting(1, 0, 7, KEYWORD);
  }

  private void highlight(String string) throws Exception {
    fileSystem = new DefaultFileSystem(tempFolder.getRoot());
    fileSystem.setEncoding(StandardCharsets.UTF_8);
    InputFile inputFile = TestInputFileBuilder.create("moduleKey", file.getName())
        .setLanguage("gherkin")
        .setCharset(StandardCharsets.UTF_8)
        .initMetadata(string)
        .setContents(string)
        .setType(InputFile.Type.MAIN)
        .build();
    fileSystem.add(inputFile);
    sensorContext.setFileSystem(fileSystem);
    highlighterVisitor = new SyntaxHighlighterVisitor(sensorContext);
    
    Tree tree = GherkinParserBuilder.createTestParser(StandardCharsets.UTF_8).parse(string);
    when(visitorContext.getTopTree()).thenReturn((GherkinDocumentTree) tree);

    Files.asCharSink(file, StandardCharsets.UTF_8).write(string);
    highlighterVisitor.scanTree(visitorContext);
  }

  private void assertHighlighting(int line, int column, int length, TypeOfText type) {
    for (int i = column; i < column + length; i++) {
      List<TypeOfText> typeOfTexts = sensorContext.highlightingTypeAt("moduleKey:" + file.getName(), line, i);
      assertThat(typeOfTexts).hasSize(1);
      assertThat(typeOfTexts.get(0)).isEqualTo(type);
    }
  }

}
