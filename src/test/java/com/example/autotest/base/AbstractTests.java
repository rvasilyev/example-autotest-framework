package com.example.autotest.base;

import com.example.autotest.test.extension.DependentTestExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.autotest.test.aspect.AioStepAspect;
import com.example.autotest.test.extension.AioExtension;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ExtendWith(DependentTestExtension.class)
public abstract class AbstractTests {

    private final Map<Object, Object> testStorage;

    @SuppressWarnings("all")
    @RegisterExtension
    private AioExtension aioExtension;

    protected AbstractTests() {
        testStorage = new HashMap<>();
    }

    protected Map<Object, Object> getTestStorage() {
        return testStorage;
    }

    @BeforeEach
    void defaultBeforeEach() {
        AioStepAspect.initContext();
    }

    @AfterEach
    void defaultAfterEach() {
        AioStepAspect.clearContext();
    }

    @Autowired
    private void setAioExtension(AioExtension aioExtension) {
        this.aioExtension = aioExtension;
    }
}
