package com.kamarena.pfc.quiniclubs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class Registro extends Activity {

    // Atributos de la clase
    private EditText usuario;
    private EditText password;
    private EditText password2;
    private EditText email;
    private EditText nombre;
    private EditText apellidos;
    private Button registro;
    private Button cancelar;

    // Control de sesión de usuario
    public SharedPreferences.Editor editor;
    private ProgressDialog pDialog; // Diálogo que se muestra mientras se obtiene la activity

    Vibrator vibrador; // Objeto que controla la vibración del dispositivo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Se inician los atributos de la clase
        usuario = (EditText) findViewById(R.id.et_registro_usuario);
        password = (EditText) findViewById(R.id.et_registro_password);
        password2 = (EditText) findViewById(R.id.et_registro_password2);
        email = (EditText) findViewById(R.id.et_registro_email);
        nombre = (EditText) findViewById(R.id.et_registro_nombre);
        apellidos = (EditText) findViewById(R.id.et_registro_apellidos);
        registro = (Button) findViewById(R.id.bt_registro_registrar);
        cancelar = (Button) findViewById(R.id.bt_registro_cancelar);
        // Se inicia la sesión de usuario para introducir el usuario si se registra correctamente
        SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        editor = sp.edit();
        // Se conectan los listeners a los botones
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(password2.getText().toString())) {
                    new RegistroWS().execute(
                            usuario.getText().toString(),
                            password.getText().toString(),
                            email.getText().toString(),
                            nombre.getText().toString(),
                            apellidos.getText().toString()
                    );
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.warning_registro_wrongpass), Toast.LENGTH_LONG).show();
                    vibrador.vibrate(300);
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class RegistroWS extends AsyncTask<String, Integer, Boolean> {

        final String NAMESPACE = getString(R.string.ws_namespace);
        final String URL = NAMESPACE + "/ws_soap_server.php?WSDL";
        final String METHOD_NAME = "nuevo_usuario";
        final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

        private Integer nuevoUsuario;
        private String usuario = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registro.this);
            pDialog.setMessage(getString(R.string.info_registro_entering));
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
            this.usuario = params[0];
            soapobject.addProperty("pass", params[1]);
            soapobject.addProperty("email", params[2]);
            soapobject.addProperty("nombre", params[3]);
            soapobject.addProperty("apellidos", params[4]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soapobject);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                nuevoUsuario = (Integer) envelope.getResponse();
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
                if (nuevoUsuario == 1) {
                    editor.putString("usuario", this.usuario);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), Pronostico.class);
                    //Intent intent = new Intent(Login.this, Pronostico.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.warning_registro_fail), Toast.LENGTH_LONG).show();
                    vibrador.vibrate(300);
                }
            } else {
                Toast.makeText(getApplicationContext(),
                       getString(R.string.warning_registro_conectionfail), Toast.LENGTH_LONG).show();
                vibrador.vibrate(300);
            }
        }

    }
}
