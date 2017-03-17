package fingertip.android.com.fingertip.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fingertip.android.com.fingertip.R;

/**
 * Created by NILESH on 13-03-2017.
 */

public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.ViewHolder> {
    public static Context context;
    private ArrayList<String> imagelist;


    public MyAdapter4(ArrayList<String> myDataset) {
        imagelist = myDataset;


    }

    @Override
    public MyAdapter4.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_gridlayout, parent, false);

        MyAdapter4.ViewHolder vh = new MyAdapter4.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter4.ViewHolder holder, int position) {


        Glide.with(context).load(imagelist.get(position)).error(R.drawable.placeholder).into(holder.mImageView);


    }

    @Override
    public int getItemCount() {

        return imagelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView mImageView;


        public ViewHolder(View v) {
            super(v);

            mImageView = (ImageView) v.findViewById(R.id.imview);
            context = v.getContext();

        }

        @Override
        public void onClick(View view) {
            Log.i(context.getString(R.string.mainActivity), context.getString(R.string.onClick) + getAdapterPosition() + " ");
        }
    }

}

