package dao;

import entity.Model;
import enums.FieldDimension;
import exception.data_access.FetchException;
import exception.data_access.StoreException;

public interface ModelDao {

    void save(Model model) throws StoreException;

    Model getByDimension(FieldDimension fieldDimension) throws FetchException;

}
