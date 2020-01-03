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
import org.sonar.plugins.gherkin.api.tree.GherkinDocumentTree;
import org.sonar.plugins.gherkin.api.visitors.DoubleDispatchVisitorCheck;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@GherkinRule
@Rule(
  key = "empty-line-end-of-file",
  name = "Files should contain an empty new line at the end",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
public class MissingNewlineAtEndOfFileCheck extends DoubleDispatchVisitorCheck {

  @Override
  public void visitGherkinDocument(GherkinDocumentTree tree) {
    String line = "";
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().getGherkinFile().inputStream()))){
      while (reader.ready()) {
        line = reader.readLine();
      }
      if (!line.isEmpty()) {
        addFileIssue("Add an empty new line at the end of this file.");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Check gherkin:" + this.getClass().getAnnotation(Rule.class).key()
        + ": Error while reading " + getContext().getGherkinFile().filename(), e);
    }
    super.visitGherkinDocument(tree);
  }

}
