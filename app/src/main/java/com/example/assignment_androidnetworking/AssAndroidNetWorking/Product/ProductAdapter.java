package com.example.assignment_androidnetworking.AssAndroidNetWorking.Product;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment_androidnetworking.AssAndroidNetWorking.config.config;
import com.example.assignment_androidnetworking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Products> productList;
    private Context context;

    public ProductAdapter(List<Products> productList, Context context) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText(product.getPrice());
        holder.textViewDescription.setText(product.getDescription());
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProductDialog(product);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deleteProduct(product);


            }
        });
    }
    private void showEditProductDialog(Products product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_edit_product, null);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        editTextName.setText(product.getName());
        editTextPrice.setText(product.getPrice());
        editTextDescription.setText(product.getDescription());

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin đã sửa từ các EditText
                String newName = editTextName.getText().toString();
                String newPrice = editTextPrice.getText().toString();
                String newDescription = editTextDescription.getText().toString();

                // Update thông tin sản phẩm trong cơ sở dữ liệu hoặc thông báo cho Fragment cần biết
                updateProductInDatabase(product.getPid(), newName, newPrice, newDescription);

                // Đóng dialog sau khi hoàn thành
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void deleteProduct(Products product) {
        RequestQueue queue= Volley.newRequestQueue(context);
        String url=config.URL_DELETE;
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "đã xóa sản phẩm có tên:"+product.getName(), Toast.LENGTH_SHORT).show();
                        productList.remove(product);
                        loadProductList();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "lỗi xóa", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> mydata=new HashMap<>();
                mydata.put("pid",product.getPid());
                return mydata;
            }
        };
        //b5. truyen tham so (neu co)
        //b6. thuc thi
        queue.add(request);
    }
    private void updateProductInDatabase(String pid, String newName, String newPrice, String newDescription) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = config.URL_UPDATE;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadProductList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pid", pid);
                params.put("name", newName);
                params.put("price", newPrice);
                params.put("description", newDescription);
                return params;
            }
        };

        queue.add(request);
    }



    private void loadProductList() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = config.URL_SELECT;

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray productsArray = response.optJSONArray("products");
                updateProductList(productsArray);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Xử lý lỗi
            }
        });

        queue.add(request);
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

                // Kiểm tra xem sản phẩm đã tồn tại trong danh sách chưa
                boolean isExisting = false;
                for (Products existingProduct : productList) {
                    if (existingProduct.getPid().equals(product.getPid())) {
                        // Sản phẩm đã tồn tại, cập nhật thông tin
                        existingProduct.setName(product.getName());
                        existingProduct.setPrice(product.getPrice());
                        existingProduct.setDescription(product.getDescription());
                        existingProduct.setUsername(product.getUsername());
                        existingProduct.setPassword(product.getPassword());
                        isExisting = true;
                        break;
                    }
                }

                if (!isExisting) {
                    // Sản phẩm mới, thêm vào danh sách
                    productList.add(product);
                }

                notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewDescription, textViewUsername, textViewPassword;
        Button buttonEdit, buttonDelete;
        ProductViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

        }
    }
}
