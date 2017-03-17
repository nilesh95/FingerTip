package fingertip.android.com.fingertip.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fingertip.android.com.fingertip.R;

/**
 * Created by NILESH on 05-06-2016.
 */
public class TabDescription extends Fragment {
    ImageView poster, title;
    TextView year, synopsis, moTitle, language;
    FloatingActionButton share;
    String mTitle, mBackdrop_Image, mOverview, mVote, mRelease_Date, mPoster_Image, mId, mUrl, mlanguage;
    Integer mpopularity;
    Context context;
    Button knowMore;

    View view;

    String API_KEY;
    RefreshGrid refreshGrid;
    private LinearLayoutManager mLayoutManager;

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
        mTitle = getArguments().getString("title");
        mBackdrop_Image = getArguments().getString("b_img");
        mOverview = getArguments().getString("overview");
        mRelease_Date = getArguments().getString("r_date");
        mPoster_Image = getArguments().getString("p_img");
        mlanguage = getArguments().getString("language");
        mUrl = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_scrolling, container, false);

        context = getActivity();
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        API_KEY = getResources().getString(R.string.API_KEY);
        poster = (ImageView) view.findViewById(R.id.poster);
        year = (TextView) view.findViewById(R.id.year);
        title = (ImageView) view.findViewById(R.id.imgBack);
        synopsis = (TextView) view.findViewById(R.id.synopsis);
        share = (FloatingActionButton) view.findViewById(R.id.fab);
        language = (TextView) view.findViewById(R.id.language_text);
        knowMore = (Button) view.findViewById(R.id.knowMore);
//        r= (RatingBar) findViewById(R.id.rating);
        mLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        moTitle = (TextView) view.findViewById(R.id.motitle);

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
        language.setText(mlanguage);
//        trailer_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String video_path = "https://www.youtube.com/watch?v="+key;
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_path));
//                startActivity(intent);
//
//            }
//        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, mTitle + mUrl);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, mOverview);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                startActivity(browserIntent);
            }
        });
        return view;
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

    public interface RefreshGrid {
        void refreshFavGrid();
    }
}



