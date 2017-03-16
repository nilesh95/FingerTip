package fingertip.android.com.fingertip;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ReadFromDB";
    List<String> sourceList;
    ValueEventListener valueEventListener;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<String> selectedList;
    Bundle extras;
    public CircleImageView messengerImageView;
    TextView mName,mEmail;
    private static final int REQUEST_INVITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        extras = getIntent().getExtras();
        if (extras != null) {
            selectedList = extras.getStringArrayList("ArrayList");
        }

        updateDB();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage(getString(R.string.invitation_message))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setElevation(0);

        navigationView.setCheckedItem(R.id.home);
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            Fragment fragment = Home_Feed_Fragment.class.newInstance();
            fragment.setArguments(extras);
            fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        View navHeaderView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(navHeaderView);

        messengerImageView = (CircleImageView) navHeaderView.findViewById(R.id.imageView);
        mName = (TextView) navHeaderView.findViewById(R.id.nameView);
        mEmail = (TextView) navHeaderView.findViewById(R.id.textView);

        Glide.with(this)
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .into(messengerImageView);

        mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        mName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    private void updateDB() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.setValue(selectedList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.fav_topic) {
            // Handle the camera action
            fragmentClass=FavouriteZone.class;
            Log.i("Called","first");
        } else if (id == R.id.trending_zone) {
            fragmentClass=Trending.class;
            Log.i("Called","second");
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.home) {
            fragmentClass=Home_Feed_Fragment.class;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();
            Log.i("Called","third");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragment.setArguments(extras);
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            Log.i("Called","fourth");
        }
        // Highlight the selected item, update the title, and close the drawer
        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<List<String>> value = new GenericTypeIndicator<List<String>>() {
                };
                sourceList = dataSnapshot.getValue(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        // Read from the database
        myRef.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(sourceList!=null)
        sourceList.clear();
        FirebaseDatabase.getInstance().getReference().removeEventListener(valueEventListener);
    }
}
