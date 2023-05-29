package com.datasrc.base;

import org.junit.jupiter.api.BeforeAll;
import reactor.blockhound.BlockHound;

public abstract class BaseTest {

    @BeforeAll
    static void beforeAll() {
        BlockHound.install();
    }
}
