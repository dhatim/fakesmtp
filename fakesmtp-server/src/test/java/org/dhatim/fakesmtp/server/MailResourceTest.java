package org.dhatim.fakesmtp.server;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.MailMessageImpl;
import com.dumbster.smtp.SmtpServer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.GenericType;
import org.dhatim.fakesmtp.client.Mail;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class MailResourceTest {

    private static final SmtpServer serverDao = mock(SmtpServer.class);
    private static final DumbsterManager dao = mock(DumbsterManager.class);
    
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new MailResource(dao))
            .build();
    
    private final Mail mail = new Mail(ImmutableMap.of("header1", ImmutableList.of("value1"), "header2", ImmutableList.of("value1", "value2")), "This is a test");
    
    @Before
    public void setUp() {
        MailMessageImpl message = new MailMessageImpl();
        message.addHeader("header1", "value1");
        message.addHeader("header2", "value1");
        message.addHeader("header2", "value2");
        message.appendBody("This is a test");
        
        when(dao.getServer()).thenReturn(serverDao);
        when(serverDao.getMessages()).thenReturn(new MailMessage[] {message});
    }
    
    @After
    public void tearDown() {
        reset(dao);
        reset(serverDao);
    }
    
    @Test
    public void testGetMessages() {
        assertThat(resources.client().target("/mails").request().get(new GenericType<List<Mail>>() {}))
                .isEqualTo(Arrays.asList(mail)); 
        verify(dao).getServer();
    }
    
}
