package blackJack;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import blackJack.model.Card;

public class CardTest {
	
	private Card card;
	
	private void checkCard(Card card, char suit, int face) { //Hjelpemetode for å sjekke om om suit og face er riktig
		assertEquals(card.getSuit(), suit);
		assertEquals(card.getFace(), face);
	}
	
	private static boolean compareImage(File fileA, File fileB) { // Inspirasjon fra Stack overflow. Returnerer true om to bilder er like     
	    try {
	        // Tar buffer data fra bilde-filer
	        BufferedImage biA = ImageIO.read(fileA);
	        DataBuffer dbA = biA.getData().getDataBuffer();
	        int sizeA = dbA.getSize();                      
	        BufferedImage biB = ImageIO.read(fileB);
	        DataBuffer dbB = biB.getData().getDataBuffer();
	        int sizeB = dbB.getSize();
	        // Sammenligner date-buffer-objektene
	        if(sizeA == sizeB) {
	            for(int i=0; i<sizeA; i++) { 
	                if(dbA.getElem(i) != dbB.getElem(i)) {
	                    return false;
	                }
	            }
	            return true;
	        }
	        else {
	            return false;
	        }
	    } 
	    catch (Exception e) { 
	        System.out.println("Failed to compare image files ...");
	        return  false;
	    }
	}
	
	@BeforeEach
	public void setup() {
		card = new Card('S', 1);
	}

	@Test
	@DisplayName("Test: Konstruktøren oppretter et Card-objekt med riktige verdier")
	public void testConstructor() {
		checkCard(card, card.getSuit(), card.getFace());
		checkCard(new Card('S', 13), 'S', 13);
		checkCard(new Card('H', 1), 'H', 1);
		checkCard(new Card('H', 13), 'H', 13);
		checkCard(new Card('D', 1), 'D', 1);
		checkCard(new Card('D', 13), 'D', 13);
		checkCard(new Card('C', 1), 'C', 1);
		checkCard(new Card('C', 13), 'C', 13);

		assertThrows(IllegalArgumentException.class, () -> {
			new Card('X', 1);
		}, "Skal ikke kunne lage et kort av typen X");

		assertThrows(IllegalArgumentException.class, () -> {
			new Card('S', 0);
		}, "Skal ikke kunne lage et kort med verdi 0");

		assertThrows(IllegalArgumentException.class, () -> {
			new Card('C', 14);
		}, "Skal ikke kunne lage et kort med verdi 14");
	}
	
	@Test
	@DisplayName("Test: getImage() gir forventet bildet av kortet, om getImage() ikke fungerer (at riktig fil ikke eksisterer) settes returneres et image av typen null")
	public void testGetImage() {
		card = new Card('S', 10);
		assertTrue(compareImage(new File("src/main/resources/blackJack/model/S10.png"),
				new File("src/main/resources/blackJack/model/" + card.toString() + ".png")));
		
		card = new Card('H', 7);
		assertTrue(compareImage(new File("src/main/resources/blackJack/model/H7.png"),
				new File("src/main/resources/blackJack/model/" + card.toString() + ".png")));
		
		card = new Card('D', 5);
		assertTrue(compareImage(new File("src/main/resources/blackJack/model/D5.png"),
				new File("src/main/resources/blackJack/model/" + card.toString() + ".png")));
		
		card = new Card('C', 1);
		assertTrue(compareImage(new File("src/main/resources/blackJack/model/C1.png"),
				new File("src/main/resources/blackJack/model/" + card.toString() + ".png")));	
		
		File gyldigNavn = new File("src/main/resources/blackJack/model/C1.png"); // Navn på C1-filen
		File ugyldigNavn = new File("src/main/resources/blackJack/model/ClubsOne.png"); // Det nye ugyldige filnavnet til C1-filen
		
		gyldigNavn.renameTo(ugyldigNavn);
		
//		assertNull(card.getImage());
		assertThrows(Exception.class,
				() -> card.getImage(), "Exception skal kastest når det ikke finnes bilde til det angitte kortet"
				);
		
		
		
	}

	@Test
	@DisplayName("Test: toString fungerer som forventet")
	public void testToString() {
		Assertions.assertEquals("S1", new Card('S', 1).toString());
		Assertions.assertEquals("H13", new Card('H', 13).toString());
	}
	
	@AfterAll
	@DisplayName("Sikrer at filnavn ikke endres pga. testen")
	static void teardown() {
		File gyldigNavn = new File("src/main/resources/blackJack/model/C1.png"); // Navn på C1-filen
		File ugyldigNavn = new File("src/main/resources/blackJack/model/ClubsOne.png");
		
		ugyldigNavn.renameTo(gyldigNavn);
	}
}
