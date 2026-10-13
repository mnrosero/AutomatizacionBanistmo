
package com.banistmo.automation.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class PaginaBanistmo {

    public static final Target PRODUCTOS_SERVICIOS =
            Target.the("Menú Productos y servicios")
                    .located(By.xpath(
                            "//*[normalize-space(text())='Productos & servicios']"
                    ));

    public static final Target CUENTAS =
            Target.the("Opción Cuentas")
                    .located(By.xpath(
                            "//*[normalize-space(text())='Cuentas']"
                    ));

    public static final Target BOTON_ENTENDIDO =
            Target.the("Botón Entendido del aviso de privacidad")
                    .located(By.xpath(
                            "//button[normalize-space(.)='Entendido']"
                    ));


    public static final Target CUENTA_AHORRO_COMERCIAL =
            Target.the("Botón Descubre más de Cuenta de Ahorro Comercial")
                    .located(By.xpath(
                            "//div[@data-testid='ml-card']" +
                                    "[.//h3[normalize-space(.)='Cuenta de Ahorro Comercial']]" +
                                    "//button[@aria-label='Descubre más']"
                    ));


    public static final Target DOCUMENTOS =
            Target.the("Sección Documentos")
                    .located(By.xpath(
                            "//*[normalize-space(text())='Documentos']"
                    ));


    public static final Target CONTRATO_PDF =
            Target.the("Contrato único de Productos y Servicios Bancarios")
                    .located(By.xpath(
                            "//*[contains(normalize-space(.)," +
                                    "'Contrato Único de Productos y Servicios Bancarios')]" +
                                    "/self::a | //a[contains(normalize-space(.)," +
                                    "'Contrato Único de Productos y Servicios Bancarios')]"
                    ));


}
