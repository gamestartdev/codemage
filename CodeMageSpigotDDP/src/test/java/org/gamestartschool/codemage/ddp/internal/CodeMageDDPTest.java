package org.gamestartschool.codemage.ddp.internal;

import org.gamestartschool.codemage.ddp.CodeMageDDP;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URISyntaxException;

public class CodeMageDDPTest {
    @Test
    public void canConstructAPersonWithAName() throws URISyntaxException {
    	CodeMageDDP ddp = new CodeMageDDP("", 0);
        assertEquals("Larry", ddp);
    }
}
