package com.example.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.example.activity.MainActivity;
import com.example.utils.FileUtils;
import com.vigorchip.treadmill.wr2.R;

/**
 * 加油,qimaisi
 */
public class DialogRefuel implements View.OnClickListener {
    AlertDialog dialog;
    Button confirm_refueling, confirm_abolish;

    public void creatDialog(Context context) {
        if (dialog != null)
            if (dialog.isShowing())
                return;
        dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_refuel);
        confirm_refueling = (Button) dialog.findViewById(R.id.confirm_refueling);
        confirm_abolish = (Button) dialog.findViewById(R.id.confirm_abolish);
        confirm_refueling.setOnClickListener(this);
        confirm_abolish.setOnClickListener(this);
    }

    public void dimss() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_refueling:
                FileUtils.writeText2File(MainActivity.getDATAPATH(), "0" + " " + "0" + " " + "0");
                dimss();
                break;
            case R.id.confirm_abolish:
                dimss();
                break;
        }
    }
}