import com.google.gson.Gson;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import java.util.List;
import static input.InputUtils.*;

public class movieProgram {
    // The key, different for each person. Edit as needed
    final static private String MOVIE_API_KEY = System.getenv("MOVIE_API_KEY");

    public static void main(String[] args) {
        // Set up
        Unirest.config().setObjectMapper(new ObjectMapper() {
            private Gson gson = new Gson();
            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                return gson.fromJson(s, aClass);
            }

            @Override
            public String writeValue(Object o) {
                return gson.toJson(o);
            }
        });

        // The base link
        String link = "http://www.omdbapi.com/?apikey=" + MOVIE_API_KEY + "&";

        // The main loop
        while (true) {
            // Sets up the link for API
            System.out.println("Enter Movie Title: ");
            String inputMovieTitle = stringInput();
            inputMovieTitle = inputMovieTitle.replace(" ", "+");    // Important: Must use + and not spaces
            String url = link + "t=" + inputMovieTitle;

            // Sets up the movie object
            Movie movie = Unirest.get(url).asObject(Movie.class).getBody();

            // If the movie exists then call the display method, otherwise explain to user
            if (movie.getTitle() != null) displayMovieInfo(movie);
            else {
                System.out.println("Movie not found! Make sure your spelling is exact.");
                System.out.println();
            }
        }
    }
    public static void displayMovieInfo(Movie movie){
        System.out.println("=============================================================================");
        System.out.println("Title: "+movie.Title);
        System.out.println("Released: "+movie.Released);

        // Prints all the raters and rating (if applicable)
        for (Ratings rating : movie.Ratings){
            System.out.println(rating.getSource() + " Rating: " + rating.getValue());
        }

        System.out.println("Runtime: "+movie.Runtime);
        System.out.println("Genre: "+movie.Genre);
        System.out.println("Director: "+movie.Director);
        System.out.println("Writer: "+movie.Writer);
        System.out.println("Actors: "+movie.Actors);
        System.out.println("Plot: "+movie.Plot);
        System.out.println("Language: "+movie.Language);
        System.out.println("Country: "+movie.Country);
        System.out.println("Awards: "+movie.Awards);
        System.out.println("=============================================================================");
        System.out.println();
    }

    class Movie {
        // The movie object has the following properties
        public String Title;
        public String Released;
        public String Runtime;
        public String Genre;
        public String Director;
        public String Writer;
        public String Actors;
        public String Plot;
        public String Language;
        public String Country;
        public String Awards;
        public List<Ratings> Ratings;
        public String score = Ratings.get(1).getValue();
        public String Rater = Ratings.get(1).getSource();

        public String getTitle() {
            return Title;
        }
    }

    class Ratings {
        // Each rating has a source (Author) and a value (Score).
        public String Source;
        public String Value;

        // Getter Methods
        public String getSource() {
            return Source;
        }

        public String getValue() {
            return Value;
        }
    }
}
