package com.moutimid.vellarentapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.activities.Authentication.LoginActivity;
import com.moutimid.vellarentapp.helper.Constants;
import com.moutimid.vellarentapp.model.UserModel;

public class ProfileFragment extends Fragment {
    ImageView profile_img;
    TextView name, dob, email, phone_number;
    String userID;
    Button logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile2, container, false);
        logout = view.findViewById(R.id.logout);
        profile_img = view.findViewById(R.id.profile_pic);
        name = view.findViewById(R.id.name);
        dob = view.findViewById(R.id.dob);
        email = view.findViewById(R.id.email);
        phone_number = view.findViewById(R.id.phone_number);
        userID = Stash.getString("userID");
        Constants.UserReference.child("uMOq2o9REveNcVDynlcyAHalaQH3").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userNew = snapshot.getValue(UserModel.class);
                name.setText(userNew.getName());
                dob.setText(userNew.dob);
                email.setText(userNew.email);
                phone_number.setText(userNew.phone_number);
                Glide.with(getContext()).load(userNew.image_url).into(profile_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });

        return view;
    }


}