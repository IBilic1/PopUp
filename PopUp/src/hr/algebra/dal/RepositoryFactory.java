/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.dal.sql.SQLRepository;

/**
 *
 * @author HT-ICT
 */
public enum RepositoryFactory {
    REPOSITORY();

    private RepositoryFactory() {
        reposiotry=getRepository();
    }
    
    private final IRepository reposiotry;

    public IRepository getRepository() {
        return new SQLRepository();
    }

}
