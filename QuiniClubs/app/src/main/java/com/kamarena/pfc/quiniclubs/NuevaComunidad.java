package com.kamarena.pfc.quiniclubs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamarena.pfc.quiniclubs.clases.ItemNavegador;
import com.kamarena.pfc.quiniclubs.clases.JSONParser;
import com.kamarena.pfc.quiniclubs.clases.NavigationAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NuevaComunidad extends ActionBarActivity {

    // Componentes de esta activity
    private TextView titulo;
    private EditText idComunidad;
    private EditText passComunidad;
    private EditText nombreComunidad;
    private Button btCrear;
    private Button btCancelar;

    // Atributos de la clase
    private String usuario; // Usuario activo en esta sesión
    private String comunidad; // Comunidad en la que esta activo el usuario

    protected ProgressDialog pDialog; // Diálogo que se muestra mientras se obtiene la activity

    // Atributos ActionBarDrawer
    protected ActionBar actionBar;
    protected ActionBarDrawerToggle drawerToggle;
    protected DrawerLayout drawerLayout;
    protected ListView listaDrawer;
    protected TypedArray iconosNavegador;
    protected String[] titulos;
    protected ArrayList<ItemNavegador> listaNavegador;
    protected NavigationAdapter navAdapter;

    protected Vibrator vibrador; // Objeto que controla la vibración del dispositivo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_comunidad);

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Se configura la ActionBar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Se inician los datos de sesión
        SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        usuario = sp.getString("usuario", "");
        comunidad = sp.getString("nombre_comunidad", "");

        crearMenuLateral(); // Se crea el DrawerLayout que servirá de menú de navegación

        // Se vinculan los componentes del layout
        titulo = (TextView) findViewById(R.id.tv_titulo_crear);
        idComunidad = (EditText) findViewById(R.id.et_comunidad_crear);
        passComunidad = (EditText) findViewById(R.id.et_password_crear);
        nombreComunidad = (EditText) findViewById(R.id.et_nombre_crear);
        btCrear = (Button) findViewById(R.id.bt_crear);
        btCancelar = (Button) findViewById(R.id.bt_cancelar_crear);

        // Se implementan los controladores de eventos en los botones
        btCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new CrearWS().execute("crear_comunidad", idComunidad.getText().toString(),
                        passComunidad.getText().toString(), nombreComunidad.getText().toString());
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish(); // Termina y devuelve la acción a la activity inferior en la pila
            }
        });
    }

    private void crearMenuLateral() {
        drawerLayout = (DrawerLayout) findViewById(R.id.contenedor_principal_crear);
        listaDrawer = (ListView) findViewById(R.id.menu_drawer_crear);
        // Tomamos listado de imágenes desde drawable
        iconosNavegador = getResources().obtainTypedArray(R.array.iconosNavegador);
        // Tomamos listado  de titulos desde el string-array de los recursos @string/nav_options
        titulos = getResources().getStringArray(R.array.opcionesNavegador);
        //Listado de titulos de barra de navegacion
        listaNavegador = new ArrayList<>();
        //Agregamos objetos Item_object al array
        for (int i = 0; i < titulos.length; i++) {
            listaNavegador.add(
                    new ItemNavegador(titulos[i], iconosNavegador.getResourceId(i, -1))
            );
        }
        //Declaramos y seteamos nuestro adaptador al cual le pasamos el array con los titulos
        navAdapter = new NavigationAdapter(this, listaNavegador);
        listaDrawer.setAdapter(navAdapter);
        listaDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent0 = new Intent(getApplicationContext(), QuinielaUsuario.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), QuinielaComunidad.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), Unirse.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), NuevaComunidad.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getApplicationContext(), Ajustes.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent5);
                        break;
                }
                listaDrawer.setItemChecked(position, true);
                drawerLayout.closeDrawer(listaDrawer);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_action_tresrayas,
                R.string.abrirNavegador,
                R.string.cerrarNavegador) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                ActivityCompat.invalidateOptionsMenu(NuevaComunidad.this);
                drawerToggle.setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ActivityCompat.invalidateOptionsMenu(NuevaComunidad.this);
            }
        };
        // Se configura el drawer toggle
        drawerLayout.setDrawerListener(drawerToggle);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Clase privada que controla la llamada al servidor para modificar datos personales
    private class CrearWS extends AsyncTask<String, Integer, Boolean> {

        // Constantes de la clase privada
        private String ws_url = getString(R.string.ws_namespace) + "/ws_metodos_json.php";
        private static final String TAG_SUCCESS = "success";

        // Objeto que servirá para ejecutar la llamada JSON y obtener la respuesta
        JSONParser jsonParser = new JSONParser();

        // Atributos de la clase privada
        private String informacion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NuevaComunidad.this);
            pDialog.setMessage(getString(R.string.info_nueva_creating));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean resultado = true;

            List<NameValuePair> parametros = new ArrayList<>();
            parametros.add(new BasicNameValuePair("metodo", params[0]));
            parametros.add(new BasicNameValuePair("id_comunidad", params[1]));
            parametros.add(new BasicNameValuePair("pass_comunidad", params[2]));
            parametros.add(new BasicNameValuePair("nombre_comunidad", params[3]));

            JSONObject json = jsonParser.makeHttpRequest(ws_url, "POST", parametros);
            Log.d("Crear: ", json.toString()); // Se comprueba el proceso

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    informacion = getString(R.string.info_nueva_nuevaok) + params[3];
                } else {
                    informacion = getString(R.string.warning_nueva_nuevafail);
                    resultado = false;
                }
            } catch (JSONException e) {
                resultado = false;
                informacion = getString(R.string.warning_ajustes_errorconnection);
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            pDialog.dismiss(); // Quita el dialogo de espera
            if (resultado) {
                Toast.makeText(getApplicationContext(), informacion, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), informacion, Toast.LENGTH_LONG).show();
                vibrador.vibrate(200);
            }
        }

    }
}
