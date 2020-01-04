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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PreciseIssue implements Issue {

  private final GherkinCheck check;
  private Double cost;
  private final IssueLocation primaryLocation;
  private final List<IssueLocation> secondaryLocations;

  public PreciseIssue(GherkinCheck check, IssueLocation primaryLocation) {
    this.check = check;
    this.primaryLocation = primaryLocation;
    this.secondaryLocations = new ArrayList<>();
    this.cost = 0.0;
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
  public PreciseIssue cost(double cost) {
    this.cost = cost;
    return this;
  }

  public IssueLocation primaryLocation() {
    return primaryLocation;
  }
  
  public List<IssueLocation> secondaryLocations() {
    return secondaryLocations;
  }

  public PreciseIssue secondary(Tree tree, String message) {
    secondaryLocations.add(new IssueLocation(tree, message));
    return this;
  }

}
