<?php

/* 
 * Guión que se ocupa de levantar el servicio web
 * 
 * Creadp 03/05/2015
 * @author: kamarena
 */

require_once 'ws_metodos.php';

$server = new SoapServer("http://localhost/kamarena/quiniclubs_server/webservice/ws_soap_server.wsdl");
$server->setClass("WSMetodos");
$server->handle();
