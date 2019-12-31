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

import org.junit.Test;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import static org.fest.assertions.Assertions.assertThat;
import java.util.List;

public class GherkinProfileTest {

  @Test
  public void should_create_sonarqube_way_profile() {
    GherkinProfile profileDef = new GherkinProfile();
    BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();
    profileDef.define(context);
    
    BuiltInQualityProfilesDefinition.BuiltInQualityProfile profile = context.profile(GherkinLanguage.KEY, "SonarQube Way");

    assertThat(profile.name()).isEqualTo("SonarQube Way");
    assertThat(profile.language()).isEqualTo("gherkin");
    List<BuiltInQualityProfilesDefinition.BuiltInActiveRule> activeRules = profile.rules();
    assertThat(activeRules.size()).as("Expected number of rules in profile").isEqualTo(36);
  }

}
