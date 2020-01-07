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
package org.sonar.gherkin;

import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.fest.assertions.Assertions.assertThat;

public class MyGherkinCustomRulesPluginTest {

  public static final Version LTS_VERSION = Version.create(7, 9);
  
  @Test
  public void should_get_the_right_version() {
    Plugin.Context context = new Plugin.Context(SonarRuntimeImpl.forSonarQube(LTS_VERSION,  SonarQubeSide.SERVER, SonarEdition.COMMUNITY));
    new MyGherkinCustomRulesPlugin().define(context);
    assertThat(context.getSonarQubeVersion().major()).isEqualTo(7);
    assertThat(context.getSonarQubeVersion().minor()).isEqualTo(9);
  }

  @Test
  public void should_get_the_right_number_of_extensions() {
    Plugin.Context context = new Plugin.Context(SonarRuntimeImpl.forSonarQube(LTS_VERSION,  SonarQubeSide.SERVER, SonarEdition.COMMUNITY));
    new MyGherkinCustomRulesPlugin().define(context);
    assertThat(context.getExtensions()).hasSize(1);
  }

}
