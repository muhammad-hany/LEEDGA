package com.leedga.seagate.leedga;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.leedga.seagate.leedga.REF.chapterNames;

/**
 * Created by Muhammad Workstation on 31/10/2016.
 */

public class LessonsRecyclerAdapter extends RecyclerView.Adapter<LessonsRecyclerAdapter.ViewHolder> {
    private LessonsItemClick listener;
    private Context context;

    public LessonsRecyclerAdapter(Context context, LessonsItemClick listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card, parent, false);
        return new CardHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardHolder cardHolder = (CardHolder) holder;
        cardHolder.bind(position, listener);
        cardHolder.textView.setText(chapterNames[position]);
        switch (position) {
            case 0:

                cardHolder.icon.setImageResource(R.drawable.ic_1_);
                break;
            case 1:
                cardHolder.icon.setImageResource(R.drawable.ic_2_);
                break;
            case 2:
                cardHolder.icon.setImageResource(R.drawable.ic_3_);
                break;
            case 3:
                cardHolder.icon.setImageResource(R.drawable.ic_4_);
                break;
            case 4:
                cardHolder.icon.setImageResource(R.drawable.ic_5_);
                break;
            case 5:
                cardHolder.icon.setImageResource(R.drawable.ic_6_);
                break;
            case 6:
                cardHolder.icon.setImageResource(R.drawable.ic_7_);
                break;
            case 7:
                cardHolder.icon.setImageResource(R.drawable.ic_8_);
                break;
            case 8:
                cardHolder.icon.setImageResource(R.drawable.ic_9_);
                break;
            case 9:
                cardHolder.icon.setImageResource(R.drawable.ic_10_);
                break;
            case 10:
                cardHolder.icon.setImageResource(R.drawable.ic_11_);
                break;
            case 11:
                cardHolder.icon.setImageResource(R.drawable.ic_12_);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface LessonsItemClick {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    public class CardHolder extends LessonsRecyclerAdapter.ViewHolder {
        ImageView icon;
        TextView textView;
        CardView cardView;

        public CardHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView15);
            cardView = (CardView) itemView.findViewById(R.id.card);
        }


        public void bind(final int position, final LessonsItemClick onMyItemClick) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMyItemClick.onItemClick(position);
                }
            });
        }
    }


}
