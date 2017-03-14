package fingertip.android.com.fingertip;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by NILESH on 05-06-2016.
 */
public class Tab_description extends Fragment {
    ImageView poster,title;
    TextView year,average,synopsis,mTrailer,mReview,moTitle,mAuthor,mText,tgenre,tpopularity,tlanguage,tvote;
    FloatingActionButton share,share_btn;
    String mTitle,mBackdrop_Image,mOverview,mVote,mRelease_Date,mPoster_Image,mId,mgenre,mlanguage;
    Integer mpopularity;
    Context context;

    View view;
    float rate;
    double d;

    String API_KEY;
    RealmConfiguration realmConfig;
    ImageButton ib;
    // Get a Realm instance for this thread
    Realm realm;
    //RealmList<Movies_Fav> a=new RealmList<>();
    Movies_Fav m,mf;
    String key;
    private ArrayList<String> videokey = new ArrayList<>();
    private ArrayList<String> reviewId = new ArrayList<>();
    private ArrayList<String> thumb_img = new ArrayList<>();
    private ArrayList<String> trailerInfo = new ArrayList<>();
    private ArrayList<String> reviewAuthor = new ArrayList<>();
    private ArrayList<String> reviewText = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    RefreshGrid refreshGrid;
    public interface RefreshGrid{
        public void refreshFavGrid();
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RefreshGrid) {
            refreshGrid = (RefreshGrid) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitle=getArguments().getString("title");
        this.mBackdrop_Image=getArguments().getString("b_img");
        this.mOverview=getArguments().getString("overview");
        this.mVote=getArguments().getString("vote");
        this.mRelease_Date=getArguments().getString("r_date");
        this.mPoster_Image=getArguments().getString("p_img");
        this.mId=getArguments().getString("id");
        this.mgenre=getArguments().getString("genre");
        this.mpopularity=getArguments().getInt("popularity");
        this.mlanguage=getArguments().getString("language");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_scrolling, container, false);

        context=getActivity();
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        API_KEY=getResources().getString(R.string.API_KEY);
        realmConfig = new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfig);
        poster= (ImageView) view.findViewById(R.id.poster);
        year= (TextView) view.findViewById(R.id.year);
        average= (TextView) view.findViewById(R.id.rating);
        title= (ImageView) view.findViewById(R.id.imgBack);
        synopsis= (TextView) view.findViewById(R.id.synopsis);
        share= (FloatingActionButton) view.findViewById(R.id.fab);
        share_btn= (FloatingActionButton) view.findViewById(R.id.fabshare);
        mAuthor= (TextView) view.findViewById(R.id.review_author_text);
        mText= (TextView) view.findViewById(R.id.review_text);
        tgenre= (TextView) view.findViewById(R.id.genre_text);
        tpopularity= (TextView) view.findViewById(R.id.popularity);
        tlanguage= (TextView) view.findViewById(R.id.language);
        tvote= (TextView) view.findViewById(R.id.rating);
        ib= (ImageButton) view.findViewById(R.id.imgVideo);
//        r= (RatingBar) findViewById(R.id.rating);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recycler_movie_details);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);
        moTitle= (TextView) view.findViewById(R.id.motitle);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setTitle(mTitle);
      Glide.with(getActivity()).load(Uri.parse(mPoster_Image)).error(R.drawable.placeholder).into(poster);
      Glide.with(getActivity()).load(Uri.parse(mBackdrop_Image)).error(R.drawable.placeholder).into(title);
        year.setText(mRelease_Date);
//        average.setText(mVote);
        moTitle.setText(mTitle);
        synopsis.setText(mOverview);
        d= Double.parseDouble(mVote);
        rate=(float)d;
        tvote.setText(String.valueOf(rate));
        if(mlanguage==null)
            tlanguage.setText("na");
        else
            tlanguage.setText(mlanguage);
        if(mpopularity==0)
            tpopularity.setText("NA");
        else
            tpopularity.setText(String.valueOf(mpopularity));
        fetchgenre();
        //r.setRating(rate / 2);

        fetchdata();
        fetchTrailer();
        fetchReview();
//        trailer_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String video_path = "https://www.youtube.com/watch?v="+key;
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_path));
//                startActivity(intent);
//
//            }
//        });
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String video_path = "https://www.youtube.com/watch?v="+videokey.get(0);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_path));
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                //m=realm.createObject(Movies_Fav.class);
                //  Toast.makeText(getBaseContext(),String.valueOf(mf.getFav()),Toast.LENGTH_SHORT).show();
                if(mf.getFav()==0) {
                    realm.commitTransaction();
                    fetchdata1();
                }
                else if(mf.getFav()==1)
                {

                    realm.commitTransaction();
                    fetchdata2();
                }
                if (refreshGrid != null)
                    refreshGrid.refreshFavGrid();
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the trailer";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,mTitle+"    https://www.youtube.com/watch?v="+videokey.get(0));
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener2(getActivity(), new RecyclerItemClickListener2.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String video_path = "https://www.youtube.com/watch?v="+videokey.get(position);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_path));
                        startActivity(intent);

                    }
                }
                ));
        return view;
    }


    public void fetchdata()
    {
        realm.beginTransaction();
        // Movies_Fav m=realm.createObject(Movies_Fav.class);
        //a=m.getMovies_id();
        m=realm.createObject(Movies_Fav.class);
        RealmResults<Movies_Fav> result3 = realm.where(Movies_Fav.class).equalTo("id", mId).findAll();
        if(result3.size()!=0)
            mf=result3.first().getObject();
        else
            mf=m;
        if(mf.getFav()==0) {
            share.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_off));
        }


        else if(mf.getFav()==1) {
            share.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_on));
        }


        realm.commitTransaction();

        //fetchdata();
        // realm.cancelTransaction();


    }
    void fetchdata1()
    {
        realm.beginTransaction();
        // Movies_Fav m=realm.createObject(Movies_Fav.class);
        mf=realm.createObject(Movies_Fav.class);
        mf.setFav(1);
        mf.setId(mId);
        mf.setObject(mf);
        mf.setBackdropImg(mBackdrop_Image);
        mf.setOverview(mOverview);
        mf.setPosterImg(mPoster_Image);
        mf.setRelease_date(mRelease_Date);
        mf.setTitle(mTitle);
        mf.setVote_average(mVote);
        mf.setGenre(mgenre);
        mf.setLanguage(mlanguage);
        mf.setPopularity(mpopularity);
        share.setImageDrawable(ContextCompat.getDrawable(getActivity(),android.R.drawable.btn_star_big_on));
        realm.commitTransaction();
        // new Favourite().fetchsData();


    }
    void fetchdata2()
    {
        realm.beginTransaction();

        share.setImageDrawable(ContextCompat.getDrawable(getActivity(),android.R.drawable.btn_star_big_off));

        RealmResults<Movies_Fav> result2 = realm.where(Movies_Fav.class).findAll();
        mf.deleteFromRealm();
        //result2.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        mf=realm.createObject(Movies_Fav.class);
        realm.commitTransaction();
//        new Favourite().fetchsData();


    }

    public void fetchReview()
    {
        String url = "https://api.themoviedb.org/3/movie/"+mId+"/reviews?api_key="+API_KEY;
startAnim2();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            JSONArray json = response.getJSONArray("results");
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jsonObject = json.getJSONObject(i);

                                reviewId.add(jsonObject.getString("id"));
                                reviewText.add(jsonObject.getString("content"));
                                reviewAuthor.add(jsonObject.getString("author"));
                            }
                            if(reviewAuthor.size()!=0 && reviewText.size()!=0) {
                                stopAnim2();
                                mAuthor.setText(reviewAuthor.get(0));
                                mText.setText(reviewText.get(0));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(getActivity()).add(jsonRequest);


    }

    public void fetchTrailer()
    {
        String url = "https://api.themoviedb.org/3/movie/"+mId+"/videos?api_key="+API_KEY;
startAnim();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            JSONArray json = response.getJSONArray("results");
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jsonObject = json.getJSONObject(i);

                                key = jsonObject.getString("key");
                                videokey.add(key);

                                String name = jsonObject.getString("name");
                                trailerInfo.add(name);
                            }
                            for(int i=0;i<videokey.size();i++)
                            {
                                String url2="http://img.youtube.com/vi/"+videokey.get(i)+"/default.jpg";
                                thumb_img.add(url2);
                            }
                            if(thumb_img.size()!=0 && trailerInfo.size()!=0) {
                                stopAnim();
                                mAdapter = new MyAdapter3(thumb_img, trailerInfo);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            else
                            {
                                ib.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(getActivity()).add(jsonRequest);


    }

    public void fetchgenre()
    {
        if(mgenre!=null) {
            String url = "https://api.themoviedb.org/3/genre/" + mgenre + "?api_key=" + API_KEY;


            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                tgenre.setText(response.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Something went wrong!!please check your connection ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void startAnim(){
        view.findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
    }

    void stopAnim(){
        view.findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
    }

    void startAnim2(){
        view.findViewById(R.id.avloadingIndicatorView2).setVisibility(View.VISIBLE);
    }

    void stopAnim2(){
        view.findViewById(R.id.avloadingIndicatorView2).setVisibility(View.GONE);
    }
}



