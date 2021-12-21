package sebastian.woowup.reaprovisionamientodeproductos.src.usecases.estimadoDeRecompra;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sebastian.woowup.reaprovisionamientodeproductos.R;
import sebastian.woowup.reaprovisionamientodeproductos.src.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> data = new ArrayList<>();
    private LayoutInflater inflater;

    public ProductAdapter(Context context, List<Product> data){
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void setData(List<Product> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_element_product,parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        holder.tvSku.setText(String.valueOf(data.get(position).getSku()));
        holder.tvFecha.setText(data.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSku, tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSku = itemView.findViewById(R.id.tvSku);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }
}
