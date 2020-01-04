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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleDescriptionsGenerator {

  private final Map<String, String> tags = ImmutableMap.<String, String>builder()
    .put("[[allForbiddenWords]]", generateForbiddenWordsHtmlTable())
    .build();

  public void generateHtmlRuleDescription(String templatePath, String outputPath) {
    try (OutputStream fileOutputStream = java.nio.file.Files.newOutputStream(Paths.get(outputPath))) {
      Writer writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
      writer.write(replaceTags(FileUtils.readFileToString(new File(templatePath), "UTF-8")));
      writer.flush();
      writer.close();
    } catch (IOException e) {
      throw new IllegalStateException("Could not generate the HTML description.", e);
    }
  }

  private String generateForbiddenWordsHtmlTable() {
    StringBuilder html = new StringBuilder("<table style=\"border: 0;\">\n");
    List<List<String>> subLists = Lists.partition(Arrays.stream(WordingBusinessLevelCheck.FORBIDDEN_WORDS).sorted().collect(Collectors.toList()), 3);
    for (List<String> subList : subLists) {
      html.append("<tr>");
      for (String word : subList) {
        html.append("<td style=\"border: 0; \">");
        html.append(word);
        html.append("</td>\n");
      }
      html.append("</tr>");
    }
    html.append("</table>\n");
    return html.toString();
  }

  private String replaceTags(String rawDescription) {
    String description = rawDescription;
    for (Map.Entry<String, String> tag : tags.entrySet()) {
      description = description.replace(tag.getKey(), tag.getValue());
    }
    return description;
  }

}
