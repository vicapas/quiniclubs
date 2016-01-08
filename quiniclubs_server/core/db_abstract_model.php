<?php
/*
 * Clase Abstracta Base para el Modelo de Base de Datos
 * 
 * Creado en 19/04/2015
 * @author: kamarena
 */

class DBModel {

    private static $db_host = "localhost";
    private static $db_user = "root";
    private static $db_pass = "abc123";
    private static $db_name = "db_quiniclubs";
    private static $conn; 

    # Método de conexión a la Base de Datos
    private function open_connection() {
        $SERVIDOR = filter_input_array(INPUT_SERVER);
        $PATH = $SERVIDOR['DOCUMENT_ROOT'] . "/kamarena/quiniclubs_server";
        include "$PATH/core/adodb5/adodb.inc.php";
        $bd=NewADOConnection("mysql");
        $bd->Connect(
            self::$db_host,
            self::$db_user,
            self::$db_pass,
            self::$db_name);
        mysql_query("SET NAMES 'utf8'");
        self::$conn = $bd;
    }

    # Ejecutar una query simple del tipo INSERT, DELETE, UPDATE
    public function execute_single_query($query) {
        if (self::$conn == null) {
            $this->open_connection();            
        }
        return self::$conn->Execute($query);
    }

    # Obtener resultados desde una consulta a un Array
    public function get_results_from_query($query) {
        if (self::$conn == null) {
            $this->open_connection();            
        }
        # Se inicia una variable con el resultado de la consulta a DB
        $result = self::$conn->Execute($query);
        $rows = array();
        # Cada fila de $row obtiene un array con los datos de la fila de la DB
        # $rows se convierte en un array bidimensional
        while (!($result->EOF)){
            $rows[]=$result->fields;
            $result->MoveNext();
        }
        # Se devuelve el array con los datos que arroja la consulta
        return $rows;
    }

}
