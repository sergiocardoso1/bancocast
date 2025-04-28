package br.com.cast.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieService {

	public static void setCookie(HttpServletResponse response, String key, String value, int segundos) throws UnsupportedEncodingException {
		Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
		cookie.setMaxAge(segundos);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	
	public static String getCookie(HttpServletRequest request, String key) throws UnsupportedEncodingException {
		String valor = Optional.ofNullable(request.getCookies())
				.flatMap(cookies -> Arrays.stream(cookies)
				.filter(cookie -> key.equals(cookie.getName()))
				.findAny())
				.map(Cookie::getValue)
				.orElse(null);

		return valor != null ? URLDecoder.decode(valor, "UTF-8") : null;
	}
}
