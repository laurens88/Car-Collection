package com.example.newcarcollection;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.laurens.newcarcollection.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingAnimation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
