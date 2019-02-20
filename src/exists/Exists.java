package exists;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

/**
 *
 * @author a16alfonsofa
 */
public class Exists {

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String driver = "org.exist.xmldb.DatabaseImpl";
        Collection col = null;
        String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/Gimnasio";
        String usu = "admin";
        String usuPwd = "admin";
        String accion = null;
        int numeroDep;
        String nombreDep;
        String localidad;

        byte op = 0;

        try {

            Class cl = Class.forName(driver);
            Database database = (Database) cl.newInstance();
            DatabaseManager.registerDatabase((org.xmldb.api.base.Database) database);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            System.out.println("Error al inicializar la BD en Exist" + e);
        }

        try {
            
            col = (Collection) DatabaseManager.getCollection(URI, usu, usuPwd);
            
            if (col == null) {
                System.err.println("ERROR -- No existe la colección. ");
            } else {
                
                //Cuota que tenemos que pagar
                String documento;
                documento = Consultas.calCuotaFinal(col);
                try {
                    File f = new File("archivo.xml");

                    PrintStream ps = new PrintStream(f);
                    ps.print(documento);
                    ps.close();
                    
                    if (!f.canRead()) {
                        System.err.println("ERROR -- No se puede leer el fichero. ");
                    } else {

                        Resource res = col.createResource("archivo.xml", "XMLResource");
                        res.setContent(f);
                        col.storeResource(res);

                    }
                    System.out.println("Información Adicional\n"
                            + "Cuotas Finales ---------------------");
                    Consultas.cuotaFinal(col);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (XMLDBException e) {

        }
    }

}
