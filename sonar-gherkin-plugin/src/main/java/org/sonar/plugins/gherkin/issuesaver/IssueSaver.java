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
package org.sonar.plugins.gherkin.issuesaver;

import com.google.common.base.Preconditions;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.gherkin.GherkinChecks;
import org.sonar.plugins.gherkin.api.GherkinCheck;
import org.sonar.plugins.gherkin.api.visitors.issue.FileIssue;
import org.sonar.plugins.gherkin.api.visitors.issue.Issue;
import org.sonar.plugins.gherkin.api.visitors.issue.IssueLocation;
import org.sonar.plugins.gherkin.api.visitors.issue.LineIssue;
import org.sonar.plugins.gherkin.api.visitors.issue.PreciseIssue;

import java.util.Optional;

public class IssueSaver {

  private static final Logger LOGGER = Loggers.get(IssueSaver.class);
  private final SensorContext sensorContext;
  private final GherkinChecks checks;

  public IssueSaver(SensorContext sensorContext, GherkinChecks checks) {
    this.sensorContext = sensorContext;
    this.checks = checks;
  }

  public <T> Optional<T> getCheck(Class<T> type) {
    return checks.all()
      .stream()
      .filter(r -> type.isAssignableFrom(r.getClass()))
      .map(type::cast)
      .findFirst();
  }

  public void saveIssues(InputFile inputFile, Issue issue) {
    RuleKey ruleKey = ruleKey(issue.check());
    try {
      if (issue instanceof FileIssue) {
        saveFileIssue(inputFile, ruleKey, (FileIssue) issue);
      } else if (issue instanceof LineIssue) {
        saveLineIssue(inputFile, ruleKey, (LineIssue) issue);
      } else {
        savePreciseIssue(inputFile, ruleKey, (PreciseIssue) issue);
      }
    } catch (IllegalArgumentException ex) {
      LOGGER.warn(ex.getMessage());
//      if (LOGGER.isDebugEnabled()) {
        LOGGER.info("InputFile: {}", inputFile.toString());
        LOGGER.info("Checker: {}", ruleKey);
//      }
    }
  }

  private void savePreciseIssue(InputFile inputFile, RuleKey ruleKey, PreciseIssue issue) {
    NewIssue newIssue = sensorContext.newIssue();

    newIssue
      .forRule(ruleKey)
      .at(newLocation(inputFile, newIssue, issue.primaryLocation()));

    if (issue.cost() != null) {
      newIssue.gap(issue.cost());
    }

    for (IssueLocation secondary : issue.secondaryLocations()) {
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("secondary issue location ??? : {}", secondary.toString());
      }
      newIssue.addLocation(newLocation(inputFile, newIssue, secondary));
    }

    newIssue.save();
  }

  private void saveFileIssue(InputFile inputFile, RuleKey ruleKey, FileIssue issue) {
    NewIssue newIssue = sensorContext.newIssue();

    NewIssueLocation primaryLocation = newIssue.newLocation()
      .message(issue.message())
      .on(inputFile);

    saveSingleIssue(newIssue, primaryLocation, ruleKey, issue);

//    InputFile secondaryFile;
//    for (IssueLocation secondary : issue.secondaryLocations()) {
//      secondaryFile = fileSystem.inputFile(fileSystem.predicates().is(secondary.file()));
//      if (secondaryFile == null) {
//        secondaryFile = primaryFile;
//      }
//      newIssue.addLocation(newLocation(secondaryFile, newIssue, secondary));
  }

  private void saveLineIssue(InputFile inputFile, RuleKey ruleKey, LineIssue issue) {
    NewIssue newIssue = sensorContext.newIssue();

    NewIssueLocation primaryLocation = newIssue.newLocation()
      .message(issue.message())
      .on(inputFile)
      .at(inputFile.selectLine(issue.line()));

    saveSingleIssue(newIssue, primaryLocation, ruleKey, issue);
  }
  
  private static void saveSingleIssue(NewIssue newIssue, NewIssueLocation primaryLocation, RuleKey ruleKey, Issue issue) {
    newIssue
      .forRule(ruleKey)
      .at(primaryLocation);

    if (issue.cost() != null) {
      newIssue.gap(issue.cost());
    }

    newIssue.save();
  }

  private NewIssueLocation newLocation(InputFile inputFile, NewIssue issue, IssueLocation location) {
    TextRange range = inputFile.newRange(
      location.startLine(), location.startLineOffset(), location.endLine(), location.endLineOffset());

    NewIssueLocation newLocation = issue.newLocation()
      .on(inputFile)
      .at(range);

    if (location.message() != null) {
      newLocation.message(location.message());
    }
    return newLocation;
  }

  private RuleKey ruleKey(GherkinCheck check) {
    Preconditions.checkNotNull(check);
    RuleKey ruleKey = checks.ruleKeyFor(check);
    if (ruleKey == null) {
      throw new IllegalStateException("No rule key found for a rule");
    }
    return ruleKey;
  }

}
