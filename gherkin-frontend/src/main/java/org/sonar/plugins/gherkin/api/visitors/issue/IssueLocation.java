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

import java.net.URI;

import javax.annotation.Nullable;

public class IssueLocation {

  private final URI uri;
  private final String message;
  private int startLine;
  private int startLineOffset;
  private int endLine;
  private int endLineOffset;

  public IssueLocation(URI uri, Tree tree, @Nullable String message) {
    this(uri, tree, tree, message);
  }

  public IssueLocation(URI uri, SyntaxToken token, int offsetStart, int offsetEnd, @Nullable String message) {
    this.uri = uri;
    this.message = message;
    this.startLine = token.line();
    this.endLine = token.line();
    this.startLineOffset = token.column() + offsetStart;
    this.endLineOffset = token.column() + offsetEnd;
  }
  
  public IssueLocation(URI uri, Tree firstTree, Tree lastTree, @Nullable String message) {
    this.uri = uri;
    this.message = message;
    SyntaxToken firstToken = ((GherkinTree) firstTree).getFirstToken();
    SyntaxToken lastToken = ((GherkinTree) lastTree).getLastToken();
    this.startLine = firstToken.line();
    this.startLineOffset = firstToken.column();
    this.endLine = lastToken.endLine();
    this.endLineOffset = lastToken.endColumn();
  }

  public URI uri() {
    return uri;
  }

  @Nullable
  public String message() {
    return message;
  }

  public int startLine() {
    return startLine;
  }

  public int startLineOffset() {
    return startLineOffset;
  }

  public int endLine() {
    return endLine;
  }

  public int endLineOffset() {
    return endLineOffset;
  }

}
