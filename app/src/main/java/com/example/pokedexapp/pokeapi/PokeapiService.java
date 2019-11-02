package com.example.pokedexapp.pokeapi;

import com.example.pokedexapp.models.PokemonAnswer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonAnswer> getPokemonList(@Query("Limit") int limit, @Query("offset") int offset);
}
