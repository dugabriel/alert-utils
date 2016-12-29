package com.fluig.dao;

/**
 * Created by eduardo.gabriel on 28/12/2016.
 */
public class Query {

    public static final String FIND_ALERTS_ID = "select aa.ID, aa.Object_ID, aa.Place_ID from FDN_ALERT aa where aa.CREATION_DATE < (now() - interval ? day)";

    public static final String REMOVE_FDN_ALERTSENDER = "DELETE FROM FDN_ALERTSENDER WHERE ALERT_ID = ?";

    public static final String REMOVE_FDN_AlertAction = "DELETE FROM FDN_AlertAction WHERE ALERT_ID = ?";

    public static final String REMOVE_FDN_AlertMetadata = "DELETE FROM FDN_AlertMetadata WHERE ALERT_ID = ?";

    public static final String REMOVE_FDN_Alert = "DELETE FROM FDN_Alert WHERE ID = ?";

    public static final String REMOVE_FDN_AlertObject = "DELETE FROM FDN_AlertObject WHERE ID = ?";

}
