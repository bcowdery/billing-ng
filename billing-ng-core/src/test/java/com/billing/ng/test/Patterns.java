package com.billing.ng.test;

import org.hamcrest.text.pattern.PatternMatcher;

import static org.hamcrest.text.pattern.Patterns.*;

/**
 * @author Brian Cowdery
 * @since 26-04-2010
 */
public class Patterns {
    public static final PatternMatcher ALPHANUMERIC
            = new PatternMatcher(
                oneOrMore(
                    either(anyCharacterIn("0-9"),
                            anyCharacterIn("A-Z"),
                            anyCharacterIn("a-z")
                    )
                )
            );

    public static final PatternMatcher URLSAFE_TOKEN
            = new PatternMatcher(
                oneOrMore(
                    either(anyCharacterIn("0-9"),
                            anyCharacterIn("A-Z"),
                            anyCharacterIn("a-z"),
                            text("-"),
                            text("_")
                    )
                )
            );
}
