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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@GherkinRule
@Rule(
  key = "bom-utf8-files",
  name = "Byte Order Mark (BOM) should not be used for UTF-8 files",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL})
public class BOMCheck extends DoubleDispatchVisitorCheck implements CharsetAwareVisitor {

  private Charset charset;

  @Override
  public void visitGherkinDocument(GherkinDocumentTree tree) {
    if ((StandardCharsets.UTF_8).equals(charset) && tree.hasByteOrderMark()) {
      addFileIssue("Remove the Byte Order Mark (BOM).");
    }
    super.visitGherkinDocument(tree);
  }

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

}
