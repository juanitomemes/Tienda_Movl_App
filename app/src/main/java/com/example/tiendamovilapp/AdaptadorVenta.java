package com.example.tiendamovilapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorVenta extends RecyclerView.Adapter<MyViewHolderVenta> {
    private Context context;
    private List<DataClass> dataList;
    private ProductoSeleccionadoCallback mProductoSeleccionadoCallback;

    public AdaptadorVenta(Context context, List<DataClass> dataList, ProductoSeleccionadoCallback productoSeleccionadoCallback) {
        this.mProductoSeleccionadoCallback = productoSeleccionadoCallback;
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderVenta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        return new MyViewHolderVenta(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderVenta holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recCodigo.setText(dataList.get(position).getDataCodigo());
        holder.recTitulo.setText(dataList.get(position).getDataNom());
        holder.recPrecio.setText(String.valueOf(dataList.get(position).getDataPrec()));

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductoSeleccionadoCallback.productoSeleccionado(dataList.get(position));

                Intent intent = new Intent(context, DetalleProducto.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Producto", dataList.get(holder.getAdapterPosition()).getDataNom());
                intent.putExtra("Precio", dataList.get(holder.getAdapterPosition()).getDataPrec());
                intent.putExtra("Codigo", dataList.get(holder.getAdapterPosition()).getDataCodigo());
                intent.putExtra("key", dataList.get(holder.getAdapterPosition()).getKey());

               // context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolderVenta extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitulo, recCodigo, recPrecio;
    CardView recCard;

    public MyViewHolderVenta(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recTitulo = itemView.findViewById(R.id.recTitulo);
        recCodigo = itemView.findViewById(R.id.recCodigo);
        recPrecio = itemView.findViewById(R.id.recPrecio);

    }
}
