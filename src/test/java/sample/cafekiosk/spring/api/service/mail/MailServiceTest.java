package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    // @Mock -> MailService.sendMail() 안에 MailSendClient.sendMail(), MailSendClient.a(), MailSendClient.b(), MailSendClient.c() 처럼 여러개를 호출할 때, MailSendClient.sendMail() 만 모킹하면 a(), b(), c() 는 호출되지 않음
    // @Syp -> MailService.sendMail() 안에 MailSendClient.sendMail(), MailSendClient.a(), MailSendClient.b(), MailSendClient.c() 처럼 여러개를 호출하지만, MailSendClient.sendMail() 만 모킹하고 a(), b(), c() 는 실제 기능을 호출할 경우
     @Mock
//    @Spy
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    public void sendMail() throws Exception {
        // given
        // @Mock 을 사용하는 경우
//        Mockito.when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(true);
        BDDMockito.given(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        // @Spy 를 사용하는 경우
//        doReturn(true)
//                .when(mailSendClient)
//                .sendMail(anyString(), anyString(), anyString(), anyString());

        // when
        final boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

}