package com.helppo.dbindexing.config;

import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.liquibase.runtime.LiquibaseConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.changelog.visitor.DefaultChangeExecListener;
import liquibase.exception.LiquibaseException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Optional;

@ApplicationScoped
public class LiquibaseManager {

    private static final Logger LOGGER = Logger.getLogger(LiquibaseManager.class);

    @ConfigProperty(name = "app.database-schema.name", defaultValue = "quarkus_start_db")
    private String databaseSchema;

    @ConfigProperty(name = "app.release-tag-liquibase-rollback", defaultValue = " ")
    private String databaseTagToRollBack;

    @Inject
    DataSource dataSource;

    public void liquibaseInit(@Observes Startup startUpEvent) {
        LiquibaseFactory liquibaseFactory = new LiquibaseFactory(getLiquibaseConfig(), dataSource, "liquibaseDs");
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.setChangeExecListener(new DefaultChangeExecListener());
            //liquibase.update(new Contexts(), new LabelExpression());

            if(isTagExists(databaseTagToRollBack, liquibase)) {
                liquibase.rollback(databaseTagToRollBack, new Contexts());
            }

        } catch (LiquibaseException exception) {
            LOGGER.error("Unable to acquire Liquibase from Liquibasefactory");
        }
    }

    protected LiquibaseConfig getLiquibaseConfig() {
        LiquibaseConfig config = new LiquibaseConfig();
        config.changeLog="db/master.xml";
        config.defaultSchemaName= Optional.of(databaseSchema);
        config.changeLogParameters=new HashMap<>();
        return config;
    }

    private Boolean isTagExists(String tagToCheck, Liquibase liquibase) throws LiquibaseException {
        return liquibase.tagExists(tagToCheck);
    }

}
