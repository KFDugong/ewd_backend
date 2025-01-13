package dev.ewd.mediashelf_spring.mediashelf_spring.controller;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.PrototypeObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PrototypeController {

    private final List<PrototypeObject> prototypeObjects = new ArrayList<>(
            List.of(
                    new PrototypeObject(1, "test1", "test1@web.com"),
                    new PrototypeObject(2, "test2", "test2@web.com")
            )
    );

    @GetMapping("/api/prototypeObjects")
    public List<PrototypeObject> getTestObjects(){
        return prototypeObjects;
    }

    @PostMapping("/api/prototypeObjects")
    public PrototypeObject addTestObject(@RequestBody PrototypeObject prototypeObject){
        prototypeObjects.add(prototypeObject);
        return prototypeObject;
    }
}
