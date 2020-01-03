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

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.gherkin.checks.annotations.GherkinRule;
import org.sonar.plugins.gherkin.api.tree.StepTree;
import org.sonar.plugins.gherkin.api.visitors.DoubleDispatchVisitorCheck;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@GherkinRule
@Rule(
  key = "when-step-regular-expression",
  name = "When steps should follow a regular expression",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
public class WhenStepRegularExpressionCheck extends DoubleDispatchVisitorCheck {

  private static final String DEFAULT_REGULAR_EXPRESSION = ".*";

  @RuleProperty(
    key = "regularExpression",
    description = "The regular expression. See " + CheckUtils.LINK_TO_JAVA_REGEX_PATTERN_DOC + " for detailed regular expression syntax.",
    defaultValue = DEFAULT_REGULAR_EXPRESSION)
  public String regularExpression = DEFAULT_REGULAR_EXPRESSION;

  @Override
  public void visitStep(StepTree tree) {
    if (tree.semanticType() == StepTree.SemanticStepType.WHEN
      && !tree.sentence().text().matches(regularExpression)) {
      addPreciseIssue(tree.sentence(), "Update the sentence to match the following regular expression: " + regularExpression);
    }
    super.visitStep(tree);
  }

  @Override
  public void validateParameters() {
    try {
      Pattern.compile(regularExpression);
    } catch (PatternSyntaxException exception) {
      throw new IllegalStateException(paramErrorMessage(), exception);
    }
  }

  private String paramErrorMessage() {
    return CheckUtils.paramErrorMessage(
      this.getClass(),
      "regularExpression parameter \"" + regularExpression + "\" is not a valid regular expression.");
  }

}
