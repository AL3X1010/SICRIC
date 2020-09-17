/*  Nombre.....:  Departamento.java
 *  Descripción:  Llama al procedimiento almacenado encargado de crear o
 *                modificar el departamento.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Julio, 2019.
 */
package Controlador;

import Modelo.$;
import Utilitarios.Consts;
import Utilitarios.ServiciosU;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Reporte extends HttpServlet {
    // Variables globales de la clase Reporte.
    Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Font.UNDERLINE, new CMYKColor(0, 0, 0, 255));
    Font tinyFont = FontFactory.getFont(FontFactory.TIMES, 5, Font.NORMAL, new CMYKColor(0, 0, 0, 255));
    Font boldFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(0, 0, 0, 255));
    Font boldRedFont = FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD, new CMYKColor(0, 100, 100, 0));
    Font boldAltFont = FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLD, new CMYKColor(0, 0, 0, 255));
    Font normalFont = FontFactory.getFont(FontFactory.TIMES, 9, Font.NORMAL, new CMYKColor(0, 0, 0, 255));

    private int fechaInicio;
    private int fechaFinal;
            
    
    // Métodos GET y SET de la clase Reporte.
    public int getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(int fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(int fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    } // Fin del método processRequest().

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.html");    // redirige a página principal.
    } // Fin del método doGet().

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Variables para trabajar con tablas.
        $ instanciaBD = null;
        ResultSet registros = null; // contiene los registros que devuelve la sentencia SELECT.

        // Variables para enviar y recibir información.
        HttpSession sesion = request.getSession(false); // crea o recupera sesion
        sesion.setAttribute("idCentro", 1);
        String btnSubmit = request.getParameter("btnSubmit");

        String reporteSolicitado = request.getParameter("seleccionReporte");
        setFechaInicio(ServiciosU.formatearFecha(request.getParameter("fechaInicio")));
        setFechaFinal(ServiciosU.formatearFecha(request.getParameter("fechaFinal")));

        if (btnSubmit != null && sesion.getAttribute("idCentro") != null) {
            try {
                int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
                int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
                instanciaBD = new $();

                boolean respuesta = false;

                if (reporteSolicitado.equals(Consts.REPORTE_INV_EQUIPO_MEDICO)) {
                    respuesta = equiposMedicos(sesion, request, response, instanciaBD = new $());
                } else if (reporteSolicitado.equals(Consts.REPORTE_INV_SUMINISTRO_MEDICO)
                        || reporteSolicitado.equals(Consts.REPORTE_INV_MEDICAMENTO)) {
                    respuesta = reporteMixto(sesion, request, response, instanciaBD = new $());
                } else if (reporteSolicitado.equals(Consts.REPORTE_TERAPEUTA)) {
                    respuesta = terapeutas(sesion, request, response, instanciaBD = new $());
                }
                /*
                else if(reporteSolicitado.equals(Consts.REPORTE_BITACORA_X_SESION))
                    respuesta = sesion(sesion, request, response, instanciaBD = new $());
                else if(reporteSolicitado.equals(Consts.REPORTE_BITACORA_X_EVENTO))
                    respuesta = evento(sesion, request, response, instanciaBD = new $());
                 */
                else
                    response.sendRedirect(Consts.REPORTE_HTML);
                
            } catch (Exception ex) {
                ex.printStackTrace();
                // Redirecciona a la página web de origen y muestra mensaje de
                // retroalimentación al usuario.
                sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                        "En este momento no puede realizar cambios debido a inconsistencias en su sesión."));
                response.sendRedirect(Consts.INDEX_HTML);
            }
        } else {
            // Redirecciona a la página web de inicio de sesión y muestra mensaje de
            // retroalimentación al usuario.
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "En este momento no puede realizar cambios debido a inconsistencias en su sesión."));
            response.sendRedirect(Consts.INDEX_HTML);
        }
    } // Fin del método doPost().
    
    
    // Genera el reporte de los equipos médicos activos y con fecha de mantenimiento
    // dentro del rango de fechas proporcionadas por el usuario.
    private boolean equiposMedicos(HttpSession sesion, HttpServletRequest request,
            HttpServletResponse response, $ instanciaBD) throws ClassNotFoundException, IOException {
        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
        int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());

        // Genera el reporte de acuerdo al rol, tiene que coincidir con el rol
        // asignado al usuario.
        if (rolAsignado != Consts.ROL_ADMINISTRADOR
                && rolAsignado != Consts.ROL_COLABORADOR) {
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "No puede acceder al recurso solicitado."));
            response.sendRedirect(Consts.REPORTE_HTML);
            return false;
        }

        try {
            String myQuery
                    = "SELECT Marca, Modelo, SerieEquipoMedico, Nombre, "
                    + "       FechaIngreso, FechaMantenimiento  "
                    + "FROM Inventario_Equipo_Medico AS I "
                    + "     INNER JOIN Catalogo_Equipo_Medico AS C "
                    + "        ON I.ID_Catalogo = C.ID_Catalogo "
                    + "WHERE FechaMantenimiento BETWEEN "
                    + getFechaInicio()
                    + " AND "
                    + getFechaFinal() + " "
                    + "  AND Activo = 1 "
                    + "  AND ID_Centro = " + idCentro + " "
                    + "ORDER BY FechaMantenimiento DESC, SerieEquipoMedico;";

            // Define las propiedades del objeto que contiene el documento a imprimir.
            Document document = new Document();
            response.setContentType("application/pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            Paragraph titulo = new Paragraph("Reporte de Mantenimiento de Equipos Médicos", titleFont);
            titulo.setAlignment(1);

            document.add(titulo);
            document.add(new Paragraph("\n\n\n", tinyFont));
            document.add(new Paragraph("Fecha de busqueda: "
                    + request.getParameter("fechaInicio") + "  -  "
                    + request.getParameter("fechaFinal")));
            document.add(new Paragraph("\n", tinyFont));

            ResultSet listaEquipoMedico = instanciaBD.execQry(myQuery);
            if (listaEquipoMedico.next()) {
                PdfPTable table = new PdfPTable(6); // Six columns
                table.setWidthPercentage(100);      // Widht 100%
                table.setSpacingBefore(5f);         // space before table
                table.setSpacingAfter(10f);         // space after table

                // Set columns widths
                float[] columnsWidth = {1f, 1f, 1f, 1f, 1f, 1f};
                table.setWidths(columnsWidth);

                // Set header names
                PdfPCell hcell_01 = new PdfPCell(new Paragraph("Marca", boldFont));
                PdfPCell hcell_02 = new PdfPCell(new Paragraph("Modelo", boldFont));
                PdfPCell hcell_03 = new PdfPCell(new Paragraph("Serie", boldFont));
                PdfPCell hcell_04 = new PdfPCell(new Paragraph("Nombre", boldFont));
                PdfPCell hcell_05 = new PdfPCell(new Paragraph("Fecha de Ingreso", boldAltFont));
                PdfPCell hcell_06 = new PdfPCell(new Paragraph("Próximo Mantenimiento", boldAltFont));

                hcell_01.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_01.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_02.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_02.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_03.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_03.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_04.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_04.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_05.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_05.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_06.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_06.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(hcell_01);
                table.addCell(hcell_02);
                table.addCell(hcell_03);
                table.addCell(hcell_04);
                table.addCell(hcell_05);
                table.addCell(hcell_06);

                do {
                    PdfPCell cell_01 = new PdfPCell(new Paragraph(
                            listaEquipoMedico.getString("Marca"), normalFont));
                    PdfPCell cell_02 = new PdfPCell(new Paragraph(
                            listaEquipoMedico.getString("Modelo"), normalFont));
                    PdfPCell cell_03 = new PdfPCell(new Paragraph(
                            listaEquipoMedico.getString("SerieEquipoMedico"), normalFont));
                    PdfPCell cell_04 = new PdfPCell(new Paragraph(
                            listaEquipoMedico.getString("Nombre"), normalFont));
                    PdfPCell cell_05 = new PdfPCell(new Paragraph(
                            ServiciosU.formatearFecha(
                                    listaEquipoMedico.getInt("FechaIngreso")), normalFont));
                    PdfPCell cell_06 = new PdfPCell(new Paragraph(
                            ServiciosU.formatearFecha(
                                    listaEquipoMedico.getInt("FechaMantenimiento")), normalFont));
                    // cell_01.setBorderColor(BaseColor.BLUE);
                    cell_01.setPaddingLeft(10);
                    cell_01.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_01.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_02.setPaddingLeft(10);
                    cell_02.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_02.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_03.setPaddingLeft(10);
                    cell_03.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_03.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_04.setPaddingLeft(10);
                    cell_04.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_04.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_05.setPaddingLeft(10);
                    cell_05.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_05.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_06.setPaddingLeft(10);
                    cell_06.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_06.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    table.addCell(cell_01);
                    table.addCell(cell_02);
                    table.addCell(cell_03);
                    table.addCell(cell_04);
                    table.addCell(cell_05);
                    table.addCell(cell_06);
                } while (listaEquipoMedico.next());

                document.add(table);
            } else {
                // Genera contenido alterno al no encontrar registros.
                Paragraph paragraph = new Paragraph("\n\n* * *   NO SE ENCONTRARON REGISTROS   * * *", boldRedFont);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);
            }

            document.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "Ocurrió un error al intentrar crear el recurso solicitado."));
            return false;
        }
    }
    
    
    // Genera el reporte de los suministros médicos o equipos... Dada la similitud
    // de ambos procesos se crean con el mismo procedimiento cambiando únicamente
    // las tablas a las que apunta.
    private boolean reporteMixto(HttpSession sesion, HttpServletRequest request,
            HttpServletResponse response, $ instanciaBD) throws ClassNotFoundException, IOException {
        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
        int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());
        String reporteSolicitado = request.getParameter("seleccionReporte");
        final String TABLAS_MEDICAMENTO = "FROM Catalogo_Medicamento C "
                + "     INNER JOIN Inventario_Medicamento I "
                + "        ON C.Serie = I.Serie ";
        final String TABLAS_SUMINISTRO = "FROM Catalogo_Suministro_Medico C "
                + "    INNER JOIN Inventario_Suministro_Medico I "
                + "       ON C.Serie = I.Serie ";

        // Genera el reporte de acuerdo al rol, tiene que coincidir con el rol
        // asignado al usuario.
        if (rolAsignado != Consts.ROL_ADMINISTRADOR
                && ((rolAsignado != Consts.ROL_COLABORADOR
                && reporteSolicitado.equals(Consts.REPORTE_INV_SUMINISTRO_MEDICO))
                || (rolAsignado != Consts.ROL_FARMACIA_APROBADOR
                && rolAsignado != Consts.ROL_FARMACIA_OPERADOR
                && reporteSolicitado.equals(Consts.REPORTE_INV_MEDICAMENTO)))) {
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "No puede acceder al recurso solicitado."));
            response.sendRedirect(Consts.REPORTE_HTML);
            return false;
        }

        try {
            // Define las propiedades del objeto que contiene el documento a imprimir.
            Document document = new Document();
            response.setContentType("application/pdf");
            
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Paragraph titulo;
            if (reporteSolicitado.equals(Consts.REPORTE_INV_MEDICAMENTO)) {
                titulo = new Paragraph("Reporte de Inventario de Medicamentos", titleFont);
            } else {
                titulo = new Paragraph("Reporte de Inventario de Suministros Médicos", titleFont);
            }

            titulo.setAlignment(1);
            document.add(titulo);
            document.add(new Paragraph("\n\n\n", tinyFont));
            document.add(new Paragraph("Fecha de busqueda: "
                    + request.getParameter("fechaInicio") + "  -  "
                    + request.getParameter("fechaFinal")));
            document.add(new Paragraph("\n", tinyFont));
            
            String myQuery
                    = "SELECT C.Serie, I.Lote, Nombre, CostoLote, CantidadIngresada, FechaVencimiento "
                    + (reporteSolicitado.equals(Consts.REPORTE_INV_SUMINISTRO_MEDICO)
                    ? TABLAS_SUMINISTRO : TABLAS_MEDICAMENTO)
                    + "WHERE FechaVencimiento BETWEEN "
                    + getFechaInicio() + " AND " + getFechaFinal() + " "
                    + "  AND CantidadIngresada > 0 "
                    + "  AND ID_Centro = " + idCentro + " "
                    + "ORDER BY FechaVencimiento ASC, Serie, CantidadIngresada DESC;";
            
            ResultSet listaMixta = instanciaBD.execQry(myQuery);
            if (listaMixta.next()) {
                PdfPTable table = new PdfPTable(6); // Six columns
                table.setWidthPercentage(100);      // Widht 100%
                table.setSpacingBefore(5f);         // space before table
                table.setSpacingAfter(10f);         // space after table

                // Set columns widths
                float[] columnsWidth = {1f, 1f, 1f, 1f, 1f, 1f};
                table.setWidths(columnsWidth);

                // Set header names
                PdfPCell hcell_01 = new PdfPCell(new Paragraph("Serie", boldFont));
                PdfPCell hcell_02 = new PdfPCell(new Paragraph("Lote", boldFont));
                PdfPCell hcell_03 = new PdfPCell(new Paragraph("Nombre", boldFont));
                PdfPCell hcell_04 = new PdfPCell(new Paragraph("Costo Lote", boldFont));
                PdfPCell hcell_05 = new PdfPCell(new Paragraph("Cantidad Ingresada", boldAltFont));
                PdfPCell hcell_06 = new PdfPCell(new Paragraph("Fecha Vencimiento", boldAltFont));

                hcell_01.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_01.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_02.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_02.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_03.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_03.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_04.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_04.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_05.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_05.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcell_06.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell_06.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(hcell_01);
                table.addCell(hcell_02);
                table.addCell(hcell_03);
                table.addCell(hcell_04);
                table.addCell(hcell_05);
                table.addCell(hcell_06);

                do {
                    PdfPCell cell_01 = new PdfPCell(new Paragraph(
                            listaMixta.getString("Serie"), normalFont));
                    PdfPCell cell_02 = new PdfPCell(new Paragraph(
                            listaMixta.getString("Lote"), normalFont));
                    PdfPCell cell_03 = new PdfPCell(new Paragraph(
                            listaMixta.getString("Nombre"), normalFont));
                    PdfPCell cell_04 = new PdfPCell(new Paragraph(
                            ServiciosU.formatearNumero(
                            listaMixta.getFloat("CostoLote")), normalFont));
                    PdfPCell cell_05 = new PdfPCell(new Paragraph(
                            listaMixta.getString("CantidadIngresada"), normalFont));
                    PdfPCell cell_06 = new PdfPCell(new Paragraph(
                            ServiciosU.formatearFecha(
                                    listaMixta.getInt("FechaVencimiento")), normalFont));
                    
                    // cell_01.setBorderColor(BaseColor.BLUE);
                    cell_01.setPaddingLeft(10);
                    cell_01.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_01.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_02.setPaddingLeft(10);
                    cell_02.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_02.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_03.setPaddingLeft(10);
                    cell_03.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_03.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_04.setPaddingLeft(10);
                    cell_04.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_04.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_05.setPaddingLeft(10);
                    cell_05.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_05.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_06.setPaddingLeft(10);
                    cell_06.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell_06.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    table.addCell(cell_01);
                    table.addCell(cell_02);
                    table.addCell(cell_03);
                    table.addCell(cell_04);
                    table.addCell(cell_05);
                    table.addCell(cell_06);
                } while (listaMixta.next());
                
                document.add(table);
            } else {
                // Genera contenido alterno al no encontrar registros.
                Paragraph paragraph = new Paragraph("\n\n* * *   NO SE ENCONTRARON REGISTROS   * * *", boldRedFont);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);
            }
            
            document.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "Ocurrió un error al intentrar crear el recurso solicitado."));
            return false;
        }
    }
    
    
    // Genera el reporte de los equipos médicos activos y con fecha de mantenimiento
    // dentro del rango de fechas proporcionadas por el usuario.
    private boolean terapeutas(HttpSession sesion, HttpServletRequest request,
            HttpServletResponse response, $ instanciaBD) throws ClassNotFoundException, IOException {
        int idCentro = Integer.parseInt(sesion.getAttribute("idCentro").toString());
        int rolAsignado = Integer.parseInt(sesion.getAttribute("idRol").toString());

        // Genera el reporte de acuerdo al rol, tiene que coincidir con el rol
        // asignado al usuario.
        if (rolAsignado != Consts.ROL_SA
                && rolAsignado != Consts.ROL_ADMINISTRADOR) {
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "No puede acceder al recurso solicitado."));
            response.sendRedirect(Consts.REPORTE_HTML);
            return false;
        }

        try {
            String myQuery =
                    "SELECT E.ID_Empleado, E.Nombres NombreTerapeuta, " +
                    "       E.Apellidos ApellidoTerapeuta, " +
                    "       ID_PlanRehabilitacion, P.ID_Paciente, " +
                    "       P.Nombres NombrePaciente, P.Apellidos ApellidoPaciente " +
                    "FROM Plan_Rehabilitacion PR " +
                    "     INNER JOIN Empleado E " +
                    "        ON PR.ID_Empleado = E.ID_Empleado " +
                    "     INNER JOIN Paciente P " +
                    "        ON PR.ID_Paciente = P.ID_Paciente " +
                    "WHERE PR.Activo = 1;";

            // Define las propiedades del objeto que contiene el documento a imprimir.
            Document document = new Document();
            response.setContentType("application/pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            document.add(new Paragraph("\n", tinyFont));
            Paragraph titulo = new Paragraph("CENTRO DE REHABILITACIÓN INTEGRAL", titleFont);
            titulo.setAlignment(1);
            document.add(titulo);
            titulo = new Paragraph("SEDE CIUDAD UNIVERSITARIA", titleFont);
            titulo.setAlignment(1);
            document.add(titulo);
            titulo = new Paragraph("Reporte de Pacientes por Terapeuta", titleFont);
            titulo.setAlignment(1);
            document.add(titulo);
            
            
            document.add(new Paragraph("\n\n\n", tinyFont));
            document.add(new Paragraph("\n", tinyFont));
            List listaTerapeutas = new List(List.ORDERED);
            List listaPacientes = new List(List.UNORDERED);
            listaPacientes.setListSymbol("• ");
            listaPacientes.setIndentationLeft(25);
            String idEmpleadoAnterior = "";

            ResultSet resultadoTerapeutas = instanciaBD.execQry(myQuery);
            if (resultadoTerapeutas.next()) {
                do{
                    String idEmpleado = resultadoTerapeutas.getString("ID_Empleado");
                    String idPlanRehabilitacion = resultadoTerapeutas.getString("ID_PlanRehabilitacion");
                    String idPaciente = resultadoTerapeutas.getString("ID_Paciente");
                    String nombreCompletoPaciente =
                            resultadoTerapeutas.getString("NombrePaciente").trim() + " " +
                            resultadoTerapeutas.getString("ApellidoPaciente").trim();


                    if(!idEmpleado.equals(idEmpleadoAnterior)){
                        String nombreCompletoTerapeuta =
                                resultadoTerapeutas.getString("NombreTerapeuta").trim() + " " +
                                resultadoTerapeutas.getString("ApellidoTerapeuta").trim();

                        if(idEmpleadoAnterior.length() > 0){
                            // Cierra grupo anterior.
                            listaTerapeutas.add(listaPacientes);
                            listaPacientes = new List(List.UNORDERED);
                            listaPacientes.setListSymbol("• ");
                            listaPacientes.setIndentationLeft(25);
                        }

                        idEmpleadoAnterior = idEmpleado;
                        listaTerapeutas.add(new ListItem("Terapeuta:      " + idEmpleado + "  -  " + nombreCompletoTerapeuta));
                    }

                    listaPacientes.add(new ListItem(idPaciente + "  -  " + nombreCompletoPaciente));
                } while(resultadoTerapeutas.next());
                
                listaTerapeutas.add(listaPacientes);            
                document.add(listaTerapeutas);
            } else {
                // Genera contenido alterno al no encontrar registros.
                Paragraph paragraph = new Paragraph("\n\n* * *   NO SE ENCONTRARON REGISTROS   * * *", boldRedFont);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);
            }

            document.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            sesion.setAttribute("mensaje", ServiciosU.sustituirCaracteres(
                    "Ocurrió un error al intentrar crear el recurso solicitado."));
            return false;
        }
    }
}