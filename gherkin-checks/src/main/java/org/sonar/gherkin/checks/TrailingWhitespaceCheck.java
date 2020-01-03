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
import org.sonar.gherkin.visitors.CharsetAwareVisitor;
import org.sonar.plugins.gherkin.api.tree.GherkinDocumentTree;
import org.sonar.plugins.gherkin.api.visitors.DoubleDispatchVisitorCheck;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@GherkinRule
@Rule(
  key = "S1131",
  name = "Lines should not end with trailing whitespaces",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
public class TrailingWhitespaceCheck extends DoubleDispatchVisitorCheck implements CharsetAwareVisitor {

  private static final String WHITESPACE = "\\t\\u000B\\f\\u0020\\u00A0\\uFEFF\\p{Zs}";
  @SuppressWarnings("unused")
  private Charset charset;

  @Override
  public void visitGherkinDocument(GherkinDocumentTree tree) {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().getGherkinFile().inputStream()))){
      while (reader.ready()) {
        lines.add(reader.readLine());
      }
    } catch (IOException e) {
      throw new IllegalStateException("Check gherking:" + this.getClass().getAnnotation(Rule.class).key()
        + ": Error while reading " + getContext().getGherkinFile().filename(), e);
    }
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if (line.length() > 0 && Pattern.matches("[" + WHITESPACE + "]", line.subSequence(line.length() - 1, line.length()))) {
        addLineIssue(i + 1, "Remove the useless trailing whitespaces at the end of this line.");
      }
    }
    super.visitGherkinDocument(tree);
  }

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

}
