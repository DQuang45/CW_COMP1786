package com.example.hikermanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ObservationAdapter
        extends RecyclerView.Adapter<
        ObservationAdapter.ObservationViewHolder> {

    private List<Observation> observationList;

    private final OnObservationClickListener listener;

    public interface OnObservationClickListener {

        void onObservationClick(
                Observation observation
        );
    }

    public ObservationAdapter(
            List<Observation> observationList,
            OnObservationClickListener listener
    ) {

        this.observationList = observationList;

        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_observation,
                        parent,
                        false
                );

        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ObservationViewHolder holder,
            int position
    ) {

        Observation observation = observationList.get(position);

        holder.txtObservationContent.setText(
                observation.getObservation()
        );

        holder.txtObservationTime.setText(
                "Time: "
                        + observation.getObservationTime()
        );

        String comment =
                observation.getComment();

        if (comment == null ||
                comment.trim().isEmpty()) {

            holder.txtObservationComment.setText(
                    "Comment: No comment"
            );

        } else {

            holder.txtObservationComment.setText(
                    "Comment: " + comment
            );
        }

        holder.itemView.setOnClickListener(v -> {

            listener.onObservationClick(
                    observation
            );
        });
    }

    @Override
    public int getItemCount() {

        if (observationList == null) {
            return 0;
        }

        return observationList.size();
    }

    public void updateData(
            List<Observation> newList
    ) {

        observationList = newList;

        notifyDataSetChanged();
    }

    static class ObservationViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtObservationContent;
        TextView txtObservationTime;
        TextView txtObservationComment;

        public ObservationViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtObservationContent =
                    itemView.findViewById(
                            R.id.txtObservationContent
                    );

            txtObservationTime =
                    itemView.findViewById(
                            R.id.txtObservationTime
                    );

            txtObservationComment =
                    itemView.findViewById(
                            R.id.txtObservationComment
                    );
        }
    }
}