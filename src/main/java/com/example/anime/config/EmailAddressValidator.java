package com.example.anime.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Getter
@Setter
@Slf4j
public class EmailAddressValidator {
	@Value("${JOB_RUN_EMAIL_ALERT_SUBSCRIBERS}")
	private String jobAlertsEmailSubscribersString;
	private List<String> jobAlertsEmailSubscribers;
	@Value("${SENDER_EMAIL_ADDRESS}")
	private String emailSender;

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	/**
	 * Validates the email addresses
	 */
	@PostConstruct
	public void init() {

		if (jobAlertsEmailSubscribersString!=null && !jobAlertsEmailSubscribersString.isEmpty()) {
			jobAlertsEmailSubscribers = Arrays.asList(jobAlertsEmailSubscribersString.split("\\s*,\\s*"));
		}

		if (emailSender!=null && !emailSender.isEmpty()) {
			Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailSender);
			if (!matcher.find()) {
				log.error("Invalid email - sender email address found for emailAlertSubscribers property. Email {} ",
						emailSender);
				throw new IllegalArgumentException(
						"Invalid email sender email address found for emailAlertSubscribers property. Email : {}"
								+ emailSender);
			}
		}

		if (jobAlertsEmailSubscribers!=null && !jobAlertsEmailSubscribers.isEmpty()) {
			validateEmails(jobAlertsEmailSubscribers);
		}

	}

	private void validateEmails(List<String> emailAddresses) {
		for (String emailAddress : emailAddresses) {
			if (emailAddress!=null && !emailAddress.isEmpty()) {
				Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailAddress);
				if (!matcher.find()) {
					log.error("Invalid email address found for emailAlertSubscribers property. Email {} ",
							emailAddress);
					throw new IllegalArgumentException(
							"Invalid Email Address found for emailAlertSubscribers property. Email : {}"
									+ emailAddress);
				}
			}
		}
	}
}
