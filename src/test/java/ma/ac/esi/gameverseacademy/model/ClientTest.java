package ma.ac.esi.gameverseacademy.model;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires de l'entite Client.
 * Permet de generer un rapport de couverture avec JaCoCo.
 */
class ClientTest {

    @Test
    void testConstructeurVide() {
        Client client = new Client();
        assertNotNull(client);
        assertEquals(0, client.getId());
    }

    @Test
    void testConstructeurComplet() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Client client = new Client(1, "Mohammed", "Douas", "med@gameverse.ma",
                "0600000000", "Sale", "Premium", now);

        assertEquals(1, client.getId());
        assertEquals("Mohammed", client.getFirstName());
        assertEquals("Douas", client.getLastName());
        assertEquals("med@gameverse.ma", client.getEmail());
        assertEquals("0600000000", client.getPhone());
        assertEquals("Sale", client.getCity());
        assertEquals("Premium", client.getSubscriptionType());
        assertEquals(now, client.getCreatedAt());
    }

    @Test
    void testSetters() {
        Client client = new Client();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        client.setId(10);
        client.setFirstName("Sara");
        client.setLastName("Alaoui");
        client.setEmail("sara@gameverse.ma");
        client.setPhone("0611111111");
        client.setCity("Rabat");
        client.setSubscriptionType("Standard");
        client.setCreatedAt(now);

        assertEquals(10, client.getId());
        assertEquals("Sara", client.getFirstName());
        assertEquals("Alaoui", client.getLastName());
        assertEquals("sara@gameverse.ma", client.getEmail());
        assertEquals("0611111111", client.getPhone());
        assertEquals("Rabat", client.getCity());
        assertEquals("Standard", client.getSubscriptionType());
        assertEquals(now, client.getCreatedAt());
    }

    @Test
    void testToString() {
        Client client = new Client();
        client.setId(5);
        client.setFirstName("Yassine");
        client.setLastName("Bennani");
        client.setEmail("yassine@gameverse.ma");
        client.setSubscriptionType("Premium");

        String resultat = client.toString();
        assertTrue(resultat.contains("Yassine"));
        assertTrue(resultat.contains("Bennani"));
        assertTrue(resultat.contains("Premium"));
    }
}
