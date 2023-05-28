package ua.java.conferences.controller.tag;

import jakarta.servlet.jsp.JspContext;
import jakarta.servlet.jsp.JspWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NowTagTest {

    @Test
    void testDoTag() throws IOException {
        try (JspWriter jspWriter = mock(JspWriter.class)) {
            JspContext jspContext = mock(JspContext.class);
            when(jspContext.getOut()).thenReturn(jspWriter);
            NowTag nowTag = new NowTag();
            nowTag.setJspContext(jspContext);
            assertDoesNotThrow(nowTag::doTag);
            verify(jspWriter).print(LocalDate.now().plusDays(1));
        }
    }
}