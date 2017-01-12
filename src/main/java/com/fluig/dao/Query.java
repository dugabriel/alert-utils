package com.fluig.dao;

/**
 * Created by eduardo.gabriel on 28/12/2016.
 */
public class Query {

    public static final String FIND_ALERTS_ID = "select aa.ID, aa.Object_ID, aa.Place_ID from FDN_ALERT aa where aa.CREATION_DATE < (now() - interval ? day)";

    public static final String REMOVE_FDN_ALERTSENDER = "DELETE FROM FDN_ALERTSENDER WHERE ALERT_ID = ?";

    public static final String REMOVE_FDN_ALERTACTION = "DELETE FROM FDN_AlertAction WHERE ALERT_ID = ?";

    public static final String REMOVE_FDN_ALERTMETADATA = "DELETE FROM FDN_AlertMetadata WHERE ALERT_ID = ?";

    public static final String REMOVE_FDN_ALERT = "DELETE FROM FDN_Alert WHERE ID = ?";

    public static final String REMOVE_FDN_ALERTOBJECT = "DELETE FROM FDN_AlertObject WHERE ID = ?";

    public static final String OPTIMIZE_TABLE = "optimize table ";

    public static final String[] TABLES = new String[] {"FDN_ALERTSENDER","FDN_ALERTACTION","FDN_ALERTMETADATA",
            "FDN_ALERT","FDN_ALERTOBJECT"};

}
