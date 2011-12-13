package org.echocat.redprecursor.compilertree;

public enum Modifier {
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    STATIC("static"),
    FINAL("final"),
    SYNCHRONIZED("synchronized"),
    VOLATILE("volatile"),
    TRANSIENT("transient"),
    NATIVE("native"),
    INTERFACE("interface"),
    ABSTRACT("abstract"),
    STRICTFP("strictfp"),
    ENUM("enum"),
    VARARGS("varargs");

    private final String _stringRepresentation;

    Modifier(String stringRepresentation) {
        _stringRepresentation = stringRepresentation;
    }

    @Override
    public String toString() {
        return _stringRepresentation;
    }
}
