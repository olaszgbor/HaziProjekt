package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class OsztalyTest {
    private Osztaly underTest;
    @BeforeEach
    public void setUp(){
        underTest = new Osztaly();
    }
    @Test
    public void letszamValidSokTest() {
        underTest.setLetszam(41);
        Assertions.assertFalse(underTest.letszamValid());
    }
    @Test
    public void letszamValidKevesTest(){
        underTest.setLetszam(-1);
        Assertions.assertFalse(underTest.letszamValid());
    }
    @Test
    public void letszamValidTobbTest(){
        underTest.setAktualisLetszam(25);
        underTest.setLetszam(24);
        Assertions.assertFalse(underTest.letszamValid());
    }
    @Test
    public void letszamValidTest(){
        underTest.setLetszam(24);
        underTest.setAktualisLetszam(23);
        Assertions.assertTrue(underTest.letszamValid());
    }
    //
    @Test
    public void azonValidNoPointTest(){
        underTest.setAzon("11c");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidNoNumTest(){
        underTest.setAzon("ab.c");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidNoLetterTest(){
        underTest.setAzon("11.1");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidZeroCTest(){
        underTest.setAzon("0.c");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidMinusTest(){
        underTest.setAzon("-1.c");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidFourteenTest(){
        underTest.setAzon("14.c");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidFullWordTest(){
        underTest.setAzon("11.class");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidOneNumTest(){ //azonValidBombasticTest
        underTest.setAzon("11.c4");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidTest(){
        underTest.setAzon("11.c");
        Assertions.assertTrue(underTest.azonValid());
    }
}
