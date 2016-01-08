<?php

/*  
 * Clase donde están las funciones de conexión a la base de datos
 * 
 * Creado el 15/05/2015
 * @author: kamarena
 */

$SERVIDOR = filter_input_array(INPUT_SERVER);
$PATH = $SERVIDOR['DOCUMENT_ROOT'] . "/kamarena/quiniclubs_server";
include "$PATH/core/db_abstract_model.php";

class DBFunciones {
    
    private static $data;      
    
    // Constructor
    public function __construct() {
        self::$data = new DBModel();
    }

    /**
     * Método que comprueba si los datos de acceso son correctos
     * 
     * @param string $id_usuario
     * @param string $pass
     * @return boolean Confiración de login
     */
    public function login($id_usuario, $pass) {
        $query = "SELECT    pass "
               . "FROM      db_usuario "
               . "WHERE     id_usuario = '$id_usuario' "
               . "AND       pass = '$pass'";
        if ($id_usuario != "" && $pass != "") {
            $rows = self::$data->get_results_from_query($query);
        }
        return count($rows) > 0;
    }

    /**
     * Añade un nuevo usuario en la base de datos
     * 
     * @param array $datos
     * @return boolean éxito en la inserción
     */
    public function nuevo_usuario($datos) {
        $query = "INSERT INTO   db_usuario "
               . "              (id_usuario, "
               . "              pass, "
               . "              nombre, "
               . "              apellidos, "
               . "              email, "
               . "              fecha_creacion) "
               . "VALUES        ('" . $datos['id_usuario'] . "', "
               . "              '" . $datos['pass'] . "', "
               . "              '" . $datos['nombre'] . "', "
               . "              '" . $datos['apellidos'] . "', "
               . "              '" . $datos['email'] . "', "
               . "              '" . $datos['fecha_creacion'] . "')";
        return self::$data->execute_single_query($query);
    }
    
    /**
     * Borra un usuario existente en la base de datos
     * 
     * @param string $id_usuario
     * @return boolean éxito en la inserción
     */
    public function borra_usuario($id_usuario) {
        $query = "DELETE FROM   db_usuario "
               . "WHERE         id_usuario = '$id_usuario'";
        return self::$data->execute_single_query($query);
    }
    
    /**
     * Añade un usuario a una comunidad
     * 
     * @param string $user_id
     * @param integer $id_comunidad
     * @param string $fecha
     * @return boolean
     */
    public function add_usuario($user_id, $id_comunidad, $fecha = '') {
        $query= "";
        // Si no se ha especificado la fecha se utiliza la de hoy
        if(strlen($fecha) < 1) {
            $fecha = date('Y-m-d');
        }
        // Si el id que se pasa por parámetro no es una cadena vacía
        if($user_id != '') {
            $query = "INSERT INTO   db_usuario_comunidad "
                   . "              (id_usuario, "
                   . "              id_comunidad, "
                   . "              fecha_inicio) "
                   . "VALUES        ('$user_id', "
                   . "              '$id_comunidad', "
                   . "              '$fecha')";
        }
        return self::$data->execute_single_query($query);
    }
    
    /**
     * Obtiene todos los datos y partidos de una jornada en esa fecha
     * 
     * @param string $fecha
     * @return array
     */
    public function get_jornada($fecha) {
        $query = "";
        if($fecha != '') {
            $query = "SELECT    j.id_jornada as 'id_jornada', "
                   . "          p.id_partido as 'id_partido', "
                   . "          p.nombre as 'nombre_partido', "
                   . "          p.resultado as 'resultado', "
                   . "          p.fecha_partido as 'fecha_partido', "
                   . "          p.numero_partido as 'numero_partido', "
                   . "          e.escudo as 'escudo_local', "
                   . "          e2.escudo as 'escudo_visitante', "
                   . "          e.nombre as 'equipo_local', "
                   . "          e2.nombre as 'equipo_visitante', "
                   . "          j.nombre as 'nombre_jornada', "
                   . "          j.fecha_inicio as 'inicio_jornada', "
                   . "          j.fecha_final as 'final_jornada' "
                   . "FROM      db_jornada j, db_partido p, "
                   . "          db_equipo e, db_equipo e2 "
                   . "WHERE     j.fecha_inicio <= '$fecha' "
                   . "AND       j.fecha_final >= '$fecha' "
                   . "AND       p.id_jornada = j.id_jornada "
                   . "AND       e.id_equipo = p.id_equipo1 "
                   . "AND       e2.id_equipo = p.id_equipo2";
        }
        return self::$data->get_results_from_query($query);
    }
    
    /**
     * Introduce un nuevo pronóstico
     * 
     * @param string $id_usuario
     * @param integer $id_partido
     * @param string $apuesta
     * @return boolean
     */
    public function nuevo_pronostico_usuario($id_usuario, $id_partido, $apuesta) {
        $query = "";
        if ($id_usuario != '' && $id_partido != '' && $apuesta != '') {
            // Se comprueba si se ha realizado ya ese pronóstico
            $query = "SELECT    id_partido FROM db_pronostico_usuario "
                    . "WHERE    id_usuario = '$id_usuario' "
                    . "AND      id_partido = '$id_partido'";
            if (self::$data->get_results_from_query($query)) {
                // Si se ha hecho el pronostico, se actualiza
                $query = "UPDATE  db_pronostico_usuario "
                       . "SET     apuesta = '$apuesta', "
                       . "        fecha_confirmacion = '".date('Y-m-d')."' "
                       . "WHERE   id_usuario = '$id_usuario' "
                       . "AND     id_partido = '$id_partido'";
            } else {
                // Si no se ha hecho el pronostico, se inserta como nuevo
                $query = "INSERT INTO   db_pronostico_usuario "
                       . "              (id_usuario, id_partido, apuesta, "
                       . "              fecha_confirmacion) "
                       . "VALUES        ('$id_usuario', $id_partido, "
                       . "              '$apuesta', '".date('Y-m-d')."')";
            }
        }
        return self::$data->execute_single_query($query);
    }
    
    /**
     * Obtiene los partidos de una jornada y su referencia para ordenarlos
     * 
     * @param date $id_jornada
     * @return array lista de partidos de la jornada con su orden
     */
    public function get_partidos($id_jornada) {
        $query = "";
        if ($id_jornada != "") {
            $query =  "SELECT   numero_partido, id_partido "
                    . "FROM     db_partido "
                    . "WHERE    id_jornada = '$id_jornada'";
        }
        return self::$data->get_results_from_query($query);
    }
    
    /**
     * Obtiene cada pronóstico que ha hecho un usuario a un partido en concreto
     * 
     * @param string $id_partido
     * @param string $id_usuario
     * @return array pronosticos por partido de un usuario
     */
    public function get_resultados_usuario($id_partido, $id_usuario) {
        $query = "";
        if($id_partido != '' && $id_usuario != '') {
            $query = "SELECT * FROM db_pronostico_usuario "
                    . "WHERE id_partido = '$id_partido' "
                    . "AND id_usuario = '$id_usuario'";
        }
        return self::$data->get_results_from_query($query);
    }
    
    /**
     * Obtiene los datos del usuario
     * 
     * @param string $id_usuario 
     * @return array datos del usuario
     */
    public function get_datos_usuario($id_usuario) {
        $query = "";
        if ($id_usuario != '') {
            $query = "SELECT u.id_usuario as 'id_usuario', "
                    . "u.nombre as 'nombre_usuario', "
                    . "u.apellidos as 'apellidos_usuario', "
                    . "u.email as 'email_usuario', "
                    . "c.id_comunidad as 'id_comunidad', "
                    . "c.nombre as 'nombre_comunidad' "
                    . "FROM db_usuario u "
                    . "LEFT JOIN db_usuario_comunidad uc "
                    . "ON u.id_usuario = uc.id_usuario "
                    . "LEFT JOIN db_comunidad c "
                    . "ON uc.id_comunidad = c.id_comunidad "
                    . "WHERE u.id_usuario = '$id_usuario' ";
        }
        return self::$data->get_results_from_query($query);
    }
    
    /**
     * Devuelve el resultado más votado de un partido
     * 
     * @param string $id_partido
     * @return string
     */
    public function get_pronostico_partido($id_partido, $id_comunidad) {
        $query = "";
        if ($id_partido != '') {
            $query = "SELECT pu.apuesta "
                    . "FROM db_pronostico_usuario pu "
                    . "LEFT JOIN db_usuario_comunidad uc "
                    . "ON pu.id_usuario = uc.id_usuario "
                    . "WHERE pu.id_partido = '$id_partido' "
                    . "AND uc.id_comunidad = '$id_comunidad' "
                    . "GROUP BY pu.apuesta "
                    . "ORDER BY COUNT(pu.apuesta) "
                    . "DESC LIMIT 1";
        }
        return self::$data->get_results_from_query($query);
    }
    
    /**
     * Comprueba que existe la comunidad y que la contraseña es correcta
     * 
     * @param string $id_comunidad
     * @param string $pass_comunidad
     * @return boolean
     */
    public function confirmar_comunidad($id_comunidad, $pass_comunidad) {
        $query = "";
        if ($id_comunidad != '' && $pass_comunidad != '') {
            $query =  "SELECT    id_comunidad "
                    . "FROM      db_comunidad "
                    . "WHERE     id_comunidad = '$id_comunidad' "
                    . "AND       pass = '$pass_comunidad'";
        }
        $rows = self::$data->get_results_from_query($query);
        return count($rows) > 0;
    }
    
    /**
     * Comprueba si el usuario pertenece a alguna comunidad ya
     * 
     * @param string $id_usuario
     * @return boolean
     */
    public function tiene_comunidad($id_usuario) {
        $query = "";
        if ($id_usuario != '') {
            $query = "SELECT id_comunidad "
                    . "FROM db_usuario_comunidad "
                    . "WHERE id_usuario = '$id_usuario'";
        }
        $rows = self::$data->get_results_from_query($query);
        return count($rows) > 0;
    }
    
    /**
     * Cambia la comunidad a la que pertenece un usuario
     * 
     * @param string $id_usuario
     * @param string $id_comunidad
     * @return boolean
     */
    public function modificar_comunidad($id_usuario, $id_comunidad) {
        $query = "";
        if ($id_usuario != '' && $id_comunidad != '') {
            $query = "UPDATE  db_usuario_comunidad "
                        . "SET     id_comunidad = '$id_comunidad', "
                        . "        fecha_inicio = '".date('Y-m-d')."' "
                        . "WHERE   id_usuario = '$id_usuario'";
        }
        return self::$data->execute_single_query($query);
    }
    
    /**
     * Ingresa el usuario en una comunidad
     * 
     * @param string $id_usuario
     * @param string $id_comunidad
     * @return boolean
     */
    public function ingresar_comunidad($id_usuario, $id_comunidad) {
        $query = "";
        if ($id_usuario != '' && $id_comunidad != '') {
            $query = "INSERT INTO   db_usuario_comunidad "
                       . "              (id_usuario, id_comunidad, fecha_inicio) "
                       . "VALUES        ('$id_usuario', $id_comunidad, '" 
                       . date('Y-m-d') . "')";
        }
        return self::$data->execute_single_query($query);
    }
    
    /**
     * Según un id_comunidad devuelve su nombre
     * 
     * @param string $id_comunidad
     * @return string
     */
    public function get_nombre_comunidad($id_comunidad) {
        $query = "";
        if ($id_comunidad != '') {
            $query = "SELECT nombre "
                    . "FROM db_comunidad "
                    . "WHERE id_comunidad = '$id_comunidad'";
        }
        $rows = self::$data->get_results_from_query($query);
        return $rows[0]["nombre"];
    }
    
    /**
     * Modifica nombre, apellidos y email de un usuario dado
     * 
     * @param string $id_usuario
     * @param string $nombre
     * @param string $apellidos
     * @param string $email
     * @return boolean
     */
    public function modificar_ajustes($id_usuario, $nombre, $apellidos, $email) {
        $query = "";
        if ($id_usuario != '' && $nombre != '' && $apellidos != '' && $email != '') {
            $query = "UPDATE  db_usuario "
                        . "SET     nombre = '$nombre', "
                        . "        apellidos = '$apellidos', "
                        . "        email = '$email' "
                        . "WHERE   id_usuario = '$id_usuario'";
        }
        return self::$data->execute_single_query($query);
    }
    
    /**
     * Obtiene los datos nombre, apellidos y email del usuario
     * 
     * @param string $id_usuario
     * @return array
     */
    public function obtener_datos_ajustes($id_usuario) {
        $query = "";
        if ($id_usuario != '') {
            $query = "SELECT nombre, apellidos, email "
                    . "FROM db_usuario "
                    . "WHERE id_usuario = '$id_usuario'";
        }
        $rows = self::$data->get_results_from_query($query);
        return $rows[0];
    }
    
    /**
     * Crea una comunidad nueva
     * 
     * @param string $id_comunidad
     * @param string $pass_comunidad
     * @param string $nombre_comunidad
     * @return boolean
     */
    public function crear_comunidad($id_comunidad, $pass_comunidad, $nombre_comunidad) {
        $query = "";
        if ($id_comunidad != '' && $pass_comunidad != '' && $nombre_comunidad != '') {
            $query = "INSERT INTO db_comunidad "
                       . "(id_comunidad, pass, nombre, fecha_creacion) "
                       . "VALUES ('$id_comunidad', '$pass_comunidad', '$nombre_comunidad', '" 
                       . date('Y-m-d') . "')";
        }
        return self::$data->execute_single_query($query);
    }
}