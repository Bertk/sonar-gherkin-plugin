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
import org.sonar.gherkin.checks.annotations.GherkinRule;
import org.sonar.plugins.gherkin.api.tree.NameTree;
import org.sonar.plugins.gherkin.api.tree.StepTree;
import org.sonar.plugins.gherkin.api.tree.SyntaxToken;
import org.sonar.plugins.gherkin.api.visitors.DoubleDispatchVisitorCheck;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@GherkinRule
@Rule(
  key = "wording-business-level",
  name = "Wording should remain at business level",
  priority = Priority.MAJOR,
  tags = {Tags.DESIGN})
public class WordingBusinessLevelCheck extends DoubleDispatchVisitorCheck {

  protected static final String[] FORBIDDEN_WORDS = {"checkbox", "click", "drop-down list", "dropdown list", "field", "fill in the form", "radio button"};

  @Override
  public void visitStep(StepTree tree) {
    checkForForbiddenWords(tree.sentence().value());
    super.visitStep(tree);
  }

  @Override
  public void visitName(NameTree tree) {
    checkForForbiddenWords(tree.value());
    super.visitName(tree);
  }

  private void checkForForbiddenWords(SyntaxToken token) {
    for (String word : FORBIDDEN_WORDS) {
      Pattern pattern = Pattern.compile(word);
      Matcher matcher = pattern.matcher(token.text());
      while (matcher.find()) {
        addPreciseIssue(token, "Remove this forbidden word.");
      }
    }
  }

}
