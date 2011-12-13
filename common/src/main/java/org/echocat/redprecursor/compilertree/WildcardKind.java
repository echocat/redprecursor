package org.echocat.redprecursor.compilertree;

public enum WildcardKind {
    EXTENDS("? extends "),
    SUPER("? super "),
    UNBOUND("?");

    private final String _name;

    private WildcardKind(String name) { _name = name; }

    public String toString() {
        return _name;
    }
}
