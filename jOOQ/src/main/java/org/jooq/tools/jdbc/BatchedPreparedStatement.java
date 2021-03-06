/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.tools.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jooq.tools.JooqLogger;

/**
 * A batched statement.
 * <p>
 * This statement doesn't execute immediately, but buffers all bind variables in
 * batch, delaying execution until a new SQL string is encountered. See
 * {@link BatchedConnection} for details.
 *
 * @author Lukas Eder
 * @see BatchedConnection
 */
public class BatchedPreparedStatement extends DefaultPreparedStatement {

    private static final JooqLogger log = JooqLogger.getLogger(BatchedPreparedStatement.class);
    int                             batches;
    boolean                         executeImmediate;

    public BatchedPreparedStatement(BatchedConnection connection, PreparedStatement delegate) {
        super(delegate, connection);
    }

    public BatchedConnection getBatchedConnection() throws SQLException {
        return (BatchedConnection) super.getConnection();
    }

    public boolean getExecuteImmediate() {
        return this.executeImmediate;
    }

    public void setExecuteImmediate(boolean executeImmediate) {
        this.executeImmediate = executeImmediate;
    }

    private void resetBatches() {
        batches = 0;
    }

    private void logExecuteImmediate() throws SQLException {
        if (log.isDebugEnabled())
            log.debug("BatchedStatement", "Skipped batching statement: " + getBatchedConnection().lastSQL);
    }

    private void logBatch() throws SQLException {
        if (log.isDebugEnabled())
            log.debug("BatchedStatement", "Batched " + batches + " times: " + getBatchedConnection().lastSQL);
    }

    private void logExecution() throws SQLException {
        if (log.isDebugEnabled())
            log.debug("BatchedStatement", "Executed with " + batches + " batched items: " + getBatchedConnection().lastSQL);

        resetBatches();
    }

    // -------------------------------------------------------------------------
    // XXX: Wrappers
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return BatchedPreparedStatement.class == iface ? (T) this : super.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return BatchedPreparedStatement.class == iface || super.isWrapperFor(iface);
    }

    // -------------------------------------------------------------------------
    // XXX: Executing queries
    // -------------------------------------------------------------------------

    @Override
    public int executeUpdate() throws SQLException {
        if (executeImmediate) {
            logExecuteImmediate();
            return super.executeUpdate();
        }
        else {
            addBatch();
            return 0;
        }
    }

    @Override
    public boolean execute() throws SQLException {
        if (executeImmediate) {
            logExecuteImmediate();
            return super.execute();
        }
        else {
            addBatch();
            return false;
        }
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }



    @Override
    public long executeLargeUpdate() throws SQLException {
        return executeUpdate();
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        return getUpdateCount();
    }



    @Override
    public void close() throws SQLException {}

    // -------------------------------------------------------------------------
    // XXX: Batch features
    // -------------------------------------------------------------------------

    @Override
    public void addBatch() throws SQLException {
        batches++;
        logBatch();
        super.addBatch();

        if (batches >= getBatchedConnection().batchSize)
            getBatchedConnection().executeLastBatch();
    }

    @Override
    public void clearBatch() throws SQLException {
        throw new UnsupportedOperationException("Clearing a batch is not yet supported");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        logExecution();
        return super.executeBatch();
    }



    @Override
    public long[] executeLargeBatch() throws SQLException {
        logExecution();
        return super.executeLargeBatch();
    }



    // -------------------------------------------------------------------------
    // XXX: Unsupported static statement execution features
    // -------------------------------------------------------------------------

    @Override
    public void addBatch(String sql) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }



    @Override
    public long executeLargeUpdate(String sql) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }



    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        throw new UnsupportedOperationException("No static statement methods can be called");
    }

    // -------------------------------------------------------------------------
    // XXX: Unsupported result set query features
    // -------------------------------------------------------------------------

    @Override
    public ResultSet executeQuery() throws SQLException {
        if (batches == 0)
            return super.executeQuery();
        else
            throw new UnsupportedOperationException("Cannot batch result queries");
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        throw new UnsupportedOperationException("Cannot batch result queries");
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new UnsupportedOperationException("Cannot batch result queries");
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        throw new UnsupportedOperationException("Cannot batch result queries");
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        throw new UnsupportedOperationException("Cannot batch result queries");
    }
}
