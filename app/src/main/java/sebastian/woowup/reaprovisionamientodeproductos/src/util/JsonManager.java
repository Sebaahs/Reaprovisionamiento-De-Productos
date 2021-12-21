package sebastian.woowup.reaprovisionamientodeproductos.src.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonManager {

    public static String cargarContenidoJsonEnString(Context contexto, String nombreDelArchivo) throws IOException {

        BufferedReader reader = null;
        String charset = "UTF-8";

        reader = new BufferedReader(new InputStreamReader(contexto.getAssets().open(nombreDelArchivo), charset));

        String contenido = "";
        String linea;

        while((linea = reader.readLine()) != null){
            contenido += linea;
        }

        return contenido;
    }
}
