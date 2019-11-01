package com.example.pokedexapp.pokeapi;

import com.example.pokedexapp.models.PokemonAnswer;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonAnswer> getPokemonList();
}
