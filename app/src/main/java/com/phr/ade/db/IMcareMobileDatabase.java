package com.phr.ade.db;


public interface IMcareMobileDatabase {


    /**
     * Create Core Database structure at the time on installation
     */
    void createDatabase();


    /**
     * Delete Core Database structure at the time of application uninstall.
     */
    void deletedDatabase();


}
