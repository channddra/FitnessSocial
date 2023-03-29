package com.example.fitnesssocial;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodAPI {
    @GET("api/recipes/v2")
    Call<EdamamSearchResponse> getFood(
            @Query("app_id") String appId,
            @Query("app_key") String appKey,
            @Query("type") String type,
            @Query("q") String query,
            @Query("mealType") String mealType,
            @Query("calories") String calories,
            @Query("random") boolean random,
            @Query("field") List<String> field
            );
}
