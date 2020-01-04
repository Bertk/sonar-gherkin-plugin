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
package org.sonar.gherkin.parser;

import com.sonar.sslr.api.typed.Optional;

import java.util.List;

import org.sonar.gherkin.tree.impl.BackgroundPrefixTreeImpl;
import org.sonar.gherkin.tree.impl.BackgroundTreeImpl;
import org.sonar.gherkin.tree.impl.DescriptionTreeImpl;
import org.sonar.gherkin.tree.impl.DocStringTreeImpl;
import org.sonar.gherkin.tree.impl.ExamplesPrefixTreeImpl;
import org.sonar.gherkin.tree.impl.ExamplesTreeImpl;
import org.sonar.gherkin.tree.impl.FeatureDeclarationTreeImpl;
import org.sonar.gherkin.tree.impl.FeaturePrefixTreeImpl;
import org.sonar.gherkin.tree.impl.FeatureTreeImpl;
import org.sonar.gherkin.tree.impl.GherkinDocumentTreeImpl;
import org.sonar.gherkin.tree.impl.LanguageDeclarationTreeImpl;
import org.sonar.gherkin.tree.impl.NameTreeImpl;
import org.sonar.gherkin.tree.impl.ScenarioOutlinePrefixTreeImpl;
import org.sonar.gherkin.tree.impl.ScenarioOutlineTreeImpl;
import org.sonar.gherkin.tree.impl.ScenarioPrefixTreeImpl;
import org.sonar.gherkin.tree.impl.ScenarioTreeImpl;
import org.sonar.gherkin.tree.impl.StepPrefixTreeImpl;
import org.sonar.gherkin.tree.impl.StepSentenceTreeImpl;
import org.sonar.gherkin.tree.impl.StepTreeImpl;
import org.sonar.gherkin.tree.impl.TableTreeImpl;
import org.sonar.gherkin.tree.impl.TagTreeImpl;
import org.sonar.plugins.gherkin.api.tree.BackgroundPrefixTree;
import org.sonar.plugins.gherkin.api.tree.BackgroundTree;
import org.sonar.plugins.gherkin.api.tree.BasicScenarioTree;
import org.sonar.plugins.gherkin.api.tree.DescriptionTree;
import org.sonar.plugins.gherkin.api.tree.DocStringTree;
import org.sonar.plugins.gherkin.api.tree.ExamplesPrefixTree;
import org.sonar.plugins.gherkin.api.tree.ExamplesTree;
import org.sonar.plugins.gherkin.api.tree.FeatureDeclarationTree;
import org.sonar.plugins.gherkin.api.tree.FeaturePrefixTree;
import org.sonar.plugins.gherkin.api.tree.FeatureTree;
import org.sonar.plugins.gherkin.api.tree.GherkinDocumentTree;
import org.sonar.plugins.gherkin.api.tree.LanguageDeclarationTree;
import org.sonar.plugins.gherkin.api.tree.NameTree;
import org.sonar.plugins.gherkin.api.tree.PrefixTree;
import org.sonar.plugins.gherkin.api.tree.ScenarioOutlinePrefixTree;
import org.sonar.plugins.gherkin.api.tree.ScenarioOutlineTree;
import org.sonar.plugins.gherkin.api.tree.ScenarioPrefixTree;
import org.sonar.plugins.gherkin.api.tree.ScenarioTree;
import org.sonar.plugins.gherkin.api.tree.StepPrefixTree;
import org.sonar.plugins.gherkin.api.tree.StepSentenceTree;
import org.sonar.plugins.gherkin.api.tree.StepTree;
import org.sonar.plugins.gherkin.api.tree.SyntaxToken;
import org.sonar.plugins.gherkin.api.tree.TableTree;
import org.sonar.plugins.gherkin.api.tree.TagTree;
import org.sonar.plugins.gherkin.api.tree.Tree;

public class TreeFactory {

  public GherkinDocumentTree gherkinDocument(Optional<SyntaxToken> byteOrderMark, Optional<LanguageDeclarationTree> language, Optional<FeatureTree> feature, SyntaxToken eof) {
    return new GherkinDocumentTreeImpl(byteOrderMark.orNull(), language.orNull(), feature.orNull(), eof);
  }

  public FeatureTree feature(FeatureDeclarationTree featureDeclaration, Optional<BackgroundTree> background, Optional<List<BasicScenarioTree>> allScenarios) {
    return new FeatureTreeImpl(featureDeclaration, background.orNull(), allScenarios.orNull());
  }

  public FeatureDeclarationTree featureDeclaration(Optional<List<TagTree>> tags, PrefixTree prefix, SyntaxToken colon, Optional<NameTree> name, Optional<DescriptionTree> description) {
    return new FeatureDeclarationTreeImpl(tags.orNull(), prefix, colon, name.orNull(), description.orNull());
  }

  public BackgroundTree background(PrefixTree prefix, SyntaxToken colon, Optional<NameTree> name, Optional<DescriptionTree> description, Optional<List<StepTree>> steps) {
    return new BackgroundTreeImpl(prefix, colon, name.orNull(), description.orNull(), steps.orNull());
  }

  public ScenarioTree scenario(Optional<List<TagTree>> tags, PrefixTree prefix, SyntaxToken colon, Optional<NameTree> name, Optional<DescriptionTree> description, Optional<List<StepTree>> steps) {
    return new ScenarioTreeImpl(tags.orNull(), prefix, colon, name.orNull(), description.orNull(), steps.orNull());
  }

  public ScenarioOutlineTree scenarioOutline(Optional<List<TagTree>> tags, PrefixTree prefix, SyntaxToken colon, Optional<NameTree> name, Optional<DescriptionTree> description, Optional<List<StepTree>> steps, List<ExamplesTree> examples) {
    return new ScenarioOutlineTreeImpl(tags.orNull(), prefix, colon, name.orNull(), description.orNull(), steps.orNull(), examples);
  }

  public ExamplesTree examples(Optional<List<TagTree>> tags, PrefixTree prefix, SyntaxToken colon, Optional<NameTree> name, Optional<DescriptionTree> description, Optional<TableTree> table) {
    return new ExamplesTreeImpl(tags.orNull(), prefix, colon, name.orNull(), description.orNull(), table.orNull());
  }

  public StepTree step(PrefixTree prefix, StepSentenceTree sentence, Optional<Tree> data) {
    return new StepTreeImpl(prefix, sentence, data.orNull());
  }

  public TagTree tag(SyntaxToken prefix, SyntaxToken value) {
    return new TagTreeImpl(prefix, value);
  }

  public DescriptionTree featureDescription(List<SyntaxToken> descriptionLines) {
    return new DescriptionTreeImpl(descriptionLines);
  }

  public DescriptionTree scenarioDescription(List<SyntaxToken> descriptionLines) {
    return new DescriptionTreeImpl(descriptionLines);
  }

  public DescriptionTree examplesDescription(List<SyntaxToken> descriptionLines) {
    return new DescriptionTreeImpl(descriptionLines);
  }

  public FeaturePrefixTree featurePrefix(SyntaxToken keyword) {
    return new FeaturePrefixTreeImpl(keyword);
  }

  public BackgroundPrefixTree backgroundPrefix(SyntaxToken keyword) {
    return new BackgroundPrefixTreeImpl(keyword);
  }

  public ScenarioPrefixTree scenarioPrefix(SyntaxToken keyword) {
    return new ScenarioPrefixTreeImpl(keyword);
  }

  public ScenarioOutlinePrefixTree scenarioOutlinePrefix(SyntaxToken keyword) {
    return new ScenarioOutlinePrefixTreeImpl(keyword);
  }

  public ExamplesPrefixTree examplesPrefix(SyntaxToken keyword) {
    return new ExamplesPrefixTreeImpl(keyword);
  }

  public StepPrefixTree stepPrefix(SyntaxToken keyword) {
    return new StepPrefixTreeImpl(keyword);
  }

  public StepSentenceTree stepSentence(SyntaxToken sentence) {
    return new StepSentenceTreeImpl(sentence);
  }

  public NameTree name(SyntaxToken name) {
    return new NameTreeImpl(name);
  }

  public DocStringTree docString(SyntaxToken prefix, Optional<SyntaxToken> contentType, Optional<List<SyntaxToken>> data, SyntaxToken suffix) {
    return new DocStringTreeImpl(prefix, contentType.orNull(), data.orNull(), suffix);
  }

  public TableTree table(List<SyntaxToken> rows) {
    return new TableTreeImpl(rows);
  }

  public LanguageDeclarationTree languageDeclaration(SyntaxToken languageDeclaration) {
    return new LanguageDeclarationTreeImpl(languageDeclaration);
  }

}
