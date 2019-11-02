package com.example.pokedexapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pokedexapp.models.Pokemon;

import java.util.ArrayList;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    private ArrayList<Pokemon> dataset;
    private Context context;

    public PokemonListAdapter(Context context) {
        this.dataset = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public PokemonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonListAdapter.ViewHolder holder, int position) {
        Pokemon pokemon = dataset.get(position);
        holder.pokemonName.setText(pokemon.getName());

        String color = pokemon.getType();
        if (color!=null)    holder.pokemonPic.setBackgroundResource(getColorResource(color));

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                        + pokemon.getNumber() + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pokemonPic);
    }

    private int getColorResource(String color) {
        switch (color) {
            case "bug":
                return R.color.bug;
            case "dark":
                return R.color.dark;
            case "dragon":
                return R.color.dragon;
            case "electric":
                return R.color.electric;
            case "fairy":
                return R.color.fairy;
            case "fighting":
                return R.color.fighting;
            case "fire":
                return R.color.fire;
            case "flying":
                return R.color.flying;
            case "ghost":
                return R.color.ghost;
            case "grass":
                return R.color.grass;
            case "ground":
                return R.color.ground;
            case "ice":
                return R.color.ice;
            case "normal":
                return R.color.normal;
            case "poison":
                return R.color.poison;
            case "psychic":
                return R.color.psychic;
            case "rock":
                return R.color.rock;
            case "steel":
                return R.color.steel;
            case "water":
                return R.color.water;
            default:
                return Color.TRANSPARENT;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addPokemonList(ArrayList<Pokemon> pokemonList) {
        dataset.addAll(pokemonList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView pokemonPic;
        private TextView pokemonName, pokemonType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pokemonPic = itemView.findViewById(R.id.pokemonPic);
            pokemonName = itemView.findViewById(R.id.pokemonName);
        }
    }
}