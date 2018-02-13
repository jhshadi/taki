package jhshadi.com.taki.controllers;

import jhshadi.com.taki.controllers.controllersUtils.TakiObjectMapper;
import jhshadi.com.taki.services.TakiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TakiController {

    @Autowired
    protected TakiService takiService;

    @Autowired
    protected TakiObjectMapper mapper;
}
