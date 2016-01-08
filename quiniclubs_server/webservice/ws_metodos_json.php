<?php

/*  
 * Clase que gestiona las llamadas que esperan un retorno en json
 * 
 * Created 24/05/2015
 * @author: kamarena
 */

$SERVIDOR = filter_input_array(INPUT_SERVER);
$PATH = $SERVIDOR['DOCUMENT_ROOT'] . "/kamarena/quiniclubs_server";
include "$PATH/clases/db_funciones_ws.php";

// Array de valores obtenido por post
$post = filter_input_array(INPUT_POST);

// Variable que discriminará el método a utilizar
$metodo = $post["metodo"];

$funcion = new DBFunciones();
$response = array();
$response["success"] = 0; // Será la respuesta por defecto

/**
 * Obtiene la jornada correspondiente a una determinada fecha
 */
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

/**
 * Confirma el pronóstico que hace el usuario de una jornada concreta
 */
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

/**
 * Obtiene los resultados que se producen en una jornada según usuario
 */
if ($metodo == "obtener_resultados") {
    $fecha = $post["fecha_jornada"];
    $id_usuario = $post["id_usuario"];
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

/**
 * Obtiene los datos de un usuario
 */
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

/**
 * Obtiene los resultados pronosticados por toda la comunidad
 */
if ($metodo == "obtener_resultados_comunidad") {
    $fecha = $post["fecha_jornada"];
    $id_comunidad = $post["id_comunidad"];
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

/**
 * Se ingresa a un usuario a una comunidad
 */
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

/**
 * Se modifican datos del usuario
 */
if ($metodo == "modificar_ajustes") {
    $response["success"] = 1; // Se prevee que se modifique el usuario
    $id_usuario = $post["id_usuario"];
    $nombre = $post["nombre"];
    $apellidos = $post["apellidos"];
    $email = $post["email"];
    $ok = $funcion->modificar_ajustes($id_usuario, $nombre, $apellidos, $email);
    if (!$ok) {
        $response["success"] = 0;
    }
}

/**
 * Se obtienen los datos modificables del usuario
 */
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

/**
 * Se crea una comunidad
 */
if ($metodo == "crear_comunidad") {
    $response["success"] = 1; // Se prevee que se modifique el usuario
    $id_comunidad = $post["id_comunidad"];
    $pass_comunidad = $post["pass_comunidad"];
    $nombre_comunidad = $post["nombre_comunidad"];
    $ok = $funcion->crear_comunidad($id_comunidad, $pass_comunidad, $nombre_comunidad);
    if (!$ok) {
        $response["success"] = 0;
    }
}

// Se devuelve la respuesta en formato json
echo json_encode($response);