package fingertip.android.com.fingertip;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by NILESH on 17-05-2016.
 */
public class Movies_Fav extends RealmObject {

    String posterImg,backdropImg,title,overview,vote_average,release_date,genre,language;
    int fav,popularity;
    Movies_Fav object;


    @PrimaryKey
    String id;


  /*  public Movies_Fav()
    {

        this.fav=0;
        this.id=null;
    }*/


    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getFav() {
        return fav;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setObject(Movies_Fav object) {
        this.object = object;
    }

    public Movies_Fav getObject() {
        return object;
    }

    public String getBackdropImg() {
        return backdropImg;
    }

    public void setBackdropImg(String backdropImg) {
        this.backdropImg = backdropImg;
    }

    public String getPosterImg() {
        return posterImg;
    }

    public void setPosterImg(String posterImg) {
        this.posterImg = posterImg;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
