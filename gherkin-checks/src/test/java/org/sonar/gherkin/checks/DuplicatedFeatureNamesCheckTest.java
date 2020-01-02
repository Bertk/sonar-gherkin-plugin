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
package org.sonar.gherkin.checks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.gherkin.parser.GherkinParserBuilder;
import org.sonar.gherkin.tree.impl.InternalSyntaxToken;
import org.sonar.gherkin.tree.impl.NameTreeImpl;
import org.sonar.gherkin.visitors.GherkinVisitorContext;
import org.sonar.plugins.gherkin.api.GherkinCheck;
import org.sonar.plugins.gherkin.api.tree.GherkinDocumentTree;
import org.sonar.plugins.gherkin.api.tree.NameTree;

import com.google.common.collect.Lists;

public class DuplicatedFeatureNamesCheckTest {

  @Test
  public void analyze_one_single_file() throws FileNotFoundException, IOException {
    String relativePath = "duplicated-feature-names/feature.feature";
    DuplicatedFeatureNamesCheck check = new DuplicatedFeatureNamesCheck();
    scanFile(check, relativePath);

    Assert.assertNotNull(check.getNames());
    Assert.assertEquals(1, check.getNames().size());

    Assert.assertTrue(check.getNames().containsKey("My feature blabla"));
    Assert.assertNotNull(check.getNames().get("My feature blabla"));
    Assert.assertEquals(1, check.getNames().get("My feature blabla").size());
    Assert.assertEquals(CheckTestUtils.getTestFilePath(relativePath), check.getNames().get("My feature blabla").get(0).getInputFile().uri());
  }

  @Test
  public void analyze_several_files() throws FileNotFoundException, IOException {
    String relativePath2 = "duplicated-feature-names/feature2.feature";
    String relativePath = "duplicated-feature-names/feature.feature";
    DuplicatedFeatureNamesCheck check = new DuplicatedFeatureNamesCheck();

    Map<String, List<FileNameTree>> names = new HashMap<>();

    NameTree nameTree1 = new NameTreeImpl(new InternalSyntaxToken(2, 1, "My feature blabla", new ArrayList<>(), false, false));
    NameTree nameTree2 = new NameTreeImpl(new InternalSyntaxToken(4, 1, "Blabla", new ArrayList<>(), false, false));

    names.put("My feature blabla", Lists.newArrayList(new FileNameTree(CheckTestUtils.getTestInputFile(relativePath2), nameTree1)));
    names.put("abc", Lists.newArrayList(new FileNameTree(CheckTestUtils.getTestInputFile(relativePath2), nameTree2)));
    check.setNames(names);

    scanFile(check, relativePath);

    Assert.assertNotNull(check.getNames());
    Assert.assertEquals(2, check.getNames().size());

    Assert.assertTrue(check.getNames().containsKey("My feature blabla"));
    Assert.assertTrue(check.getNames().containsKey("abc"));

    Assert.assertNotNull(check.getNames().get("My feature blabla"));
    Assert.assertNotNull(check.getNames().get("abc"));

    Assert.assertEquals(2, check.getNames().get("My feature blabla").size());
    Assert.assertEquals(1, check.getNames().get("abc").size());

    Assert.assertEquals(CheckTestUtils.getTestFilePath(relativePath2), check.getNames().get("My feature blabla").get(0).getInputFile().uri());
    Assert.assertEquals(CheckTestUtils.getTestFilePath(relativePath), check.getNames().get("My feature blabla").get(1).getInputFile().uri());

    Assert.assertEquals(CheckTestUtils.getTestFilePath(relativePath2), check.getNames().get("abc").get(0).getInputFile().uri());
  }

  private void scanFile(GherkinCheck check, String fileName) throws FileNotFoundException, IOException {

    InputFile inputFile = CheckTestUtils.getTestInputFile(fileName);
    
    GherkinDocumentTree gherkinDocument = (GherkinDocumentTree) GherkinParserBuilder
        .createTestParser(StandardCharsets.UTF_8).parse(inputFile.contents());

    GherkinVisitorContext context = new GherkinVisitorContext(gherkinDocument, inputFile);

    check.scanFile(context);
  }

}
