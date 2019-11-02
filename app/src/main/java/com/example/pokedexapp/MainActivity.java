package com.example.pokedexapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.pokedexapp.models.Pokemon;
import com.example.pokedexapp.models.PokemonAnswer;
import com.example.pokedexapp.models.PokemonDetail;
import com.example.pokedexapp.models.Type;
import com.example.pokedexapp.models.TypeDetail;
import com.example.pokedexapp.pokeapi.PokeapiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";

    private Retrofit retrofit;

    private int offset;

    private boolean availableToLoad;

    private RecyclerView recyclerView;
    private PokemonListAdapter pokemonListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        pokemonListAdapter = new PokemonListAdapter(this);
        recyclerView.setAdapter(pokemonListAdapter);
        recyclerView.setHasFixedSize(true);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int pastVisibleItems = gridLayoutManager.findFirstCompletelyVisibleItemPosition();

                    if (availableToLoad) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, "final");

                            availableToLoad = false;
                            offset+=17;
                            getData(offset);
                        }
                    }
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        availableToLoad = true;
        offset = 0;
        getData(offset);
    }

    private void loadType(final Pokemon pokemon) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonDetail> pokemonDetailCall = service.getPokemonDetail(pokemon.getNumber());

        pokemonDetailCall.enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                if (response.isSuccessful()) {
                    PokemonDetail pokemonDetail = response.body();
                    ArrayList<Type> typesArrayList = pokemonDetail.getTypes();

                    Type type;
                    if (typesArrayList.size()>1) {
                        type = typesArrayList.get(1);
                    }
                    else {
                        type = typesArrayList.get(0);
                    }

                    TypeDetail typeDetail = type.getType();
                    String typeName = typeDetail.getName();

                    pokemon.setType(typeName);
                } else {
                    Log.e(TAG, "onResponseDetail: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                Log.e(TAG, "onFailureDetail: " + t.getMessage());
            }
        });
    }

    private void getData(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonAnswer> pokemonAnswerCall = service.getPokemonList(20, offset);

        pokemonAnswerCall.enqueue(new Callback<PokemonAnswer>() {
            @Override
            public void onResponse(Call<PokemonAnswer> call, Response<PokemonAnswer> response) {
                availableToLoad = true;
                if (response.isSuccessful()){
                    PokemonAnswer pokemonAnswer = response.body();
                    ArrayList<Pokemon> pokemonList = pokemonAnswer.getResults();

                    for (int i=0; i<pokemonList.size(); i++) {
                        loadType(pokemonList.get(i));
                    }

                    pokemonListAdapter.addPokemonList(pokemonList);
                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonAnswer> call, Throwable t) {
                availableToLoad = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }
}
