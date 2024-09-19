package com.odeyalo.sonata.piano.support.web.resolver;

import com.odeyalo.sonata.piano.api.dto.RegistrationFormDto;
import com.odeyalo.sonata.piano.model.Birthdate;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.InputPassword;
import com.odeyalo.sonata.piano.service.RegistrationForm;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.annotation.AbstractMessageReaderArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Function;

@Component
@Log4j2
public final class RegistrationFormResolver extends AbstractMessageReaderArgumentResolver {

    public RegistrationFormResolver(final List<HttpMessageReader<?>> readers) {
        super(readers);
    }

    @Override
    public boolean supportsParameter(@NotNull final MethodParameter parameter) {
        return parameter.getParameterType().equals(RegistrationForm.class);
    }

    @Override
    @NotNull
    public Mono<Object> resolveArgument(@NotNull final MethodParameter parameter,
                                        @NotNull final BindingContext bindingContext,
                                        @NotNull final ServerWebExchange exchange) {

        final Parameter bodyType = resolveBodyTypeParameter();

        return readBody(MethodParameter.forParameter(bodyType), parameter, true, bindingContext, exchange)
                .cast(RegistrationFormDto.class)
                .map(RegistrationFormResolver::toRegistrationForm)
                .doOnNext(res -> logger.debug("Successfully map the RegistrationFormDto to RegistrationForm"))
                .map(Function.identity());
    }

    @NotNull
    private static RegistrationForm toRegistrationForm(@NotNull final RegistrationFormDto dto) {
        return RegistrationForm.builder()
                .email(Email.valueOf(dto.email()))
                .gender(dto.gender())
                .password(InputPassword.valueOf(dto.password()))
                .birthdate(Birthdate.of(dto.birthdate()))
                .build();
    }

    private Parameter resolveBodyTypeParameter() {
        //noinspection DataFlowIssue never be null because we always have method in this class
        return ReflectionUtils
                .findMethod(this.getClass(), "methodParameterProviderSupport", RegistrationFormDto.class)
                .getParameters()[0];
    }

    @SuppressWarnings("unused")
    private void methodParameterProviderSupport(RegistrationFormDto dto) {
    }
}
