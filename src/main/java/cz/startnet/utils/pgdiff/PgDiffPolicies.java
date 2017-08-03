/**
 * Copyright 2006 StartNet s.r.o.
 *
 * Distributed under MIT license
 */
package cz.startnet.utils.pgdiff;

import cz.startnet.utils.pgdiff.schema.*;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PgDiffPolicies {

    public static void createPolicies(final PrintWriter writer,
            final PgSchema oldSchema, final PgSchema newSchema,
            final SearchPathHelper searchPathHelper) {
        for (final PgTable newTable : newSchema.getTables()) {
            final String newTableName = newTable.getName();
            final PgTable oldTable;

            if (oldSchema == null) {
                oldTable = null;
            } else {
                oldTable = oldSchema.getTable(newTableName);
            }

            for (final PgPolicy policy : newTable.getPolicies()) {
                PgPolicy oldPolicy = oldTable != null?oldTable.getPolicy(policy.getName()):null;
                if(oldPolicy == null){
                    searchPathHelper.outputSearchPath(writer);
                    createPolicySQL(writer, policy);
                }
            }
        }
    }

    public static void alterPolicies(final PrintWriter writer,
            final PgSchema oldSchema, final PgSchema newSchema,
            final SearchPathHelper searchPathHelper) {
        for (final PgTable newTable : newSchema.getTables()) {
            final String newTableName = newTable.getName();

            if (oldSchema != null) {
                final PgTable oldTable = oldSchema.getTable(newTableName);
                if (oldTable != null){
                  for (final PgPolicy policy : oldTable.getPolicies()) {
                      PgPolicy newPolicy = newTable.getPolicy(policy.getName());
                      if(newPolicy != null){
                          // ALTER POLICY doesn't support changing command(ALL,
                          // SELECT..) so we drop it and create it
                          String newCommand = newPolicy.getCommand();
                          String oldCommand = policy.getCommand();
                          if(newCommand != null && oldCommand != null
                             && !newCommand.equals(oldCommand)){
                              dropPolicySQL(writer, newPolicy);
                              createPolicySQL(writer, newPolicy);
                          }
                      }
                  }
                }
            }
        }
    }

    public static void dropPolicies(final PrintWriter writer,
            final PgSchema oldSchema, final PgSchema newSchema,
            final SearchPathHelper searchPathHelper) {
        for (final PgTable newTable : newSchema.getTables()) {
            final String newTableName = newTable.getName();

            if (oldSchema != null) {
                final PgTable oldTable = oldSchema.getTable(newTableName);
                if (oldTable != null){
                  for (final PgPolicy policy : oldTable.getPolicies()) {
                      if(newTable.getPolicy(policy.getName()) == null){
                        searchPathHelper.outputSearchPath(writer);
                        dropPolicySQL(writer, policy);
                      }
                  }
                }
            }
        }
    }

    private static void createPolicySQL(final PrintWriter writer, final PgPolicy policy){
        writer.print("CREATE POLICY "
                + PgDiffUtils.getQuotedName(policy.getName())
                + " ON "
                + PgDiffUtils.getQuotedName(policy.getTableName()));
        writer.print(" FOR " + policy.getCommand());
        writer.println(";");
    }

    private static void dropPolicySQL(final PrintWriter writer, final PgPolicy policy){
        writer.println("DROP POLICY "
            + PgDiffUtils.getQuotedName(policy.getName())
            + " ON "
            + PgDiffUtils.getQuotedName(policy.getTableName())
            + ";");
    }

    private PgDiffPolicies() {
    }
}
