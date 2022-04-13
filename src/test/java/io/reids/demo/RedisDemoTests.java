package io.reids.demo;

import io.reids.demo.annotation.Cluster;
import io.reids.demo.annotation.Sentinel;
import io.reids.demo.annotation.Standalone;
import org.junit.jupiter.api.Test;

public class RedisDemoTests {

    @Test
    @Standalone
    public void standalone() {
        System.out.println("standalone");
    }

    @Test
    @Sentinel
    public void sentinel() {
        System.out.println("sentinel");
    }

    @Test
    @Cluster
    public void cluster() {
        System.out.println("cluster");
    }
}
