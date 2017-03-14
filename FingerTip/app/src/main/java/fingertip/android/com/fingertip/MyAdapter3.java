package fingertip.android.com.fingertip;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by NILESH on 21-05-2016.
 */
public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.ViewHolder> {
    private ArrayList<String> keylist;
    private ArrayList<String> namelist;
    private ArrayList<String> thumblist;
    public static Context context;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    int a,b;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mKeyView;


        public ViewHolder(View v) {
            super(v);

            mImageView= (ImageView) v.findViewById(R.id.trailer_image);
            mTextView= (TextView) v.findViewById(R.id.title_text);
            context = v.getContext();

        }

        @Override
        public void onClick(View view) {
            Log.i("MyActivity", "onClick " + getAdapterPosition() + " ");
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter3(ArrayList<String> myDataset, ArrayList<String> myDataset2){
        this.thumblist = myDataset;
        this.namelist= myDataset2;



    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_layout, parent, false);

        ViewHolder vh = new ViewHolder((View) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Glide.with(context).load(Uri.parse(thumblist.get(position))).error(R.drawable.placeholder).into(holder.mImageView);
        holder.mTextView.setText(namelist.get(position));

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return thumblist.size();
    }

}