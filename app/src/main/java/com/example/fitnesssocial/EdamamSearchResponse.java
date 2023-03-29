package com.example.fitnesssocial;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EdamamSearchResponse {

    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;

    public EdamamSearchResponse() {
    }

    /**
     *
     * @param hits
     * @param count
     * @param from
     * @param links
     * @param to
     */
    public EdamamSearchResponse(String from, String to, String count, Links links, List<Hit> hits) {
        super();
        this.from = from;
        this.to = to;
        this.count = count;
        this.links = links;
        this.hits = hits;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public EdamamSearchResponse withFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public EdamamSearchResponse withTo(String to) {
        this.to = to;
        return this;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public EdamamSearchResponse withCount(String count) {
        this.count = count;
        return this;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public EdamamSearchResponse withLinks(Links links) {
        this.links = links;
        return this;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public EdamamSearchResponse withHits(List<Hit> hits) {
        this.hits = hits;
        return this;
    }

}
