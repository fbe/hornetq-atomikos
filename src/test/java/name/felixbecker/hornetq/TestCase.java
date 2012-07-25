package name.felixbecker.hornetq;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.SystemException;

import org.h2.jdbcx.JdbcDataSource;


import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;


/**
 * http://fogbugz.atomikos.com/default.asp?community.6.2177.4
 * 
 * Issue is that resume of TX doesn't work with the XA Data Source from H2 (h2 seems to be broken)
 * 
 *
 */
public class TestCase {

    private XADataSource dataSource;
    private UserTransactionManager transactionManager;
    private AtomikosDataSourceBean dataSourceBean;
    private Connection connection;

    public void createTransactionManager() throws SystemException {
        transactionManager = new UserTransactionManager();
        transactionManager.setTransactionTimeout(90000000);
        transactionManager.init();
        transactionManager.setTransactionTimeout(90000000);
    }

    public void createDataSource() throws SQLException {
        createH2DataSource();
        connection = ((DataSource) dataSource).getConnection();
        connection.setAutoCommit(true);
        dataSourceBean = new AtomikosDataSourceBean();
        dataSourceBean.setXaDataSource(dataSource);
        dataSourceBean.setPoolSize(2);
        dataSourceBean.setTestQuery("select * from information_schema.tables");
        dataSourceBean.setUniqueResourceName("xyz");
    }

    private void createH2DataSource() {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:test;LOCK_MODE=1;MVCC=TRUE;DB_CLOSE_DELAY=-1");
        jdbcDataSource.setUser("sa");
        dataSource = jdbcDataSource;
    }

    // private void createMySQLBaseDataSource() {
    // MysqlXADataSource source = new MysqlXADataSource();
    // source.setURL("jdbc:mysql://localhost:3306/BPE?characterEncoding=UTF-8");
    // source.setUser("syncron");
    // source.setPassword("syncron");
    // dataSource = source;
    // }

    private void setUp() throws Exception {
        createTransactionManager();
        createDataSource();
    }

    public void testCase() throws Exception {
        setUp();

        transactionManager.begin();
        Connection connection1 = dataSourceBean.getConnection();
        Connection connection2 = dataSourceBean.getConnection();
        connection1.createStatement();
        connection2.createStatement();
        connection1.close();
        connection1 = dataSourceBean.getConnection();
//        connection1.createStatement(); // this fails
        connection2.close();
        transactionManager.commit();
    }

    public static void main(String[] args) throws Exception {
        new TestCase().testCase();
    }

}