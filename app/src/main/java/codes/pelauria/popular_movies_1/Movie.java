package codes.pelauria.popular_movies_1;

import org.parceler.Parcel;

@Parcel
public class Movie {
    String title;
    String releaseDate;
    String posterUrl;
    String overview;
    int voteAvg;

    public Movie(){}

    public Movie(String title, String releaseDate, String posterUrl, String overview, int voteAvg) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.voteAvg = voteAvg;
    }

    public String getTitle() {
        return title;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getPosterUrl() {
        return posterUrl;
    }
    public String getOverview() {
        return overview;
    }
    public int getVoteAverage() {
        return voteAvg;
    }
}
