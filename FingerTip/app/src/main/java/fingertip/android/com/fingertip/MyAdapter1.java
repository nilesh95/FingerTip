package fingertip.android.com.fingertip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.ViewHolder> {
    private ArrayList<String> imagelist;

    public static Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {




        public ImageView mImageView;


        public ViewHolder(View v) {
            super(v);

            mImageView= (ImageView) v.findViewById(R.id.imview);
            context = v.getContext();

        }
        @Override
        public void onClick(View view) {
            Log.i("MyActivity", "onClick " + getAdapterPosition() + " ");
        }
    }


    public MyAdapter1(ArrayList<String> myDataset){
        imagelist = myDataset;


    }


    @Override
    public MyAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridlayout, parent, false);

        ViewHolder vh = new ViewHolder((View) v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Glide.with(context).load(imagelist.get(position)).error(R.drawable.placeholder).into(holder.mImageView);


    }


    @Override
    public int getItemCount() {

        return imagelist.size();
    }

}

