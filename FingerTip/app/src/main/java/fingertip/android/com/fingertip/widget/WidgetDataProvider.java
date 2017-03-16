package fingertip.android.com.fingertip.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

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
import java.util.List;

import fingertip.android.com.fingertip.MyAdapter;
import fingertip.android.com.fingertip.R;

/**
 * Created by nilnayal on 3/16/2017.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    Context mContext = null;
    private Cursor data = null;
    private List<String> imgList;
    private List<String> titleList;
    String API_KEY;
    private int min_count = 4;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        API_KEY = context.getResources().getString(R.string.API_KEY);
        imgList = new ArrayList<String>();
        titleList = new ArrayList<String>();
    }

    @Override
    public void onCreate() {
    }


    public void onDataSetChanged() {

        initData();

    }

    @Override
    public void onDestroy() {
        if (imgList != null && titleList !=null) {
            imgList.clear();
            titleList.clear();
        }
    }

    @Override
    public int getCount() {
            return min_count;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION ||titleList.size() == 0 || !(titleList.size()>position)) {
            return null;
        }

        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_collection_item);

        view.setTextViewText(R.id.bid_price, titleList.get(position));

        view.setImageViewUri(R.id.stock_symbol, Uri.parse(imgList.get(position)));

        final Intent fillInIntent = new Intent();

        view.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        if (imgList != null && titleList !=null) {
            imgList.clear();
            titleList.clear();
        }

        final long token = Binder.clearCallingIdentity();

        final String url = "https://newsapi.org/v1/articles?source=techcrunch&sortBy=top&apiKey=" + API_KEY;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                            JSONArray json = response.getJSONArray("articles");
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jsonObject = json.getJSONObject(i);
                                imgList.add(jsonObject.getString("urlToImage"));
                                titleList.add(jsonObject.getString("title"));

                            }
                            Log.i("Widget","InitStep 3 "+imgList.size()+" "+titleList.size());
                            Binder.restoreCallingIdentity(token);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(mContext).add(jsonRequest);
    }


}
