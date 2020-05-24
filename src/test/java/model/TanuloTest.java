package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TanuloTest {
    private Tanulo underTest;
    @BeforeEach
    public void setUp(){
        underTest = new Tanulo();
    }
    @Test
    public void eletkorValidKevesTest() {
        underTest.setKor(1);
        Assertions.assertFalse(underTest.eletkorValid());
    }
    @Test
    public void eletkorValidTest() {
        underTest.setKor(41);
        Assertions.assertTrue(underTest.eletkorValid());
    }
    @Test
    public void nevValidFirstLastNameTeszt() {
        underTest.setNev("András");
        Assertions.assertFalse(underTest.nevValid());
    }
    @Test
    public void nevValidTitleShortTeszt() {
        underTest.setNev("id. András");
        Assertions.assertFalse(underTest.nevValid());
    }
    @Test
    public void nevValidTitleCapitalTeszt() {
        underTest.setNev("id. kiss András");
        Assertions.assertFalse(underTest.nevValid());
    }
    @Test
    public void nevValidTitleTeszt() {
        underTest.setNev("id. Kiss András");
        Assertions.assertTrue(underTest.nevValid());
    }
    @Test
    public void nevValidTeszt() {
        underTest.setNev("Kiss András");
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
    public void szulIdoValidTeszt() throws ParseException {
        underTest.setKor(8);
        underTest.setSzuletesiIdo(new SimpleDateFormat("yyyy-MM-dd").parse("2011-01-01"));
        Assertions.assertTrue(underTest.szulIdoValid());
    }
}
