package org.eclipse.trace.coordinator.contextresolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;

	public ObjectMapperContextResolver() {
		this.objectMapper = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build();

		this.objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
				.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return this.objectMapper;
	}

}
