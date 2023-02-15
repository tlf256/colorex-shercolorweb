package com.sherwin.shercolor.customershercolorweb.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//Exclude Vault config normally, only used in certain use-cases such as local development and testing
@Configuration
@Profile("store")
@EnableAutoConfiguration(exclude = org.springframework.cloud.vault.config.VaultAutoConfiguration.class)
public class VaultConfiguration {
}
