package com.sparta.models;

public class Link {
    private String href;
    private String rel;
    private String method;

    public Link() {}

    // getters
    public String getHref() { return href; }
    public String getRel() { return rel; }
    public String getMethod() { return method; }

    // setters
    public void setHref(String href) { this.href = href; }
    public void setRel(String rel) { this.rel = rel; }
    public void setMethod(String method) { this.method = method; }
}
