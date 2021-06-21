package com.example.democoordinator.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.democoordinator.R;
import com.example.democoordinator.model.Place;
import com.example.democoordinator.roomdatabase.PlaceDatabase;

import java.util.List;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.ViewHolder> {
    List<Place> mList;
    Context context;
    Place tempPlace;

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AdapterMain(List<Place> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public AdapterMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_demo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterMain.ViewHolder holder, int position) {
        Place mPlace = mList.get(position);
        holder.tvName.setText(mPlace.getName());
        holder.tvDesc.setText(mPlace.getLocation());
        holder.tvRate.setText(mPlace.getRate() + "");
        holder.checkBoxDemo.setChecked(mPlace.isFavourite());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, holder.itemView, position, 0);
            }
        });

        holder.checkBoxDemo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlace.setFavourite(isChecked);
                tempPlace = mPlace;
                Log.d("TAG", "check mPlace id" + mPlace.getId());
                Log.d("TAG", "check tempPlace id" + tempPlace.getId());
                new updateSttFavourite().start();

            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvRate;
        CheckBox checkBoxDemo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameDemo);
            tvDesc = itemView.findViewById(R.id.tvDesDemo);
            tvRate = itemView.findViewById(R.id.tvRateDemo);
            checkBoxDemo = itemView.findViewById(R.id.checkBoxDemo);
        }
    }


    private class updateSttFavourite extends Thread {
        @Override
        public void run() {
            super.run();
            PlaceDatabase.getInstance(context).placeDAO().updateSttFavorite(tempPlace.isFavourite(), tempPlace.getId());
        }
    }
}
