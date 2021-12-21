package sebastian.woowup.reaprovisionamientodeproductos.src.usecases.estimadoDeRecompra;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sebastian.woowup.reaprovisionamientodeproductos.src.model.Customer;
import sebastian.woowup.reaprovisionamientodeproductos.src.model.Product;
import sebastian.woowup.reaprovisionamientodeproductos.src.model.Purchase;
import sebastian.woowup.reaprovisionamientodeproductos.src.util.JsonManager;

import static java.time.temporal.ChronoUnit.DAYS;

public class estimadoDeRecompraViewModel extends ViewModel {

    private List<Product> productos = new ArrayList<>();
    private List<Purchase> compras = new ArrayList<>();
    private Customer customer;

    //LiveData
    private MutableLiveData<List<Product>> listaDeProductos;

    public estimadoDeRecompraViewModel(){
        listaDeProductos = new MutableLiveData<>();
    }

    //Getter
    public MutableLiveData<List<Product>> getListaDeProductos() {
        return listaDeProductos;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculoDeCadaProducto(Context context) {

        List<String> fechasDeCompra = new ArrayList<>();
        List<Integer> plazosDeCompra = new ArrayList<>();
        int regularidadDeCompra;
        // ESTIMACION DE SIGUIENTE COMPRA LIVE DATA

        obtenerListadoDeObjetosJSON(context);
        List<Integer> listaDeSku = obtenerSkuSinRepetir();

        for (int i = 0; i < listaDeSku.size(); i ++){
            fechasDeCompra = listarFechasDeCompraDeUnProducto(listaDeSku.get(i));
            if (fechasDeCompra != null){
                plazosDeCompra = calcularPlazosDeCompra(fechasDeCompra);
                regularidadDeCompra = calcularRegularidadDeCompra(plazosDeCompra);
                productos.add(estimacionDeSiguienteCompra(fechasDeCompra, regularidadDeCompra, listaDeSku.get(i)));
            }
        }
        listaDeProductos.postValue(productos);
    }


    private void obtenerListadoDeObjetosJSON(Context context) {

        String contenidoDelJson = null;

        try {
            contenidoDelJson = JsonManager.cargarContenidoJsonEnString(context, "purchases-v2.json");

            JSONObject objetoJson = new JSONObject(contenidoDelJson);
            JSONObject customerJSON = objetoJson.getJSONObject("customer");
            JSONArray purchaseJSON = customerJSON.getJSONArray("purchases");

            for (int i = 0; i < purchaseJSON.length(); i ++){

                JSONObject purchaseJsonObject = purchaseJSON.getJSONObject(i);

                JSONArray productJSON = purchaseJsonObject.getJSONArray("products");

                for(int j = 0; j < productJSON.length(); j ++){

                    JSONObject productJsonObject = productJSON.getJSONObject(j);

                    productos.add(new Product(productJsonObject.getInt("sku"), productJsonObject.getString("name")));
                }

                compras.add(new Purchase(purchaseJsonObject.getString("number"), purchaseJsonObject.getString("date"), productos));
                productos = new ArrayList<>();
            }

            customer = new Customer(compras);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


    private List<Integer> obtenerSkuSinRepetir() {

        List<Integer> listaDeSku = new ArrayList<>();

        for(int i = 0; i < customer.getPurchase().size(); i ++){
            for (int j = 0; j < customer.getPurchase().get(i).getProductList().size(); j ++){
                if (!listaDeSku.contains(customer.getPurchase().get(i).getProductList().get(j).getSku())){
                    listaDeSku.add(customer.getPurchase().get(i).getProductList().get(j).getSku());
                }
            }
        }

        return listaDeSku;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<String> listarFechasDeCompraDeUnProducto(int sku){

        int cantidadRecompra = 0;;
        List<String> fechasDeCompra = new ArrayList<>();

        for (int i = 0; i < customer.getPurchase().size(); i ++){
            for (int j = 0; j < customer.getPurchase().get(i).getProductList().size(); j ++){

                if (customer.getPurchase().get(i).getProductList().get(j).getSku() == sku){

                    cantidadRecompra ++;
                    fechasDeCompra.add(customer.getPurchase().get(i).getDate());
                }
            }
        }

        // Eliminar los productos que no aparescas por lo menos mas de una vez
        if (cantidadRecompra < 2){
            return null;
        }

        return fechasDeCompra;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Integer> calcularPlazosDeCompra(List<String> fechasDeCompra) {

        List<Integer> plazosDeCompra = new ArrayList<>();

        for(int i = 1; i < fechasDeCompra.size(); i ++){

            LocalDate fechaActual = LocalDate.parse(fechasDeCompra.get(i));
            LocalDate fechaAnterior = LocalDate.parse(fechasDeCompra.get(i-1));

            plazosDeCompra.add((int) DAYS.between(fechaAnterior, fechaActual));
        }

        return plazosDeCompra;
    }


    private int calcularRegularidadDeCompra(List<Integer> plazosDeCompra) {

        final int columnas = 2, filas = 8;
        int[][] intervalos = new int[filas][columnas];
        int intervaloDeMayorFrecuencia = 0;
        int tendencia = 0;

        // --> Sumar en cada intervalo el valor plazo correspondiente para sacar una aproximacion
        for(int plazo : plazosDeCompra){
            if ( plazo > 0 && plazo <= 6){
                intervalos[0][0] += plazo;
                intervalos[0][1] += 1;
            }
            if ( plazo > 6 && plazo <= 16){
                intervalos[1][0] += plazo;
                intervalos[1][1] += 1;
            }
            if ( plazo > 16 && plazo <= 26){
                intervalos[2][0] += plazo;
                intervalos[2][1] += 1;
            }
            if ( plazo > 26 && plazo <= 36){
                intervalos[3][0] += plazo;
                intervalos[3][1] += 1;
            }
            if ( plazo > 36 && plazo < 46){
                intervalos[4][0] += plazo;
                intervalos[4][1] += 1;
            }
            if ( plazo > 46 && plazo <= 60){
                intervalos[5][0] += plazo;
                intervalos[5][1] += 1;
            }
            if ( plazo > 60 && plazo <= 90){
                intervalos[6][0] += plazo;
                intervalos[6][1] += 1;
            }
            if ( plazo > 90){
                intervalos[7][0] += plazo;
                intervalos[7][1] += 1;
            }
        }

        // --> Promediar los plazos en cada intervalo
        for (int i = 0; i < filas; i ++){

            if (intervalos[i][0] != 0 && intervalos[i][1] != 0){
                intervalos[i][0] /= intervalos[i][1];
            }

            if (intervaloDeMayorFrecuencia < intervalos[i][1]) intervaloDeMayorFrecuencia = intervalos[i][1];
        }

        // --> Sacar tendencia en relacion al intervalo que abarque mayor cantidad de plazos de compra
        for (int i = 0; i < filas; i ++){
            if (intervaloDeMayorFrecuencia == intervalos[i][1]) tendencia = intervalos[i][0];
        }

        return tendencia;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Product estimacionDeSiguienteCompra(List<String> fechasDeCompra, int moda, int sku) {

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate fechaEstimadaDeRecompra = null, fechaActual = null, fechaMasReciente = LocalDate.parse(fechasDeCompra.get(0), formatoFecha);

        for (int i = 1; i < fechasDeCompra.size(); i ++){

            fechaActual =  LocalDate.parse(fechasDeCompra.get(i), formatoFecha);

            if (fechaMasReciente.isBefore(fechaActual)){
                fechaMasReciente = fechaActual;
            }
        }

        fechaEstimadaDeRecompra = fechaMasReciente.plusDays(moda);

        return new Product(sku, fechaEstimadaDeRecompra);
    }
}
