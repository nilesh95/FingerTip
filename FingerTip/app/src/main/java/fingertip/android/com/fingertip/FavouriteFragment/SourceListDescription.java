package fingertip.android.com.fingertip.FavouriteFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fingertip.android.com.fingertip.R;

/**
 * Created by nilnayal on 3/15/2017.
 */

public class SourceListDescription extends AppCompatActivity {
    ImageView poster, title;
    TextView year, average, synopsis, moTitle, tpopularity, tlanguage;
    FloatingActionButton share;
    String mTitle, mBackdrop_Image, mOverview, mPoster_Image, mId, mgenre, mlanguage, mUrl, mCountry;
    Context context;
    Button knowMore;

    float rate;
    double d;

    String API_KEY;
    String key;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_source);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        API_KEY = getResources().getString(R.string.API_KEY);
        poster = (ImageView) findViewById(R.id.poster);
        year = (TextView) findViewById(R.id.year);
        title = (ImageView) findViewById(R.id.imgBack);
        synopsis = (TextView) findViewById(R.id.synopsis);
        share = (FloatingActionButton) findViewById(R.id.fab);
        tpopularity = (TextView) findViewById(R.id.popularity);
        tlanguage = (TextView) findViewById(R.id.language);
        knowMore = (Button) findViewById(R.id.knowMore);
        mLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        moTitle = (TextView) findViewById(R.id.motitle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {


            mTitle = extras.getString("title");
            mBackdrop_Image = extras.getString("b_img");
            mOverview = extras.getString("overview");
            mPoster_Image = extras.getString("p_img");
            mId = extras.getString("id");
            mgenre = extras.getString("genre");
            mlanguage = extras.getString("language");
            mUrl = extras.getString("url");
            mCountry = extras.getString("country");
        }
        toolbar.setTitle(mTitle);
        Glide.with(getApplicationContext()).load(Uri.parse(mPoster_Image)).error(R.drawable.placeholder).into(poster);
        Glide.with(getApplicationContext()).load(Uri.parse(mBackdrop_Image)).error(R.drawable.placeholder).into(title);
//        average.setText(mVote);
        moTitle.setText(mTitle);
        synopsis.setText(mOverview);
        year.setText(mgenre);
        tpopularity.setText(mCountry);
        tlanguage.setText(mlanguage);
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
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mTitle + mUrl);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
