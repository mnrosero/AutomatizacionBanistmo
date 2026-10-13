
package com.banistmo.automation.runners;

import com.banistmo.automation.userinterfaces.PaginaBanistmo;

import net.serenitybdd.annotations.Managed;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actions.Scroll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.net.URI;
import java.util.Set;

public class AbrirBanistmoTest {

    @Managed(driver = "chrome")
    WebDriver navegador;

    @Test
    public void abrirPaginaDeBanistmo() {

        // Crear el actor de Screenplay
        Actor usuario = Actor.named("Usuario Banistmo");

        // Asignar el navegador al actor
        usuario.can(BrowseTheWeb.with(navegador));

        // Ejecutar la navegación automatizada
        usuario.attemptsTo(

                // Abrir la página de Banistmo
                Open.url("https://www.banistmo.com/empresas"),

                // Aceptar aviso de privacidad
                Click.on(PaginaBanistmo.BOTON_ENTENDIDO),

                // Ingresar a Productos y servicios
                Click.on(PaginaBanistmo.PRODUCTOS_SERVICIOS),

                // Seleccionar Cuentas
                Click.on(PaginaBanistmo.CUENTAS),

                // Desplazarse hasta Cuenta de Ahorro Comercial
                Scroll.to(PaginaBanistmo.CUENTA_AHORRO_COMERCIAL),

                // Abrir Cuenta de Ahorro Comercial
                Click.on(PaginaBanistmo.CUENTA_AHORRO_COMERCIAL),

                // Desplazarse hasta Documentos
                Scroll.to(PaginaBanistmo.DOCUMENTOS),

                // Seleccionar Documentos
                Click.on(PaginaBanistmo.DOCUMENTOS),

                // Desplazarse hasta el contrato
                Scroll.to(PaginaBanistmo.CONTRATO_PDF),

                // Abrir el contrato PDF
                Click.on(PaginaBanistmo.CONTRATO_PDF)

        );

        // Obtener el navegador activo de Screenplay
        WebDriver navegadorActivo =
                BrowseTheWeb.as(usuario).getDriver();

        // Espera temporal para que aparezca la nueva pestaña
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Obtener las pestañas abiertas
        Set<String> pestanas =
                navegadorActivo.getWindowHandles();

        System.out.println(
                "Número de pestañas abiertas: " + pestanas.size()
        );

        // Verificar que se abrió otra pestaña
        Assertions.assertTrue(
                pestanas.size() > 1,
                "El contrato no se abrió en una nueva pestaña"
        );

        // Identificar la pestaña principal
        String pestanaPrincipal =
                navegadorActivo.getWindowHandle();

        // Cambiar a la pestaña del contrato
        for (String pestana : pestanas) {

            if (!pestana.equals(pestanaPrincipal)) {

                navegadorActivo.switchTo().window(pestana);
                break;
            }
        }

        // Obtener la URL del PDF
        String urlPdf = navegadorActivo.getCurrentUrl();

        System.out.println("URL del documento: " + urlPdf);

        // Comprobar que la URL corresponde a un PDF
        Assertions.assertTrue(
                urlPdf.toLowerCase().contains(".pdf"),
                "La nueva pestaña no contiene una URL de PDF"
        );

        // Leer el contenido del PDF utilizando PDFBox
        try (
                InputStream entrada =
                        URI.create(urlPdf).toURL().openStream();

                PDDocument documento =
                        Loader.loadPDF(entrada.readAllBytes())
        ) {

            PDFTextStripper extractor =
                    new PDFTextStripper();

            String textoPdf =
                    extractor.getText(documento);

            // Mostrar número de páginas
            System.out.println(
                    "Número de páginas del PDF: "
                            + documento.getNumberOfPages()
            );

            // Mostrar una parte del contenido
            System.out.println(
                    "Primeros caracteres del PDF:"
            );

            System.out.println(
                    textoPdf.substring(
                            0,
                            Math.min(500, textoPdf.length())
                    )
            );

            // Verificar que se pudo extraer texto
            Assertions.assertFalse(
                    textoPdf.isBlank(),
                    "El PDF no contiene texto legible"
            );

            String textoNormalizado = textoPdf
                    .replaceAll("\\s+", " ")
                    .toLowerCase(java.util.Locale.ROOT);

            Assertions.assertTrue(
                    textoNormalizado.contains(
                            "contrato único de productos y servicios bancarios"
                    ),
                    "El PDF no corresponde al Contrato Único de Productos y Servicios Bancarios"
            );

            Assertions.assertTrue(
                    documento.getNumberOfPages() > 0,
                    "El documento PDF no contiene páginas"
            );

            System.out.println(
                    "VALIDACIÓN EXITOSA: El PDF corresponde al contrato solicitado."
            );


            System.out.println(
                    "LECTURA EXITOSA: Se pudo leer el contenido del PDF."
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "No se pudo leer el contenido del PDF: "
                            + e.getMessage(),
                    e
            );
        }

        System.out.println(
                "PRUEBA FINALIZADA: Navegación y lectura del PDF."
        );

        // Espera temporal para observar el navegador
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
