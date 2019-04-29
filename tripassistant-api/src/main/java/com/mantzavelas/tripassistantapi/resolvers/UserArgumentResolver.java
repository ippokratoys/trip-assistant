package com.mantzavelas.tripassistantapi.resolvers;

import com.mantzavelas.tripassistantapi.annotations.AuthenticatedUser;
import com.mantzavelas.tripassistantapi.repositories.UserRepository;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	private UserRepository repository = BeanUtil.getBean(UserRepository.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(AuthenticatedUser.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof UserDetails) {
				return repository.findByUsername(((UserDetails) principal).getUsername());
			}
		}

		return null;
	}
}
