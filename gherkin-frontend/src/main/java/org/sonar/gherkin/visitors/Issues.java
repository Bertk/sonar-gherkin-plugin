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
package org.sonar.gherkin.visitors;

import org.sonar.plugins.gherkin.api.GherkinCheck;
import org.sonar.plugins.gherkin.api.tree.SyntaxToken;
import org.sonar.plugins.gherkin.api.tree.Tree;
import org.sonar.plugins.gherkin.api.visitors.issue.FileIssue;
import org.sonar.plugins.gherkin.api.visitors.issue.Issue;
import org.sonar.plugins.gherkin.api.visitors.issue.IssueLocation;
import org.sonar.plugins.gherkin.api.visitors.issue.LineIssue;
import org.sonar.plugins.gherkin.api.visitors.issue.PreciseIssue;

import java.util.ArrayList;
import java.util.List;

public class Issues {

  private List<Issue> issueList;
  private final GherkinCheck check;

  public Issues(GherkinCheck check) {
    this.check = check;
    this.issueList = new ArrayList<>();
  }

  public PreciseIssue addPreciseIssue(Tree tree, String message) {
    PreciseIssue issue = new PreciseIssue(check, new IssueLocation(tree, message));
    issueList.add(issue);
    return issue;
  }

  public PreciseIssue addPreciseIssue(SyntaxToken token, int startOffset, int endOffset, String message) {
    PreciseIssue issue = new PreciseIssue(check, new IssueLocation(token, startOffset, endOffset, message));
    issueList.add(issue);
    return issue;
  }

  public FileIssue addFileIssue(String message) {
    FileIssue issue = new FileIssue(check, message);
    issueList.add(issue);
    return issue;
  }

  public LineIssue addLineIssue(int line, String message) {
    LineIssue issue = new LineIssue(check, line, message);
    issueList.add(issue);
    return issue;
  }

  public List<Issue> getList() {
    return issueList;
  }

  public void reset() {
    issueList.clear();
  }

}
