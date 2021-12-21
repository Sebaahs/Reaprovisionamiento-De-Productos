package sebastian.woowup.reaprovisionamientodeproductos.src.usecases.estimadoDeRecompra;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import sebastian.woowup.reaprovisionamientodeproductos.R;
import sebastian.woowup.reaprovisionamientodeproductos.src.model.Product;

public class MainActivity extends AppCompatActivity {

    private estimadoDeRecompraViewModel viewModel;
    private ProductAdapter adapter;
    private List<Product> listaDeProductos;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaDeProductos = new ArrayList<>();

        RecyclerView rvLista = findViewById(R.id.rvLista);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(layoutManager);

        viewModel = new ViewModelProvider(this).get(estimadoDeRecompraViewModel.class);

        adapter = new ProductAdapter(this, listaDeProductos);
        rvLista.setAdapter(adapter);

        viewModel.getListaDeProductos().observe(this, products -> {
            //ACTUALIZAR FECHA DE RECOMPRA ESTIMADA
            if (products != null){
                listaDeProductos = products;
                adapter.setData(products);
            }
        });

        //METHODS
        viewModel.calculoDeCadaProducto(this);
    }
}