package com.sparta.utilities;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class ConfigReaderTest {
    @Test
    public void get_returnsNullForMissingKey() {
        MatcherAssert.assertThat(ConfigReader.get("nonexistent.key"), Matchers.nullValue());
    }

}
