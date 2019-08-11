package org.craftsrecords.columbiadexpress.domain;

import org.craftsrecords.columbiadexpress.domain.search.criteria.JourneyParameterResolver;
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePortParameterResolver;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@ExtendWith(SpacePortParameterResolver.class)
@ExtendWith(JourneyParameterResolver.class)
public @interface InjectDomainObjects {
}
