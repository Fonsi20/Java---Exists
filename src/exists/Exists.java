package exists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                System.out.println(" COLECCIÓN NO EXISTENTE ");
            } else {
                do {
                    System.out.println("--- MENU PRINCIPAL ---"
                            + "\n1. Insertar departamento"
                            + "\n2. Borrar departamento"
                            + "\n3. Modificar departamento"
                            + "\n4. Visualizar un departamento"
                            + "\n5. Visualizar nodos"
                            + "\n6. Salir");
                    op = Byte.parseByte(in.readLine());

                    switch (op) {
                        case 1:
                            System.out.println("Escribe el número del departamento:");
                            numeroDep = Integer.parseInt(in.readLine());

                            System.out.println("Escribe el nombre del departamento:");
                            nombreDep = in.readLine();

                            System.out.println("Escribe la localidad del departamento:");
                            localidad = in.readLine();

                            accion = "update insert "
                                    + "<filedepar>"
                                    + "<dept_no>" + numeroDep + "</dept_no>"
                                    + "<dnombre>" + nombreDep + "</dnombre>"
                                    + "<loc>" + localidad + "</loc>"
                                    + "</filedepar>"
                                    + "into /departamentos";
                            ejecutarAccion(col, accion);
                            break;
                        case 2:
                            System.out.println("Escribe el número del departamento a borrar:");
                            numeroDep = Integer.parseInt(in.readLine());
                            accion = "update delete /departamentos/filedepar[dept_no=" + numeroDep + "]";
                            ejecutarAccion(col, accion);
                            break;
                        case 3:
                                System.out.println("Introduzca el número del departamento que desea modificar");
                                numeroDep = Integer.parseInt(in.readLine());
                            do {

                              

                                System.out.println("---MODIFICACIONES---");
                                System.out.println("¿Qué desea modificar?"
                                        + "\n1.- Número departamento"
                                        + "\n2.- Nombre departamento"
                                        + "\n3.- Localidad departamento"
                                        + "\n4.- Salir");
                                      
                                 op = Byte.parseByte(in.readLine());
                                switch (op) {
                                    case 1:
                                        System.out.println("Escriba el nuevo número de departamento:");
                                        int nuevonumDep = Integer.parseInt(in.readLine());
                                        accion = "update value /departamentos/filedepar[dept_no=" + numeroDep + "]/dept_no with '" + nuevonumDep + "'";

                                        break;
                                    case 2:

                                        System.out.println("Escribe el nombre del departamento:");
                                        nombreDep = in.readLine();
                                        accion = "update value /departamentos/filedepar[dept_no=" + numeroDep + "]/dnombre with '" + nombreDep + "'";

                                        break;
                                    case 3:
                                        System.out.println("Escribe la localidad del departamento:");
                                        localidad = in.readLine();
                                        accion = "update value /departamentos/filedepar[dept_no=" + numeroDep + "]/loc with '" + localidad + "'";
                                        break;
                                    case 4:
                                        System.out.println("Has salido de modificaciones");
                                        break;
                                    default:
                                        System.out.println("la opción" + op + "no está contemplada");
                                        
                              
                                }
                             
                                
                                
                                ejecutarAccion(col, accion);
                          
                           
                            } while (op != 4);
                            break;
                        case 4:
                            System.out.println("Escribe el número del departamento:");
                            numeroDep = Integer.parseInt(in.readLine());
                            accion = "for $dep in /departamentos/filedepar[dept_no=" + numeroDep + "] $return dep";
                            ejecutarAccion(col, accion);
                            break;
                        case 5:
                            accion = "for $dep in /departamentos return $dep";
                            ejecutarAccion(col, accion);
                            break;
                        case 6:
                            System.out.println("Has salido del programa");
                            break;
                        default:
                            System.out.println("La opción" + op + "no está contemplada");
                    }
                   col.close();
                } while (op != 6);
            }
        } catch (XMLDBException | IOException ex) {
            System.out.println(ex);
        }

    }

    private static void ejecutarAccion(Collection col, String accion) {
        try {
            XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

            ResourceSet result = servicio.query(accion);

            ResourceIterator i;

            i = result.getIterator();

            if (!i.hasMoreResources()) {

                System.out.println(" LA CONSULTA NO DEVUELVE NADA ");

            }

            while (i.hasMoreResources()) {

                Resource r = (Resource) i.nextResource();
                System.out.println((String) r.getContent());
            }
        } catch (XMLDBException ex) {
            Logger.getLogger(Exists.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
