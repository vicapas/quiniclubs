package com.kamarena.pfc.quiniclubs;

/**
 * Creado por kamarena
 */

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamarena.pfc.quiniclubs.clases.ItemNavegador;
import com.kamarena.pfc.quiniclubs.clases.JSONParser;
import com.kamarena.pfc.quiniclubs.clases.ListaPartidosAdapter;
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

public class Pronostico extends ActionBarActivity {

    // Componentes de esta Activity
    private TextView tvTitulo;
    private ListView lvListaPartidos;
    private Button btConfirmar;
    private TextView tvInfo;

    private String usuario; // Usuario activo en esta sesión
    private String comunidad; // Comunidad en la que esta activo el usuario
    private ArrayList<Partido> listaPartidos; // Lista con todos los partidos de la jornada
    private ListaPartidosAdapter lpa; // Adaptador que encapsula los elementos de la ListView
    private Boolean controlPronostico; // Controla que no se deje vacío ningún pronóstico

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
        super.onCreate(savedInstanceState); // Se crea la activity
        setContentView(R.layout.activity_pronosticos); // Se designa el layout correspondiente

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Se configura la ActionBar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Se inician los datos de sesión
        SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        usuario = sp.getString("usuario", "");
        comunidad = sp.getString("nombre_comunidad", "");

        this.crearMenuLateral(); // Se crea el DrawerLayout que servirá de menú de navegación

        // Se incian los componentes del layout
        this.tvTitulo = (TextView) findViewById(R.id.tv_titulo_pronostico);
        this.lvListaPartidos = (ListView) findViewById(R.id.lv_partidos_pronostico);
        this.btConfirmar = (Button) findViewById(R.id.bt_confirmar_pronostico);
        this.tvInfo = (TextView) findViewById(R.id.tv_pronostico_info);
        this.tvInfo.setVisibility(View.GONE);

        this.listaPartidos = new ArrayList<>(); // Se inicia la lista de partidos de esta jornada
        this.lpa = new ListaPartidosAdapter(this, listaPartidos); // Se inicia el adaper con los partidos

        if (lvListaPartidos != null) {
            lvListaPartidos.setAdapter(lpa);
        }

        // Se introduce la fecha del sistema para obtener la jornada actual
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha =  sdf.format(c.getTime());

        JornadaWS jornada = new JornadaWS();
        jornada.execute("obtener_partidos", fecha);

        // Se le da comportamiento al botón confirmar
        this.btConfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controlPronostico = true;
                String pronostico = getPronostico();
                String idJornada = listaPartidos.get(0).getIdJornada();
                if (controlPronostico) {
                    new PronosticoWS().execute("confirmar_pronostico", pronostico, usuario, idJornada);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.warning_pronostico_complete),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void crearMenuLateral() {
        drawerLayout = (DrawerLayout) findViewById(R.id.contenedor_principal_pronosticos);
        listaDrawer = (ListView) findViewById(R.id.menu_drawer_pronosticos);
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
                ActivityCompat.invalidateOptionsMenu(Pronostico.this);
                drawerToggle.setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ActivityCompat.invalidateOptionsMenu(Pronostico.this);
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
        // Si necesitaramos algún icono en la actionbar lo inflariamos aquí
        // getMenuInflater().inflate(R.menu.menu_pronosticos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getPronostico() {
        String pronostico = "";
        String[] arrayOrdenado = new String[listaPartidos.size()];

        for (int i = 0; i < arrayOrdenado.length; i++) {
            Partido partido = listaPartidos.get(i);
            int indicePartido = Integer.parseInt(partido.getNumeroPartido()) - 1;
            if (partido.isCb1()) {
                arrayOrdenado[indicePartido] = "1";
            }
            if (partido.isCbX()) {
                arrayOrdenado[indicePartido] = "X";
            }
            if (partido.isCb2()) {
                arrayOrdenado[indicePartido] = "2";
            }
            if (!(partido.isCb1() || partido.isCbX() || partido.isCb2())) {
                controlPronostico = false;
            }
        }

        for (String resultado : arrayOrdenado) {
            pronostico += resultado;
        }

        return pronostico;
    }

    // Clase privada que controla la llamada al servidor para obtener la jornada actual
    private class JornadaWS extends AsyncTask<String, Integer, Boolean> {

        // Constantes de la clase privada
        private String ws_url = getString(R.string.ws_namespace) + "/ws_metodos_json.php";
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_PARTIDOS = "partidos";

        // Objeto que servirá para ejecutar la llamada JSON y obtener la respuesta
        JSONParser jsonParser = new JSONParser();
        JSONArray partidos = null;
        private String nombreJornada = "";
        private String informacion = "";
        private boolean noListView = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Pronostico.this);
            pDialog.setMessage(getString(R.string.info_pronostico_downloadingmatches));
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

            JSONObject json = jsonParser.makeHttpRequest(ws_url, "POST", parametros);
            Log.d("Partidos: ", json.toString()); // Se comprueba el proceso

            try {
                int success = json.getInt(TAG_SUCCESS);
                // Si se han encontrado partidos en la fecha actual
                if (success == 1) {
                    partidos = json.getJSONArray(TAG_PARTIDOS);

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
                        // Añado el partido al ArrayList
                        listaPartidos.add(p);
                        // Se añade el nombre de la jornada
                        if (i == 0) {
                            nombreJornada = p.getNombreJornada();
                        }
                    }
                } else {
                    informacion = getString(R.string.info_pronostico_noquiniela);
                    noListView = true;
                }
            } catch (JSONException e) {
                resultado = false;
                informacion = getString(R.string.info_pronostico_failquiniela);
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
                        lpa.notifyDataSetChanged();
                        actionBar.setSubtitle(nombreJornada);
                        if (!comunidad.equals("null")) {
                            tvTitulo.setText(comunidad);
                        }
                        // Si no se muestra la lista
                        if (noListView) {
                            btConfirmar.setVisibility(View.GONE);
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

    // Clase privada que controla el envio del pronóstico del usuario al servidor para guardarlo
    private class PronosticoWS extends AsyncTask<String, Integer, Boolean> {

        // Constantes de la clase privada
        private String ws_url = getString(R.string.ws_namespace) + "/ws_metodos_json.php";
        private static final String TAG_SUCCESS = "success";

        // Objeto que servirá para ejecutar la llamada JSON y obtener la respuesta
        JSONParser jsonParser = new JSONParser();

        // Atributos de la clase privada
        String informacion = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Pronostico.this);
            pDialog.setMessage(getString(R.string.info_pronostico_sending));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean resultado = true; // suponemos que la llamada REST será satisfactoria

            List<NameValuePair> parametros = new ArrayList<>();
            parametros.add(new BasicNameValuePair("metodo", params[0]));
            parametros.add(new BasicNameValuePair("pronostico", params[1]));
            parametros.add(new BasicNameValuePair("usuario", params[2]));
            parametros.add(new BasicNameValuePair("id_jornada", params[3]));

            JSONObject json = jsonParser.makeHttpRequest(ws_url, "POST", parametros);
            Log.d("Confirmacion: ", json.toString()); // Se comprueba el proceso

            try {
                int success = json.getInt(TAG_SUCCESS);
                // Si se han encontrado partidos en la fecha actual
                if (success == 1) {
                    informacion = getString(R.string.info_pronostico_stored);
                } else {
                    informacion = getString(R.string.info_pronostico_failcommit);
                }
            } catch (JSONException e) {
                resultado = false;
                informacion = getString(R.string.info_pronostico_failconnection);
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            pDialog.dismiss(); // Quita el dialogo de espera
            Toast.makeText(getApplicationContext(), informacion, Toast.LENGTH_LONG).show();
            if (!resultado) {
                vibrador.vibrate(200);
            }
        }
    }
}
