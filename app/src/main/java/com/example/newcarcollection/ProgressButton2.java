package com.example.newcarcollection;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.laurens.newcarcollection.R;

public class ProgressButton2 {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;

    public ProgressButton2(Context context, View view){
        cardView = view.findViewById(R.id.progress_cardview_update);
        layout = view.findViewById(R.id.progress_constraint_layout_update);
        progressBar = view.findViewById(R.id.progressBar2_update);
        textView = view.findViewById(R.id.progress_text_update);
    }

    public void buttonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("");
    }

    public void buttonFinished(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.teal_200));
        progressBar.setVisibility(View.GONE);
        textView.setText("DONE");
    }

    public void buttonReset(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.black));
        progressBar.setVisibility(View.GONE);
        textView.setText("UPDATE");
    }
}
