package com.kamarena.pfc.quiniclubs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamarena.pfc.quiniclubs.clases.ItemNavegador;
import com.kamarena.pfc.quiniclubs.clases.JSONParser;
import com.kamarena.pfc.quiniclubs.clases.ListaQuinielaAdapter;
import com.kamarena.pfc.quiniclubs.clases.NavigationAdapter;
import com.kamarena.pfc.quiniclubs.clases.Partido;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kamarena on 03/06/2015.
 */
public class QuinielaComunidad extends ActionBarActivity {

    // Componentes de la Activity
    private TextView titulo;
    private ListView lvListaResultados;
    private TextView tvInfo;
    private Button btPronosticar;

    // Atributos de esta clase
    private String usuario; // usuario activo de esta sesión
    private String nombreComunidad; // nombre de la comunidad en la que el usuario está activo
    private String idComunidad; // ID de la comunidad en la que el usuario está activo
    private ArrayList<Partido> listaResultados; // todos los resultados de esta jornada
    private ListaQuinielaAdapter lra; // Adaptador que encapsula los elementos de la ListView

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
        setContentView(R.layout.activity_quiniela);

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Se configura la ActionBar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Se inician los datos de sesión
        SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        usuario = sp.getString("usuario", "");
        nombreComunidad = sp.getString("nombre_comunidad", "");
        idComunidad = sp.getString("id_comunidad", "");

        this.crearMenuLateral(); // Se crea el DrawerLayout que servirá de menú de navegación

        // Se inician los componentes de la activity
        titulo = (TextView) findViewById(R.id.tv_titulo_quiniela);
        lvListaResultados = (ListView) findViewById(R.id.lv_partidos_quiniela);
        tvInfo = (TextView) findViewById(R.id.tv_quiniela_info);
        tvInfo.setVisibility(View.GONE);
        btPronosticar = (Button) findViewById(R.id.bt_quiniela_pronosticar);
        btPronosticar.setVisibility(View.GONE);

        this.listaResultados = new ArrayList<>(); // Se inicia la lista de los resultados de esta jornada
        this.lra = new ListaQuinielaAdapter(this, listaResultados); // Se inicia el adaper con los resultados

        // Enlaza la listview con el arrayadapter
        if (lvListaResultados != null) {
            lvListaResultados.setAdapter(lra);
        }

        // Se introduce la fecha del sistema para obtener la jornada actual
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha =  sdf.format(c.getTime());

        ResultadoWS jornada = new ResultadoWS();
        jornada.execute("obtener_resultados_comunidad", fecha, idComunidad);
    }

    /**
     * Configura e infla el menú lateral DrawerLayout
     */
    private void crearMenuLateral() {
        drawerLayout = (DrawerLayout) findViewById(R.id.contenedor_principal_quiniela);
        listaDrawer = (ListView) findViewById(R.id.menu_drawer_quiniela);
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
                ActivityCompat.invalidateOptionsMenu(QuinielaComunidad.this);
                drawerToggle.setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ActivityCompat.invalidateOptionsMenu(QuinielaComunidad.this);
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
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Clase privada que controla la llamada al servidor para obtener los pronosticos realizados
    private class ResultadoWS extends AsyncTask<String, Integer, Boolean> {

        // Constantes de la clase privada
        private String ws_url = getString(R.string.ws_namespace) + "/ws_metodos_json.php";
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_PRONOSTICADO = "pronosticado";
        private static final String TAG_RESULTADOS = "resultados";

        // Objeto que servirá para ejecutar la llamada JSON y obtener la respuesta
        JSONParser jsonParser = new JSONParser();
        JSONArray partidos = null;
        private String nombreJornada = "";
        private String informacion = "";
        private boolean noListView = false;

        // Método que se ejecuta al inicio de la tarea asíncrona
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QuinielaComunidad.this);
            pDialog.setMessage("Obteniendo datos");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean resultado = true;

            List<NameValuePair> parametros = new ArrayList<>();
            parametros.add(new BasicNameValuePair("metodo", params[0]));
            parametros.add(new BasicNameValuePair("fecha_jornada", params[1]));
            parametros.add(new BasicNameValuePair("id_comunidad", params[2]));

            JSONObject json = jsonParser.makeHttpRequest(ws_url, "POST", parametros);
            Log.d("Resultados: ", json.toString()); // Se comprueba el proceso

            try {
                int success = json.getInt(TAG_SUCCESS);
                int pronosticado = json.getInt(TAG_PRONOSTICADO);
                // Si se han encontrado partidos en la fecha actual
                if (success == 1 && pronosticado == 1) {
                    partidos = json.getJSONArray(TAG_RESULTADOS);

                    // Itera a través de los partidos obtenidos
                    for (int i = 0; i < partidos.length(); i++) {
                        JSONObject c = partidos.getJSONObject(i);
                        // Obtengo el Partido que guardaré en el ArrayList
                        Partido p = new Partido(
                                c.getString("id_jornada"),
                                c.getString("id_partido"),
                                c.getString("nombre_partido"),
                                c.getString("resultado"),
                                c.getString("fecha_partido"),
                                c.getString("numero_partido"),
                                c.getString("escudo_local"),
                                c.getString("escudo_visitante"),
                                c.getString("equipo_local"),
                                c.getString("equipo_visitante"),
                                c.getString("nombre_jornada"),
                                c.getString("inicio_jornada"),
                                c.getString("final_jornada")
                        );
                        // Añado el pronostico de la comunidad
                        p.setPronostico(c.getString("apuesta_comunidad"));
                        // Añado el partido al ArrayList
                        listaResultados.add(p);
                        // Se añade el nombre de la jornada
                        if (i == 0) {
                            nombreJornada = p.getNombreJornada();
                        }
                    }
                } else if (pronosticado == 0) {
                    informacion = getString(R.string.warning_quinielcomunidad_nodata);
                    noListView = true;
                } else {
                    informacion = getString(R.string.warning_quinielcomunidad_noquiniela);
                    noListView = true;
                }
            } catch (JSONException e) {
                resultado = false;
                informacion = getString(R.string.warning_quinielcomunidad_failquiniela);
                noListView = true;
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
                        lra.notifyDataSetChanged();
                        if (!nombreJornada.equals("null")) {
                            actionBar.setSubtitle(nombreJornada);
                        }
                        if (!nombreComunidad.equals("null")) {
                            titulo.setText(nombreComunidad);
                        } else {
                            titulo.setText(getString(R.string.info_quinielcomunidad_nocommunity));
                        }
                        // Si no se muestra la lista
                        if (noListView) {
                            tvInfo.setVisibility(View.VISIBLE);
                            tvInfo.setText(informacion);
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), informacion, Toast.LENGTH_LONG).show();
                vibrador.vibrate(200);
            }
        }
    }

}
