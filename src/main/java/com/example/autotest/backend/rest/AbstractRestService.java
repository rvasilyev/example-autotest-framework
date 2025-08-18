package com.example.autotest.backend.rest;

import com.example.autotest.logging.PrettyFeignLogger;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractRestService {

    private final String name;
    private final String url;
    private final Feign.Builder builder;

    protected AbstractRestService(String name, String url, HttpMessageConverter<?> converter, RequestInterceptor requestInterceptor) {
        this.name = name;
        this.url = url;

        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(converter);
        Encoder springEncoder = new SpringEncoder(objectFactory);
        Decoder springDecoder = new SpringDecoder(objectFactory);
        this.builder = Feign.builder()
                .contract(new SpringMvcContract(List.of(new FeignIgnoredParamParameterProcessor(), new ExampleHeaderMapParameterProcessor())))
                .encoder(springEncoder)
                .decoder(new ResponseEntityDecoder(springDecoder))
                //SpringMvcContract выбрасывает исключения при коде ответа >= 400, исправляем это настройкой перехватчика ответов
                .responseInterceptor((invocationContext, chain) -> invocationContext.decoder().decode(invocationContext.response(), invocationContext.returnType()))
                .logLevel(Logger.Level.FULL);
        Optional.ofNullable(requestInterceptor).ifPresent(this.builder::requestInterceptor);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractRestService that = (AbstractRestService) o;

        return name.equals(that.name) && url.equals(that.url) && builder.equals(that.builder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, builder);
    }

    protected <T> T createController(Class<T> controllerClass) {
        Class<FeignClient> feignClientAnnotationClass = FeignClient.class;
        String pathPrefix = Objects.requireNonNull(
                controllerClass.getAnnotation(feignClientAnnotationClass),
                String.format("Не найдена аннотация %s у класса %s", feignClientAnnotationClass.getName(), controllerClass.getName())
        ).path();
        return builder.logger(new PrettyFeignLogger(controllerClass)).target(controllerClass, url + pathPrefix);
    }
}
