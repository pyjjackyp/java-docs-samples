/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.automl;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.TestCase.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GetModelEvaluationTest {
  private static final String PROJECT_ID = System.getenv("AUTOML_PROJECT_ID");
  private static final String MODEL_ID = System.getenv("ENTITY_EXTRACTION_MODEL_ID");
  private String modelEvaluationId;
  private ByteArrayOutputStream bout;
  private PrintStream out;

  private static void requireEnvVar(String varName) {
    assertNotNull(
        System.getenv(varName),
        "Environment variable '" + varName + "' is required to perform these tests.");
  }

  @BeforeClass
  public static void checkRequirements() {
    requireEnvVar("GOOGLE_APPLICATION_CREDENTIALS");
    requireEnvVar("AUTOML_PROJECT_ID");
    requireEnvVar("ENTITY_EXTRACTION_MODEL_ID");
  }

  @Before
  public void setUp() throws IOException {
    bout = new ByteArrayOutputStream();
    out = new PrintStream(bout);
    System.setOut(out);

    // Get a model evaluation ID from the List request first to be used in the Get call
    ListModelEvaluations.listModelEvaluations(PROJECT_ID, MODEL_ID);
    String got = bout.toString();
    modelEvaluationId = got.split(MODEL_ID + "/modelEvaluations/")[1].split("\n")[0];
    assertThat(got).contains("Model Evaluation Name:");

    bout = new ByteArrayOutputStream();
    out = new PrintStream(bout);
    System.setOut(out);
  }

  @After
  public void tearDown() {
    System.setOut(null);
  }

  @Test
  public void testGetModelEvaluation() throws IOException {
    GetModelEvaluation.getModelEvaluation(PROJECT_ID, MODEL_ID, modelEvaluationId);
    String got = bout.toString();
    assertThat(got).contains("Model Evaluation Name:");
  }
}
