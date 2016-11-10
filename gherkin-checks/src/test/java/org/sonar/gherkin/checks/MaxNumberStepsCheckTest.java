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
package org.sonar.gherkin.checks;

import org.junit.Test;
import org.sonar.gherkin.checks.verifier.GherkinCheckVerifier;

public class MaxNumberStepsCheckTest {

  @Test
  public void test_with_no_background() {
    GherkinCheckVerifier.verify(new MaxNumberStepsCheck(), CheckTestUtils.getTestFile("max-number-steps/max-number-steps-no-background.feature"));
  }

  @Test
  public void test_with_background() {
    GherkinCheckVerifier.verify(new MaxNumberStepsCheck(), CheckTestUtils.getTestFile("max-number-steps/max-number-steps-background.feature"));
  }

  @Test
  public void test_custom_threshold() {
    MaxNumberStepsCheck check = new MaxNumberStepsCheck();
    check.setThreshold(5);
    GherkinCheckVerifier.verify(check, CheckTestUtils.getTestFile("max-number-steps/max-number-steps-custom-threshold.feature"));
  }

}