public boolean match(Object... patterns) {
    boolean peek = peek(patterns);

    if (peek) {

        for (int i = 0; i < patterns.length; i++) {
            tokens.advance();
        }

    }
    return peek;
}
