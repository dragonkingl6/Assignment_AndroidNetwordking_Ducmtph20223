package com.example.assignment_androidnetworking.AssAndroidNetWorking.Product;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment_androidnetworking.AssAndroidNetWorking.config.config;
import com.example.assignment_androidnetworking.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class createproducts extends Fragment {
    TextInputEditText edtname, edtprice, edtdecrip;
    Button btnadd, btnreset;

    public createproducts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createproducts, container, false);

        edtname = view.findViewById(R.id.edtname);
        edtprice = view.findViewById(R.id.edtprice);
        edtdecrip = view.findViewById(R.id.edtdescrtion);
        btnadd = view.findViewById(R.id.btnthem);
        btnreset = view.findViewById(R.id.btnhuy);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtname.getText().toString().trim();
                String price = edtprice.getText().toString().trim();
                String description = edtdecrip.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(description)) {
                    addProductToDatabase(name, price, description);
                } else {
                    Toast.makeText(requireContext(), "lỗi nhé ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtname.setText("");
                edtprice.setText("");
                edtdecrip.setText("");
            }
        });

        return view;
    }

    private void addProductToDatabase(String name, String price, String description) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = config.URL_INSERT;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Sau khi thêm sản phẩm thành công, cập nhật lại danh sách sản phẩm
                        Toast.makeText(requireContext(), "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                        edtname.setText("");
                        edtprice.setText("");
                        edtdecrip.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> mydata = new HashMap<>();
                mydata.put("name", name);
                mydata.put("price", price);
                mydata.put("description", description);
                mydata.put("username", "admin");
                mydata.put("password", "admin");
                return mydata;
            }
        };

        queue.add(request);
    }


}