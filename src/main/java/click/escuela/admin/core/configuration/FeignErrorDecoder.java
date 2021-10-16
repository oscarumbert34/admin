package click.escuela.admin.core.configuration;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.admin.core.exception.ErrorStudent;
import click.escuela.admin.core.exception.TransactionException;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
	private static final String ERROR_PARSE = "Error al recuperar Error de Feign";
	private static final String LIST_EMPTY = "La lista esta vacia";

	private ObjectMapper mapper = initMapper();
    
    @Override
    public TransactionException decode(String s, Response response) {        
        Decoder.Default decoder = new Decoder.Default();
        Object body = null;
        String error = null;
        
        try {
        	body = decoder.decode(response, String.class);
        	error = body.toString().contains("validationErrors") ?  getError(body) : body.toString();
 
		} catch (IOException e) {
			error = ERROR_PARSE;
		}
 
        return new TransactionException(String.valueOf(response.status()), error);
    }
    
    private String getError(Object body) throws JsonProcessingException {
    	ErrorStudent errorStudent = mapper.readValue(body.toString(), ErrorStudent.class);
		if(!Objects.isNull(errorStudent) && !Objects.isNull(errorStudent.getValidationErrors())) {
			return errorStudent.toString();
		}
		return LIST_EMPTY;
    }
    
    private ObjectMapper initMapper() {
    	return new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
		.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
