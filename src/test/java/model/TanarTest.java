package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TanarTest {
    private Tanar underTest;
    @BeforeEach
    public void setUp(){
        underTest = new Tanar();
    }
    @Test
    public void eletkorValidSokTest() {
        underTest.setKor(61);
        Assertions.assertFalse(underTest.eletkorValid());
    }
    @Test
    public void eletkorValidKevesTest() {
        underTest.setKor(10);
        Assertions.assertFalse(underTest.eletkorValid());
    }
    @Test
    public void eletkorValidTest() {
        underTest.setKor(41);
        Assertions.assertTrue(underTest.eletkorValid());
    }
    @Test
    public void nevValidFirstLastNameTeszt() {
        underTest.setNev("Pál");
        Assertions.assertFalse(underTest.nevValid());
    }
    @Test
    public void nevValidTitleShortTeszt() {
        underTest.setNev("dr. Pál");
        Assertions.assertFalse(underTest.nevValid());
    }
    @Test
    public void nevValidTitleCapitalTeszt() {
        underTest.setNev("dr. pál István");
        Assertions.assertFalse(underTest.nevValid());
    }
    @Test
    public void nevValidDrTeszt() {
        underTest.setNev("dr. Pál István");
        Assertions.assertTrue(underTest.nevValid());
    }
    @Test
    public void nevValidIfjTeszt() {
        underTest.setNev("ifj. Pál István");
        Assertions.assertTrue(underTest.nevValid());
    }
    @Test
    public void nevValidIdTeszt() {
        underTest.setNev("id. Pál István");
        Assertions.assertTrue(underTest.nevValid());
    }
    @Test
    public void nevValidProfTeszt() {
        underTest.setNev("prof. Pál István");
        Assertions.assertTrue(underTest.nevValid());
    }
    @Test
    public void nevValidTeszt() {
        underTest.setNev("Pál István");
        Assertions.assertTrue(underTest.nevValid());
    }
    @Test
    public void azonValidBlankTeszt() {
        underTest.setAzon("   ");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidEmptyTeszt() {
        underTest.setAzon("");
        Assertions.assertFalse(underTest.azonValid());
    }
    @Test
    public void azonValidTeszt() {
        underTest.setAzon("PI42");
        Assertions.assertTrue(underTest.azonValid());
    }
    @Test
    public void szulIdoNotValidTeszt() {
        underTest.setKor(50);
        underTest.setSzuletesiIdo(new Date());
        Assertions.assertFalse(underTest.szulIdoValid());
    }
    @Test
    public void szulIdoFalseValidTeszt() {
        underTest.setKor(0);
        underTest.setSzuletesiIdo(new Date());
        Assertions.assertTrue(underTest.szulIdoValid());
    }
    @Test
    public void szulIdoValidTeszt() throws ParseException {
        underTest.setKor(8);
        underTest.setSzuletesiIdo(new SimpleDateFormat("yyyy-MM-dd").parse("2011-01-01"));
        Assertions.assertTrue(underTest.szulIdoValid());
    }
}
