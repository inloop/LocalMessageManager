package eu.inloop.localmessagemanager.sample.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.inloop.localmessagemanager.LocalMessageManager;
import eu.inloop.localmessagemanager.sample.MyCustomObject;
import eu.inloop.localmessagemanager.sample.R;

public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_fire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalMessageManager.getInstance().sendEmptyMessage(R.id.msg_sample_event);
            }
        });

        view.findViewById(R.id.btn_fire_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalMessageManager.getInstance().sendMessage(R.id.msg_custom_payload_event, new MyCustomObject());
            }
        });
    }
}
