package com.example.assignment_androidnetworking.AssAndroidNetWorking.Product;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment_androidnetworking.AssAndroidNetWorking.config.config;
import com.example.assignment_androidnetworking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ListProductFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Products> productList = new ArrayList<>();
    private Button btnlogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listproducts, container, false);
        btnlogout = view.findViewById(R.id.logoutButton);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                getActivity().finish();
            }
        });
        recyclerView = view.findViewById(R.id.rcvds);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        productAdapter = new ProductAdapter(productList, requireContext());
        recyclerView.setAdapter(productAdapter);

        // Thực hiện request và cập nhật danh sách sản phẩm trong onResponse
        performProductRequest();


        return view;
    }

    private void performProductRequest() {
        if (productList.isEmpty()) { // Kiểm tra danh sách sản phẩm đã có dữ liệu chưa
            RequestQueue queue = Volley.newRequestQueue(requireContext());
            String url = config.URL_SELECT;
            JsonObjectRequest request = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray productsArray = response.getJSONArray("products");
                                productList.clear();
                                updateProductList(productsArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "lỗi dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(request);
        }
    }

    private void updateProductList(JSONArray productsArray) {
        for (int i = 0; i < productsArray.length(); i++) {
            try {
                JSONObject productObject = productsArray.getJSONObject(i);
                Products product = new Products();
                product.setPid(productObject.getString("pid"));
                product.setName(productObject.getString("name"));
                product.setPrice(productObject.getString("price"));
                product.setDescription(productObject.getString("description"));
                product.setUsername(productObject.getString("username"));
                product.setPassword(productObject.getString("password"));
                productList.add(product);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        productList.clear();
        performProductRequest();
    }
}

