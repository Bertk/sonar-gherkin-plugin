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

import org.junit.Ignore;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.gherkin.parser.GherkinParserBuilder;
import org.sonar.plugins.gherkin.api.tree.GherkinDocumentTree;
import org.sonar.plugins.gherkin.api.visitors.TreeVisitorContext;

import TestUtils.TestUtils;

import java.io.File;
import java.io.IOException;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetricsVisitorTest {

  @Test @Ignore("relocate test to sonar-gherkin-plugin project")
  public void verify_metrics() throws IOException {
    File moduleBaseDir = new File("src/test/resources/metrics/");
    InputFile inputFile = TestUtils.getTestInputFile("metrics/metrics.feature", StandardCharsets.UTF_8);
    
    SensorContextTester context = SensorContextTester.create(moduleBaseDir);
    context.fileSystem().add(inputFile);
    MetricsVisitor metricsVisitor = new MetricsVisitor(context);

    TreeVisitorContext treeVisitorContext = mock(TreeVisitorContext.class);
    when(treeVisitorContext.getGherkinFile()).thenReturn(inputFile);
    when(treeVisitorContext.getTopTree()).thenReturn((GherkinDocumentTree) GherkinParserBuilder.createTestParser(StandardCharsets.UTF_8).parse(inputFile.contents()));

    metricsVisitor.scanTree(treeVisitorContext);
    
    String key = "moduleKey:metrics.feature";
    assertThat(context.measure(key, CoreMetrics.NCLOC).value()).isEqualTo(22);
    assertThat(context.measure(key, CoreMetrics.STATEMENTS).value()).isEqualTo(10);
    assertThat(context.measure(key, CoreMetrics.COMMENT_LINES).value()).isEqualTo(2);
    assertThat(context.measure(key, CoreMetrics.FUNCTIONS).value()).isEqualTo(4);
    assertThat(context.measure(key, CoreMetrics.CLASSES).value()).isEqualTo(1);
  }

}
