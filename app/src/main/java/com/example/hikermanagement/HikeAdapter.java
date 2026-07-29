package com.example.hikermanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HikeAdapter
        extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private List<Hike> hikeList;

    private final OnHikeClickListener listener;

    public interface OnHikeClickListener {

        void onHikeClick(Hike hike);
    }

    public HikeAdapter(
            List<Hike> hikeList,
            OnHikeClickListener listener
    ) {

        this.hikeList = hikeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_hike,
                        parent,
                        false
                );

        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull HikeViewHolder holder,
            int position
    ) {

        Hike hike = hikeList.get(position);

        holder.txtHikeName.setText(
                hike.getName()
        );

        holder.txtHikeLocation.setText(
                hike.getLocation()
        );

        String dateAndLength =
                hike.getDate()
                        + " • "
                        + hike.getLength()
                        + " km";

        holder.txtHikeDateLength.setText(
                dateAndLength
        );

        holder.txtHikeDifficulty.setText(
                "Difficulty: " + hike.getDifficulty()
        );

        holder.btnDetails.setOnClickListener(v -> {

            listener.onHikeClick(hike);

        });

        holder.itemView.setOnClickListener(v -> {

            listener.onHikeClick(hike);

        });
    }

    @Override
    public int getItemCount() {

        if (hikeList == null) {
            return 0;
        }

        return hikeList.size();
    }

    public void updateData(List<Hike> newHikeList) {

        this.hikeList = newHikeList;

        notifyDataSetChanged();
    }

    public static class HikeViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtHikeName;
        TextView txtHikeLocation;
        TextView txtHikeDateLength;
        TextView txtHikeDifficulty;

        Button btnDetails;

        public HikeViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtHikeName =
                    itemView.findViewById(
                            R.id.txtHikeName
                    );

            txtHikeLocation =
                    itemView.findViewById(
                            R.id.txtHikeLocation
                    );

            txtHikeDateLength =
                    itemView.findViewById(
                            R.id.txtHikeDateLength
                    );

            txtHikeDifficulty =
                    itemView.findViewById(
                            R.id.txtHikeDifficulty
                    );

            btnDetails =
                    itemView.findViewById(
                            R.id.btnDetails
                    );
        }
    }
}