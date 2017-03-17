package fingertip.android.com.fingertip.HomeFragment;

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
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class NewsDescription extends AppCompatActivity {
    ImageView poster, title;
    TextView year, language, synopsis, moTitle;
    FloatingActionButton share;
    Button knowMore;
    String mTitle, mBackdrop_Image, mOverview, mRelease_Date, mPoster_Image, mlanguage, mUrl;
    Context context;

//    float rate;
//    double d;

    String API_KEY;
    RealmConfiguration realmConfig;
    // Get a Realm instance for this thread
    Realm realm;
    //RealmList<Movies_Fav> a=new RealmList<>();
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        API_KEY = getResources().getString(R.string.API_KEY);
        realmConfig = new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfig);
        poster = (ImageView) findViewById(R.id.poster);
        year = (TextView) findViewById(R.id.year);
        title = (ImageView) findViewById(R.id.imgBack);
        language = (TextView) findViewById(R.id.language_text);
        synopsis = (TextView) findViewById(R.id.synopsis);
        share = (FloatingActionButton) findViewById(R.id.fab);
        knowMore = (Button) findViewById(R.id.knowMore);
//        r= (RatingBar) findViewById(R.id.rating);
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
            mRelease_Date = extras.getString("r_date");
            mPoster_Image = extras.getString("p_img");
            mlanguage = extras.getString("language");
            mUrl = extras.getString("url");
        }
        toolbar.setTitle(mTitle);
        Glide.with(getApplicationContext()).load(Uri.parse(mPoster_Image)).error(R.drawable.placeholder).into(poster);
        Glide.with(getApplicationContext()).load(Uri.parse(mBackdrop_Image)).error(R.drawable.placeholder).into(title);
        year.setText(mRelease_Date);
//        average.setText(mVote);
        moTitle.setText(mTitle);
        language.setText(mlanguage);
        synopsis.setText(mOverview);
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
//        trailer_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String video_path = "https://www.youtube.com/watch?v="+key;
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_path));
//                startActivity(intent);
//
//            }
//        });

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


