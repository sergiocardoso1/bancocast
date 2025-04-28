package br.com.cast.service.authentication;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.cast.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			response.sendRedirect("/login");
			return false;
		}
		if (CookieService.getCookie(request, "usuarioId") != null) 
			return true;
		

		response.sendRedirect("/login");
		return false;

	}
	/*
	 * @Override public void postHandle(HttpServletRequest request,
	 * HttpServletResponse response, Object handler, ModelAndView modelAndView)
	 * throws Exception{ System.out.println("Post Handle method is Calling"); }
	 * 
	 * @Override public void afterCompletion(HttpServletRequest request,
	 * HttpServletResponse response, Object handler, Exception exception) throws
	 * Exception{ System.out.println("Request and Response is completed"); }
	 */
}
