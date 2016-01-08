<?php

/*  
 * Clase para hacer pruebas de los métodos del Servicio Web
 * 
 * Created 24/05/2015
 * @author: kamarena
 */

$SERVIDOR = filter_input_array(INPUT_SERVER);
$PATH = $SERVIDOR['DOCUMENT_ROOT'] . "/kamarena/quiniclubs_server";
include "$PATH/clases/db_funciones_ws.php";

// Array de valores obtenido por post
//$post = filter_input_array(INPUT_POST);

// Variable que discriminará el método a utilizar
$metodo = "crear_comunidad";

$funcion = new DBFunciones();
$response = array();
$response["success"] = 0; // Será la respuesta por defecto

if ($metodo == "obtener_partidos") {        
    $fecha = $post["fecha_jornada"];    
    $rows = $funcion->get_jornada($fecha);
    if ($rows) {
        $response["success"] = 1;
        $response["partidos"] = array();
        for ($i = 0; $i < count($rows); $i++) {
            $partidos = array();
            $partidos["id_jornada"] = $rows[$i]["id_jornada"];
            $partidos["id_partido"] = $rows[$i]["id_partido"];
            $partidos["nombre_partido"] = $rows[$i]["nombre_partido"];
            $partidos["resultado"] = $rows[$i]["resultado"];
            $partidos["fecha_partido"] = $rows[$i]["fecha_partido"];
            $partidos["numero_partido"] = $rows[$i]["numero_partido"];
            $partidos["escudo_local"] = $rows[$i]["escudo_local"];
            $partidos["escudo_visitante"] = $rows[$i]["escudo_visitante"];
            $partidos["equipo_local"] = $rows[$i]["equipo_local"];
            $partidos["equipo_visitante"] = $rows[$i]["equipo_visitante"];
            $partidos["nombre_jornada"] = $rows[$i]["nombre_jornada"];
            $partidos["inicio_jornada"] = $rows[$i]["inicio_jornada"];
            $partidos["final_jornada"] = $rows[$i]["final_jornada"];
            array_push($response["partidos"], $partidos);
        }
    }
}

if ($metodo == "confirmar_pronostico") {
    $response["success"] = 1; // Se prevee que todo se inserte bien
    $pronostico = $post["pronostico"];
    $usuario = $post["usuario"];
    $id_jornada = $post["id_jornada"];
    $db_partidos = $funcion->get_partidos($id_jornada);
    foreach ($db_partidos as $fila) {
        $id_partido = $fila["id_partido"];
        $apuesta = $pronostico{$fila["numero_partido"] - 1};
        if (!$funcion->nuevo_pronostico_usuario(
                $usuario, $id_partido, $apuesta)) { 
            $response["success"] = 0; // Si hay un fallo en la inserción
        }
    }
}

if ($metodo == "obtener_resultados") {
    $fecha = "2015-06-06";
    $id_usuario = "xuspitin";
    $rows = $funcion->get_jornada($fecha);
    if ($rows) {
        $response["success"] = 1;        
        $response["resultados"] = array();
        for ($i = 0; $i < count($rows); $i++) {
            $partidos = array();
            $partidos["id_jornada"] = $rows[$i]["id_jornada"];
            $partidos["id_partido"] = $rows[$i]["id_partido"];
            $partidos["nombre_partido"] = $rows[$i]["nombre_partido"];
            $partidos["resultado"] = $rows[$i]["resultado"];
            $partidos["fecha_partido"] = $rows[$i]["fecha_partido"];
            $partidos["numero_partido"] = $rows[$i]["numero_partido"];
            $partidos["escudo_local"] = $rows[$i]["escudo_local"];
            $partidos["escudo_visitante"] = $rows[$i]["escudo_visitante"];
            $partidos["equipo_local"] = $rows[$i]["equipo_local"];
            $partidos["equipo_visitante"] = $rows[$i]["equipo_visitante"];
            $partidos["nombre_jornada"] = $rows[$i]["nombre_jornada"];
            $partidos["inicio_jornada"] = $rows[$i]["inicio_jornada"];
            $partidos["final_jornada"] = $rows[$i]["final_jornada"];
            // Obtengo la apuesta del usuario
            $apuesta_usuario = $funcion->get_resultados_usuario(
                    $partidos["id_partido"], 
                    $id_usuario
                    );
            if ($apuesta_usuario) {
                $partidos["apuesta_usuario"] = $apuesta_usuario[0]["apuesta"];
                $response["pronosticado"] = 1;
            } else {
                $partidos["apuesta_usuario"] = "0";
                $response["pronosticado"] = 0;
            }
            array_push($response["resultados"], $partidos);
        }
    }
}

if ($metodo == "obtener_usuario") {
    $id_usuario = $post["id_usuario"];
    $rows = $funcion->get_datos_usuario($id_usuario);
    if ($rows) {
        $response["success"] = 1;        
        $response["id_usuario"] = $rows[0]["id_usuario"];
        $response["nombre_usuario"] = $rows[0]["nombre_usuario"];
        $response["apellidos_usuario"] = $rows[0]["apellidos_usuario"];
        $response["id_comunidad"] = $rows[0]["id_comunidad"];
        $response["nombre_comunidad"] = $rows[0]["nombre_comunidad"];
    }
}

if ($metodo == "obtener_resultados_comunidad") {
    $fecha = "2015-06-06";
    $id_comunidad = "la_nueva";
    $rows = $funcion->get_jornada($fecha);
    if ($rows) {
        $response["success"] = 1;        
        $response["resultados"] = array();
        for ($i = 0; $i < count($rows); $i++) {
            $partidos = array();
            $partidos["id_jornada"] = $rows[$i]["id_jornada"];
            $partidos["id_partido"] = $rows[$i]["id_partido"];
            $partidos["nombre_partido"] = $rows[$i]["nombre_partido"];
            $partidos["resultado"] = $rows[$i]["resultado"];
            $partidos["fecha_partido"] = $rows[$i]["fecha_partido"];
            $partidos["numero_partido"] = $rows[$i]["numero_partido"];
            $partidos["escudo_local"] = $rows[$i]["escudo_local"];
            $partidos["escudo_visitante"] = $rows[$i]["escudo_visitante"];
            $partidos["equipo_local"] = $rows[$i]["equipo_local"];
            $partidos["equipo_visitante"] = $rows[$i]["equipo_visitante"];
            $partidos["nombre_jornada"] = $rows[$i]["nombre_jornada"];
            $partidos["inicio_jornada"] = $rows[$i]["inicio_jornada"];
            $partidos["final_jornada"] = $rows[$i]["final_jornada"];
            // Obtengo las apuestas de todos los usuarios
            $apuesta_comunidad = $funcion->get_pronostico_partido(
                    $partidos["id_partido"], $id_comunidad);
            if ($apuesta_comunidad) {
                $partidos["apuesta_comunidad"] = $apuesta_comunidad[0]["apuesta"];
                $response["pronosticado"] = 1;
            } else {
                $partidos["apuesta_comunidad"] = "0";
                $response["pronosticado"] = 0;
            }
            array_push($response["resultados"], $partidos);
        }
    }
}

if ($metodo == "unirse_comunidad") {
    $id_usuario = $post["id_usuario"];
    $id_comunidad = $post["id_comunidad"];
    $pass_comunidad = $post["pass_comunidad"];
    // Se comprueba si el id_comunidad y el pass_comunidad son correctos
    $acceso = $funcion->confirmar_comunidad($id_comunidad, $pass_comunidad);
    // Si la contraseña era correcta
    if ($acceso) {
        // Se comprueba si el usuario ya pertenece a una comunidad
        $unido = $funcion->tiene_comunidad($id_usuario);
        $ok = true; // Se supone que el proceso se completará bien
        if ($unido) {
            // Si el usuario ya pertenece a una comunidad se editará ese registro
            $ok = $funcion->modificar_comunidad($id_usuario, $id_comunidad);
        } else {
            // Si no tiene comunidad ingresará en la nueva
            $ok = $funcion->ingresar_comunidad($id_usuario, $id_comunidad);
        }
        // Si se ha añadido bien
        if ($acceso && $ok) {
            $response["success"] = 1;
            $response["id_comunidad"] = $id_comunidad;
            $response["nombre_comunidad"] = 
                    $funcion->get_nombre_comunidad($id_comunidad);
        } else {
            $response["success"] = 0;
            $response["pass"] = 1;
        }
    } else {
        $response["success"] = 0;
        $response["pass"] = 0;
    }
}

if ($metodo == "modificar_ajustes") {
    $response["success"] = 1; // Se prevee que se modifique el usuario
    $id_usuario = "alberto22";
    $nombre = "Alberto";
    $apellidos = "Briones";
    $email = "alberto22@hotmail.com";
    $ok = $funcion->modificar_ajustes($id_usuario, $nombre, $apellidos, $email);
    if (!$ok) {
        $response["success"] = 0;
    }
}

if ($metodo == "obtener_datos_ajustes") {
    $id_usuario = $post["id_usuario"];
    $rows = $funcion->obtener_datos_ajustes($id_usuario);
    if ($rows) {
        $response["success"] = 1;
        $response["nombre"] = $rows["nombre"];
        $response["apellidos"] = $rows["apellidos"];
        $response["email"] = $rows["email"];
    } else {
        $response["success"] = 0;
    }
}

if ($metodo == "crear_comunidad") {
    $response["success"] = 1; // Se prevee que se modifique el usuario
    $id_comunidad = "los_ganadores_2";
    $pass_comunidad = "abc123";
    $nombre_comunidad = "Los Ganadores 2";
    $ok = $funcion->crear_comunidad($id_comunidad, $pass_comunidad, $nombre_comunidad);
    if (!$ok) {
        $response["success"] = 0;
    }
}

// Se devuelve la respuesta en formato json
echo json_encode($response);
echo "<br />";
echo "-----------------------------------------------------------------";
desplegar($response);

function desplegar($array, $tab = 0) {
    echo "<br />";
    foreach ($array as $clave => $valor) {
        for ($i = 0; $i < $tab; $i++) {
            echo "....";
        }
        echo $clave;
        echo " => ";
        if (gettype($valor) == 'array') {
            $tab++;
            desplegar($valor, $tab);
            $tab--;
        } else {
            echo $valor;
            echo "<br />";
        }
    }
}