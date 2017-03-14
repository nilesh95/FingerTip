package fingertip.android.com.fingertip;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;




public class Top_rated extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;
    final ArrayList<String> imglist = new ArrayList<String>();
    final ArrayList<String> titlelist = new ArrayList<String>();
    final ArrayList<String> back_imglist = new ArrayList<String>();
    final ArrayList<String> plotlist = new ArrayList<String>();
    final ArrayList<String> user_ratinglist = new ArrayList<String>();
    final ArrayList<String> release_datelist = new ArrayList<String>();
    ArrayList<String> genre = new ArrayList<String>();
    ArrayList<Integer> popularity = new ArrayList<Integer>();
    ArrayList<String> language = new ArrayList<String>();

    final ArrayList<String> id_list = new ArrayList<String>();
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    String API_KEY;
    FragmentManager fm;

    public Top_rated() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        fm= getFragmentManager();
        mRecyclerView.setHasFixedSize(true);
        API_KEY=getActivity().getResources().getString(R.string.API_KEY);
        if (getResources().getConfiguration().orientation == 2) {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
            mRecyclerView.setLayoutManager(mLayoutManager);
            String url = "https://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;


            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray json = response.getJSONArray("results");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    String movy = jsonObject.getString("poster_path");
                                    String site = "http://image.tmdb.org/t/p/w300/" + movy;
                                    imglist.add(site);
                                    String title = jsonObject.getString("title");
                                    titlelist.add(title);
                                    String backdrop_path = jsonObject.getString("backdrop_path");
                                    String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                    back_imglist.add(back_img);
                                    String plot = jsonObject.getString("overview");
                                    plotlist.add(plot);
                                    String user_rating = jsonObject.getString("vote_average");
                                    user_ratinglist.add(user_rating);
                                    String release_date = jsonObject.getString("release_date");
                                    release_datelist.add(release_date);
                                    String id= jsonObject.getString("id");
                                    id_list.add(id);
                                    popularity.add(jsonObject.getInt("popularity"));
                                    language.add(jsonObject.getString("original_language"));
                                    JSONArray ja= jsonObject.getJSONArray("genre_ids");
                                    genre.add(ja.getString(0));
                                }
                                mAdapter = new MyAdapter1(imglist);
                                mRecyclerView.setAdapter(mAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();

                                isOnline();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                            isOnline();
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);

            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {

                    String url = "https://api.themoviedb.org/3/movie/top_rated?page=" + current_page + "&&api_key="+API_KEY;;


                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        JSONArray json = response.getJSONArray("results");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject jsonObject = json.getJSONObject(i);
                                            String movy = jsonObject.getString("poster_path");
                                            String site = "http://image.tmdb.org/t/p/w300/" + movy;
                                            imglist.add(site);
                                            String title = jsonObject.getString("title");
                                            titlelist.add(title);
                                            String backdrop_path = jsonObject.getString("backdrop_path");
                                            String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                            back_imglist.add(back_img);
                                            String plot = jsonObject.getString("overview");
                                            plotlist.add(plot);
                                            String user_rating = jsonObject.getString("vote_average");
                                            user_ratinglist.add(user_rating);
                                            String release_date = jsonObject.getString("release_date");
                                            release_datelist.add(release_date);
                                            String id= jsonObject.getString("id");
                                            id_list.add(id);
                                            popularity.add(jsonObject.getInt("popularity"));
                                            language.add(jsonObject.getString("original_language"));
                                            JSONArray ja= jsonObject.getJSONArray("genre_ids");
                                            genre.add(ja.getString(0));
                                        }
                                        mAdapter.notifyItemInserted(imglist.size() - 1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                        isOnline();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();

                                    isOnline();
                                }
                            });

                    Volley.newRequestQueue(getActivity()).add(jsonRequest);

                }
            });


            jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            mRecyclerView.addOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(new HelixEffect());


            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            boolean dual_pane = getResources().getBoolean(R.bool.dual_pane);
                            if (dual_pane) {
                                Tab_description tabletDetailFragment = new Tab_description();
                                Bundle b1 = new Bundle();


                                b1.putString("title", titlelist.get(position));
                                b1.putString("b_img", back_imglist.get(position));
                                b1.putString("overview", plotlist.get(position));
                                b1.putString("vote", user_ratinglist.get(position));
                                b1.putString("r_date", release_datelist.get(position));
                                b1.putString("p_img", imglist.get(position));
                                b1.putString("id", id_list.get(position));
                                b1.putString("genre", genre.get(position));
                                b1.putInt("popularity", popularity.get(position));
                                b1.putString("language", language.get(position));
                                tabletDetailFragment.setArguments(b1);
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.details_frag, tabletDetailFragment);
                                ft.commit();


                            } else {
                                Intent intent = new Intent(getActivity(), Movie_description.class);
                                intent.putExtra("title", titlelist.get(position));
                                intent.putExtra("b_img", back_imglist.get(position));
                                intent.putExtra("overview", plotlist.get(position));
                                intent.putExtra("vote", user_ratinglist.get(position));
                                intent.putExtra("r_date", release_datelist.get(position));
                                intent.putExtra("p_img", imglist.get(position));
                                intent.putExtra("id", id_list.get(position));
                                intent.putExtra("genre", genre.get(position));
                                intent.putExtra("popularity", popularity.get(position));
                                intent.putExtra("language", language.get(position));
                                startActivity(intent);
                            }
                        }
                    })
            );

        } else if (getResources().getConfiguration().orientation == 1) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            String url = "https://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;;


            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray json = response.getJSONArray("results");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    String movy = jsonObject.getString("poster_path");
                                    String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                    imglist.add(site);
                                    String title = jsonObject.getString("title");
                                    titlelist.add(title);
                                    String backdrop_path = jsonObject.getString("backdrop_path");
                                    String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                    back_imglist.add(back_img);
                                    String plot = jsonObject.getString("overview");
                                    plotlist.add(plot);
                                    String user_rating = jsonObject.getString("vote_average");
                                    user_ratinglist.add(user_rating);
                                    String release_date = jsonObject.getString("release_date");
                                    release_datelist.add(release_date);
                                    String id= jsonObject.getString("id");
                                    id_list.add(id);
                                    popularity.add(jsonObject.getInt("popularity"));
                                    language.add(jsonObject.getString("original_language"));
                                    JSONArray ja= jsonObject.getJSONArray("genre_ids");
                                    genre.add(ja.getString(0));
                                }
                                mAdapter = new MyAdapter1(imglist);
                                mRecyclerView.setAdapter(mAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                isOnline();

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            isOnline();

                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);

            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {


                @Override
                public void onLoadMore(int current_page) {

                    String url = "https://api.themoviedb.org/3/movie/top_rated?page=" + current_page + "&&api_key="+API_KEY;


                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        JSONArray json = response.getJSONArray("results");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject jsonObject = json.getJSONObject(i);
                                            String movy = jsonObject.getString("poster_path");
                                            String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                            imglist.add(site);
                                            String title = jsonObject.getString("title");
                                            titlelist.add(title);
                                            String backdrop_path = jsonObject.getString("backdrop_path");
                                            String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                            back_imglist.add(back_img);
                                            String plot = jsonObject.getString("overview");
                                            plotlist.add(plot);
                                            String user_rating = jsonObject.getString("vote_average");
                                            user_ratinglist.add(user_rating);
                                            String release_date = jsonObject.getString("release_date");
                                            release_datelist.add(release_date);
                                            String id= jsonObject.getString("id");
                                            id_list.add(id);
                                            popularity.add(jsonObject.getInt("popularity"));
                                            language.add(jsonObject.getString("original_language"));
                                            JSONArray ja= jsonObject.getJSONArray("genre_ids");
                                            genre.add(ja.getString(0));
                                        }
                                        mAdapter.notifyItemInserted(imglist.size() - 1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        isOnline();

                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    isOnline();

                                }
                            });

                    Volley.newRequestQueue(getActivity()).add(jsonRequest);

                }
            });


            jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            mRecyclerView.addOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(new HelixEffect());


            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            boolean dual_pane = getResources().getBoolean(R.bool.dual_pane);
                            if (dual_pane) {
                                Tab_description tabletDetailFragment = new Tab_description();
                                Bundle b1 = new Bundle();


                                b1.putString("title", titlelist.get(position));
                                b1.putString("b_img", back_imglist.get(position));
                                b1.putString("overview", plotlist.get(position));
                                b1.putString("vote", user_ratinglist.get(position));
                                b1.putString("r_date", release_datelist.get(position));
                                b1.putString("p_img", imglist.get(position));
                                b1.putString("id", id_list.get(position));
                                b1.putString("genre", genre.get(position));
                                b1.putInt("popularity", popularity.get(position));
                                b1.putString("language", language.get(position));
                                tabletDetailFragment.setArguments(b1);
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.details_frag, tabletDetailFragment);
                                ft.commit();


                            } else {
                                Intent intent = new Intent(getActivity(), Movie_description.class);
                                intent.putExtra("title", titlelist.get(position));
                                intent.putExtra("b_img", back_imglist.get(position));
                                intent.putExtra("overview", plotlist.get(position));
                                intent.putExtra("vote", user_ratinglist.get(position));
                                intent.putExtra("r_date", release_datelist.get(position));
                                intent.putExtra("p_img", imglist.get(position));
                                intent.putExtra("id", id_list.get(position));
                                intent.putExtra("genre", genre.get(position));
                                intent.putExtra("popularity", popularity.get(position));
                                intent.putExtra("language", language.get(position));
                                startActivity(intent);
                            }
                        }
                    })
            );

        }
        return view;
    }

    public void isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        API_KEY=getActivity().getResources().getString(R.string.API_KEY);
        if(activeNetwork==null){
            Snackbar.make(mRecyclerView, "Please Connect to the Internet", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isOnline();
                        }
                    })
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();

      }
else
        {
            imglist.clear();
            titlelist.clear();
            back_imglist.clear();
            plotlist.clear();
            user_ratinglist.clear();
            release_datelist.clear();
            id_list.clear();
            genre.clear();
            language.clear();
            popularity.clear();
            String url = "https://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONArray json = response.getJSONArray("results");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    String movy = jsonObject.getString("poster_path");
                                    String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                    imglist.add(site);
                                    String title = jsonObject.getString("title");
                                    titlelist.add(title);
                                    String backdrop_path = jsonObject.getString("backdrop_path");
                                    String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                    back_imglist.add(back_img);
                                    String plot = jsonObject.getString("overview");
                                    plotlist.add(plot);
                                    String user_rating = jsonObject.getString("vote_average");
                                    user_ratinglist.add(user_rating);
                                    String release_date = jsonObject.getString("release_date");
                                    release_datelist.add(release_date);
                                    String id= jsonObject.getString("id");
                                    id_list.add(id);
                                    popularity.add(jsonObject.getInt("popularity"));
                                    language.add(jsonObject.getString("original_language"));
                                    JSONArray ja= jsonObject.getJSONArray("genre_ids");
                                    genre.add(ja.getString(0));
                                }
                                mAdapter = new MyAdapter(imglist);
                                mRecyclerView.setAdapter(mAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                isOnline();
                                //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            isOnline();
                            //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);
            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    // do something...
                    String url = "https://api.themoviedb.org/3/movie/top_rated?page=" + current_page + "&&api_key="+API_KEY;

                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // the response is already constructed as a JSONObject!
                                    try {
                                        JSONArray json = response.getJSONArray("results");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject jsonObject = json.getJSONObject(i);
                                            String movy = jsonObject.getString("poster_path");
                                            String site = "http://image.tmdb.org/t/p/w300/" + movy;
                                            imglist.add(site);
                                            String title = jsonObject.getString("title");
                                            titlelist.add(title);
                                            String backdrop_path = jsonObject.getString("backdrop_path");
                                            String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                            back_imglist.add(back_img);
                                            String plot = jsonObject.getString("overview");
                                            plotlist.add(plot);
                                            String user_rating = jsonObject.getString("vote_average");
                                            user_ratinglist.add(user_rating);
                                            String release_date = jsonObject.getString("release_date");
                                            release_datelist.add(release_date);
                                            String id= jsonObject.getString("id");
                                            id_list.add(id);
                                            popularity.add(jsonObject.getInt("popularity"));
                                            language.add(jsonObject.getString("original_language"));
                                            JSONArray ja= jsonObject.getJSONArray("genre_ids");
                                            genre.add(ja.getString(0));
                                        }
                                        mAdapter.notifyItemInserted(imglist.size() - 1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        isOnline();
                                        //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    isOnline();
                                    //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Volley.newRequestQueue(getActivity()).add(jsonRequest);

                }
            });

        }


    }


}

