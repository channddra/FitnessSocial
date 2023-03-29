package com.example.fitnesssocial;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("url")
    @Expose
    private String url;

    public Recipe() {
    }

    /**
     *
     * @param image
     * @param label
     * @param source
     * @param uri
     * @param url
     */
    public Recipe(String uri, String label, String image, String source, String url) {
        super();
        this.uri = uri;
        this.label = label;
        this.image = image;
        this.source = source;
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Recipe withUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Recipe withLabel(String label) {
        this.label = label;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Recipe withImage(String image) {
        this.image = image;
        return this;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Recipe withSource(String source) {
        this.source = source;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Recipe withUrl(String url) {
        this.url = url;
        return this;
    }

}
