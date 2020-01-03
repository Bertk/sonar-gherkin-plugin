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

import java.nio.charset.StandardCharsets;

import TestUtils.TestUtils;

import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.gherkin.api.tree.GherkinDocumentTree;

import static org.fest.assertions.Assertions.assertThat;

public class GherkinDocumentTreeFrTest extends GherkinTreeTest {

  public GherkinDocumentTreeFrTest() {
    super(GherkinLexicalGrammar.GHERKIN_DOCUMENT, "fr");
  }

  @Test
  public void gherkinDocumentFr() throws Exception {
    GherkinDocumentTree tree;

    tree = checkParsed(TestUtils.getTestInputFile("parser/parse-fr.feature", StandardCharsets.UTF_8));
    assertThat(tree.hasByteOrderMark()).isEqualTo(false);
    assertThat(tree.feature()).isNotNull();
    assertThat(tree.feature().scenarioOutlines()).hasSize(1);
    assertThat(tree.feature().scenarios()).hasSize(2);
    assertThat(tree.feature().background()).isNotNull();
    assertThat(tree.languageDeclaration()).isNotNull();
    assertThat(tree.language()).isEqualTo("fr");
  }

  private GherkinDocumentTree checkParsed(InputFile inputFile) throws Exception {
    GherkinDocumentTree tree = (GherkinDocumentTree) parser().parse(inputFile.contents());
    assertThat(tree).isNotNull();
    assertThat(tree.languageDeclaration()).isNotNull();
    assertThat(tree.language()).isNotNull();
    assertThat(tree.language()).isEqualTo("fr");
    return tree;
  }

}
