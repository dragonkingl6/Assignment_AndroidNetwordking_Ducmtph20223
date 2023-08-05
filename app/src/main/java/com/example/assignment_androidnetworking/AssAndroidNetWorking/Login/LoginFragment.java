package com.example.assignment_androidnetworking.AssAndroidNetWorking.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment_androidnetworking.AssAndroidNetWorking.Product.MainActivityProducts;
import com.example.assignment_androidnetworking.AssAndroidNetWorking.config.config;
import com.example.assignment_androidnetworking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginFragment extends Fragment {
    EditText edtusername, edtpassword;
    Button btnlogin;
    RequestQueue queue;
    private static final String LOGIN_URL = config.URL_SELECT; // Thay thế URL của API đăng nhập

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        edtusername = view.findViewById(R.id.login_user);
        edtpassword = view.findViewById(R.id.login_password);
        btnlogin = view.findViewById(R.id.login_button);
        queue = Volley.newRequestQueue(requireContext());

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtusername.getText().toString().trim();
                String password = edtpassword.getText().toString().trim();
                performLogin(username, password);
            }
        });

        return view;
    }

    private void performLogin(String username, String password) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, LOGIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray products = response.getJSONArray("products");
                            boolean isLoggedIn = false;

                            for (int i = 0; i < products.length(); i++) {
                                JSONObject product = products.getJSONObject(i);
                                String usernamen = product.getString("username");
                                String passwordn = product.getString("password");

                                if (usernamen.equals(username) && passwordn.equals(password)) {
                                    isLoggedIn = true;
                                    break;
                                }
                            }

                            if (isLoggedIn) {
                                // Đăng nhập thành công
                                SharedPreferences.Editor editor = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE).edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();
                                Intent i=new Intent(getContext(), MainActivityProducts.class);
                                startActivity(i);
                                Toast.makeText(getContext(), "oke", Toast.LENGTH_SHORT).show();
                            } else {
                                // Đăng nhập thất bại
                                Toast.makeText(getContext(), "lõi", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent i=new Intent(getContext(), MainActivityProducts.class);
            startActivity(i);
        } else {
            Toast.makeText(requireContext(), "Bạn cần đăng nhập trước", Toast.LENGTH_SHORT).show();
        }
    }

}
