package com.example.shubham.simplechatapp.Fragment;

/**
 * Created by Shubham on 02-09-2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.simplechatapp.Activity.Register;
import com.example.shubham.simplechatapp.Activity.Users;
import com.example.shubham.simplechatapp.Model.User_Details;
import com.example.shubham.simplechatapp.R;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private ImageView profile_pic = null;
    private TextView tv = null;
    private TextView name = null;
    private TextView facebookLink = null;
    private TextView email = null;
    private Button button = null;
    private Button logout_btn = null;
    private Profile profile = null;
    Button registerToChat;
    String imguri;
    String username;
    String userid;
//    Toolbar tb;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        profile_pic = (ImageView) view.findViewById(R.id.profile_image);
        tv = (TextView) view.findViewById(R.id.username);
        name = (TextView) view.findViewById(R.id.user_name_tv);
        facebookLink = (TextView) view.findViewById(R.id.user_facebook_tv);
        email = (TextView) view.findViewById(R.id.user_email_tv);
        button = (Button) view.findViewById(R.id.goto_chat_btn);
        logout_btn = (Button) view.findViewById(R.id.logout_btn);
        registerToChat = (Button) view.findViewById(R.id.register_to_chat_btn);

//        tb = (Toolbar) view.findViewById(R.id.toolbar);
//        Toolbar tb = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);

        context = getActivity();

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Firebase.setAndroidContext(context);

        Bundle bundle = getArguments();

        if (bundle != null) {
            profile = (Profile) bundle.getParcelable(LoginFragment.PARCEL_KEY);
        } else {
            profile = Profile.getCurrentProfile();
        }


        tv.setText(profile.getName());
        User_Details.username=profile.getName();


        name.setText(profile.getName());
        facebookLink.setText(profile.getLinkUri().toString());


        Picasso.with(getActivity()).load(profile.getProfilePictureUri(400, 400).toString()).into(profile_pic);

        registerToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = profile.getId();
                username = profile.getName();
                imguri = profile.getProfilePictureUri(400,400).toString();

                final ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("Loading...");
                pd.show();

                String url = "https://mychatappproject-2ea71.firebaseio.com/users.json";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        Firebase reference = new Firebase("https://mychatappproject-2ea71.firebaseio.com/users");

                        if(s.equals("null")) {
                            reference.child(username).child("userID").setValue(userid);
                            reference.child(username).child("userImgUri").setValue(imguri);
                            Toast.makeText(context, "registration successful", Toast.LENGTH_LONG).show();
                            User_Details.username=username;
                        }
                        else {
                            try {
                                JSONObject obj = new JSONObject(s);

                                if (!obj.has(username)) {
                                    reference.child(username).child("userID").setValue(userid);
                                    reference.child(username).child("userImgUri").setValue(imguri);
                                    Toast.makeText(context, "registration successful", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "username already exists", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        pd.dismiss();
                    }

                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError );
                        pd.dismiss();
                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(context);
                rQueue.add(request);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Users.class);
                startActivity(intent);
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(user_pro_pg_menu);
//        tb.getMenu();
//        tb.setOnMenuItemClickListener(
//                new Toolbar.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        return onOptionsItemSelected(item);
//                    }
//                });
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        tb.inflateMenu(R.menu.user_pro_pg_menu);
//        tb.setOnMenuItemClickListener(
//                new Toolbar.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        return onOptionsItemSelected(item);
//                    }
//                });
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.settings_overflow:
//                logout();
//                Toast.makeText(context, "Logout Selected!!", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
////        if (item.getItemId() == android.R.id.home) {
////            finish();
////        }
//        return true;
//    }

    private void logout() {
        LoginManager.getInstance().logOut();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new LoginFragment());
        fragmentTransaction.commit();
    }


}
