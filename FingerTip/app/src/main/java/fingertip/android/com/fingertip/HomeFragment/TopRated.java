package fingertip.android.com.fingertip.HomeFragment;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fingertip.android.com.fingertip.Adapter.MyAdapter;
import fingertip.android.com.fingertip.R;
import fingertip.android.com.fingertip.RecyclerItemClickListner.RecyclerItemClickListener2;


public class TopRated extends Fragment {
    ArrayList<String> imglist = new ArrayList<>();
    ArrayList<String> titlelist = new ArrayList<>();
    ArrayList<String> desclist = new ArrayList<>();
    ArrayList<String> urllist = new ArrayList<>();
    ArrayList<String> authorlist = new ArrayList<>();
    ArrayList<String> publishedTimelist = new ArrayList<>();
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    String API_KEY;
    SmartTabLayout viewPagerTab;
    List<String> selectedList;
    FragmentManager fm;
    ConnectivityManager connectivityManager;
    Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter, smAdapter;
    private GridLayoutManager mLayoutManager;
//    private OnItemSelectedListener listener;


//    public interface OnItemSelectedListener {
//        public void onMovieSelected(Movie movie);
//    }
//    public void updateDetail(Movie movie) {
//        listener.onMovieSelected(movie);
//    }
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnItemSelectedListener) {
//            listener = (OnItemSelectedListener) context;
//        } else {
//            throw new ClassCastException(context.toString()
//                    + " must implement MyListFragment.OnItemSelectedListener");
//        }
//    }

    public TopRated() {
        // Required empty public constructor
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
        setHasOptionsMenu(true);
        mRecyclerView.setHasFixedSize(true);
        API_KEY = getActivity().getResources().getString(R.string.API_KEY);
        fm = getFragmentManager();

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        context = getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selectedList = bundle.getStringArrayList("ArrayList");
        }

        if (getResources().getConfiguration().orientation == 2) {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
            mRecyclerView.setLayoutManager(mLayoutManager);
            for (String source : selectedList) {
                final String url = "https://newsapi.org/v1/articles?source=" + source + "&sortBy=top&apiKey=" + API_KEY;

                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // the response is already constructed as a JSONObject!
                                try {

                                    JSONArray json = response.getJSONArray("articles");
                                    for (int i = 0; i < json.length(); i++) {
                                        JSONObject jsonObject = json.getJSONObject(i);
                                        imglist.add(jsonObject.getString("urlToImage"));
                                        authorlist.add(jsonObject.getString("author"));
                                        titlelist.add(jsonObject.getString("title"));
                                        desclist.add(jsonObject.getString("description"));
                                        urllist.add(jsonObject.getString("url"));
                                        publishedTimelist.add(jsonObject.getString("publishedAt"));

                                    }
                                    mAdapter = new MyAdapter(imglist, titlelist);
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
            }

            jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            mRecyclerView.addOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(new HelixEffect());

            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener2(getActivity(), new RecyclerItemClickListener2.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            boolean dual_pane = getResources().getBoolean(R.bool.dual_pane);
                            if (dual_pane) {
                                TabDescription tabletDetailFragment = new TabDescription();
                                Bundle b1 = new Bundle();
                                b1.putString("title", titlelist.get(position));
                                b1.putString("b_img", imglist.get(position));
                                b1.putString("overview", desclist.get(position));
                                b1.putString("r_date", publishedTimelist.get(position));
                                b1.putString("p_img", imglist.get(position));
                                b1.putString("language", authorlist.get(position));
                                b1.putString("url", urllist.get(position));
                                tabletDetailFragment.setArguments(b1);
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.details_frag, tabletDetailFragment);
                                ft.commit();


                            } else {
                                Intent intent = new Intent(getActivity(), NewsDescription.class);
                                intent.putExtra("title", titlelist.get(position));
                                intent.putExtra("b_img", imglist.get(position));
                                intent.putExtra("overview", desclist.get(position));
                                intent.putExtra("r_date", publishedTimelist.get(position));
                                intent.putExtra("p_img", imglist.get(position));
                                intent.putExtra("language", authorlist.get(position));
                                intent.putExtra("url", urllist.get(position));
                                startActivity(intent);
                            }
                        }

                    })
            );

        } else if (getResources().getConfiguration().orientation == 1) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            for (String source : selectedList) {
                final String url = "https://newsapi.org/v1/articles?source=" + source + "&sortBy=top&apiKey=" + API_KEY;

                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // the response is already constructed as a JSONObject!
                                try {

                                    JSONArray json = response.getJSONArray("articles");
                                    for (int i = 0; i < json.length(); i++) {
                                        JSONObject jsonObject = json.getJSONObject(i);
                                        imglist.add(jsonObject.getString("urlToImage"));
                                        authorlist.add(jsonObject.getString("author"));
                                        titlelist.add(jsonObject.getString("title"));
                                        desclist.add(jsonObject.getString("description"));
                                        urllist.add(jsonObject.getString("url"));
                                        publishedTimelist.add(jsonObject.getString("publishedAt"));

                                    }

                                    mAdapter = new MyAdapter(imglist, titlelist);
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
            }

            jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            mRecyclerView.addOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(new HelixEffect());


            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener2(getActivity(), new RecyclerItemClickListener2.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            boolean dual_pane = getResources().getBoolean(R.bool.dual_pane);
                            if (dual_pane) {
                                TabDescription tabletDetailFragment = new TabDescription();
                                Bundle b1 = new Bundle();

                                b1.putString("title", titlelist.get(position));
                                b1.putString("b_img", imglist.get(position));
                                b1.putString("overview", desclist.get(position));
                                b1.putString("r_date", publishedTimelist.get(position));
                                b1.putString("p_img", imglist.get(position));
                                b1.putString("language", authorlist.get(position));
                                b1.putString("url", urllist.get(position));
                                tabletDetailFragment.setArguments(b1);
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.details_frag, tabletDetailFragment);
                                ft.commit();


                            } else {
                                Intent intent = new Intent(getActivity(), NewsDescription.class);
                                intent.putExtra("title", titlelist.get(position));
                                intent.putExtra("b_img", imglist.get(position));
                                intent.putExtra("overview", desclist.get(position));
                                intent.putExtra("r_date", publishedTimelist.get(position));
                                intent.putExtra("p_img", imglist.get(position));
                                intent.putExtra("language", authorlist.get(position));

                                intent.putExtra("url", urllist.get(position));
                                startActivity(intent);
                            }
                        }
                    })
            );

        }


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void isOnline() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Snackbar.make(mRecyclerView, getString(R.string.pleaseConnectInternet), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isOnline();
                        }
                    })
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();
        } else {


            imglist.clear();
            titlelist.clear();
            authorlist.clear();
            urllist.clear();
            desclist.clear();
            publishedTimelist.clear();
            for (String source : selectedList) {
                final String url = "https://newsapi.org/v1/articles?source=" + source + "&sortBy=top&apiKey=" + API_KEY;

                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // the response is already constructed as a JSONObject!
                                try {

                                    JSONArray json = response.getJSONArray("articles");
                                    for (int i = 0; i < json.length(); i++) {
                                        JSONObject jsonObject = json.getJSONObject(i);
                                        imglist.add(jsonObject.getString("urlToImage"));
                                        authorlist.add(jsonObject.getString("author"));
                                        titlelist.add(jsonObject.getString("title"));
                                        desclist.add(jsonObject.getString("description"));
                                        urllist.add(jsonObject.getString("url"));
                                        publishedTimelist.add(jsonObject.getString("publishedAt"));

                                    }

                                    mAdapter = new MyAdapter(imglist, titlelist);
                                    mRecyclerView.setAdapter(mAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();


                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();

                            }
                        });

                Volley.newRequestQueue(context).add(jsonRequest);
            }

        }


    }

}

