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
package org.sonar.plugins.gherkin;

import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.*;
import org.sonar.check.Rule;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.gherkin.checks.CommentConventionCheck;
import org.sonar.gherkin.checks.MissingNewlineAtEndOfFileCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GherkinSquidSensorTest {

  private static final Logger LOGGER = Loggers.get(GherkinSquidSensorTest.class);
  private final File baseDir = new File("src/test/resources");
  private final SensorContextTester context = SensorContextTester.create(baseDir);
  private CheckFactory checkFactory = new CheckFactory(mock(ActiveRules.class));
  private DefaultFileSystem fileSystem;

  @Test
  public void should_create_a_valid_sensor_descriptor() {
    DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
    createGherkinSquidSensor().describe(descriptor);
    assertThat(descriptor.name()).isEqualTo("Gherkin Squid Sensor");
    assertThat(descriptor.languages()).containsOnly("gherkin");
    assertThat(descriptor.type()).isEqualTo(InputFile.Type.MAIN);
  }

  @Test
  public void should_execute_and_compute_valid_measures_on_UTF8_file() {
    String relativePath = "my-feature.feature";
    inputFile(relativePath, StandardCharsets.UTF_8);
    createGherkinSquidSensor().execute(context);
    assertMeasure("moduleKey:" + relativePath);
  }

  @Test
  public void should_execute_and_compute_valid_measures_on_UTF8_with_BOM_file() {
    String relativePath = "my-feature-bom.feature";
    inputFile(relativePath, StandardCharsets.UTF_8);
    createGherkinSquidSensor().execute(context);
    assertMeasure("moduleKey:" + relativePath);
  }

  private void assertMeasure(String key) {
    assertThat(context.measure(key, CoreMetrics.NCLOC).value()).isEqualTo(9);
    assertThat(context.measure(key, CoreMetrics.STATEMENTS).value()).isEqualTo(6);
    assertThat(context.measure(key, CoreMetrics.COMMENT_LINES).value()).isEqualTo(2);
    assertThat(context.measure(key, CoreMetrics.FUNCTIONS).value()).isEqualTo(2);
    assertThat(context.measure(key, CoreMetrics.CLASSES).value()).isEqualTo(1);
  }

  @Test
  public void should_execute_and_compute_valid_measures_on_UTF8_file_french() {
    String relativePath = "my-feature-fr.feature";
    inputFile(relativePath, StandardCharsets.UTF_8);
    createGherkinSquidSensor().execute(context);
    assertMeasureFr("moduleKey:" + relativePath);
  }

  @Test
  public void should_execute_and_compute_valid_measures_on_UTF8_with_BOM_file_french() {
    String relativePath = "my-feature-bom-fr.feature";
    inputFile(relativePath, StandardCharsets.UTF_8);
    createGherkinSquidSensor().execute(context);
    assertMeasureFr("moduleKey:" + relativePath);
  }

  private void assertMeasureFr(String key) {
    assertThat(context.measure(key, CoreMetrics.NCLOC).value()).isEqualTo(10);
    assertThat(context.measure(key, CoreMetrics.STATEMENTS).value()).isEqualTo(6);
    assertThat(context.measure(key, CoreMetrics.COMMENT_LINES).value()).isEqualTo(2);
    assertThat(context.measure(key, CoreMetrics.FUNCTIONS).value()).isEqualTo(2);
    assertThat(context.measure(key, CoreMetrics.CLASSES).value()).isEqualTo(1);
  }

  @Test
  public void should_execute_and_save_issues_on_UTF8_with_BOM_file() {
    inputFile("my-feature-bom.feature", StandardCharsets.UTF_8);

    ActiveRules activeRules = (new ActiveRulesBuilder())
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, CommentConventionCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, MissingNewlineAtEndOfFileCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .build();
    checkFactory = new CheckFactory(activeRules);

    createGherkinSquidSensor().execute(context);

    assertThat(context.allIssues()).hasSize(3);
  }

  @Test
  public void should_execute_and_save_issues_on_UTF8_file() {
    inputFile("my-feature.feature", StandardCharsets.UTF_8);

    ActiveRules activeRules = (new ActiveRulesBuilder())
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, CommentConventionCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, MissingNewlineAtEndOfFileCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .build();
    checkFactory = new CheckFactory(activeRules);

    createGherkinSquidSensor().execute(context);

    assertThat(context.allIssues()).hasSize(3);
  }

  @Test
  public void should_execute_and_save_issues_on_UTF8_with_BOM_file_french() {
    inputFile("my-feature-bom-fr.feature", StandardCharsets.UTF_8);

    ActiveRules activeRules = (new ActiveRulesBuilder())
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, CommentConventionCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, MissingNewlineAtEndOfFileCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .build();
    checkFactory = new CheckFactory(activeRules);

    createGherkinSquidSensor().execute(context);

    assertThat(context.allIssues()).hasSize(3);
  }

  @Test
  public void should_execute_and_save_issues_on_UTF8_file_french() {
    inputFile("my-feature-fr.feature", StandardCharsets.UTF_8);

    ActiveRules activeRules = (new ActiveRulesBuilder())
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, CommentConventionCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, MissingNewlineAtEndOfFileCheck.class.getAnnotation(Rule.class).key()))
      .activate()
      .build();
    checkFactory = new CheckFactory(activeRules);

    createGherkinSquidSensor().execute(context);

    assertThat(context.allIssues()).hasSize(3);
  }

  @Test
  public void should_raise_an_issue_because_the_parsing_error_rule_is_activated() {
    inputFile("parsing-error.feature", StandardCharsets.UTF_8);

    ActiveRules activeRules = (new ActiveRulesBuilder())
      .create(RuleKey.of(GherkinRulesDefinition.REPOSITORY_KEY, "S2260"))
      .activate()
      .build();

    checkFactory = new CheckFactory(activeRules);

    context.setActiveRules(activeRules);
    createGherkinSquidSensor().execute(context);
    Collection<Issue> issues = context.allIssues();
    assertThat(issues).hasSize(1);
    Issue issue = issues.iterator().next();
    assertThat(issue.primaryLocation().textRange().start().line()).isEqualTo(1);
  }

  @Test
  public void should_not_raise_any_issue_because_the_parsing_error_rule_is_not_activated() {
    inputFile("parsing-error.feature", StandardCharsets.UTF_8);

    ActiveRules activeRules = new ActiveRulesBuilder().build();
    checkFactory = new CheckFactory(activeRules);

    context.setActiveRules(activeRules);
    createGherkinSquidSensor().execute(context);
    Collection<Issue> issues = context.allIssues();
    assertThat(issues).hasSize(0);
  }

  private GherkinSquidSensor createGherkinSquidSensor() {
    return new GherkinSquidSensor(context.fileSystem(), checkFactory, null);
  }

  private void inputFile(String relativePath, Charset charset) {
    fileSystem = new DefaultFileSystem(baseDir.toPath());
    fileSystem.setEncoding(StandardCharsets.UTF_8);
    try {
      InputFile inputFile = TestInputFileBuilder
          .create("moduleKey", relativePath)
          .setCharset(charset)
          .setLanguage(GherkinLanguage.KEY)
          .setModuleBaseDir(baseDir.toPath())
          .setType(InputFile.Type.MAIN)
          .setMetadata(new FileMetadata().readMetadata(new FileReader(baseDir.toPath().resolve(relativePath).toString())))
          .setContents(new String(Files.readAllBytes(baseDir.toPath().resolve(relativePath)), charset))
          .build();
      fileSystem.add(inputFile);
      context.setFileSystem(fileSystem);
    } catch (IOException e) {
      LOGGER.error("inputFile create failed", e);
    }
  }
      
}
