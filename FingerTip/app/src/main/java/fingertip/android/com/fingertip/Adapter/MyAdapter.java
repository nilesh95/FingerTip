package fingertip.android.com.fingertip.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fingertip.android.com.fingertip.R;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public static Context context;
    private ArrayList<String> imagelist;
    private ArrayList<String> titlelist;


    public MyAdapter(ArrayList<String> myDataset, ArrayList<String> titleList) {
        imagelist = myDataset;
        titlelist = titleList;


    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridlayout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.mTextView.setText(titlelist.get(position));
        Glide.with(context).load(imagelist.get(position)).error(R.drawable.placeholder).into(holder.mImageView);


    }

    @Override
    public int getItemCount() {

        return imagelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView mImageView;
        public TextView mTextView;


        public ViewHolder(View v) {
            super(v);

            mImageView = (ImageView) v.findViewById(R.id.imview);
            mTextView = (TextView) v.findViewById(R.id.txtview);
            context = v.getContext();

        }

        @Override
        public void onClick(View view) {
            Log.i(context.getString(R.string.mainActivity), context.getString(R.string.onClick) + getAdapterPosition() + " ");
        }
    }

}

