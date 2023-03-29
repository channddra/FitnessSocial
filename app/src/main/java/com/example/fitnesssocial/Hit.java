package com.example.fitnesssocial;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hit {

    @SerializedName("recipe")
    @Expose
    private Recipe recipe;
    @SerializedName("_links")
    @Expose
    private Links links;

    public Hit() {
    }

    /**
     *
     * @param recipe
     * @param links
     */
    public Hit(Recipe recipe, Links links) {
        super();
        this.recipe = recipe;
        this.links = links;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Hit withRecipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Hit withLinks(Links links) {
        this.links = links;
        return this;
    }

}
