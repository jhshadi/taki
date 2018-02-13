package jhshadi.com.taki.controllers.controllersUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TakiObjectMapper extends ObjectMapper {

    public TakiObjectMapper() {
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
