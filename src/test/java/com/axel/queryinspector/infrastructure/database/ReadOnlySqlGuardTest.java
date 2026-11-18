package com.axel.queryinspector.infrastructure.database;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import org.junit.jupiter.api.Test;
class ReadOnlySqlGuardTest {
 private final ReadOnlySqlGuard guard=new ReadOnlySqlGuard();
 @Test void allowsSelectFromKnownObject(){assertDoesNotThrow(()->guard.validate("SELECT ID FROM FACTURAS",Set.of("FACTURAS")));}
 @Test void rejectsDelete(){assertThrows(IllegalArgumentException.class,()->guard.validate("DELETE FROM FACTURAS",Set.of("FACTURAS")));}
 @Test void rejectsUnknownObject(){assertThrows(IllegalArgumentException.class,()->guard.validate("SELECT * FROM USERS",Set.of("FACTURAS")));}
 @Test void rejectsMultipleStatements(){assertThrows(IllegalArgumentException.class,()->guard.validate("SELECT ID FROM FACTURAS; DELETE FROM FACTURAS",Set.of("FACTURAS")));}
 @Test void rejectsForUpdate(){assertThrows(IllegalArgumentException.class,()->guard.validate("SELECT ID FROM FACTURAS FOR UPDATE",Set.of("FACTURAS")));}
}
