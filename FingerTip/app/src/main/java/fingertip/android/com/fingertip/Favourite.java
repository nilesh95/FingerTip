package fingertip.android.com.fingertip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class Favourite extends Fragment implements TabsActivity.UpdateableFragment{
    RealmConfiguration realmConfig;
    // Get a Realm instance for this thread
    Realm realm;
View v;
    RecyclerView mRecyclerView;
    String API_KEY;
    GridLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    final ArrayList<String> imglist = new ArrayList<String>();
    final ArrayList<String> titlelist = new ArrayList<String>();
    final ArrayList<String> back_imglist = new ArrayList<String>();
    final ArrayList<String> plotlist = new ArrayList<String>();
    final ArrayList<String> user_ratinglist = new ArrayList<String>();
    final ArrayList<String> release_datelist = new ArrayList<String>();
    final ArrayList<String> id_list = new ArrayList<String>();
    ArrayList<String> genre = new ArrayList<String>();
    ArrayList<Integer> popularity = new ArrayList<Integer>();
    ArrayList<String> language = new ArrayList<String>();
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    FragmentManager fm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realmConfig = new RealmConfiguration.Builder(getActivity()).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfig);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.recycler_view_main, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        setHasOptionsMenu(true);
        mRecyclerView.setHasFixedSize(true);
        API_KEY=getActivity().getResources().getString(R.string.API_KEY);
        fm= getFragmentManager();

        fetchsData();
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
            return v;
    }
    @Nullable
    void fetchsData()
    {
        imglist.clear();
        titlelist.clear();
        back_imglist.clear();
        plotlist.clear();
        user_ratinglist.clear();
        release_datelist.clear();
        id_list.clear();
        genre.clear();
        popularity.clear();
        language.clear();
        if (getResources().getConfiguration().orientation == 2) {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new MyAdapter1(imglist);
            mRecyclerView.setAdapter(mAdapter);

            RealmResults<Movies_Fav> result3 = realm.where(Movies_Fav.class).equalTo("fav", 1).findAll();
            if(result3.size()!=0)
            {
                for (int i=0;i<result3.size();i++)
                {
                    imglist.add(result3.get(i).getPosterImg());
                    titlelist.add(result3.get(i).getTitle());
                    back_imglist.add(result3.get(i).getBackdropImg());
                    plotlist.add(result3.get(i).getOverview());
                    user_ratinglist.add(result3.get(i).getVote_average());
                    release_datelist.add(result3.get(i).getRelease_date());
                    id_list.add(result3.get(i).getId());
                    genre.add(result3.get(i).getGenre());
                    popularity.add(result3.get(i).getPopularity());
                    language.add(result3.get(i).getLanguage());
                }
            }
            mAdapter.notifyDataSetChanged();
            jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            mRecyclerView.addOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(new HelixEffect());


        }
        else if (getResources().getConfiguration().orientation == 1) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new MyAdapter1(imglist);
            mRecyclerView.setAdapter(mAdapter);

            RealmResults<Movies_Fav> result3 = realm.where(Movies_Fav.class).equalTo("fav", 1).findAll();
            if(result3.size()!=0)
            {
                for (int i=0;i<result3.size();i++)
                {
                    imglist.add(result3.get(i).getPosterImg());
                    titlelist.add(result3.get(i).getTitle());
                    back_imglist.add(result3.get(i).getBackdropImg());
                    plotlist.add(result3.get(i).getOverview());
                    user_ratinglist.add(result3.get(i).getVote_average());
                    release_datelist.add(result3.get(i).getRelease_date());
                    id_list.add(result3.get(i).getId());
                    genre.add(result3.get(i).getGenre());
                    popularity.add(result3.get(i).getPopularity());
                    language.add(result3.get(i).getLanguage());
                }
            }
            mAdapter.notifyDataSetChanged();

            jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            mRecyclerView.addOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(new HelixEffect());


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter!=null) {

            fetchsData();


        }
    }



    @Override
    public void update() {
        if(mAdapter!=null) {

            fetchsData();


        }
        else
        {
            Toast.makeText(getActivity(),"testing", Toast.LENGTH_SHORT).show();
        }
    }
}
