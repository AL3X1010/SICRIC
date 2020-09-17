/*  Nombre.....:  ServiciosU.java
 *  Descripción:  .
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Utilitarios;

import static Utilitarios.Consts.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

public class ServiciosU {
    // Variables públicas de la clase ServiciosU.

    private List<FileItem> formMap;

    // Método constructor de la clase ServiciosU.
    public ServiciosU(List<FileItem> form) {
        this.formMap = form;
    } // Fin del método constructor.
    
    
    // Encripta el recurso (url, usualmente) proporcionado en base64.
    public static String encryptBase64(String url) throws IOException{
         byte[] imageBytes = IOUtils.toByteArray(new URL(url));
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        return base64;
    }
    

    // Encripta la contraseña con SHA512.
    public static String encryptThisString(String input) {
        /*
     Función obtenida a partir de
     https://www.youtube.com/watch?v=dh8ura4rVUM
         */
        try {
            // getInstance() method is called with algorithm SHA-512 
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            md.update(input.getBytes());
            byte[] digestedBytes = md.digest();
            String hashtext = DatatypeConverter.printHexBinary(digestedBytes).toUpperCase();

            // return the HashText 
            return hashtext;
        } // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    } // Fin del método encryptThisString().
    

    public static void fb(ResultSet resultado) throws SQLException {
        JFrame panel = new JFrame();
        JOptionPane.showMessageDialog(
                panel, resultado.getInt(1) + "::"
                + resultado.getInt(2) + "::"
                + resultado.getString(3),
                "Error SQL", JOptionPane.ERROR_MESSAGE);
    }

    // Método para convertir DD/MMM/YYYY a {YYYY, MM, DD}.
    // Creado a partir de
    // https://stackoverflow.com/questions/30455790/not-able-to-use-getparameter-in-servlet-due-to-multipart-form-data
    public static int[] getLocalDate(String date) {
        String fecha = String.valueOf(formatearFecha(date));
        int[] arregloFecha = {
                Integer.parseInt(fecha.substring(0, 4)),
                Integer.parseInt(fecha.substring(4, 6)),
                Integer.parseInt(fecha.substring(6)) };  
        
        return arregloFecha;
    }

    // Método para recuperar los parámetros del request en Servlets, esto debido
    // a que al enviar documentos los parámetros ya no son accesibles de la manera
    // habitual sino que a través de un mapa de datos.
    // Creado a partir de
    // https://stackoverflow.com/questions/30455790/not-able-to-use-getparameter-in-servlet-due-to-multipart-form-data
    public String getParameter(String paramName) {
        List<FileItem> fileItemsList = this.formMap;
        Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();

        while (fileItemsIterator.hasNext()) {
            FileItem fileItem = fileItemsIterator.next();
            if (fileItem.getFieldName().equals(paramName)) {
                return fileItem.getString();
            }
        }

        return null;
    }

    // Permite conocer si un texto está compuesto únicamente de número.
    public static boolean isNumeric(String cadena) {
        /*
     Función obtenida a partir de
     https://es.stackoverflow.com/questions/92139/c%C3%B3mo-verificar-que-el-valor-de-una-variable-string-es-un-integer-en-java
         */
        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    } // Fin del método isNumeric().

    public String padLeftZeros(String inputString, int length) {
        // Método para llenar con ceros a la izquierda la cadena de texto proporcionada.
        // Creado a partir de
        // https://www.baeldung.com/java-pad-string
        if (inputString.length() >= length) {
            return inputString;
        }

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }

        return sb.append(inputString).toString();
    } // Fin del método padLeftZeros().

    public void setFormMap(List<FileItem> form) {
        this.formMap = form;
    } // Fin del método setFormMat().
    

    // Sustituye vocales tildadas y otros signos que debido al encoding no se
    // interpretan bien por la página web.
    public static String eliminarCaracteresEspeciales(String cadena) {
        String cadenaSustituida = cadena;

        cadenaSustituida = cadenaSustituida.replace(A_MAYUSCULA_TILDADA, A_MAYUSCULA);
        cadenaSustituida = cadenaSustituida.replace(INICIO_ADMIRACION, INICIO_ADMIRACION_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(E_MAYUSCULA_TILDADA, E_MAYUSCULA);
        cadenaSustituida = cadenaSustituida.replace(I_MAYUSCULA_TILDADA, I_MAYUSCULA);
        cadenaSustituida = cadenaSustituida.replace(O_MAYUSCULA_TILDADA, O_MAYUSCULA);
        cadenaSustituida = cadenaSustituida.replace(U_MAYUSCULA_TILDADA, U_MAYUSCULA);
        cadenaSustituida = cadenaSustituida.replace(N_MAYUSCULA_TILDADA, N_MAYUSCULA);
        cadenaSustituida = cadenaSustituida.replace(A_MINUSCULA_TILDADA, A_MINUSCULA);
        cadenaSustituida = cadenaSustituida.replace(E_MINUSCULA_TILDADA, E_MINUSCULA);
        cadenaSustituida = cadenaSustituida.replace(I_MINUSCULA_TILDADA, I_MINUSCULA);
        cadenaSustituida = cadenaSustituida.replace(O_MINUSCULA_TILDADA, O_MINUSCULA);
        cadenaSustituida = cadenaSustituida.replace(U_MINUSCULA_TILDADA, U_MINUSCULA);
        cadenaSustituida = cadenaSustituida.replace(N_MINUSCULA_TILDADA, N_MINUSCULA);

        return cadenaSustituida;
    } // Fin del método eliminarCaracteresEspeciales().

    public static long fechaATimestamp() {
        Instant instant = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        String fechaSalida = String.valueOf(instant);

        fechaSalida = fechaSalida.substring(0, 4)
                + fechaSalida.substring(5, 7)
                + fechaSalida.substring(8, 10)
                + fechaSalida.substring(11, 13)
                + fechaSalida.substring(14, 16)
                + fechaSalida.substring(17, 19)
                + fechaSalida.substring(20, 23);

        return Long.valueOf(fechaSalida);
    } // Fin del método fechaATimestamp.
    

    // Cambia la fecha DD/MM/YYYY por su equivalente en número YYYYMMDD.
    public static int formatearFecha(String fecha) {
        int fechaSalida = FECHA_PREDEFINIDA;
        String fechaTexto = fecha.toString().replace("/", "");
        fechaTexto = fechaTexto.replace("Ene", "01");
        fechaTexto = fechaTexto.replace("Feb", "02");
        fechaTexto = fechaTexto.replace("Mar", "03");
        fechaTexto = fechaTexto.replace("Abr", "04");
        fechaTexto = fechaTexto.replace("May", "05");
        fechaTexto = fechaTexto.replace("Jun", "06");
        fechaTexto = fechaTexto.replace("Jul", "07");
        fechaTexto = fechaTexto.replace("Ago", "08");
        fechaTexto = fechaTexto.replace("Sep", "09");
        fechaTexto = fechaTexto.replace("Oct", "10");
        fechaTexto = fechaTexto.replace("Nov", "11");
        fechaTexto = fechaTexto.replace("Dic", "12");

        if (isNumeric(fechaTexto)) {
            // establece el año
            fechaSalida
                    = Integer.parseInt(fechaTexto.substring(4)) * 10000
                    + Integer.parseInt(fechaTexto.substring(2, 4)) * 100
                    + Integer.parseInt(fechaTexto.substring(0, 2));
        }

        return fechaSalida;
    } // Fin del método formatearFecha().
    

    // Extrae fecha, hora de inicio y hora final del los paramteros brindados.
    public static int formatearFecha(String fechaCompleta, boolean fecha) throws ParseException {
        if(fecha){
            String anio = fechaCompleta.substring(11, 15);
            String dia = fechaCompleta.substring(8, 10);
            String mes = fechaCompleta.substring(4, 7);
            mes = mes.equals("Jan") ? "01" :
                  mes.equals("Feb") ? "02" :
                  mes.equals("Mar") ? "03" :
                  mes.equals("Apr") ? "04" :
                  mes.equals("May") ? "05" :
                  mes.equals("Jun") ? "06" :
                  mes.equals("Jul") ? "07" :
                  mes.equals("Aug") ? "08" :
                  mes.equals("Sep") ? "09" :
                  mes.equals("Oct") ? "10" :
                  mes.equals("Nov") ? "11" : "12";
            
            return Integer.parseInt(anio + mes + dia);
        } else
            return Integer.parseInt(fechaCompleta.substring(16, 21).replace(":", ""));
    } // Fin del método formatearFecha().
    
    
    // Cambia el entero provista por su equivalente en texto DD-MM-YYYY.
    public static String formatearFecha(int fecha) {
        String fechaSalida;

        if (fecha < FECHA_PREDEFINIDA) {
            fechaSalida
                    = String.valueOf(FECHA_PREDEFINIDA).substring(6, 8)
                    + "/" + String.valueOf(FECHA_PREDEFINIDA).substring(4, 6) + "/"
                    + String.valueOf(FECHA_PREDEFINIDA).substring(0, 4);
        } else {
            fechaSalida
                    = String.valueOf(fecha).substring(6, 8)
                    + "/" + String.valueOf(fecha).substring(4, 6) + "/"
                    + String.valueOf(fecha).substring(0, 4);
        }

        fechaSalida = fechaSalida.replace("/01/", "/Ene/");
        fechaSalida = fechaSalida.replace("/02/", "/Feb/");
        fechaSalida = fechaSalida.replace("/03/", "/Mar/");
        fechaSalida = fechaSalida.replace("/04/", "/Abr/");
        fechaSalida = fechaSalida.replace("/05/", "/May/");
        fechaSalida = fechaSalida.replace("/06/", "/Jun/");
        fechaSalida = fechaSalida.replace("/07/", "/Jul/");
        fechaSalida = fechaSalida.replace("/08/", "/Ago/");
        fechaSalida = fechaSalida.replace("/09/", "/Sep/");
        fechaSalida = fechaSalida.replace("/10/", "/Oct/");
        fechaSalida = fechaSalida.replace("/11/", "/Nov/");
        fechaSalida = fechaSalida.replace("/12/", "/Dic/");

        return fechaSalida;
    } // Fin del método formatearFecha().
    
    
    // Cambia el entero provisto por su equivalente en texto DD-MM-YYYY HH:MM:SS.
    public static String formatearFecha(long fecha) {
        String fechaSalida;
        
        fechaSalida =
                String.valueOf(fecha).substring(6, 8) + "/" + 
                String.valueOf(fecha).substring(4, 6) + "/" +
                String.valueOf(fecha).substring(0, 4) + " " +
                String.valueOf(fecha).substring(8, 10) + ":" +
                String.valueOf(fecha).substring(10, 12) + ":" +
                String.valueOf(fecha).substring(12, 14);

        fechaSalida = fechaSalida.replace("/01/", "/Ene/");
        fechaSalida = fechaSalida.replace("/02/", "/Feb/");
        fechaSalida = fechaSalida.replace("/03/", "/Mar/");
        fechaSalida = fechaSalida.replace("/04/", "/Abr/");
        fechaSalida = fechaSalida.replace("/05/", "/May/");
        fechaSalida = fechaSalida.replace("/06/", "/Jun/");
        fechaSalida = fechaSalida.replace("/07/", "/Jul/");
        fechaSalida = fechaSalida.replace("/08/", "/Ago/");
        fechaSalida = fechaSalida.replace("/09/", "/Sep/");
        fechaSalida = fechaSalida.replace("/10/", "/Oct/");
        fechaSalida = fechaSalida.replace("/11/", "/Nov/");
        fechaSalida = fechaSalida.replace("/12/", "/Dic/");

        return fechaSalida;
    } // Fin del método formatearFecha().
    
    
    // Convierte la hora HHMM en formato HH:MM XM.
    public static String formatearHora(int hora) {
        String meridiano = hora >= 1200 ? "PM" : "AM";
        String HH = String.format("%02d", hora % 1200 / 100);
        String MM = String.format("%02d", hora % 100);
        return HH + ":" + MM + " " + meridiano;
    } // Fin del método formatearNumero().
    
    
    // Convierte la hora HHMM en formato HH:MM de 24 horas.
    public static String formatearHora24(int hora) {
        String HH = String.format("%02d", hora / 100);
        String MM = String.format("%02d", hora % 100);
        return HH + ":" + MM;
    } // Fin del método formatearNumero().

    
    // Completa el número proporcionado hasta dos cifras decimales.
    public static String formatearNumero(float numero) {
        DecimalFormat df2 = new DecimalFormat("0.00");
        return df2.format(numero);
    } // Fin del método formatearNumero().

    
    // Permite guardar archivos subidos por el usuario en el servidor.
    public void guardarArchivo(int idCentro, String idGenerico, String idBorrar, List nombreArchivo,
            String remitente, int[] gravedad) throws Exception {
        // Funcionalidad elaborada a partir de
        // https://www.journaldev.com/1964/servlet-upload-file-download-example
        int numeroDeArchivos = 1;
        int numeroMaximo = 0;
        String idCentroTexto = String.format("%05d", idCentro);
        String directorioRaiz = System.getProperty("catalina.home");
        String nombreArchivoAGuardar = null;
        String nombreArchivoCreado = null;
        String nombreArchivoBorrar = null;
        String extensionPermitida = null;
        String[] extensionesPermitidas = null;
        File directorioFinal = null;

        // Crea el directorio de la aplicación en el directorio raíz.
        File directorioApp = new File(
                directorioRaiz + File.separator + Consts.CARPETA_APP + File.separator
                + Consts.SUBCARPETA_APP + idCentroTexto);

        if (!directorioApp.exists()) {
            if (!directorioApp.mkdirs()) {
                return;
            }
        }

        // Define en qué directorio se almacenarán los archivos.
        if (remitente.equals(Consts.REMITE_CENTRO)) {
            directorioFinal = new File(directorioApp + File.separator + Consts.CARPETA_CENTRO);
            nombreArchivoCreado = Consts.PREFIJO_CENTRO + idCentroTexto;
            extensionPermitida = Consts.EXTENSION_PERMITIDA_CENTRO;
            numeroMaximo = Consts.NUMERO_MAXIMO_ARCHIVOS_CENTRO;
        } else if (remitente.equals(Consts.REMITE_EMPLEADO)) {
            directorioFinal = new File(directorioApp + File.separator + Consts.CARPETA_EMPLEADOS);
            nombreArchivoCreado = Consts.PREFIJO_EMPLEADOS + padLeftZeros(idGenerico, 12);
            nombreArchivoBorrar = Consts.PREFIJO_EMPLEADOS + padLeftZeros(idBorrar, 12);
            extensionPermitida = Consts.EXTENSION_PERMITIDA_EMPLEADO;
            numeroMaximo = Consts.NUMERO_MAXIMO_ARCHIVOS_EMPLEADO;
        } else if (remitente.equals(Consts.REMITE_PACIENTE)) {
            directorioFinal = new File(directorioApp + File.separator + Consts.CARPETA_PACIENTES);
            nombreArchivoCreado = Consts.PREFIJO_PACIENTES + padLeftZeros(idGenerico, 12);
            extensionesPermitidas = Consts.EXTENSIONES_PERMITIDAS_PACIENTE;
            numeroMaximo = Consts.NUMERO_MAXIMO_ARCHIVOS_PACIENTE;
        } else {
            return;
        }

        // Crea el directorio final donde se almacenarán los archivos.
        if (!directorioFinal.exists()) {
            if (!directorioFinal.mkdir()) {
                return;
            }
        }

        // Almacena el archivo proporcionado.
        List<FileItem> fileItemsList = formMap;
        Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
        while (fileItemsIterator.hasNext()) {
            FileItem fileItem = fileItemsIterator.next();
            if (fileItem.getFieldName().equals(Consts.CAMPO_ARCHIVO) // campo de archivos
                    && fileItem.getName().lastIndexOf(".") > 0 // tiene extensión
                    && numeroDeArchivos <= numeroMaximo) {
                // Antes de guardarlo verifica la extensión del archivo proporcionado.
                String extensionArchivo = fileItem.getName().substring(fileItem.getName().lastIndexOf(".")).toLowerCase();
                if (numeroMaximo == 1) {
                    if (!extensionArchivo.equals(extensionPermitida)) // Itera de nuevo por si viene un archivo que coincida con la ext. permitida
                    {
                        continue;
                    }

                    nombreArchivoAGuardar = nombreArchivoCreado + extensionArchivo;
                } else {
                    if (!Arrays.asList(extensionesPermitidas).contains(extensionArchivo)) // Itera de nuevo por si viene un archivo que coincida con la ext. permitida
                    {
                        continue;
                    }

                    nombreArchivoAGuardar = nombreArchivoCreado + "_"
                            + String.format("%03d", numeroDeArchivos++)
                            + extensionArchivo;
                }

                // Agrega extensión al nombre del archivo que se irá a guardar, así como al
                // que se eliminará cuando dicho procedimiento aplique.
                if (remitente.equals(Consts.REMITE_EMPLEADO)) {
                    nombreArchivoBorrar += extensionArchivo;
                    File fileToErase = new File(directorioFinal + File.separator + nombreArchivoBorrar);
                    if (idBorrar.trim().length() > 0 && fileToErase.exists()) {
                        fileToErase.delete();
                    }
                }

                File file = new File(directorioFinal + File.separator + nombreArchivoAGuardar);
                if (file.exists()) {
                    file.delete();
                }

                fileItem.write(file);
                nombreArchivo.add(fileItem.getName());
                gravedad[0] = Consts.GRAVEDAD_CERO;
            } else if (fileItem.getFieldName().equals(CAMPO_ARCHIVO) && numeroDeArchivos > numeroMaximo) {
                gravedad[0] = Consts.GRAVEDAD_ADVERTENCIA;
                return;
            } else if (fileItem.getFieldName().equals(CAMPO_ARCHIVO)
                    && fileItem.getFieldName().lastIndexOf(".") <= 0
                    && numeroMaximo == 1) {
                return;
            }
        }
    } // Fin del método guardarArchivo().
    
    
    // Convierte los parametros proporcionados (YYYYMMDD, HHMM) en formato
    // YYYY-MM-DDTHH:MM:SS.MMM-06:00.
    public static String obtenerFechaEvento(int fecha, int hora) {
        String fechaTexto = String.valueOf(fecha).substring(0, 4) + "-" +
                            String.valueOf(fecha).substring(4, 6) + "-" +
                            String.valueOf(fecha).substring(6);
        String horaTexto = hora < 1000 ? (
                            "0" + String.valueOf(hora).substring(0, 1) + ":" +
                            String.valueOf(hora).substring(1) + ":00.000") :
                           (String.valueOf(hora).substring(0, 2) + ":" +
                            String.valueOf(hora).substring(2) + ":00.000");
        

        return fechaTexto + "T" + horaTexto + "-06:00";
    } // Fin del método obtenerFechaEvento().
    

    // Cambia el formato HH:MM XM a numero entero.
    public static int obtenerHora(String texto) {
        String[] partes = texto.split(":");
        int hora = Integer.parseInt(partes[0]) * 100;
        hora = partes[1].split(" ")[1].equals("AM") ? hora : hora + 1200;
        int minutos = Integer.parseInt(partes[1].split(" ")[0]);

        return hora + minutos;

    } // Fin del método obtenerParametro().
    

    // Toma el primer nombre y el primer apellido de los parámetros proporcionados.
    public static String obtenerNombreCompleto(String nombres, String apellidos) {
        String[] partesNombre = nombres.split(" ");
        String[] partesApellido = apellidos.split(" ");
        

        return partesNombre[0] + " " + partesApellido[0];
    } // Fin del método obtenerNombreCompleto().
    

    // Recorta el valor proporcionado por la página web para asegurar que sea del
    // mismo tamaño que el campo correspondiente de la BD.
    public static String obtenerParametro(String texto, int longitud) {
        int longitudCampo = texto.trim().length();
        longitudCampo = longitudCampo > longitud ? longitud : longitudCampo;

        return texto.trim().substring(0, longitudCampo);
    } // Fin del método obtenerParametro().

    
    // Recorta el valor proporcionado por la página web para asegurar que sea del 
    // mismo tamaño que el campo correspondiente de la BD.
    public static int obtenerParametro(String texto, int minimo, int maximo) {
        int numero = Integer.parseInt(texto);

        if (numero < minimo || numero > maximo) {
            throw new AssertionError();
        } else {
            return numero;
        }
    } // Fin del método obtenerParametro().

    
    public static String redondear(float number) {
        /*
     Función modificada a partir de
     https://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals
         */

        BigDecimal bd = new BigDecimal(Float.toString(number));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP); // 2 -> decimal places
        return textoDosDecimales(bd.floatValue());
    } // Fin del método redondear().

    
    public static String redondear(String number) {
        /*
     Función modificada a partir de
     https://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals
         */

        float localNumber = Float.parseFloat(number);
        BigDecimal bd = new BigDecimal(Float.toString(localNumber));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP); // 2 -> decimal places
        return textoDosDecimales(String.valueOf(bd.floatValue()));
    } // Fin del método redondear().

    
    // Reemplaza los caracteres especiales por su equivalente en UNICODE.
    public static String sustituirCaracteres(String cadena) {
        String cadenaSustituida = cadena;

        cadenaSustituida = cadenaSustituida.replace(A_MAYUSCULA_TILDADA, A_MAYUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(INICIO_ADMIRACION, INICIO_ADMIRACION_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(E_MAYUSCULA_TILDADA, E_MAYUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(I_MAYUSCULA_TILDADA, I_MAYUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(O_MAYUSCULA_TILDADA, O_MAYUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(U_MAYUSCULA_TILDADA, U_MAYUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(N_MAYUSCULA_TILDADA, N_MAYUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(A_MINUSCULA_TILDADA, A_MINUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(E_MINUSCULA_TILDADA, E_MINUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(I_MINUSCULA_TILDADA, I_MINUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(O_MINUSCULA_TILDADA, O_MINUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(U_MINUSCULA_TILDADA, U_MINUSCULA_TILDADA_UNICODE);
        cadenaSustituida = cadenaSustituida.replace(N_MINUSCULA_TILDADA, N_MINUSCULA_TILDADA_UNICODE);

        return cadenaSustituida;
    } // Fin del método sustituirCaracteres().

    
    // Recibe un número decimal y lo devuelve como texto, siempre manteniendo
    // dos posiciones decimales.
    public static String textoDosDecimales(float numero) {
        DecimalFormat df = new DecimalFormat("####.##");
        df.setMaximumFractionDigits(2);
        String monto = String.valueOf(df.format(numero));

        if (monto.indexOf(".") == -1) {
            return monto + ".00";
        } else if (monto.indexOf(".") == monto.length() - 2) {
            return monto + "0";
        } else {
            return monto;
        }
    } // Fin del método textoDosDecimales().

    
    // Recibe un número decimal en texto y lo devuelve flotante.
    public static String textoDosDecimales(String numero) {
        if (numero.indexOf(".") == -1) {
            return numero + ".00";
        } else if (numero.indexOf(".") == numero.length() - 2) {
            return numero + "0";
        } else {
            return numero;
        }
    } // Fin del método textoDosDecimales().
} // Fin de la clase ServiciosU.java
