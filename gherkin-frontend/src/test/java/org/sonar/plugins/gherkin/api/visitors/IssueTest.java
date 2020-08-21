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
package org.sonar.plugins.gherkin.api.visitors;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.gherkin.tree.impl.InternalSyntaxToken;
import org.sonar.plugins.gherkin.api.GherkinCheck;
import org.sonar.plugins.gherkin.api.visitors.issue.FileIssue;
import org.sonar.plugins.gherkin.api.visitors.issue.IssueLocation;
import org.sonar.plugins.gherkin.api.visitors.issue.LineIssue;
import org.sonar.plugins.gherkin.api.visitors.issue.PreciseIssue;

import java.nio.file.Paths;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;

public class IssueTest {

  private static final GherkinCheck CHECK = new DoubleDispatchVisitorCheck() {
  };
  
  private static final String MESSAGE = "message";
  private static final InternalSyntaxToken TOKEN = new InternalSyntaxToken(5, 1, "value", Collections.emptyList(), false, false);
  private static InputFile inputFile;

  @Before
  public void PrepareInputFile() {
        inputFile =    new TestInputFileBuilder("moduleKey", "relative/path/from/module/baseDir.java")
        .setModuleBaseDir(Paths.get("."))
        .build();
  }
  
  @Test
  public void test_file_issue() throws Exception {
    FileIssue fileIssue = new FileIssue(inputFile.uri(), CHECK, MESSAGE);

    assertThat(fileIssue.check()).isEqualTo(CHECK);
    assertThat(fileIssue.cost()).isEqualTo(0.0);
    assertThat(fileIssue.message()).isEqualTo(MESSAGE);

    fileIssue.cost(42);
    assertThat(fileIssue.cost()).isEqualTo(42);

    fileIssue.secondary(TOKEN, "secondary message");
    assertThat(fileIssue.secondaryLocations()).hasSize(1);
    assertThat(fileIssue.secondaryLocations().get(0).message()).isEqualTo("secondary message");

    fileIssue.secondary(TOKEN, "new secondary message");
    assertThat(fileIssue.secondaryLocations()).hasSize(2);
    assertThat(fileIssue.secondaryLocations().get(1).message()).isEqualTo("new secondary message");
  }

  @Test
  public void test_line_issue() throws Exception {
    LineIssue lineIssue = new LineIssue(inputFile.uri(), CHECK, 42, MESSAGE);

    assertThat(lineIssue.check()).isEqualTo(CHECK);
    assertThat(lineIssue.cost()).isEqualTo(0.0);
    assertThat(lineIssue.message()).isEqualTo(MESSAGE);
    assertThat(lineIssue.line()).isEqualTo(42);

    lineIssue.cost(42);
    assertThat(lineIssue.cost()).isEqualTo(42);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_negative_line_issue() throws Exception {
    new LineIssue(inputFile.uri(), CHECK, -1, MESSAGE);
  }

  @Test
  public void test_precise_issue() throws Exception {
    IssueLocation primaryLocation = new IssueLocation(inputFile.uri(), TOKEN, MESSAGE);
    PreciseIssue preciseIssue = new PreciseIssue(CHECK, primaryLocation);

    assertThat(preciseIssue.check()).isEqualTo(CHECK);
    assertThat(preciseIssue.cost()).isEqualTo(0.0);
    assertThat(preciseIssue.primaryLocation()).isEqualTo(primaryLocation);
    assertThat(preciseIssue.secondaryLocations()).isEmpty();

    preciseIssue.cost(42);
    assertThat(preciseIssue.cost()).isEqualTo(42);

    assertThat(primaryLocation.startLine()).isEqualTo(5);
    assertThat(primaryLocation.endLine()).isEqualTo(5);
    assertThat(primaryLocation.startLineOffset()).isEqualTo(1);
    assertThat(primaryLocation.endLineOffset()).isEqualTo(6);
    assertThat(primaryLocation.message()).isEqualTo(MESSAGE);

    preciseIssue.secondary(TOKEN, "secondary message");
    assertThat(preciseIssue.secondaryLocations()).hasSize(1);
    assertThat(preciseIssue.secondaryLocations().get(0).message()).isEqualTo("secondary message");

    preciseIssue.secondary(TOKEN, "new secondary message");
    assertThat(preciseIssue.secondaryLocations()).hasSize(2);
    assertThat(preciseIssue.secondaryLocations().get(1).message()).isEqualTo("new secondary message");
  }

}
