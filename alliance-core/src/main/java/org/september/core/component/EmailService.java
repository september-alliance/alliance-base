package org.september.core.component;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.september.core.component.log.LogHelper;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class EmailService {

	protected final LogHelper logHelper = LogHelper.getLogger(this.getClass());

//	private JavaMailSender mailSender;

	private String host;

	private Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

//    public boolean sendEmail(String subject, String content, String... to) {
//        return sendEmail(this.from , subject , content);
//    }

	public boolean sendEmail(String sender, String senderPwd, String subject, String content, String... to) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setUsername(sender);
		mailSender.setPassword(senderPwd);
		mailSender.setDefaultEncoding("utf-8");
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		mailSender.setJavaMailProperties(prop);
		return innerSend(mailSender, sender, subject, content, to, null, null);
	}

//	public void setMailSender(JavaMailSender mailSender) {
//		this.mailSender = mailSender;
//	}

	private boolean innerSend(JavaMailSender eSender, String from, String subject, String content, String[] to,
			String[] attachmentUrl, String[] attachmentName) {
		return innerSend(eSender, from, subject, content, to, attachmentUrl, attachmentName, "", new String[] {});
	}

	private boolean innerSend(JavaMailSender eSender, String from, String subject, String content, String[] to,
			String[] attachmentUrl, String[] attachmentName, String senderName, String[] ccList) {
		if (StringUtils.isEmpty(from)) {
			logHelper.getBuilder().error("发件人为空，发送失败");
			return false;
		}
		if (to == null || to.length < 1) {
			logHelper.getBuilder().error("收件人为空，发送失败");
			return false;
		}
		MimeMessage message = eSender.createMimeMessage();

		try {
			MimeMessageHelper helper = null;
			if (attachmentUrl != null && attachmentName != null && attachmentUrl.length == attachmentName.length) { // 附件不为空
				helper = new MimeMessageHelper(message, true);
				for (int i = 0; i < attachmentUrl.length; i++) {
					URL url = new URL(attachmentUrl[i]);
					InputStreamSource src = new UrlResource(url);
					helper.addAttachment(attachmentName[i], src);
				}
			} else {
				helper = new MimeMessageHelper(message);
			}

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			if (StringUtils.isEmpty(senderName)) {
				helper.setFrom(from);
			} else {
				try {
					helper.setFrom(from, senderName);
				} catch (UnsupportedEncodingException e) {
					logHelper.getBuilder()
							.error("发送邮件失败，发送人昵称编码错误。from:" + from + ", to:" + to + ", subject:" + subject, e);
					helper.setFrom(from);
				}
			}
			if (ccList != null) {
				helper.setCc(ccList);
			}
			eSender.send(message);
		} catch (MailException e) {
			logHelper.getBuilder().error("发送邮件失败。from:" + from + ", to:" + to + ", subject:" + subject, e);
			return false;
		} catch (MessagingException e) {
			logHelper.getBuilder().error("发送邮件失败。from:" + from + ", to:" + to + ", subject:" + subject, e);
			return false;
		} catch (MalformedURLException e) {
			logHelper.getBuilder().error("发送邮件失败(附件URL不正确)。from:" + from + ", to:" + to + ", attachmentUrl:"
					+ String.join(",", attachmentUrl), e);
			return false;
		}

		logHelper.getBuilder().info("发送邮件成功。from:" + from + ", to:" + to + ", subject:" + subject);
		return true;
	}

	public boolean sendEmail(String sender, String senderPwd, String subject, String content, String senderName,
			String[] to, String[] ccList) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setUsername(sender);
		mailSender.setPassword(senderPwd);
		mailSender.setDefaultEncoding("utf-8");
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		mailSender.setJavaMailProperties(prop);
		return innerSend(mailSender, sender, subject, content, to, null, null, senderName, ccList);
	}

	public boolean sendEmailBySender(JavaMailSender mailSender, String subject, String content, String[] to,
			String[] attachmentUrl, String[] attachmentName, String senderName, String[] ccList) {
		JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
		String from = impl.getJavaMailProperties().getProperty("default_from");
		return innerSend(mailSender, from, subject, content, to, attachmentUrl, attachmentName, senderName, ccList);
	}

	public boolean sendEmailBySender(JavaMailSender mailSender, String subject, String content, String senderName,
			String[] to, String[] ccList) {
		JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
		String from = impl.getJavaMailProperties().getProperty("default_from");
		return innerSend(mailSender, from, subject, content, to, null, null, senderName, ccList);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean testEmailAddress(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		Matcher matcher = emailPattern.matcher(email);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
}
