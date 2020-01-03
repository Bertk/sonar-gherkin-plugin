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
package org.sonar.gherkin.visitors.metrics;

import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.gherkin.parser.GherkinParserBuilder;
import org.sonar.plugins.gherkin.api.tree.Tree;

import TestUtils.TestUtils;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class MetricsTest {

  @Test
  public void metrics_UTF8_file() throws IOException {
    InputFile inputFile = TestUtils.getTestInputFile("metrics/metrics.feature", StandardCharsets.UTF_8);
    Tree tree = GherkinParserBuilder.createTestParser(StandardCharsets.UTF_8).parse(inputFile.contents());
    assertMetrics(tree);
  }

  @Test
  public void metrics_UTF8_file_with_BOM() throws IOException {
    InputFile inputFile = TestUtils.getTestInputFile("metrics/metrics-bom.feature", StandardCharsets.UTF_8);
    Tree tree = GherkinParserBuilder.createTestParser(StandardCharsets.UTF_8).parse(inputFile.contents());
    assertMetrics(tree);
  }

  private void assertMetrics(Tree tree) {
    assertThat(new LinesOfCodeVisitor(tree).getNumberOfLinesOfCode()).isEqualTo(22);
    assertThat(new StatementsVisitor(tree).getNumberOfStatements()).isEqualTo(10);
    assertThat(new CommentLinesVisitor(tree).getNumberOfCommentLines()).isEqualTo(2);
    assertThat(new FunctionsVisitor(tree).getNumberOfFunctions()).isEqualTo(4);
    assertThat(new ClassesVisitor(tree).getNumberOfClasses()).isEqualTo(1);
  }

}
