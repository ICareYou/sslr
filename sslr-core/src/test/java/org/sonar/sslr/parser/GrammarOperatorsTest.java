/*
 * SonarSource Language Recognizer
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.sslr.parser;

import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.api.Trivia.TriviaKind;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.sslr.internal.matchers.EndOfInputMatcher;
import org.sonar.sslr.internal.matchers.FirstOfMatcher;
import org.sonar.sslr.internal.matchers.Matcher;
import org.sonar.sslr.internal.matchers.NothingMatcher;
import org.sonar.sslr.internal.matchers.OneOrMoreMatcher;
import org.sonar.sslr.internal.matchers.OptionalMatcher;
import org.sonar.sslr.internal.matchers.PatternMatcher;
import org.sonar.sslr.internal.matchers.SequenceMatcher;
import org.sonar.sslr.internal.matchers.StringMatcher;
import org.sonar.sslr.internal.matchers.TestMatcher;
import org.sonar.sslr.internal.matchers.TestNotMatcher;
import org.sonar.sslr.internal.matchers.TokenMatcher;
import org.sonar.sslr.internal.matchers.TriviaMatcher;
import org.sonar.sslr.internal.matchers.ZeroOrMoreMatcher;

import java.lang.reflect.Constructor;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GrammarOperatorsTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void test() {
    Matcher subMatcher = mock(Matcher.class);
    assertThat(GrammarOperators.sequence(subMatcher)).isSameAs(subMatcher);
    assertThat(GrammarOperators.sequence(subMatcher, subMatcher)).isInstanceOf(SequenceMatcher.class);
    assertThat(GrammarOperators.sequence("foo")).isInstanceOf(StringMatcher.class);
    assertThat(GrammarOperators.sequence('f')).isInstanceOf(StringMatcher.class);

    assertThat(GrammarOperators.firstOf(subMatcher)).isSameAs(subMatcher);
    assertThat(GrammarOperators.firstOf(subMatcher, subMatcher)).isInstanceOf(FirstOfMatcher.class);

    assertThat(GrammarOperators.optional(subMatcher)).isInstanceOf(OptionalMatcher.class);

    assertThat(GrammarOperators.oneOrMore(subMatcher)).isInstanceOf(OneOrMoreMatcher.class);

    assertThat(GrammarOperators.zeroOrMore(subMatcher)).isInstanceOf(ZeroOrMoreMatcher.class);

    assertThat(GrammarOperators.next(subMatcher)).isInstanceOf(TestMatcher.class);

    assertThat(GrammarOperators.nextNot(subMatcher)).isInstanceOf(TestNotMatcher.class);

    assertThat(GrammarOperators.regexp("foo")).isInstanceOf(PatternMatcher.class);

    assertThat(GrammarOperators.endOfInput()).isInstanceOf(EndOfInputMatcher.class);

    assertThat(GrammarOperators.nothing()).isInstanceOf(NothingMatcher.class);

    assertThat(GrammarOperators.token(mock(TokenType.class), subMatcher)).isInstanceOf(TokenMatcher.class);
  }

  @Test
  public void test_commentTrivia() {
    Matcher subMatcher = mock(Matcher.class);
    Object matcher = GrammarOperators.commentTrivia(subMatcher);
    assertThat(matcher).isInstanceOf(TriviaMatcher.class);
    assertThat(((TriviaMatcher) matcher).getTriviaKind()).isEqualTo(TriviaKind.COMMENT);
  }

  @Test
  public void test_skippedTrivia() {
    Matcher subMatcher = mock(Matcher.class);
    Object matcher = GrammarOperators.skippedTrivia(subMatcher);
    assertThat(matcher).isInstanceOf(TriviaMatcher.class);
    assertThat(((TriviaMatcher) matcher).getTriviaKind()).isEqualTo(TriviaKind.SKIPPED_TEXT);
  }

  @Test
  public void illegal_argument() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("java.lang.Object");
    GrammarOperators.sequence(new Object());
  }

  @Test
  public void private_constructor() throws Exception {
    Constructor constructor = GrammarOperators.class.getDeclaredConstructor();
    assertThat(constructor.isAccessible()).isFalse();
    constructor.setAccessible(true);
    constructor.newInstance();
  }

}
