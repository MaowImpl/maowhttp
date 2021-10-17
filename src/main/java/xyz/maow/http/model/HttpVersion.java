package xyz.maow.http.model;

public enum HttpVersion {
    V1_0("1.0"),
    V1_1("1.1"),
    V2("2"),
    ;

    private final String name;

    HttpVersion(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HTTP/" + name;
    }
}