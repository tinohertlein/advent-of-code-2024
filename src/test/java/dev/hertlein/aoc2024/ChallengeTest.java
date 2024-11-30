package dev.hertlein.aoc2024;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All input files <code>(src/test/resources/*-challenge.txt)</code> are excluded from the repository with <code>.gitignore</code> –
 * they should not be posted publicly, as <a href="https://twitter.com/ericwastl/status/1465805354214830081">Eric Wastl requested for</a>.
 * <p>
 * As a consequence, challenge tests are disabled in public CI/CD environments like GitHub Actions by setting env variable below to <code>true</code.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test
@DisabledIfEnvironmentVariable(named = "DISABLE_TESTS_FOR_CHALLENGES", matches = "true")
public @interface ChallengeTest {
}
