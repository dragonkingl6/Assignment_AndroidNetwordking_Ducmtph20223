package com.example.assignment_androidnetworking.AssAndroidNetWorking.Register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment_androidnetworking.AssAndroidNetWorking.config.config;
import com.example.assignment_androidnetworking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {
    private ImageView imageView;
    private EditText edtusername, edtpass, edtrepass;
    private Button btnsignup;
    RequestQueue queue;
    private static final String REGISTER_URL = config.URL_INSERT; // Thay thế URL của API đăng nhập
    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_register, container, false);
       imageView = view.findViewById(R.id.img_signup);
         edtusername = view.findViewById(R.id.signup_username);
            edtpass = view.findViewById(R.id.signup_password);
            edtrepass = view.findViewById(R.id.signup_confirmpass);
            btnsignup = view.findViewById(R.id.signup_button);
        queue = Volley.newRequestQueue(requireContext());
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtusername.getText().toString().trim();
                String password = edtpass.getText().toString().trim();
                performRegistration(username, password);
            }
        });

        return view;
    }
    private void performRegistration(String username, String password) {
        if (username.isEmpty() || password.isEmpty() ) {
            Toast.makeText(getContext(), "nhập đầy đủ hộ đi", Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int success = jsonResponse.getInt("success");
                            if (success == 1) {
                                // Đăng kí thành công
                                Toast.makeText(getContext(), "oke", Toast.LENGTH_SHORT).show();
                                edtusername.setText("");
                                edtpass.setText("");
                                edtrepass.setText("");

                            } else {
                                // Đăng kí thất bại
                                Toast.makeText(getContext(), "lỗi", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "đăng ký lõi", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> requestData = new HashMap<>();
                requestData.put("name", "no name");
                requestData.put("price", "no phone");
                requestData.put("description", "not infomation");
                requestData.put("username", username);
                requestData.put("password", password);
                return requestData;
            }
        };

        queue.add(request);
    }
}