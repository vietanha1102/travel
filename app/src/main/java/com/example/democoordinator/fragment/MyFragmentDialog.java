package com.example.democoordinator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.democoordinator.R;
import com.example.democoordinator.model.Place;
import com.example.democoordinator.roomdatabase.PlaceDatabase;

public class MyFragmentDialog extends DialogFragment {
    EditText edtName, edtLocation, edtDetail;
    CheckBox favourite;
    RatingBar ratingBar;
    Button btnDone, btnClose;
    String nameTemp, locationTemp, detailTemp;
    boolean favouriteTemp;
    float ratingTemp;
    IsendData mIsendData;
    long insertId;


    public interface IsendData {
        void sendData(Place newPlace);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIsendData = (IsendData) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_add_dialog, container);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtName = view.findViewById(R.id.edtName);
        edtLocation = view.findViewById(R.id.edtLocation);
        edtDetail = view.findViewById(R.id.edtDetail);
        favourite = view.findViewById(R.id.cbFavourite);
        ratingBar = view.findViewById(R.id.ratingBar);
        btnDone = view.findViewById(R.id.btnDone);
        btnClose = view.findViewById(R.id.btnClose);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTemp = edtName.getText().toString().trim();
                locationTemp = edtLocation.getText().toString().trim();
                detailTemp = edtDetail.getText().toString().trim();
                favouriteTemp = favourite.isChecked();
                ratingTemp = ratingBar.getRating();

                if (TextUtils.isEmpty(nameTemp) || TextUtils.isEmpty(locationTemp)
                        || TextUtils.isEmpty(detailTemp) || ratingTemp == 0) {
                    Toast.makeText(getContext(), "Xin moi nhap du", Toast.LENGTH_SHORT).show();
                    return;
                }
                new doSave().start();

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private class doSave extends Thread {
        @Override
        public void run() {
            super.run();
            Place dataPlace = new Place(nameTemp, locationTemp, detailTemp, ratingTemp, favouriteTemp);
            insertId = PlaceDatabase.getInstance(getContext()).placeDAO().insertReturnId(dataPlace);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), "Them Thanh Cong", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        }
    }

    private class getDataById extends Thread {
        @Override
        public void run() {
            super.run();
            Place place = PlaceDatabase.getInstance(getContext()).placeDAO().getPlaceById((int) insertId);
            mIsendData.sendData(place);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (insertId != 0) {
            new getDataById().start();
        }


    }
}
