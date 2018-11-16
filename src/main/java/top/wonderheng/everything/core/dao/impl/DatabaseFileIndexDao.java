package top.wonderheng.everything.core.dao.impl;

import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.FileType;
import top.wonderheng.everything.core.model.Thing;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.dao.impl
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:14
 */
public class DatabaseFileIndexDao implements FileIndexDao {

    private final DataSource dataSource;

    public DatabaseFileIndexDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Thing thing) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            String sql = "insert into file_index (name, file_type, path, depth, pinyin) values (?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, thing.getName());
            statement.setString(2, thing.getFileType().name());
            statement.setString(3, thing.getPath());
            statement.setInt(4, thing.getDepth());
            statement.setString(5, thing.getPinyin());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void insert(List<Thing> things) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            String sql = "insert into file_index (name, file_type, path, depth, pinyin) values (?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            for (Thing thing : things) {
                statement.setString(1, thing.getName());
                statement.setString(2, thing.getFileType().name());
                statement.setString(3, thing.getPath());
                statement.setInt(4, thing.getDepth());
                statement.setString(5, thing.getPinyin());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(Thing thing) {

    }

    @Override
    public void delete(Thing thing) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            if (new File(thing.getPath()).isFile()) {
                String sql = "delete from file_index where path= ? ";
                statement = connection.prepareStatement(sql);
                statement.setString(1, thing.getPath());
            } else {
                String sql = "delete  from file_index where  path like %" + thing.getPath() + "?%";
                statement = connection.prepareStatement(sql);
            }
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public List<Thing> query(Condition condition) {
        List<Thing> things = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "select name,file_type,path,depth,pinyin from file_index where name like ? order by depth asc  limit  30";

            statement = connection.prepareStatement(sql);

            statement.setString(1, condition.getName() + "%");

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Thing thing = new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setFileType(FileType.lookupByName(
                        resultSet.getString("file_type")
                ));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                thing.setPinyin(resultSet.getString("pinyin"));

                things.add(thing);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return things;
    }
}
