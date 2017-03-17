package fingertip.android.com.fingertip.FavouriteFragment;

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
import android.widget.Button;

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
import java.util.List;

import fingertip.android.com.fingertip.Adapter.MyAdapter4;
import fingertip.android.com.fingertip.HomeFragment.TabDescription;
import fingertip.android.com.fingertip.ListActivity;
import fingertip.android.com.fingertip.R;
import fingertip.android.com.fingertip.RecyclerItemClickListner.RecyclerItemClickListener2;

/**
 * Created by NILESH on 12-03-2017.
 */

public class FavouriteZone extends Fragment {
    ArrayList<String> imglist = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<String> languageList = new ArrayList<>();
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> urlList = new ArrayList<>();
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    String API_KEY;
    FragmentManager fm;
    List<String> selectedList;
    ConnectivityManager connectivityManager;
    Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter, smAdapter;
    private GridLayoutManager mLayoutManager;
    private Button addMore;

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

    public FavouriteZone() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fav_zone, container, false);
        addMore = (Button) view.findViewById(R.id.addButton);
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
            final String url = "https://newsapi.org/v1/sources?language=en";

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONArray json = response.getJSONArray("sources");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    if (selectedList.contains(jsonObject.getString("id"))) {
                                        imglist.add(jsonObject.getJSONObject("urlsToLogos").getString("large"));
                                        idList.add(jsonObject.getString("id"));
                                        urlList.add(jsonObject.getString("url"));
                                        nameList.add(jsonObject.getString("name"));
                                        descList.add(jsonObject.getString("description"));
                                        categoryList.add(jsonObject.getString("category"));
                                        languageList.add(jsonObject.getString("language"));
                                        countryList.add(jsonObject.getString("country"));

                                    }

                                }

                                mAdapter = new MyAdapter4(imglist);
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


                                b1.putString("title", nameList.get(position));
                                b1.putString("b_img", imglist.get(position));
                                b1.putString("overview", descList.get(position));
                                b1.putString("id", idList.get(position));
                                b1.putString("genre", categoryList.get(position));
                                b1.putString("language", languageList.get(position));
                                b1.putString("country", countryList.get(position));
                                b1.putString("url", urlList.get(position));
                                b1.putString("p_img", imglist.get(position));
                                tabletDetailFragment.setArguments(b1);
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.details_frag, tabletDetailFragment);
                                ft.commit();


                            } else {
                                Intent intent = new Intent(getActivity(), SourceListDescription.class);
                                intent.putExtra("title", nameList.get(position));
                                intent.putExtra("b_img", imglist.get(position));
                                intent.putExtra("overview", descList.get(position));
                                intent.putExtra("id", idList.get(position));
                                intent.putExtra("genre", categoryList.get(position));
                                intent.putExtra("language", languageList.get(position));
                                intent.putExtra("country", countryList.get(position));
                                intent.putExtra("url", urlList.get(position));
                                intent.putExtra("p_img", imglist.get(position));
                                startActivity(intent);
                            }
                        }
                    })
            );

        } else if (getResources().getConfiguration().orientation == 1) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            String url = "https://newsapi.org/v1/sources?language=en";

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONArray json = response.getJSONArray("results");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    if (selectedList.contains(jsonObject.getString("id"))) {
                                        imglist.add(jsonObject.getJSONObject("urlsToLogos").getString("large"));
                                        idList.add(jsonObject.getString("id"));
                                        urlList.add(jsonObject.getString("url"));
                                        nameList.add(jsonObject.getString("name"));
                                        descList.add(jsonObject.getString("description"));
                                        categoryList.add(jsonObject.getString("category"));
                                        languageList.add(jsonObject.getString("language"));
                                        countryList.add(jsonObject.getString("country"));

                                    }

                                }

                                mAdapter = new MyAdapter4(imglist);
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

                                b1.putString("title", nameList.get(position));
                                b1.putString("b_img", imglist.get(position));
                                b1.putString("overview", descList.get(position));
                                b1.putString("id", idList.get(position));
                                b1.putString("genre", categoryList.get(position));
                                b1.putString("language", languageList.get(position));
                                b1.putString("country", countryList.get(position));
                                b1.putString("url", urlList.get(position));
                                b1.putString("p_img", imglist.get(position));

                                tabletDetailFragment.setArguments(b1);
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.details_frag, tabletDetailFragment);
                                ft.commit();


                            } else {
                                Intent intent = new Intent(getActivity(), SourceListDescription.class);
                                intent.putExtra("title", nameList.get(position));
                                intent.putExtra("b_img", imglist.get(position));

                                intent.putExtra("p_img", imglist.get(position));
                                intent.putExtra("overview", descList.get(position));
                                intent.putExtra("id", idList.get(position));
                                intent.putExtra("genre", categoryList.get(position));
                                intent.putExtra("language", languageList.get(position));
                                intent.putExtra("country", countryList.get(position));
                                intent.putExtra("url", urlList.get(position));
                                startActivity(intent);
                            }
                        }
                    })
            );

        }

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                startActivity(intent);
            }
        });


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
            nameList.clear();
            descList.clear();
            categoryList.clear();
            languageList.clear();
            countryList.clear();
            idList.clear();

            String url = "https://newsapi.org/v1/sources?language=en";

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONArray json = response.getJSONArray("sources");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    if (selectedList.contains(jsonObject.getString("id"))) {
                                        imglist.add(jsonObject.getJSONObject("urlsToLogos").getString("large"));
                                        idList.add(jsonObject.getString("id"));
                                        urlList.add(jsonObject.getString("url"));
                                        nameList.add(jsonObject.getString("name"));
                                        descList.add(jsonObject.getString("description"));
                                        categoryList.add(jsonObject.getString("category"));
                                        languageList.add(jsonObject.getString("language"));
                                        countryList.add(jsonObject.getString("country"));

                                    }
                                }

                                mAdapter = new MyAdapter4(imglist);
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

                        }
                    });

            Volley.newRequestQueue(context).add(jsonRequest);

        }


    }

}

