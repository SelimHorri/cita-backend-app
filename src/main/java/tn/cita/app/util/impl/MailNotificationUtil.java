package tn.cita.app.util.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tn.cita.app.exception.wrapper.NotificationNotProcessedException;
import tn.cita.app.model.dto.notif.MailNotification;
import tn.cita.app.util.MailContentBuilder;
import tn.cita.app.util.NotificationUtil;

import java.util.Map;

@Component
@RequiredArgsConstructor
class MailNotificationUtil implements NotificationUtil {
	
	private final JavaMailSender javaMailSender;
	private final MailContentBuilder mailContentBuilder;
	
	@Value("${spring.mail.username}")
	private String mailer;
	
	@Async
	@Override
	public void sendMail(final MailNotification mailNotification) {
		try {
			final MimeMessagePreparator mimeMessagePreparator = this.createPreparator(mailNotification, null);
			this.javaMailSender.send(mimeMessagePreparator);
		}
		catch (MailException e) {
			throw new NotificationNotProcessedException(
					"Sending mail to %s not processed as expected".formatted(mailNotification.to()));
		}
	}
	
	@Async
	@Override
	public void sendHtmlMail(final MailNotification mailNotification, final Map<String, Object> bodyProps) {
		try {
			final MimeMessagePreparator mimeMessagePreparator = this.createPreparator(mailNotification, bodyProps);
			this.javaMailSender.send(mimeMessagePreparator);
		}
		catch (MailException e) {
			throw new NotificationNotProcessedException(
					"Sending mail to %s not processed as expected".formatted(mailNotification.to()));
		}
	}
	
	private MimeMessagePreparator createPreparator(final MailNotification mailNotification, final Map<String, Object> bodyProps) {
		return mimeMessage -> {
			final boolean isHTML = mailNotification.body() == null || mailNotification.body().isBlank();
			final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setFrom(this.mailer);
			mimeMessageHelper.setTo(mailNotification.to());
			mimeMessageHelper.setSubject(mailNotification.subject());
			mimeMessageHelper.setText(
					isHTML ? this.mailContentBuilder.build(bodyProps) : mailNotification.body(), isHTML);
		};
	}
	
}




