package fingertip.android.com.fingertip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by NILESH on 11-03-2017.
 */

public class ListActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private ListView mListView;
    private Button mButton;
    private ArrayList<String> selectedString;
    private int selectedCount;
    private ProgressBar mProgressBar;
    // Initializing a new list
    final List<String> trees = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_main);

        selectedString = new ArrayList<String>();
        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = ListActivity.this;

        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.r1);
        mListView = (ListView) findViewById(R.id.lv);

        mButton = (Button) findViewById(R.id.buttonSubmit);


        fetchSourceList();

        // Initialize a new ArrayAdapter
        adapter = new ArrayAdapter(mActivity,android.R.layout.simple_list_item_multiple_choice,trees);

        // Set the adapter for ListView
        mListView.setAdapter(adapter);


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Set an item click listener for the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray clickedItemPositions = mListView.getCheckedItemPositions();

                selectedCount = 0;
                for(int index=0;index<clickedItemPositions.size();index++){
                    // Get the checked status of the current item
                    boolean checked = clickedItemPositions.valueAt(index);
                    if(checked){
                        // If the current item is checked
                        int key = clickedItemPositions.keyAt(index);
                        mButton.setEnabled(true);
                    }
                    else
                    {
                        selectedCount++;
                    }
                }
                if(selectedCount==clickedItemPositions.size())
                {
                    mButton.setEnabled(false);
                }
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray clickedItemPositions = mListView.getCheckedItemPositions();


                for(int index=0;index<clickedItemPositions.size();index++){
                    // Get the checked status of the current item
                    boolean checked = clickedItemPositions.valueAt(index);

                    Log.i("Info","Value called In Loop");
                    if(checked){
                        // If the current item is checked
                        int key = clickedItemPositions.keyAt(index);
                        Log.i("Info","Value called");
                        selectedString.add(trees.get(key));

                    }
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putStringArrayListExtra("ArrayList" , selectedString);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchSourceList() {
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

                                trees.add(jsonObject.getString("id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                        }
                        adapter = new ArrayAdapter(mActivity,android.R.layout.simple_list_item_multiple_choice,trees);

                        // Set the adapter for ListView
                        mListView.setAdapter(adapter);
                        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);
    }
}
