package com.cdac.mumbai.common;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.cdac.mumbai.model.User;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final User user;
    private byte[] publicCipherKey;
    public OnRegistrationCompleteEvent(final User user, final Locale locale,  byte[] publicCipherKey,final String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.publicCipherKey=publicCipherKey;
    }

    //

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }

	public byte[] getPublicCipherKey() {
		return publicCipherKey;
	}

	

}
