/**
 *
 */
package de.hrw.wi.service;

import de.hrw.wi.business.Car;
import de.hrw.wi.persistence.DatabaseReadInterface;
import de.hrw.wi.persistence.DatabaseWriteInterface;
import de.hrw.wi.types.Datum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Teste CarRentalServiceImpl mit Mocking
 * <p>
 * TODO A14: Diese Testklasse soll Mockito zum Test von CarRentalServiceImpl (Attribut
 * carRentalService) mit Mock-Objekten für die Datenbankschicht verwenden. Es ist erst ein
 * Testfall testGetAllCars() vorhanden, allerdings wird die Testklasse noch nicht fertig für die
 * Durchführung von Tests initialisiert. Im Einzelnen:
 * 1.	Vervollständigen Sie die Initialisierung der vorhandenen Attribute dbReadMock und
 * dbWriteMock durch Mockito-Mock-Objekte so, dass der Testfall testGetAllCars() korrekt
 * durchläuft. Die Initialisierung soll so platziert werden, dass sie automatisch für weitere
 * Testfälle in der Klasse nutzbar wäre. Vorhanden dafür sind bereits die vollständigen Testdaten
 * als Konstanten. Achtung: Die Initialisierung soll nur die notwendigen Daten beinhalten.
 * 2.	Initialisieren Sie das Attribut carRentalService mit einem Objekt des Typs
 * CarRentalServiceImpl so, dass die Mock-Objekte auch verwendet werden.
 *
 * @author Andriessens
 */
public class CarRentalServiceImplTest {

    private static final int TEST_YEAR = 2024;
    private static final String LP_UL_M_123 = "UL-M 123";
    private static final String LP_RV_HS_111 = "RV-HS 111";
    private static final String BRAND_PORSCHE = "Porsche";
    private static final String BRAND_PEUGEOT = "Peugeot";

    // Testdaten
    private static final Car CAR_UL_M_123 = new Car(BRAND_PORSCHE, LP_UL_M_123);
    private static final Car CAR_RV_HS_111 = new Car(BRAND_PEUGEOT, LP_RV_HS_111);
    Set<String> carsTestData =
            new HashSet<String>(Arrays.asList(CAR_RV_HS_111.getId(), CAR_UL_M_123.getId()));
    private Datum from = new Datum(2025, 1, 31);
    private Datum till = new Datum(2025, 12, 31);

    private CarRentalServiceInterface carRentalService;
    private DatabaseReadInterface dbReadMock;
    private DatabaseWriteInterface dbWriteMock;

    @BeforeEach
    public void setUp() {
        dbReadMock = Mockito.mock(DatabaseReadInterface.class);
        dbWriteMock = Mockito.mock(DatabaseWriteInterface.class);
        when(dbReadMock.getAllCars()).thenReturn(carsTestData);

        //ab hier nur notwendig
        when(dbReadMock.getCarBrand(LP_UL_M_123)).thenReturn(BRAND_PORSCHE);
        when(dbReadMock.getCarBrand(LP_RV_HS_111)).thenReturn(BRAND_PEUGEOT);
        when(dbReadMock.findAvailableCar(from, till)).thenReturn(carsTestData);
        when(dbReadMock.isCarAvailable(BRAND_PORSCHE, from, till)).thenReturn(true);
        when(dbReadMock.isCarAvailable(BRAND_PEUGEOT, from, till)).thenReturn(true);
        when(dbReadMock.getFirstName("1")).thenReturn("Peter");
        when(dbReadMock.getFirstName("2")).thenReturn("Hans");
        //
        
        carRentalService = new CarRentalServiceImpl(dbReadMock, dbWriteMock);
    }

    @Test
    public void testGetAllCars() {
        Set<Car> cars = carRentalService.getAllCars();
        assertEquals(2, cars.size());
        assertTrue(cars.contains(CAR_RV_HS_111));
        assertTrue(cars.contains(CAR_UL_M_123));
    }

}