/**
 * Copyright 2006 StartNet s.r.o.
 *
 * Distributed under MIT license
 */
package cz.startnet.utils.pgdiff.parsers;

import cz.startnet.utils.pgdiff.Resources;
import cz.startnet.utils.pgdiff.schema.*;

import java.text.MessageFormat;

public class CreatePolicyParser {

    public static void parse(final PgDatabase database,
            final String statement) {
        final Parser parser = new Parser(statement);

        final PgPolicy policy = new PgPolicy();

        parser.expect("CREATE", "POLICY");
        final String policyName = parser.parseIdentifier();

        parser.expect("ON");

        final String qualifiedTableName = parser.parseIdentifier();
        final String schemaName =
                ParserUtils.getSchemaName(qualifiedTableName, database);

        final PgSchema schema = database.getSchema(schemaName);
        if (schema == null) {
            throw new RuntimeException(MessageFormat.format(
                    Resources.getString("CannotFindSchema"), schemaName,
                    statement));
        }

        final PgTable table = schema.getTable(ParserUtils.getObjectName(qualifiedTableName));
        if (table == null) {
            throw new RuntimeException(MessageFormat.format(
                    Resources.getString("CannotFindTable"), qualifiedTableName,
                    statement));
        }

        if (parser.expectOptional("FOR")) {
            String command = parser.expectOptionalOneOf("ALL", "SELECT",
                "INSERT", "UPDATE", "DELETE");
            policy.setCommand(command);
        } else{
            policy.setCommand("ALL");
        }

        policy.setName(policyName);
        policy.setTableName(table.getName());
        table.addPolicy(policy);
    }

    private CreatePolicyParser() {
    }
}
