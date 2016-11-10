/*
 * SonarQube Gherkin Analyzer
 * Copyright (C) 2016-2016 David RACODON
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
package org.sonar.gherkin.tree.impl;

import org.sonar.plugins.gherkin.api.tree.SyntaxToken;
import org.sonar.plugins.gherkin.api.tree.TableTree;
import org.sonar.plugins.gherkin.api.tree.Tree;
import org.sonar.plugins.gherkin.api.visitors.DoubleDispatchVisitor;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TableTreeImpl extends GherkinTree implements TableTree {

  private final List<SyntaxToken> rows;

  public TableTreeImpl(List<SyntaxToken> rows) {
    this.rows = rows;
  }

  @Override
  public Kind getKind() {
    return Kind.TABLE;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return rows.stream().map(t -> (Tree) t).collect(Collectors.toList()).iterator();
  }

  @Override
  public List<SyntaxToken> rows() {
    return rows;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitTable(this);
  }

}