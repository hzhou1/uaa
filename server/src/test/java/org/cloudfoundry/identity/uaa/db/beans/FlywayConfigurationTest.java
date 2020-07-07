package org.cloudfoundry.identity.uaa.db.beans;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.mock.env.MockEnvironment;

@ExtendWith(MockitoExtension.class)
class FlywayConfigurationTest {

  @Mock
  private ConditionContext mockConditionContext;

  private MockEnvironment mockEnvironment;

  @BeforeEach
  void setUp() {
    mockEnvironment = new MockEnvironment();
    when(mockConditionContext.getEnvironment()).thenReturn(mockEnvironment);
  }

  @Test
  void flywayConfiguration_RunsMigrations_WhenTheConfigurationIsNotSet() {
    assertTrue(new FlywayConfiguration.FlywayConfigurationWithMigration.ConfiguredWithMigrations()
        .matches(mockConditionContext, null));
    assertFalse(
        new FlywayConfiguration.FlywayConfigurationWithoutMigrations.ConfiguredWithoutMigrations()
            .matches(mockConditionContext, null));
  }

  @Test
  void flywayConfiguration_RunsMigrations_WhenTheyAreEnabled() {
    mockEnvironment.setProperty("uaa.migrationsEnabled", "true");

    assertTrue(new FlywayConfiguration.FlywayConfigurationWithMigration.ConfiguredWithMigrations()
        .matches(mockConditionContext, null));
    assertFalse(
        new FlywayConfiguration.FlywayConfigurationWithoutMigrations.ConfiguredWithoutMigrations()
            .matches(mockConditionContext, null));
  }

  @Test
  void flywayConfiguration_RunsMigrations_WhenTheyAreDisabled() {
    mockEnvironment.setProperty("uaa.migrationsEnabled", "false");

    assertFalse(new FlywayConfiguration.FlywayConfigurationWithMigration.ConfiguredWithMigrations()
        .matches(mockConditionContext, null));
    assertTrue(
        new FlywayConfiguration.FlywayConfigurationWithoutMigrations.ConfiguredWithoutMigrations()
            .matches(mockConditionContext, null));
  }

  @Test
  void flywayConfiguration_RunsMigration_WhenInvalidConfiguration() {
    mockEnvironment.setProperty("uaa.migrationsEnabled", "bogus");

    assertTrue(new FlywayConfiguration.FlywayConfigurationWithMigration.ConfiguredWithMigrations()
        .matches(mockConditionContext, null));
    assertFalse(
        new FlywayConfiguration.FlywayConfigurationWithoutMigrations.ConfiguredWithoutMigrations()
            .matches(mockConditionContext, null));
  }
}