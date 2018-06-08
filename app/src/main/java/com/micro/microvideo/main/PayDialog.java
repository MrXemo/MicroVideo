package com.micro.microvideo.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.micro.microvideo.R;

/**
 * Created by William on 2018/6/3.
 */

public class PayDialog extends DialogFragment {

    WarnListener mWarnListener;

    public interface WarnListener {
        void affirmListener();
    }

    public void setWarnListener(WarnListener warnListener) {
        mWarnListener = warnListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dailog_pay, container, false);

        TextView sure = (TextView) view.findViewById(R.id.sure);
        ImageView close = (ImageView) view.findViewById(R.id.close);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWarnListener != null) {
                    mWarnListener.affirmListener();
                    dismiss();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
