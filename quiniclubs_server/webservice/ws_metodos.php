<?php
/* 
 * Clase que provee al Servicio Web de todos los métodos que necesita
 * 
 * Created 03/05/2015
 * @author: kamarena
 */


class WSMetodos {

    /**
     * Login-
     * 
     * @param string
     * @param string
     * @return integer
     */
    public function login($id, $pass) {
        $SERVIDOR = filter_input_array(INPUT_SERVER);
        $PATH = $SERVIDOR['DOCUMENT_ROOT'] . "/kamarena/quiniclubs_server";
        include "$PATH/clases/db_funciones_ws.php";
        $funcion = new DBFunciones();
        $resultado = 0;
        if ($funcion->login($id, $pass)) {$resultado = 1;}
        return $resultado;
    }
    
    /**
     * Inserta-
     * 
     * @param string $id
     * @param string $pass
     * @param string $email
     * @param string $nombre
     * @param string $apellidos
     * @return integer
     */
    public function nuevo_usuario($id, $pass, $email, $nombre, $apellidos) {
        $SERVIDOR = filter_input_array(INPUT_SERVER);
        $PATH = $SERVIDOR['DOCUMENT_ROOT'] . "/kamarena/quiniclubs_server";
        include "$PATH/clases/db_funciones_ws.php";
        $funcion = new DBFunciones();
        $resultado = 0;
        $datos = array(
            'id_usuario' => $id, 
            'pass' => $pass, 
            'nombre' => $nombre, 
            'apellidos' => $apellidos, 
            'email' => $email, 
            'fecha_creacion' => date("Y-m-d H:i:s")
            );
        if($funcion->nuevo_usuario($datos)) { $resultado = 1; }
        return $resultado;
    }
    
    /**
     * Borra-
     * 
     * @param string
     * @return integer
     */
    public function borra_usuario($id) {
        $SERVIDOR = filter_input_array(INPUT_SERVER);
        $PATH = $SERVIDOR['DOCUMENT_ROOT'] . "/kamarena/quiniclubs_server";
        include "$PATH/clases/db_funciones_ws.php";
        $funcion = new DBFunciones();
        $resultado = 0;
        if ($funcion->borra_usuario($id)) {$resultado = 1;}        
        return $resultado;
    }
    
    /**
     * NuevoPronosticoUsuario-
     * 
     * @param string $id_usuario
     * @param integer $id_partido
     * @param string $apuesta
     * @return boolean
     */
    public function nuevo_pronostico_usuario($id_usuario,$id_partido,$apuesta) {
        $SERVIDOR = filter_input_array(INPUT_SERVER);
        $PATH = $SERVIDOR['DOCUMENT_ROOT'] . "/kamarena/quiniclubs_server";
        include "$PATH/clases/db_funciones_ws.php";
        $funcion = new DBFunciones();
        return $funcion->nuevo_pronostico_usuario($id_usuario, $id_partido, $apuesta);
    }
    
}