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

import java.util.Arrays;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.gherkin.checks.AddCommonGivenStepsToBackgroundCheck;
import org.sonar.gherkin.checks.AllStepTypesInScenarioCheck;
import org.sonar.gherkin.checks.AllowedTagsCheck;
import org.sonar.gherkin.checks.BOMCheck;
import org.sonar.gherkin.checks.CommentConventionCheck;
import org.sonar.gherkin.checks.CommentRegularExpressionCheck;
import org.sonar.gherkin.checks.DuplicatedFeatureNamesCheck;
import org.sonar.gherkin.checks.DuplicatedScenarioNamesCheck;
import org.sonar.gherkin.checks.DuplicatedStepsCheck;
import org.sonar.gherkin.checks.EndLineCharactersCheck;
import org.sonar.gherkin.checks.FileNameCheck;
import org.sonar.gherkin.checks.FixmeTagPresenceCheck;
import org.sonar.gherkin.checks.GivenStepRegularExpressionCheck;
import org.sonar.gherkin.checks.IncompleteExamplesTableCheck;
import org.sonar.gherkin.checks.IndentationCheck;
import org.sonar.gherkin.checks.MaxNumberScenariosCheck;
import org.sonar.gherkin.checks.MaxNumberStepsCheck;
import org.sonar.gherkin.checks.MissingDataTableColumnCheck;
import org.sonar.gherkin.checks.MissingFeatureDescriptionCheck;
import org.sonar.gherkin.checks.MissingFeatureNameCheck;
import org.sonar.gherkin.checks.MissingNewlineAtEndOfFileCheck;
import org.sonar.gherkin.checks.MissingScenarioNameCheck;
import org.sonar.gherkin.checks.NoFeatureCheck;
import org.sonar.gherkin.checks.NoScenarioCheck;
import org.sonar.gherkin.checks.NoStepCheck;
import org.sonar.gherkin.checks.NoTagExamplesCheck;
import org.sonar.gherkin.checks.OneSingleWhenPerScenarioCheck;
import org.sonar.gherkin.checks.OnlyGivenStepsInBackgroundCheck;
import org.sonar.gherkin.checks.ParsingErrorCheck;
import org.sonar.gherkin.checks.SameFeatureLanguageCheck;
import org.sonar.gherkin.checks.SpellingCheck;
import org.sonar.gherkin.checks.StarStepPrefixCheck;
import org.sonar.gherkin.checks.StepOfUnknownTypeCheck;
import org.sonar.gherkin.checks.StepSentenceLengthCheck;
import org.sonar.gherkin.checks.StepsRightOrderCheck;
import org.sonar.gherkin.checks.TabCharacterCheck;
import org.sonar.gherkin.checks.TagNameCheck;
import org.sonar.gherkin.checks.TagRightLevelCheck;
import org.sonar.gherkin.checks.ThenStepRegularExpressionCheck;
import org.sonar.gherkin.checks.TodoTagPresenceCheck;
import org.sonar.gherkin.checks.TrailingWhitespaceCheck;
import org.sonar.gherkin.checks.UnusedVariableCheck;
import org.sonar.gherkin.checks.UseAndButCheck;
import org.sonar.gherkin.checks.UselessTagCheck;
import org.sonar.gherkin.checks.WhenStepRegularExpressionCheck;
import org.sonar.gherkin.checks.WordingBusinessLevelCheck;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

public class GherkinRulesDefinition implements RulesDefinition {

  public static final String REPOSITORY_KEY = "gherkin";
  private static final String REPOSITORY_NAME = "SonarQube";

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(REPOSITORY_KEY, GherkinLanguage.KEY)
      .setName(REPOSITORY_NAME);

    new AnnotationBasedRulesDefinition(repository, GherkinLanguage.KEY).addRuleClasses(false, Arrays.asList(getChecks()));
    repository.done();
  }

  @SuppressWarnings("rawtypes")
  public static Class[] getChecks() {
    return new Class[] {
      AddCommonGivenStepsToBackgroundCheck.class,
      AllowedTagsCheck.class,
      AllStepTypesInScenarioCheck.class,
      BOMCheck.class,
      CommentConventionCheck.class,
      CommentRegularExpressionCheck.class,
      SameFeatureLanguageCheck.class,
      DuplicatedFeatureNamesCheck.class,
      DuplicatedScenarioNamesCheck.class,
      DuplicatedStepsCheck.class,
      EndLineCharactersCheck.class,
      FileNameCheck.class,
      FixmeTagPresenceCheck.class,
      GivenStepRegularExpressionCheck.class,
      IncompleteExamplesTableCheck.class,
      IndentationCheck.class,
      MaxNumberScenariosCheck.class,
      MaxNumberStepsCheck.class,
      MissingDataTableColumnCheck.class,
      MissingFeatureDescriptionCheck.class,
      MissingFeatureNameCheck.class,
      MissingNewlineAtEndOfFileCheck.class,
      MissingScenarioNameCheck.class,
      NoFeatureCheck.class,
      NoScenarioCheck.class,
      NoStepCheck.class,
      NoTagExamplesCheck.class,
      OneSingleWhenPerScenarioCheck.class,
      OnlyGivenStepsInBackgroundCheck.class,
      ParsingErrorCheck.class,
      SpellingCheck.class,
      StarStepPrefixCheck.class,
      StepOfUnknownTypeCheck.class,
      StepSentenceLengthCheck.class,
      StepsRightOrderCheck.class,
      TabCharacterCheck.class,
      TagNameCheck.class,
      TagRightLevelCheck.class,
      ThenStepRegularExpressionCheck.class,
      TodoTagPresenceCheck.class,
      TrailingWhitespaceCheck.class,
      UnusedVariableCheck.class,
      UseAndButCheck.class,
      UselessTagCheck.class,
      WhenStepRegularExpressionCheck.class,
      WordingBusinessLevelCheck.class
    };
  }

}
