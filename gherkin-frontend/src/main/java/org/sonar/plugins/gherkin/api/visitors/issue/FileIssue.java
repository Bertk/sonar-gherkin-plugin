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

import org.sonar.plugins.gherkin.api.GherkinCheck;
import org.sonar.plugins.gherkin.api.tree.Tree;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class FileIssue implements Issue {

  private final GherkinCheck check;
  private final URI uri;
  private Double cost;
  private final String message;
  private final List<IssueLocation> secondaryLocations;

  public FileIssue(URI uri, GherkinCheck check, String message) {
    this.uri = uri;
    this.check = check;
    this.message = message;
    this.cost = 0.0;
    this.secondaryLocations = new ArrayList<>();
  }

  public String message() {
    return message;
  }

  @Override
  public GherkinCheck check() {
    return check;
  }

  @Nullable
  @Override
  public Double cost() {
    return cost;
  }

  @Override
  public Issue cost(double cost) {
    this.cost = cost;
    return this;
  }
  
  public URI uri() {
    return uri;
  }

  public List<IssueLocation> secondaryLocations() {
    return secondaryLocations;
  }

  public FileIssue secondary(Tree tree, String message) {
    secondaryLocations.add(new IssueLocation(uri, tree, message));
    return this;
  }

  public FileIssue secondary(URI uri, Tree tree, String message) {
    secondaryLocations.add(new IssueLocation(uri, tree, message));
    return this;
  }

}
