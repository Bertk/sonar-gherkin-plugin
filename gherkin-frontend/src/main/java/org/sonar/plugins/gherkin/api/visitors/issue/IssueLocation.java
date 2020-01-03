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
package org.sonar.plugins.gherkin.api.visitors.issue;

import org.sonar.gherkin.tree.impl.GherkinTree;
import org.sonar.plugins.gherkin.api.tree.SyntaxToken;
import org.sonar.plugins.gherkin.api.tree.Tree;

import javax.annotation.Nullable;

public class IssueLocation {

  private final String message;
  private final SyntaxToken firstToken;
  private final SyntaxToken lastToken;

  public IssueLocation(Tree tree, @Nullable String message) {
    this(tree, tree, message);
  }

  public IssueLocation(Tree firstTree, Tree lastTree, @Nullable String message) {
    this.message = message;
    firstToken = ((GherkinTree) firstTree).getFirstToken();
    lastToken = ((GherkinTree) lastTree).getLastToken();
  }

  @Nullable
  public String message() {
    return message;
  }

  public int startLine() {
    return firstToken.line();
  }

  public int startLineOffset() {
    return firstToken.column();
  }

  public int endLine() {
    return lastToken.endLine();
  }

  public int endLineOffset() {
    return lastToken.endColumn();
  }

}
