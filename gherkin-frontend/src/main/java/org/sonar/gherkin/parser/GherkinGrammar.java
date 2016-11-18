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
package org.sonar.gherkin.parser;

import com.sonar.sslr.api.typed.GrammarBuilder;
import org.sonar.gherkin.tree.impl.InternalSyntaxToken;
import org.sonar.plugins.gherkin.api.tree.*;

public class GherkinGrammar {

  private final GrammarBuilder<InternalSyntaxToken> b;
  private final TreeFactory f;

  public GherkinGrammar(GrammarBuilder<InternalSyntaxToken> b, TreeFactory f) {
    this.b = b;
    this.f = f;
  }

  public GherkinDocumentTree GHERKIN_DOCUMENT() {
    return b.<GherkinDocumentTree>nonterminal(GherkinLexicalGrammar.GHERKIN_DOCUMENT).is(
      f.gherkinDocument(
        b.optional(b.token(GherkinLexicalGrammar.BOM)),
        b.optional(FEATURE()),
        b.token(GherkinLexicalGrammar.EOF)));
  }

  public FeatureTree FEATURE() {
    return b.<FeatureTree>nonterminal(GherkinLexicalGrammar.FEATURE).is(
      f.feature(
        FEATURE_DECLARATION(),
        b.optional(BACKGROUND()),
        b.zeroOrMore(
          b.firstOf(
            SCENARIO_OUTLINE(),
            SCENARIO()))));
  }

  public FeatureDeclarationTree FEATURE_DECLARATION() {
    return b.<FeatureDeclarationTree>nonterminal(GherkinLexicalGrammar.FEATURE_DECLARATION).is(
      f.featureDeclaration(
        b.zeroOrMore(TAG()),
        FEATURE_PREFIX(),
        b.token(GherkinLexicalGrammar.COLON),
        NAME(),
        b.optional(DESCRIPTION())));
  }

  public BackgroundTree BACKGROUND() {
    return b.<BackgroundTree>nonterminal(GherkinLexicalGrammar.BACKGROUND).is(
      f.background(
        BACKGROUND_PREFIX(),
        b.token(GherkinLexicalGrammar.COLON),
        b.optional(DESCRIPTION()),
        b.zeroOrMore(STEP())));
  }

  public ScenarioTree SCENARIO() {
    return b.<ScenarioTree>nonterminal(GherkinLexicalGrammar.SCENARIO).is(
      f.scenario(
        b.zeroOrMore(TAG()),
        SCENARIO_PREFIX(),
        b.token(GherkinLexicalGrammar.COLON),
        NAME(),
        b.optional(DESCRIPTION()),
        b.zeroOrMore(STEP())));
  }

  public ScenarioOutlineTree SCENARIO_OUTLINE() {
    return b.<ScenarioOutlineTree>nonterminal(GherkinLexicalGrammar.SCENARIO_OUTLINE).is(
      f.scenarioOutline(
        b.zeroOrMore(TAG()),
        SCENARIO_OUTLINE_PREFIX(),
        b.token(GherkinLexicalGrammar.COLON),
        NAME(),
        b.optional(DESCRIPTION()),
        b.zeroOrMore(STEP()),
        EXAMPLES()));
  }

  public ExamplesTree EXAMPLES() {
    return b.<ExamplesTree>nonterminal(GherkinLexicalGrammar.EXAMPLES).is(
      f.examples(
        b.zeroOrMore(TAG()),
        EXAMPLES_PREFIX(),
        b.token(GherkinLexicalGrammar.COLON),
        b.optional(DESCRIPTION()),
        b.optional(TABLE())));
  }

  public StepTree STEP() {
    return b.<StepTree>nonterminal(GherkinLexicalGrammar.STEP).is(
      f.step(
        STEP_PREFIX(),
        b.token(GherkinLexicalGrammar.STEP_SENTENCE),
        b.optional(
          b.firstOf(
            TABLE(),
            DOC_STRING()))));
  }

  public TagTree TAG() {
    return b.<TagTree>nonterminal(GherkinLexicalGrammar.TAG).is(
      f.tag(
        b.token(GherkinLexicalGrammar.TAG_PREFIX),
        b.token(GherkinLexicalGrammar.TAG_LITERAL)));
  }

  public DescriptionTree DESCRIPTION() {
    return b.<DescriptionTree>nonterminal(GherkinLexicalGrammar.DESCRIPTION).is(
      f.description(
        b.oneOrMore(b.token(GherkinLexicalGrammar.DESCRIPTION_SENTENCE))));
  }

  public FeaturePrefixTree FEATURE_PREFIX() {
    return b.<FeaturePrefixTree>nonterminal(GherkinLexicalGrammar.FEATURE_PREFIX).is(
      f.featurePrefix(b.token(GherkinLexicalGrammar.FEATURE_KEYWORD)));
  }

  public BackgroundPrefixTree BACKGROUND_PREFIX() {
    return b.<BackgroundPrefixTree>nonterminal(GherkinLexicalGrammar.BACKGROUND_PREFIX).is(
      f.backgroundPrefix(b.token(GherkinLexicalGrammar.BACKGROUND_KEYWORD)));
  }

  public ScenarioPrefixTree SCENARIO_PREFIX() {
    return b.<ScenarioPrefixTree>nonterminal(GherkinLexicalGrammar.SCENARIO_PREFIX).is(
      f.scenarioPrefix(b.token(GherkinLexicalGrammar.SCENARIO_KEYWORD)));
  }

  public ScenarioOutlinePrefixTree SCENARIO_OUTLINE_PREFIX() {
    return b.<ScenarioOutlinePrefixTree>nonterminal(GherkinLexicalGrammar.SCENARIO_OUTLINE_PREFIX).is(
      f.scenarioOutlinePrefix(b.token(GherkinLexicalGrammar.SCENARIO_OUTLINE_KEYWORD)));
  }

  public ExamplesPrefixTree EXAMPLES_PREFIX() {
    return b.<ExamplesPrefixTree>nonterminal(GherkinLexicalGrammar.EXAMPLES_PREFIX).is(
      f.examplesPrefix(b.token(GherkinLexicalGrammar.EXAMPLES_KEYWORD)));
  }

  public StepPrefixTree STEP_PREFIX() {
    return b.<StepPrefixTree>nonterminal(GherkinLexicalGrammar.STEP_PREFIX).is(
      f.stepPrefix(b.token(GherkinLexicalGrammar.STEP_KEYWORD)));
  }

  public NameTree NAME() {
    return b.<NameTree>nonterminal(GherkinLexicalGrammar.NAME).is(
      f.name(b.token(GherkinLexicalGrammar.NAME_LITERAL)));
  }

  public DocStringTree DOC_STRING() {
    return b.<DocStringTree>nonterminal(GherkinLexicalGrammar.DOC_STRING).is(
      f.docString(
        b.token(GherkinLexicalGrammar.DOC_STRING_PREFIX),
        b.optional(b.token(GherkinLexicalGrammar.DOC_STRING_CONTENT_TYPE)),
        b.zeroOrMore(b.token(GherkinLexicalGrammar.DOC_STRING_DATA_ROW)),
        b.token(GherkinLexicalGrammar.DOC_STRING_SUFFIX)));
  }

  public TableTree TABLE() {
    return b.<TableTree>nonterminal(GherkinLexicalGrammar.TABLE).is(
      f.table(
        b.oneOrMore(b.token(GherkinLexicalGrammar.TABLE_DATA_ROW))));
  }

}
