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
package org.sonar.plugins.gherkin;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.sonar.api.server.profile.BuiltInQualityProfileAnnotationLoader;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import com.google.common.io.Resources;
import com.google.gson.Gson;

public class GherkinProfile implements BuiltInQualityProfilesDefinition {

  private static final String SONARQUBE_WAY_PROFILE_NAME = "SonarQube Way";

  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(SONARQUBE_WAY_PROFILE_NAME, GherkinLanguage.KEY);

    Profile jsonProfile = readProfile();
    BuiltInQualityProfileAnnotationLoader annotationProfileLoader = new BuiltInQualityProfileAnnotationLoader();
    annotationProfileLoader.load(profile, GherkinRulesDefinition.REPOSITORY_KEY, GherkinRulesDefinition.getChecks());
    for (String key : jsonProfile.ruleKeys) {
      profile.activateRule(GherkinRulesDefinition.REPOSITORY_KEY, key);
    }

    profile.setDefault(true);

    profile.done();
  }

  static Profile readProfile() {
    URL resource = GherkinProfile.class.getResource("/org/sonar/l10n/gherkin/rules/gherkin/Sonar_way_profile.json");
    return new Gson().fromJson(readResource(resource), Profile.class);
  }

  private static String readResource(URL resource) {
    try {
      return Resources.toString(resource, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read: " + resource, e);
    }
  }

  static class Profile {
    String name;
    List<String> ruleKeys;
  }
}
