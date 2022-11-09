package com.example.volley_recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Usuario> listaUsuario;
    private RequestQueue rq;
    private RecyclerView rv1;
    private AdaptadorUsuario adaptadorUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaUsuario = new ArrayList<>();
        rq= Volley.newRequestQueue(this);
        rv1 = findViewById(R.id.rv1);

        for (int i=0;i<100;i++)  cargarPersona();

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        rv1.setLayoutManager(linearLayoutManager);
        adaptadorUsuario = new AdaptadorUsuario();
        rv1.setAdapter(adaptadorUsuario);
    }

    private void cargarPersona() {
        String url="https://randomuser.me/api/";
        JsonObjectRequest requerimiento = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String valor=response.get("results").toString();
                            JSONArray arreglo = new JSONArray(valor);
                            JSONObject objeto = new JSONObject(arreglo.get(0).toString());
                            String mail = objeto.getString("email");
                            String nombre = objeto.getJSONObject("name").getString("last");
                            String foto = objeto.getJSONObject("picture").getString("large");
                            Usuario usuario = new Usuario(nombre,mail,foto);
                            listaUsuario.add(usuario);

                            adaptadorUsuario.notifyItemRangeInserted(listaUsuario.size(),1);
                            //carga de datos

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        rq.add(requerimiento);

    }

    private class AdaptadorUsuario extends RecyclerView.Adapter<AdaptadorUsuario.AdaptadorUsuarioHolder>{
        @NonNull
        @Override
        public AdaptadorUsuario.AdaptadorUsuarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            
            return new AdaptadorUsuarioHolder(getLayoutInflater().inflate(R.layout.layout_tarjeta,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorUsuario.AdaptadorUsuarioHolder holder, int position) {
            holder.imprimir(position);

        }

        @Override
        public int getItemCount() {
            return listaUsuario.size();
        }

        public class AdaptadorUsuarioHolder extends RecyclerView.ViewHolder {
            TextView tvnombre,tvmail;
            ImageView ivfoto;
            public AdaptadorUsuarioHolder(@NonNull View itemView) {
                super(itemView);
                tvnombre= itemView.findViewById(R.id.tvnombre);
                tvmail= itemView.findViewById(R.id.tvmail);
                ivfoto = itemView.findViewById(R.id.ivfoto);
            }

            public void imprimir(int position) {
                tvnombre.setText("Nombre: "+listaUsuario.get(position).getNombre());
                tvmail.setText("Mail: "+listaUsuario.get(position).getMail());
                recuperarImagen(listaUsuario.get(position).getFoto().toString(),ivfoto);
            }
            public void recuperarImagen(String foto, ImageView iv){

                ImageRequest peticion = new ImageRequest(foto,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                iv.setImageBitmap(response);
                            }
                        },
                        0,
                        0,
                        null,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                rq.add(peticion);
            }
        }
    }
}