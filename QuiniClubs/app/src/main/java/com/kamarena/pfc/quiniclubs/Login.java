package com.kamarena.pfc.quiniclubs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kamarena.pfc.quiniclubs.clases.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase que le da funcionalidad a la activity_login
 */
public class Login extends Activity {

    private EditText etUsuario;
    private EditText etPassword;
    private Button btAceptar;
    private Button btRegistrar;

    private ProgressDialog pDialog; // Diálogo que se muestra mientras se obtiene la activity
    private SharedPreferences.Editor editor;

    Vibrator vibrador; // Objeto que controla la vibración del dispositivo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        editor = sp.edit();

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        etUsuario = (EditText) findViewById(R.id.et_usuario_login);
        etPassword = (EditText) findViewById(R.id.et_password_login);
        btAceptar = (Button) findViewById(R.id.bt_login);
        btRegistrar = (Button) findViewById(R.id.bt_registrar);

        btAceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new LogingWS().execute(etUsuario.getText().toString(), etPassword.getText().toString());
            }
        });

        btRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registro.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Clase privada que controla el comportamiento de la llamada al Servicio Web
     */
    private class LogingWS extends AsyncTask<String, Integer, Boolean> {

        final String NAMESPACE = getString(R.string.ws_namespace);
        final String URL = NAMESPACE + "/ws_soap_server.php?WSDL";
        final String METHOD_NAME = "login";
        final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

        private Integer login = 0;
        private String usuario = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Accediendo");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Método que ejecuta la llamada SOAP al WebService en segundo plano
         *
         * @param params (String) Parámetros que se le pasan al método
         * @return (Boolean) Devuelve si la conexión se ha realizado con éxito
         */
        protected Boolean doInBackground(String... params) {
            boolean resultado = true;

            SoapObject soapobject = new SoapObject(NAMESPACE, METHOD_NAME);
            soapobject.addProperty("id", params[0]);
            usuario = params[0];
            soapobject.addProperty("pass", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soapobject);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                login = (Integer) envelope.getResponse();
            } catch (Exception e) {
                resultado = false;
            }
            return resultado;
        }

        /**
         * Método que se ejecuta una vez ya realizada la conexión al WebService
         *
         * @param result (Boolean) Recibe la confirmación de que la conexión se ha realizado
         */
        protected void onPostExecute(Boolean result) {
            pDialog.dismiss(); // Quita el dialogo de espera
            if (result) {
                if (login == 1) {
                    new DatosWS().execute("obtener_usuario", usuario);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.warning_login_acceso), Toast.LENGTH_LONG).show();
                    vibrador.vibrate(300);
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Fallo de conexión", Toast.LENGTH_LONG).show();
                vibrador.vibrate(300);
            }
        }
    }

    /**
     * Clase privada asíncrona que obtiene los datos del usuario antes de loguearlo
     */
    private class DatosWS extends AsyncTask<String, Integer, Boolean> {

        // Constantes de la clase privada
        private String ws_url = getString(R.string.ws_namespace) + "/ws_metodos_json.php";
        private static final String TAG_SUCCESS = "success";

        // Objeto que servirá para ejecutar la llamada JSON y obtener la respuesta
        JSONParser jsonParser = new JSONParser();
        ArrayList<String> arrayDatos = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Cargando Aplicación");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean resultado = true;

            List<NameValuePair> parametros = new ArrayList<>();
            parametros.add(new BasicNameValuePair("metodo", params[0]));
            parametros.add(new BasicNameValuePair("id_usuario", params[1]));

            JSONObject json = jsonParser.makeHttpRequest(ws_url, "POST", parametros);
            Log.d("Datos: ", json.toString()); // Se comprueba el proceso

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    arrayDatos.add(json.getString("id_usuario"));
                    arrayDatos.add(json.getString("nombre_usuario"));
                    arrayDatos.add(json.getString("apellidos_usuario"));
                    arrayDatos.add(json.getString("id_comunidad"));
                    arrayDatos.add(json.getString("nombre_comunidad"));
                }
            } catch (JSONException e) {
                resultado = false;
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            pDialog.dismiss(); // Quita el dialogo de espera
            if (resultado) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editor.putString("usuario", arrayDatos.get(0));
                        editor.putString("nombre_usuario", arrayDatos.get(1));
                        editor.putString("apellidos_usuario", arrayDatos.get(2));
                        editor.putString("id_comunidad", arrayDatos.get(3));
                        editor.putString("nombre_comunidad", arrayDatos.get(4));
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), QuinielaUsuario.class);
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "" +
                        getString(R.string.warning_login_nodata), Toast.LENGTH_LONG).show();
                vibrador.vibrate(200);
            }
        }

    }

}
