public boolean peek(Object... patterns) {
    for (int i=0; i < patterns.length; i++) {
        if (!tokens.has(i)) {
            return false;
        } else if (patterns[i] instanceof Token.Type) {
            if (patterns[i] != tokens.get(i).getType())
                return false;
        } else if (patterns[i] instanceof String) {
            if (!patterns[i].equals(tokens.get(i).getLiteral()))
                return false
        } else {
            throw new AssertionError("Invalid pattern object: " + patterns[i].getClass());
        }
    }
    return true;
}
