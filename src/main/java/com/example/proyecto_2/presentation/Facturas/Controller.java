package com.example.proyecto_2.presentation.Facturas;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.proyecto_2.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;


import java.io.IOException;
import java.io.PrintWriter;

@SessionAttributes({"Sfacturas"})
@org.springframework.stereotype.Controller("Factura")
public class Controller {

    @Autowired
    private Service service;

    @GetMapping("/presentation/Facturas/show")
    public String show(Model model, HttpSession session) {
        Usuarios u= (Usuarios) session.getAttribute("usuario");
        Proveedores p= u.getProveedoresByIdprov();

        if(session.getAttribute("factura_BUSQUEDA")!=null){
            model.addAttribute("Sfacturas", session.getAttribute("factura_BUSQUEDA"));
            session.setAttribute("factura_BUSQUEDA",null);
        }
        else {
            Iterable<Facturas> f = service.findFacturasByIdProveedor(p.getIdP());
            session.setAttribute("Sfacturas", f);
            model.addAttribute("Sfacturas", f);
        }


        return "/Presentation/Facturas/view";
    }
    @GetMapping("/Facturas/pdf")
    public void pdf(@RequestParam("numFact") int numFact, HttpServletResponse response) throws IOException{
        try {
            Facturas facturas = service.get_FacturaXid(numFact);

            // Configure the HTTP response for the PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=Factura" + numFact + ".pdf");

            // Initialize the PDF writer and document
            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            // Add title of invoice
            Paragraph title = new Paragraph("Factura #" + facturas.getNumFact());
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Create table
            Table table = new Table(5);

            // Add table headers
            table.addCell(new Paragraph("Código"));
            table.addCell(new Paragraph( "Nombre"));
            table.addCell(new Paragraph( "Valor por unidad"));
            table.addCell(new Paragraph( "Cantidad"));
            table.addCell(new Paragraph( "Valor final"));

            // Add customer and supplier information
            document.add(new Paragraph( "Cliente: " + (facturas.getClientesByIdCliente().getNombreC())));
            document.add(new Paragraph( "Proveedor: " + (facturas.getProveedoresByIdProveedor().getNombreP())));

            // Add invoice details to the table
        Iterable<Detalle> detallesFactura = service.findDetallesByFacturaNumFact(numFact);
            for (Detalle detalle : detallesFactura) {
        Producto producto = (detalle.getProductoByIdProd());  // Store product details in a variable
        table.addCell(new Paragraph( producto.getIdPr()));
        table.addCell(new Paragraph(producto.getNombreP()));
        table.addCell(new Paragraph(String.valueOf(producto.getPrecio())));
        table.addCell(new Paragraph( String.valueOf(detalle.getCantidad())));
        table.addCell(new Paragraph( String.valueOf(detalle.getMonto())));
         }

    // Add the table to the document
            document.add(table);

    // Add total to the right of the document
            Paragraph total = new Paragraph("Total: " + facturas.getTotal());
            total.setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

    // Close the document
            document.close();
        } catch (Exception e) {
        // Handle potential exceptions during PDF generation
        e.printStackTrace();
        }
    }
    @GetMapping("/Facturas/xml")
    public void xml(@RequestParam("numFact") int numFact, HttpServletResponse response) throws IOException{
        Facturas factura = service.get_FacturaXid(numFact) ;
        Iterable<Detalle> detallesXml = service.findDetallesByFacturaNumFact(numFact);
        Integer totalFactura = factura.getTotal();
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(new AnnotationConfigApplicationContext());
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".xml");
        resolver.setCharacterEncoding ("UTF-8");
        resolver.setTemplateMode (TemplateMode. XML) ;
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine. setTemplateResolver(resolver);
        Context ctx = new Context();
        ctx.setVariable ("facturaXml", factura);
        ctx.setVariable( "detallesXml", detallesXml);
        ctx.setVariable("totalFactura", totalFactura);
        String xml = engine.process( "Presentation/Facturas/XmlView", ctx);
        response.setContentType("application/xml");
        //response.setHeader ( "Content-Disposition","attachment; filename=factura" + numFact + ".xml");
        PrintWriter writer= response.getWriter();
        writer.print(xml);
        writer.close();
    }
    @PostMapping("/factura/buscar")
    public String buscPro(@RequestParam("numFact") String numFact,Model model, HttpSession session){
        if(numFact==""){

            return "redirect:/presentation/Facturas/show";
        }
        Usuarios u=(Usuarios) session.getAttribute("usuario");
        session.setAttribute("factura_BUSQUEDA", service.findAllByIdProveedorAndNumFact(u.getProveedoresByIdprov().getIdP(),Integer.parseInt(numFact)));
        model.addAttribute("factura_BUSQUEDA", session.getAttribute("factura_BUSQUEDA"));
        return "redirect:/presentation/Facturas/show";
    }

}
