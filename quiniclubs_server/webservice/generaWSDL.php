<?php

/* 
 * Clase que genera el WSDL
 * 
 * Creado 07-may-2015
 * @author: kamarena
 */

require_once dirname(__DIR__) . '/webservice/ws_metodos.php';
require_once dirname(__DIR__) . '/webservice/WSDLDocument.php';

$wsdl = new WSDLDocument(
        "WSMetodos",
        "http://localhost/kamarena/quiniclubs_server/webservice/ws_soap_server.php",
        "http://localhost/kamarena/quiniclubs_server/webservice"
        );

echo $wsdl->saveXML();