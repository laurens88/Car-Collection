package com.example.carcollectiondatabase;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;

    public ProgressButton(Context context, View view){
        cardView = view.findViewById(R.id.progress_cardview);
        layout = view.findViewById(R.id.progress_constraint_layout);
        progressBar = view.findViewById(R.id.progressBar2);
        textView = view.findViewById(R.id.progress_text);
    }

    public void buttonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Loading...");
    }

    public void buttonFinished(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.teal_200));
        progressBar.setVisibility(View.GONE);
        textView.setText("Done");
    }

    public void buttonReset(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.black));
        progressBar.setVisibility(View.GONE);
        textView.setText("Lookup");
    }
}
