package exists;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

/**
 *
 * @author a16alfonsofa
 */
class Consultas {

    static String calCuotaFinal(Collection col) {
   
          String doc = "<cuotas_adicionales>";
        String accion = "for $socio in /Socios/socio\n"
                + "return <datos>\n"
                + "        <cod>{data($socio/@codigo)}</cod>\n"
                + "        <nombresocio>{data($socio/nombre)}</nombresocio>\n"
                + "        {\n"
                + "            for $usos in /USO_GIMNASIO/fila_uso\n"
                + "            let $activ := /Actividades/actividad[@codigo=$usos/CODACTIV]\n"
                + "            let $horas := number($usos/HORAFINAL/text())-number($usos/HORAINICIO/text())\n"
                + "            let $cadic := number($activ/cuota_adicional/text()) * $horas\n"
                + "            where $usos/CODSOCIO = $socio/@codigo\n"
                + "            return <usos>\n"
                + "                {$usos/CODACTIV}\n"
                + "                <nombre>{data($activ/nombre)}</nombre>\n"
                + "                <tipoact>{data($activ/@tipo)}</tipoact>\n"
                + "                <horas>{$horas}</horas>\n"
                + "                <cuota_adicional>{$cadic}</cuota_adicional>\n"
                + "            </usos>\n"
                + "        }\n"
                + "    </datos>";

        try {
            XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

            ResourceSet result = servicio.query(accion);

            ResourceIterator i;

            i = result.getIterator();

            if (!i.hasMoreResources()) {

                System.out.println(" LA CONSULTA NO DEVUELVE NADA ");

            }

            while (i.hasMoreResources()) {

                Resource r = i.nextResource();
                doc += (String) r.getContent();
            }
        } catch (XMLDBException ex) {
            Logger.getLogger(Consultas.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc += "</cuotas_adicionales>";
        return doc;
    
    }

    static void cuotaFinal(Collection col) {
   
          String accion = "for $socio in /Socios/socio\n"
                + "let $adictot := sum(/cuotas_adicionales/datos[cod=$socio/@codigo]/usos/cuota_adicional)\n"
                + "let $ctot := number($socio/cuota/text()) + $adictot\n"
                + "return <datos>\n"
                + "    <cod>{data($socio/@codigo)}</cod>\n"
                + "    {$socio/nombre}\n"
                + "    <cuota_fija>{data($socio/cuota)}</cuota_fija>\n"
                + "    <sum_cuota_adic>{$adictot}</sum_cuota_adic>\n"
                + "    <cuota_total>{$ctot}</cuota_total>\n"
                + "    </datos>";

        try {
            XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

            ResourceSet result = servicio.query(accion);

            ResourceIterator i;

            i = result.getIterator();

            if (!i.hasMoreResources()) {

                System.out.println(" LA CONSULTA NO DEVUELVE NADA ");

            }

            while (i.hasMoreResources()) {
                Resource r = i.nextResource();
                System.out.println((String) r.getContent());
            }
        } catch (XMLDBException ex) {
            Logger.getLogger(Consultas.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
}
