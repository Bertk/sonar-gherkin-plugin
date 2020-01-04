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
package org.sonar.gherkin.tree.impl;

import com.google.common.collect.Iterators;
import org.sonar.plugins.gherkin.api.tree.DescriptionTree;
import org.sonar.plugins.gherkin.api.tree.FeatureDeclarationTree;
import org.sonar.plugins.gherkin.api.tree.NameTree;
import org.sonar.plugins.gherkin.api.tree.PrefixTree;
import org.sonar.plugins.gherkin.api.tree.SyntaxToken;
import org.sonar.plugins.gherkin.api.tree.TagTree;
import org.sonar.plugins.gherkin.api.tree.Tree;
import org.sonar.plugins.gherkin.api.visitors.DoubleDispatchVisitor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeatureDeclarationTreeImpl extends GherkinTree implements FeatureDeclarationTree {

  private final List<TagTree> tags;
  private final PrefixTree prefix;
  private final SyntaxToken colon;
  private final NameTree name;
  private final DescriptionTree description;

  public FeatureDeclarationTreeImpl(@Nullable List<TagTree> tags, PrefixTree prefix, SyntaxToken colon, @Nullable NameTree name, @Nullable DescriptionTree description) {
    this.tags = tags != null ? tags : new ArrayList<>();
    this.prefix = prefix;
    this.colon = colon;
    this.name = name;
    this.description = description;
  }

  @Override
  public Kind getKind() {
    return Kind.FEATURE_DECLARATION;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      tags.iterator(),
      Iterators.forArray(prefix, colon, name, description));
  }

  @Override
  public List<TagTree> tags() {
    return tags;
  }

  @Override
  public PrefixTree prefix() {
    return prefix;
  }

  @Override
  public SyntaxToken colon() {
    return colon;
  }

  @Override
  @Nullable
  public NameTree name() {
    return name;
  }

  @Override
  @Nullable
  public DescriptionTree description() {
    return description;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitFeatureDeclaration(this);
  }

}
