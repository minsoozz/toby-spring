package hello.toby.mail;

import hello.toby.user.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class JavaMail {

  private MailSender mailSender;

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  private void sendUpgradeEMail(User user) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("mail.server.com");

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(user.getEmail());
    mailMessage.setFrom("minsoozz@kakao.com");
    mailMessage.setSubject("토비의");
    mailMessage.setText("스프링");

    mailSender.send(mailMessage);
  }
}
