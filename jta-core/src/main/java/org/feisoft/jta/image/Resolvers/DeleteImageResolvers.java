package org.feisoft.jta.image.Resolvers;

import net.sf.jsqlparser.JSQLParserException;
import org.feisoft.common.utils.SqlpraserUtils;
import org.feisoft.jta.image.BackInfo;
import org.feisoft.jta.image.Image;

import javax.transaction.xa.XAException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteImageResolvers extends BaseResolvers {

    DeleteImageResolvers(String orginSql, BackInfo backInfo) {
        this.orginSql = orginSql;
        this.backInfo = backInfo;
    }

    @Override
    public Image genBeforeImage() throws JSQLParserException, SQLException, XAException {
        return genImage();

    }

    @Override
    public Image genAfterImage() {
        Image image = new Image();
        image.setSchemaName(schema);
        image.setTableName(tableName);
        image.setLine(new ArrayList<>());
        return image;
    }

    @Override
    public String getTable() throws JSQLParserException, XAException {

        List<String> tables = SqlpraserUtils.name_delete_table(orginSql);
        if (tables.size() > 1) {
            throw new XAException("Delete.UnsupportMultiTables");
        }
        return tables.get(0);
    }

    @Override
    protected String getSqlWhere() throws JSQLParserException {
        return SqlpraserUtils.name_delete_where(orginSql);
    }

    @Override
    public String getLockedSet() throws JSQLParserException {
        return beforeImageSql;
    }

}
