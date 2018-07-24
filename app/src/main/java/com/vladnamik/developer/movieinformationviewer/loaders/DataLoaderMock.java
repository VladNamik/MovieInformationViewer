package com.vladnamik.developer.movieinformationviewer.loaders;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vladnamik.developer.movieinformationviewer.components.Application;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataLoaderMock implements IDataLoader {
    private static final String mockMovie1 = "{\"Title\":\"Batman\",\"Year\":\"1989\",\"Rated\":\"PG-13\",\"Released\":\"23 Jun 1989\",\"Runtime\":\"126 min\",\"Genre\":\"Action, Adventure\",\"Director\":\"Tim Burton\",\"Writer\":\"Bob Kane (Batman characters), Sam Hamm (story), Sam Hamm (screenplay), Warren Skaaren (screenplay)\",\"Actors\":\"Michael Keaton, Jack Nicholson, Kim Basinger, Robert Wuhl\",\"Plot\":\"Gotham City. Crime boss Carl Grissom (Jack Palance) effectively runs the town but there's a new crime fighter in town - Batman (Michael Keaton). Grissom's right-hand man is Jack Napier (Jack Nicholson), a brutal man who is not entirely sane... After falling out between the two Grissom has Napier set up with the Police and Napier falls to his apparent death in a vat of chemicals. However, he soon reappears as The Joker and starts a reign of terror in Gotham City. Meanwhile, reporter Vicki Vale (Kim Basinger) is in the city to do an article on Batman. She soon starts a relationship with Batman's everyday persona, billionaire Bruce Wayne.\",\"Language\":\"English, French\",\"Country\":\"USA, UK\",\"Awards\":\"Won 1 Oscar. Another 9 wins & 22 nominations.\",\"Poster\":\"https://images-na.ssl-images-amazon.com/images/M/MV5BMTYwNjAyODIyMF5BMl5BanBnXkFtZTYwNDMwMDk2._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.6/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"72%\"},{\"Source\":\"Metacritic\",\"Value\":\"69/100\"}],\"Metascore\":\"69\",\"imdbRating\":\"7.6\",\"imdbVotes\":\"283,401\",\"imdbID\":\"tt0096895\",\"Type\":\"movie\",\"DVD\":\"25 Mar 1997\",\"BoxOffice\":\"N/A\",\"Production\":\"Warner Bros. Pictures\",\"Website\":\"N/A\",\"Response\":\"True\"}";
    private static final String mockMovie2 = "{\"Title\":\"Batman Returns\",\"Year\":\"1992\",\"Rated\":\"PG-13\",\"Released\":\"19 Jun 1992\",\"Runtime\":\"126 min\",\"Genre\":\"Action\",\"Director\":\"Tim Burton\",\"Writer\":\"Bob Kane (Batman characters), Daniel Waters (story), Sam Hamm (story), Daniel Waters (screenplay)\",\"Actors\":\"Michael Keaton, Danny DeVito, Michelle Pfeiffer, Christopher Walken\",\"Plot\":\"In the sewers of gotham city to the rooftops of the gotham city the penguin wants to know where he came from well in his villain ways catwoman plans to kill rich man of gotham max shreak but as he battles with millionaire Bruce Wayne both ladies men have their own secrets Bruce Wayne is back as Bat man trying to stop the penguin Max is helping penguin steal gotham city while selina Kyle/catwoman tries to help penguin not knowing her man murder target also her murder is helping him but all four men have their goals taking gotham from crime winning gotham city assassination for two men and more money to be gotham citys number one rich man.\",\"Language\":\"English\",\"Country\":\"USA, UK\",\"Awards\":\"Nominated for 2 Oscars. Another 2 wins & 16 nominations.\",\"Poster\":\"https://images-na.ssl-images-amazon.com/images/M/MV5BOGZmYzVkMmItM2NiOS00MDI3LWI4ZWQtMTg0YWZkODRkMmViXkEyXkFqcGdeQXVyODY0NzcxNw@@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.0/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"80%\"},{\"Source\":\"Metacritic\",\"Value\":\"68/100\"}],\"Metascore\":\"68\",\"imdbRating\":\"7.0\",\"imdbVotes\":\"227,566\",\"imdbID\":\"tt0103776\",\"Type\":\"movie\",\"DVD\":\"29 Apr 1997\",\"BoxOffice\":\"N/A\",\"Production\":\"Warner Bros. Pictures\",\"Website\":\"N/A\",\"Response\":\"True\"}";

    @Override
    public SearchPage loadPage(String search, int pageNumber) throws IOException {
        SearchPage searchPage = new SearchPage();
        searchPage.setPageNumber(pageNumber);
        searchPage.setSearchQuery(new SearchQuery(search, new Date()));
        List<Movie> movies = new ArrayList<>();
        Movie movie = new Movie();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Float.class, new Application.FloatNewDeserializer())
                .registerTypeAdapter(Date.class, new Application.DateNewDeserializer())
                .create();
        movies.add(gson.fromJson(mockMovie1, Movie.class));
        movies.add(gson.fromJson(mockMovie2, Movie.class));
        searchPage.setMovies(movies);
        searchPage.setTotalResults(movies.size());
        return pageNumber == 1 ? searchPage : null;
    }

    @Override
    public Movie loadFullMovieByImdbId(String imdbId) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Float.class, new Application.FloatNewDeserializer())
                .registerTypeAdapter(Date.class, new Application.DateNewDeserializer())
                .create();
        return gson.fromJson(mockMovie1, Movie.class);
    }

    @Override
    public Movie loadFullMovieByTitle(String title) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Float.class, new Application.FloatNewDeserializer())
                .registerTypeAdapter(Date.class, new Application.DateNewDeserializer())
                .create();
        return gson.fromJson(mockMovie1, Movie.class);
    }
}
